package tk.mybatis.mapper.page;

/**
 * 传递分页参数
 */
public class PageHelper {

    private static final ThreadLocal<Pageable<?>> pageThreadLocal = new ThreadLocal<>();

    public static void startPage(long page, long size) {
        pageThreadLocal.set(new SimplePage<>(page, size));
    }

    public static void startPage(Pageable<?> page) {
        pageThreadLocal.set(page);
    }

    /**
     * 非标准分页，不提供total，一般是只需要返回条数受限的List时使用，防止OOM
     *
     * @param maxSize 最大返回条数
     */
    public static void startLimit(int maxSize) {
        pageThreadLocal.set(new SimplePage<>(1, maxSize).setNeedTotal(false));
    }

    public static void clear() {
        pageThreadLocal.remove();
    }

    public static Pageable<?> getAndClear() {
        Pageable<?> pageable = pageThreadLocal.get();
        if (pageable != null) {
            clear();
        }
        return pageable;
    }
}
