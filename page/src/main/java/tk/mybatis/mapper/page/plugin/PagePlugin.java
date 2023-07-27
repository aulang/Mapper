package tk.mybatis.mapper.page.plugin;

import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import tk.mybatis.mapper.page.PageHelper;
import tk.mybatis.mapper.page.Pageable;
import tk.mybatis.mapper.page.dialect.Dialect;
import tk.mybatis.mapper.page.dialect.DialectFactory;
import tk.mybatis.mapper.page.dialect.SimpleSQLParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Intercepts({@Signature(method = "prepare", type = StatementHandler.class, args = {Connection.class, Integer.class})})
public class PagePlugin implements Interceptor {

    private Dialect dialect;

    private final SimpleSQLParser parser = new SimpleSQLParser();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        BoundSql boundSql = statementHandler.getBoundSql();
        Object params = boundSql.getParameterObject();

        Pageable<?> pageable = detectPageParam(params);
        if (pageable != null) {
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

            if (pageable.needTotal()) {
                MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

                Connection connection = (Connection) invocation.getArgs()[0];

                setTotal(pageable, params, mappedStatement, connection);

            }

            String pageSql = dialect.paginationSQL(boundSql.getSql(), pageable.getOffset(), pageable.getSize());
            metaObject.setValue("delegate.boundSql.sql", pageSql);
        }

        return invocation.proceed();
    }

    @Override
    public void setProperties(Properties properties) {
        String dialectName = properties.getProperty("dialect");
        if (dialectName == null || dialectName.isEmpty()) {
            throw new PagePluginException("Configuration error, dialect property not set");
        }
        dialect = DialectFactory.create(dialectName);
    }

    private Pageable<?> detectPageParam(Object parameterObj) {
        if (parameterObj instanceof Pageable<?> pageable) {
            return pageable;
        }

        if (parameterObj instanceof Map<?, ?> paramMap) {
            for (Map.Entry<?, ?> entry : paramMap.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Pageable<?> pageable) {
                    return pageable;
                }
                if (value instanceof Pageable<?>[] pages) {
                    if (pages.length > 0) {
                        return pages[0];
                    }
                }
            }
        }

        return PageHelper.getAndClear();
    }

    private void setTotal(Pageable<?> pageable, Object params, MappedStatement mappedStatement, Connection conn) throws SQLException {
        BoundSql boundSql = mappedStatement.getBoundSql(params);
        String countSql = parser.getSmartCountSql(boundSql.getSql());

        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        BoundSql countBoundSql = new BoundSql(mappedStatement.getConfiguration(), countSql, parameterMappings, params);
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                countBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }

        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, params, countBoundSql);

        try (PreparedStatement statement = conn.prepareStatement(countSql)) {
            parameterHandler.setParameters(statement);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    pageable.setTotal(rs.getLong(1));
                }
            }
        }
    }
}
