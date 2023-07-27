package tk.mybatis.mapper.page;

import java.util.List;

/**
 * 分页接口
 *
 * @author wulang
 */
public interface Pageable<T> {

    /**
     * 获得当前页页码
     */
    long getPage();

    /**
     * 设置当前页面
     */
    Pageable<T> setPage(long page);

    /**
     * 获得每页的记录最大数量
     */
    long getSize();

    /**
     * 设置每页的最大数量
     */
    Pageable<T> setSize(long size);

    /**
     * 取得总记录数
     */
    long getTotal();

    /**
     * 设置系统中一共有多行记录
     */
    Pageable<T> setTotal(long total);

    /**
     * 是否需要计算total
     */
    boolean needTotal();

    /**
     * 取得页内的记录列表.
     */
    List<T> getList();

    /**
     * 设置当前页的记录列表
     */
    Pageable<T> setList(List<T> list);

    /**
     * 根据page和size计算当前页第一条记录在总结果集中的偏移量
     */
    long getOffset();

    /**
     * 根据pageSize与totalCount计算总页数
     */
    long getTotalPages();

    /**
     * 是否还有下一页.
     */
    boolean hasNext();

    /**
     * 取得下一页页码，页码最小值为1
     */
    long getNextPage();

    /**
     * 是否还有上一页.
     */
    boolean hasPre();

    /**
     * 取得上一页页码，页码从1开始
     */
    long getPrePage();

    /**
     * 将list数据进行转换
     */
    <K> Pageable<K> convert(Converter<T, K> converter);
}
