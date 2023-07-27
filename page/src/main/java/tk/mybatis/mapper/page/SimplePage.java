package tk.mybatis.mapper.page;

import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * 简化版本的分页
 *
 * @author wulang
 */
public class SimplePage<T> implements Pageable<T> {

    public static final String ASC = "asc";
    public static final String DESC = "desc";

    public static final int DEFAULT_PAGE_SIZE = 15;

    private long page = 1L;                      // IN/OUT
    private long size = DEFAULT_PAGE_SIZE;       // IN/OUT
    private long total = 0;                      // OUT

    @Transient
    private boolean needTotal = true;            // IN
    private List<T> list = new ArrayList<>();    // OUT

    public SimplePage() {
    }

    public SimplePage(Pageable<T> page) {
        this(page.getPage(), page.getSize(), page.getTotal(), page.getList());
    }

    public SimplePage(final long page) {
        this(page, DEFAULT_PAGE_SIZE);
    }

    public SimplePage(final long page, final long size) {
        this(page, size, 0);
    }

    public SimplePage(final long page, final long size, final long total) {
        this(page, size, total, new ArrayList<>());
    }

    public SimplePage(final long page, final long size, final long total, List<T> list) {
        setPage(page);
        setSize(size);
        setTotal(total);

        setList(list);
    }

    @Override
    public long getOffset() {
        return ((page - 1) * size);
    }

    @Override
    public long getTotalPages() {
        if (total == 0) {
            return 0;
        }
        long count = total / size;
        return total % size > 0 ? ++count : count;
    }

    @Override
    public boolean hasNext() {
        return page < getTotalPages();
    }

    @Override
    public long getNextPage() {
        return hasNext() ? page + 1 : page;
    }

    @Override
    public boolean hasPre() {
        return page > 1;
    }

    @Override
    public long getPrePage() {
        return hasPre() ? page - 1 : page;
    }

    @Override
    public long getPage() {
        return page;
    }

    public Pageable<T> setPage(long page) {
        this.page = page > 0 ? page : 1L;
        return this;
    }

    @Override
    public long getSize() {
        return size;
    }

    public Pageable<T> setSize(long size) {
        this.size = size > 0 ? size : DEFAULT_PAGE_SIZE;
        return this;
    }

    @Override
    public long getTotal() {
        return total;
    }

    public Pageable<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    @Override
    public boolean needTotal() {
        return needTotal;
    }

    public Pageable<T> setNeedTotal(boolean needTotal) {
        this.needTotal = needTotal;
        return this;
    }

    @Override
    public List<T> getList() {
        return list;
    }

    public Pageable<T> setList(List<T> list) {
        this.list = list;
        return this;
    }
}
