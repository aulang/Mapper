package tk.mybatis.mapper.annotation;

import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.mapperhelper.MapperHelper;

/**
 * @author liuzh
 */
public class RegisterMapperTest {

    private Config config;

    private Configuration configuration;

    @BeforeEach
    public void beforeTest() {
        config = new Config();
        config.setStyle(Style.normal);

        configuration = new Configuration();
    }

    @RegisterMapper
    interface MapperHashRegisterMapper {

    }

    interface UserMapper extends MapperHashRegisterMapper {

    }

    @Test
    public void testHashRegisterMapper() {
        MapperHelper mapperHelper = new MapperHelper();
        Assertions.assertTrue(mapperHelper.isExtendCommonMapper(UserMapper.class));
    }

    interface RoleMapper {

    }

    @Test
    public void testRoleMapper() {
        MapperHelper mapperHelper = new MapperHelper();
        Assertions.assertFalse(mapperHelper.isExtendCommonMapper(RoleMapper.class));
    }

    @RegisterMapper
    interface RoleMapper2 {

    }

    @Test
    public void testRoleMapper2() {
        MapperHelper mapperHelper = new MapperHelper();
        Assertions.assertFalse(mapperHelper.isExtendCommonMapper(RoleMapper2.class));
    }

}
