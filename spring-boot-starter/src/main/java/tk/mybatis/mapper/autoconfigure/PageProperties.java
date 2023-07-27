package tk.mybatis.mapper.autoconfigure;

/**
 * @author wulang
 */
public class PageProperties {

    private boolean enabled = true;
    private String dialect = "mysql";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }
}
