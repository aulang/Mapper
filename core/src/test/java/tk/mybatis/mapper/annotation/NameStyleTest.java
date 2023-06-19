package tk.mybatis.mapper.annotation;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.StringTypeHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

import java.util.Set;

/**
 * @author liuzh
 */
public class NameStyleTest {

    private Config config;

    private Configuration configuration;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    @NameStyle(Style.camelhump)
    static class UserCamelhump {
        private String userName;
    }

    @Test
    public void testCamelhump() {
        EntityHelper.initEntityNameMap(UserCamelhump.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCamelhump.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("user_camelhump", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("user_name", column.getColumn());
            Assertions.assertEquals("userName", column.getProperty());

            Assertions.assertEquals("user_name = #{userName}", column.getColumnEqualsHolder());
            Assertions.assertEquals("user_name = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{userName}", column.getColumnHolder());
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("user_name", resultMapping.getColumn());
        Assertions.assertEquals("userName", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.camelhumpAndUppercase)
    static class UserCamelhumpAndUppercase {
        private String userName;
    }

    @Test
    public void testCamelhumpAndUppercase() {
        EntityHelper.initEntityNameMap(UserCamelhumpAndUppercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCamelhumpAndUppercase.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("USER_CAMELHUMP_AND_UPPERCASE", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("USER_NAME", column.getColumn());
            Assertions.assertEquals("userName", column.getProperty());

            Assertions.assertEquals("USER_NAME = #{userName}", column.getColumnEqualsHolder());
            Assertions.assertEquals("USER_NAME = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{userName}", column.getColumnHolder());
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("USER_NAME", resultMapping.getColumn());
        Assertions.assertEquals("userName", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.camelhumpAndLowercase)
    static class UserCamelhumpAndLowercase {
        private String userName;
    }

    @Test
    public void testCamelhumpAndLowercase() {
        EntityHelper.initEntityNameMap(UserCamelhumpAndLowercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCamelhumpAndLowercase.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("user_camelhump_and_lowercase", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("user_name", column.getColumn());
            Assertions.assertEquals("userName", column.getProperty());

            Assertions.assertEquals("user_name = #{userName}", column.getColumnEqualsHolder());
            Assertions.assertEquals("user_name = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{userName}", column.getColumnHolder());
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("user_name", resultMapping.getColumn());
        Assertions.assertEquals("userName", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.normal)
    static class UserNormal {
        private String userName;
    }

    @Test
    public void testNormal() {
        EntityHelper.initEntityNameMap(UserNormal.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserNormal.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("UserNormal", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("userName", column.getColumn());
            Assertions.assertEquals("userName", column.getProperty());

            Assertions.assertEquals("userName = #{userName}", column.getColumnEqualsHolder());
            Assertions.assertEquals("userName = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{userName}", column.getColumnHolder());
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USERNAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("userName", resultMapping.getColumn());
        Assertions.assertEquals("userName", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.uppercase)
    static class UserUppercase {
        private String userName;
    }

    @Test
    public void testUppercase() {
        EntityHelper.initEntityNameMap(UserUppercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserUppercase.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("USERUPPERCASE", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("USERNAME", column.getColumn());
            Assertions.assertEquals("userName", column.getProperty());

            Assertions.assertEquals("USERNAME = #{userName}", column.getColumnEqualsHolder());
            Assertions.assertEquals("USERNAME = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{userName}", column.getColumnHolder());
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USERNAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("USERNAME", resultMapping.getColumn());
        Assertions.assertEquals("userName", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    @NameStyle(Style.lowercase)
    static class UserLowercase {
        private String userName;
    }

    @Test
    public void testLowercase() {
        EntityHelper.initEntityNameMap(UserLowercase.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserLowercase.class);
        Assertions.assertNotNull(entityTable);
        Assertions.assertEquals("userlowercase", entityTable.getName());

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("username", column.getColumn());
            Assertions.assertEquals("userName", column.getProperty());

            Assertions.assertEquals("username = #{userName}", column.getColumnEqualsHolder());
            Assertions.assertEquals("username = #{record.userName}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{userName}", column.getColumnHolder());
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.userName}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.userNamesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USERNAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("username", resultMapping.getColumn());
        Assertions.assertEquals("userName", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }
}
