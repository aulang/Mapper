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

import jakarta.persistence.Column;
import java.util.Set;

/**
 * @author liuzh
 */
public class ColumnTest {

    private Config config;

    private Configuration configuration;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    class UserColumn {
        @Column(name = "user_name")
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

}
