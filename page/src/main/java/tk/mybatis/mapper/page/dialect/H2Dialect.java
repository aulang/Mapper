package tk.mybatis.mapper.page.dialect;

public class H2Dialect implements Dialect {

    @Override
    public String paginationSQL(String sql, long offset, long limit) {
        return sql + ((offset > 0) ? " limit " + limit + " offset " + offset : " limit " + limit);
    }
}
