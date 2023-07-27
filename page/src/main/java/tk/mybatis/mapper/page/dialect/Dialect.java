package tk.mybatis.mapper.page.dialect;

public interface Dialect {

    String paginationSQL(String sql, long offset, long limit);
}
