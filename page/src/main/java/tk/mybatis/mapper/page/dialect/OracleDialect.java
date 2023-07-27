package tk.mybatis.mapper.page.dialect;

public class OracleDialect implements Dialect {

    /**
     * Get Oracle Pagination SQL
     *
     * @param sql    origin sql
     * @param offset recorder offset
     * @param limit  the max count returned
     */
    @Override
    public String paginationSQL(String sql, long offset, long limit) {
        StringBuilder sb = new StringBuilder();
        offset = offset + 1;
        sb.insert(0, "select t.*, rownum r from (").append(sql).append(") t where rownum < ").append(offset + limit);
        sb.insert(0, "select * from (").append(") where r >= ").append(offset);
        //Example: select * from (select t.*, rownum r from (select * from t_user) t where rownum < 31) where r >= 16
        return sb.toString();
    }
}
