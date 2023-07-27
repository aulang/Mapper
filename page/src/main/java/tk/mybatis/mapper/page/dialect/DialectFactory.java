package tk.mybatis.mapper.page.dialect;

import tk.mybatis.mapper.page.plugin.PagePluginException;

public class DialectFactory {

    public enum RMDBType {
        MYSQL,
        MARIADB,
        ORACLE,
        ORACLE12,
        POSTGRESQL,
        SQLSERVER,
        H2
    }

    public static Dialect create(String dialectName) {
        try {
            RMDBType dbType = RMDBType.valueOf(dialectName.toUpperCase());
            return switch (dbType) {
                case MYSQL, MARIADB -> new MysqlDialect();
                case ORACLE -> new OracleDialect();
                case POSTGRESQL -> new PostgreDialect();
                case H2 -> new H2Dialect();
                default -> throw new PagePluginException("Database dialect " + dialectName + " is not support");
            };
        } catch (IllegalArgumentException e) {
            throw new PagePluginException("Dialect configuration error, dialect name: " + dialectName);
        }
    }
}