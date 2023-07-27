package tk.mybatis.mapper.page;

/**
 * 转换方法
 *
 * @author wulang
 */
@FunctionalInterface
public interface Converter<S, T> {

    T convert(final S message);
}
