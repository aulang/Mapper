package tk.mybatis.mapper.annotation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import jakarta.persistence.Table;

/**
 * @author liuzh
 */
public class TableTest {

    private Config config;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);
    }

    @Table(name = "sys_user")
    class User {
        private String name;
    }

    @Test
    public void testColumn() {
        EntityHelper.initEntityNameMap(User.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(User.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("sys_user", entityTable.getName());
    }

}
