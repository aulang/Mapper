package tk.mybatis.mapper.page.dialect;

public class MysqlDialect implements Dialect {

    /**
     * Get Oracle Pagination SQL
     *
     * @param sql    origin sql
     * @param offset recorder offset
     * @param limit  the max count returned
     */
    @Override
    public String paginationSQL(String sql, long offset, long limit) {
        return sql + " limit " + offset + ',' + limit;
    }
}