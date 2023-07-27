package tk.mybatis.mapper.page.plugin;

public class PagePluginException extends RuntimeException {

    public PagePluginException() {
        super();
    }

    public PagePluginException(String message) {
        super(message);
    }

    public PagePluginException(String message, Throwable cause) {
        super(message, cause);
    }
}
