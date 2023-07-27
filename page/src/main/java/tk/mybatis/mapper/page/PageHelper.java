package tk.mybatis.mapper.page;

/**
 * 传递mybatis分页参数的数据交换空间
 * <p>
 * 实现方式为ThreadLocal，以支持系统中使用tk通用mybatis Example等方式实现分页查询时，
 * 如何将Pageable传递给PagePlugin。
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
