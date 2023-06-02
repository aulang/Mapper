package tk.mybatis.mapper.annotation;

import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.code.IdentityDialect;
import tk.mybatis.mapper.code.ORDER;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.Set;

/**
 * @author liuzh
 */
public class KeySqlTest {

    private Config config;

    private Configuration configuration;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserJDBC {
        @KeySql(useGeneratedKeys = true)
        private Long id;
    }

    @Test
    public void testUseGeneratedKeys() {
        EntityHelper.initEntityNameMap(UserJDBC.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserJDBC.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("JDBC", column.getGenerator());
            Assertions.assertTrue(column.isIdentity());
        }
    }

    class UserDialect {
        @KeySql(dialect = IdentityDialect.MYSQL)
        private Long id;
    }

    @Test
    public void testDialect() {
        EntityHelper.initEntityNameMap(UserDialect.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserDialect.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("SELECT LAST_INSERT_ID()", column.getGenerator());
            Assertions.assertEquals(ORDER.AFTER, column.getOrder());
            Assertions.assertTrue(column.isIdentity());
        }
    }

    class UserSql {
        @KeySql(sql = "select seq.nextval from dual", order = ORDER.BEFORE)
        private Long id;
    }

    @Test
    public void testSql() {
        EntityHelper.initEntityNameMap(UserSql.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserSql.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("select seq.nextval from dual", column.getGenerator());
            Assertions.assertEquals(ORDER.BEFORE, column.getOrder());
            Assertions.assertTrue(column.isIdentity());
        }
    }

    class UserAll {
        @KeySql(useGeneratedKeys = true, dialect = IdentityDialect.MYSQL, sql = "select 1", order = ORDER.BEFORE)
        private Long id;
    }

    @Test
    public void testAll() {
        EntityHelper.initEntityNameMap(UserAll.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserAll.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("JDBC", column.getGenerator());
            Assertions.assertTrue(column.isIdentity());
        }
    }

    class UserAll2 {
        @KeySql(dialect = IdentityDialect.MYSQL, sql = "select 1", order = ORDER.BEFORE)
        private Long id;
    }

    @Test
    public void testAll2() {
        EntityHelper.initEntityNameMap(UserAll2.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserAll2.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("SELECT LAST_INSERT_ID()", column.getGenerator());
            Assertions.assertEquals(ORDER.AFTER, column.getOrder());
            Assertions.assertTrue(column.isIdentity());
        }
    }

}
