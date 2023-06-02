package tk.mybatis.mapper.annotation;

import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BlobTypeHandler;
import org.apache.ibatis.type.EnumOrdinalTypeHandler;
import org.apache.ibatis.type.JdbcType;
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
public class ColumnTypeTest {

    private Config config;

    private Configuration configuration;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserColumn {
        @ColumnType(column = "user_name")
        private String name;
    }

    @Test
    public void testColumn() {
        EntityHelper.initEntityNameMap(UserColumn.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserColumn.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("user_name", column.getColumn());
            Assertions.assertEquals("name", column.getProperty());

            Assertions.assertEquals("user_name = #{name}", column.getColumnEqualsHolder());
            Assertions.assertEquals("user_name = #{record.name}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{name}", column.getColumnHolder());
            Assertions.assertEquals("#{record.name}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.name}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.namesuffix},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("user_name", resultMapping.getColumn());
        Assertions.assertEquals("name", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    class UserJdbcTypeVarchar {
        @ColumnType(jdbcType = JdbcType.VARCHAR)
        private String name;
    }

    @Test
    public void testJdbcTypeVarchar() {
        EntityHelper.initEntityNameMap(UserJdbcTypeVarchar.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserJdbcTypeVarchar.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("name", column.getColumn());
            Assertions.assertEquals("name", column.getProperty());

            Assertions.assertEquals("name = #{name, jdbcType=VARCHAR}", column.getColumnEqualsHolder());
            Assertions.assertEquals("name = #{record.name, jdbcType=VARCHAR}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{name, jdbcType=VARCHAR}", column.getColumnHolder());
            Assertions.assertEquals("#{record.name, jdbcType=VARCHAR}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.name, jdbcType=VARCHAR}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.namesuffix, jdbcType=VARCHAR},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("name", resultMapping.getColumn());
        Assertions.assertEquals("name", resultMapping.getProperty());
        Assertions.assertNotNull(resultMapping.getJdbcType());
        Assertions.assertEquals(JdbcType.VARCHAR, resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    class UserJdbcTypeBlob {
        @ColumnType(jdbcType = JdbcType.BLOB)
        private String name;
    }

    @Test
    public void testJdbcTypeBlob() {
        EntityHelper.initEntityNameMap(UserJdbcTypeBlob.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserJdbcTypeBlob.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("name", column.getColumn());
            Assertions.assertEquals("name", column.getProperty());

            Assertions.assertEquals("name = #{name, jdbcType=BLOB}", column.getColumnEqualsHolder());
            Assertions.assertEquals("name = #{record.name, jdbcType=BLOB}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{name, jdbcType=BLOB}", column.getColumnHolder());
            Assertions.assertEquals("#{record.name, jdbcType=BLOB}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.name, jdbcType=BLOB}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.namesuffix, jdbcType=BLOB},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("name", resultMapping.getColumn());
        Assertions.assertEquals("name", resultMapping.getProperty());
        Assertions.assertNotNull(resultMapping.getJdbcType());
        Assertions.assertEquals(JdbcType.BLOB, resultMapping.getJdbcType());
        Assertions.assertEquals(StringTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    class UserTypehandler {
        @ColumnType(typeHandler = BlobTypeHandler.class)
        private String name;
    }

    @Test
    public void testTypehandler() {
        EntityHelper.initEntityNameMap(UserTypehandler.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserTypehandler.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("name", column.getColumn());
            Assertions.assertEquals("name", column.getProperty());

            Assertions.assertEquals("name = #{name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder());
            Assertions.assertEquals("name = #{record.name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder());
            Assertions.assertEquals("#{record.name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.name, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.namesuffix, typeHandler=org.apache.ibatis.type.BlobTypeHandler},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNotNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("name", resultMapping.getColumn());
        Assertions.assertEquals("name", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(BlobTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

    enum UserEnum {
        A, B
    }

    class UserEnumOrdinalTypeHandler {
        @ColumnType(typeHandler = EnumOrdinalTypeHandler.class)
        private UserEnum name;
    }

    @Test
    public void testEnumOrdinalTypeHandler() {
        EntityHelper.initEntityNameMap(UserEnumOrdinalTypeHandler.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserEnumOrdinalTypeHandler.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("name", column.getColumn());
            Assertions.assertEquals("name", column.getProperty());

            Assertions.assertEquals("name = #{name, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}", column.getColumnEqualsHolder());
            Assertions.assertEquals("name = #{record.name, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{name, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}", column.getColumnHolder());
            Assertions.assertEquals("#{record.name, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.name, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.namesuffix, typeHandler=org.apache.ibatis.type.EnumOrdinalTypeHandler},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNotNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("name", resultMapping.getColumn());
        Assertions.assertEquals("name", resultMapping.getProperty());
        Assertions.assertNull(resultMapping.getJdbcType());
        Assertions.assertEquals(EnumOrdinalTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }


    class UserAll {
        @ColumnType(column = "user_name", jdbcType = JdbcType.BLOB, typeHandler = BlobTypeHandler.class)
        private String name;
    }

    @Test
    public void testAll() {
        EntityHelper.initEntityNameMap(UserAll.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserAll.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertEquals("user_name", column.getColumn());
            Assertions.assertEquals("name", column.getProperty());

            Assertions.assertEquals("user_name = #{name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder());
            Assertions.assertEquals("user_name = #{record.name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnEqualsHolder("record"));
            Assertions.assertEquals("#{name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder());
            Assertions.assertEquals("#{record.name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record"));
            Assertions.assertEquals("#{record.name, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler}", column.getColumnHolder("record", "suffix"));
            Assertions.assertEquals("#{record.namesuffix, jdbcType=BLOB, typeHandler=org.apache.ibatis.type.BlobTypeHandler},", column.getColumnHolder("record", "suffix", ","));
            Assertions.assertNotNull(column.getTypeHandler());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals("[USER_NAME]", resultMap.getMappedColumns().toString());

        Assertions.assertEquals(1, resultMap.getResultMappings().size());

        ResultMapping resultMapping = resultMap.getResultMappings().get(0);
        Assertions.assertEquals("user_name", resultMapping.getColumn());
        Assertions.assertEquals("name", resultMapping.getProperty());
        Assertions.assertNotNull(resultMapping.getJdbcType());
        Assertions.assertEquals(BlobTypeHandler.class, resultMapping.getTypeHandler().getClass());
    }

}
