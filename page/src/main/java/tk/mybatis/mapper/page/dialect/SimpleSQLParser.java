package tk.mybatis.mapper.page.dialect;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;
import tk.mybatis.mapper.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SQL解析类，提供更智能的count SQL
 * <p>
 * 1. 标准方案是剔除order by子句，改造原语句为select count('count column')的形式
 * 2. 当原查询语句含聚合函及分组等情况时，使用简单模式，将原始查询包装为子查询再求count
 * <p>
 * 若开发者有特殊理由，不希望自己的SQL查询在求count时剔除order by子句，可以在SQL中添加
 * 特殊标记，详见{@link #KEEP_ORDER_BY}
 */
public class SimpleSQLParser {

    /**
     * SQL注释中如果包含这个前缀，则采用简单模式生成count sql语句，不删除order by子句
     */
    public static final String KEEP_ORDER_BY = "/*keep order by*/";

    /**
     * 聚合函数及聚合函数前缀列表
     * <p>
     * 有部分是聚合函数前缀，分析包含与否，需要上面的cache来帮助提升效率，因为不是判断函数是否存在于该列表
     */
    private static final Set<String> AGGREGATE_FUNCTIONS = new HashSet<>(Arrays.asList(
            "APPROX_COUNT_DISTINCT",
            "ARRAY_AGG",
            "AVG",
            "BIT_",
            "BOOL_",
            "CHECKSUM_AGG",
            "COLLECT",
            "CORR",
            "COUNT",
            "COVAR",
            "CUME_DIST",
            "DENSE_RANK",
            "EVERY",
            "FIRST",
            "GROUP",
            "JSON_",
            "LAST",
            "LISTAGG",
            "MAX",
            "MEDIAN",
            "MIN",
            "PERCENT_",
            "RANK",
            "REGR_",
            "SELECTIVITY",
            "STATS_",
            "STD",
            "STRING_AGG",
            "SUM",
            "SYS_OP_ZONE_ID",
            "SYS_XMLAGG",
            "VAR",
            "XMLAGG"
    ));

    private static final Alias TABLE_ALIAS;

    static {
        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }

    /**
     * 已经分析过的非聚合函数集合Cache，但此函数不在rejectFunCache中
     */
    private final Set<String> analyzedFunCache = Collections.synchronizedSet(new HashSet<>());

    /**
     * 已经分析过的属于聚合的函数集合Cache，命中可以立即返回false，SQL语句包含聚合函数，不应被简化
     */
    private final Set<String> rejectFunCache = Collections.synchronizedSet(new HashSet<>());

    /**
     * 添加到聚合函数，可以是逗号隔开的多个函数前缀
     *
     * @param functions 以逗号分隔的聚合函数列表，如果一组聚合函数前缀相同，可以只添加前缀
     */
    public static void addAggregateFunctions(String functions) {
        if (StringUtil.isNotEmpty(functions)) {
            String[] funArray = functions.split(",");
            for (String fun : funArray) {
                AGGREGATE_FUNCTIONS.add(fun.toUpperCase());
            }
        }
    }

    /**
     * 获取智能的countSql
     *
     * @param sql 原始的select查询语句
     */
    public String getSmartCountSql(String sql) {
        return getSmartCountSql(sql, "*");
    }

    /**
     * 获取智能的countSql
     *
     * @param sql         原始的select查询语句
     * @param countColumn count column
     */
    public String getSmartCountSql(String sql, String countColumn) {
        Statement stmt;
        if (sql.contains(KEEP_ORDER_BY)) {
            return getSimpleCountSql(sql, countColumn);
        }
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (Throwable e) {
            //无法解析的用一般方法返回count语句
            return getSimpleCountSql(sql, countColumn);
        }
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        try {
            //处理select body，去除order by
            processSelectBody(selectBody);
        } catch (Exception e) {
            //当sql包含group by时，不去除order by
            return getSimpleCountSql(sql, countColumn);
        }
        //处理with语句去除order by
        processWithItemsList(select.getWithItemsList());

        //处理为count查询
        sqlToCount(select, countColumn);

        return select.toString();
    }

    /**
     * 获取普通的count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql) {
        return getSimpleCountSql(sql, "*");
    }

    /**
     * 获取普通的count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql, String countColumn) {
        return "select count(" + countColumn + ") from (" + sql + ") table_count";
    }

    /**
     * 将sql转换为count查询
     *
     * @param select      select
     * @param countColumn count column
     */
    private void sqlToCount(Select select, String countColumn) {
        SelectBody selectBody = select.getSelectBody();

        //是否能简化count查询
        List<SelectItem> countItem = new ArrayList<>();
        countItem.add(new SelectExpressionItem(new Column("count(" + countColumn + ")")));

        if (selectBody instanceof PlainSelect plainSelect && isSimpleCount(plainSelect)) {
            plainSelect.setSelectItems(countItem);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(countItem);

            select.setSelectBody(plainSelect);
        }
    }

    /**
     * 检查是否只能用简单的count查询方式
     *
     * @param select 查询
     */
    private boolean isSimpleCount(PlainSelect select) {
        // 包含group by、distinct、having时不是
        if (select.getGroupBy() != null || select.getDistinct() != null || select.getHaving() != null) {
            return false;
        }

        for (SelectItem item : select.getSelectItems()) {
            //select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            //如果查询列中包含函数，也不可以，函数可能会聚合列
            if (item instanceof SelectExpressionItem selectExpressionItem) {
                Expression expression = selectExpressionItem.getExpression();
                if (expression instanceof Function function) {
                    String name = function.getName();
                    if (name == null) {
                        continue;
                    }
                    name = name.toUpperCase();
                    if (analyzedFunCache.contains(name)) {
                        continue;
                    }

                    if (rejectFunCache.contains(name)) {
                        return false;
                    } else {
                        for (String aggregateFunction : AGGREGATE_FUNCTIONS) {
                            if (name.startsWith(aggregateFunction)) {
                                rejectFunCache.add(name);
                                return false;
                            }
                        }
                        analyzedFunCache.add(name);
                    }
                } else if (expression instanceof Parenthesis && selectExpressionItem.getAlias() != null) {
                    // 当存在 (a+b) as c 时，c 如果出现在order by 或者 having中时，会找不到对应的列
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 处理selectBody去除order by
     *
     * @param selectBody select body
     */
    private void processSelectBody(SelectBody selectBody) {
        if (selectBody == null) {
            return;
        }

        if (selectBody instanceof PlainSelect plainSelect) {
            processPlainSelect(plainSelect);
        } else if (selectBody instanceof WithItem withItem) {
            if (withItem.getSubSelect() != null) {
                processSelectBody(withItem.getSubSelect().getSelectBody());
            }
        } else if (selectBody instanceof SetOperationList setOperationList) {
            List<SelectBody> selectBodies = setOperationList.getSelects();
            if (isNotEmpty(selectBodies)) {
                for (SelectBody select : selectBodies) {
                    processSelectBody(select);
                }
            }

            if (orderByHasNoParameters(setOperationList.getOrderByElements())) {
                setOperationList.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect plain select
     */
    private void processPlainSelect(PlainSelect plainSelect) {
        if (orderByHasNoParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }

        processFromItem(plainSelect.getFromItem());

        List<Join> joins = plainSelect.getJoins();
        if (isNotEmpty(joins)) {
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList withItem list
     */
    private void processWithItemsList(List<WithItem> withItemsList) {
        if (!isNotEmpty(withItemsList)) {
            return;
        }

        for (WithItem item : withItemsList) {
            if (item.getSubSelect() != null) {
                processSelectBody(item.getSubSelect().getSelectBody());
            }
        }
    }

    /**
     * 处理子查询
     *
     * @param fromItem from item
     */
    private void processFromItem(FromItem fromItem) {
        if (fromItem == null) {
            return;
        }

        if (fromItem instanceof SubJoin subJoin) {
            List<Join> joins = subJoin.getJoinList();

            if (isNotEmpty(joins)) {
                for (Join join : joins) {
                    processFromItem(join.getRightItem());
                }
            }
            processFromItem(subJoin.getLeft());
        } else if (fromItem instanceof SubSelect subSelect) {
            processSelectBody(subSelect.getSelectBody());
        } else if (fromItem instanceof LateralSubSelect lateralSubSelect) {
            SubSelect subSelect = lateralSubSelect.getSubSelect();
            if (subSelect != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        }
        // ValuesList时不处理; Table时不用处理
    }

    /**
     * 分析order by中是否未含参数
     *
     * @param orderByElements order by elements
     * @return order by未含参数返回true，否则返回false
     */
    private boolean orderByHasNoParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return true;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断集合是否为空
     *
     * @param collection 集合对象
     * @return 如果集合为null或则为empty，都返回true，否则返回false
     */
    private boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
