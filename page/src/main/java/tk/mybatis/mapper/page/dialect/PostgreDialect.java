package tk.mybatis.mapper.page.dialect;

/**
 * Postgre 数据库分页语句组装实现
 */
public class PostgreDialect implements Dialect {

    @Override
    public String paginationSQL(String sql, long offset, long limit) {
        return sql + " limit " + limit + " offset " + offset;
    }
}
