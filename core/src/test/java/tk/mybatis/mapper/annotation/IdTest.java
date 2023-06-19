package tk.mybatis.mapper.annotation;

import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import jakarta.persistence.Id;
import java.util.Set;

/**
 * @author liuzh
 */
public class IdTest {

    private Config config;

    private Configuration configuration;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    static class UserSingleId {
        @Id
        private String name;
    }

    @Test
    public void testSingleId() {
        EntityHelper.initEntityNameMap(UserSingleId.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserSingleId.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(1, columns.size());

        for (EntityColumn column : columns) {
            Assertions.assertTrue(column.isId());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals(1, resultMap.getResultMappings().size());
        Assertions.assertTrue(resultMap.getResultMappings().get(0).getFlags().contains(ResultFlag.ID));

        Assertions.assertEquals("<where> AND name = #{name}</where>", SqlHelper.wherePKColumns(UserSingleId.class));
    }

    static class UserCompositeKeys {
        @Id
        private String name;

        @Id
        private String orgId;
    }

    @Test
    public void testCompositeKeys() {
        EntityHelper.initEntityNameMap(UserCompositeKeys.class, config);
        EntityTable entityTable = EntityHelper.getEntityTable(UserCompositeKeys.class);
        Assertions.assertNotNull(entityTable);

        Set<EntityColumn> columns = entityTable.getEntityClassColumns();
        Assertions.assertEquals(2, columns.size());
        Assertions.assertEquals(2, entityTable.getEntityClassPKColumns().size());

        for (EntityColumn column : columns) {
            Assertions.assertTrue(column.isId());
        }

        ResultMap resultMap = entityTable.getResultMap(configuration);
        Assertions.assertEquals(2, resultMap.getResultMappings().size());
        Assertions.assertTrue(resultMap.getResultMappings().get(0).getFlags().contains(ResultFlag.ID));
        Assertions.assertTrue(resultMap.getResultMappings().get(1).getFlags().contains(ResultFlag.ID));

        Assertions.assertEquals("<where> AND name = #{name} AND orgId = #{orgId}</where>", SqlHelper.wherePKColumns(UserCompositeKeys.class));
    }

}
