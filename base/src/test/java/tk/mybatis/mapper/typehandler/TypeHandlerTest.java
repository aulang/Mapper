package tk.mybatis.mapper.typehandler;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @author liuzh
 */
public class TypeHandlerTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setEnumAsSimpleType(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(TypeHandlerTest.class.getResource("mybatis-config-typehandler.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(TypeHandlerTest.class.getResource("CreateDB.sql"));
    }

    @Test
    public void testSelect() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = userMapper.selectAll();
            Assertions.assertNotNull(users);
            Assertions.assertEquals(2, users.size());

            Assertions.assertEquals("abel533", users.get(0).getName());
            Assertions.assertEquals("Hebei", users.get(0).getAddress().getProvince());
            Assertions.assertEquals("Shijiazhuang", users.get(0).getAddress().getCity());
            Assertions.assertEquals(StateEnum.enabled, users.get(0).getState());

            Assertions.assertEquals("isea533", users.get(1).getName());
            Assertions.assertEquals("Hebei/Handan", users.get(1).getAddress().toString());
            Assertions.assertEquals(StateEnum.disabled, users.get(1).getState());

            User user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals("Hebei", user.getAddress().getProvince());
            Assertions.assertEquals("Shijiazhuang", user.getAddress().getCity());
            Assertions.assertEquals(StateEnum.enabled, user.getState());
        }
    }

    @Test
    public void testInsert() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setId(3);
            user.setName("liuzh");
            Address address = new Address();
            address.setProvince("Hebei");
            address.setCity("Qinhuangdao");
            user.setAddress(address);
            user.setState(StateEnum.enabled);

            Assertions.assertEquals(1, userMapper.insert(user));

            user = userMapper.selectByPrimaryKey(3);
            Assertions.assertEquals("liuzh", user.getName());
            Assertions.assertEquals("Hebei", user.getAddress().getProvince());
            Assertions.assertEquals("Qinhuangdao", user.getAddress().getCity());
            Assertions.assertEquals(StateEnum.enabled, user.getState());
        }
    }

    @Test
    public void testUpdate() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals("Hebei", user.getAddress().getProvince());
            Assertions.assertEquals("Shijiazhuang", user.getAddress().getCity());
            Assertions.assertEquals(StateEnum.enabled, user.getState());

            user.setState(StateEnum.disabled);
            user.getAddress().setCity("Handan");
            Assertions.assertEquals(1, userMapper.updateByPrimaryKey(user));

            user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals("Hebei", user.getAddress().getProvince());
            Assertions.assertEquals("Handan", user.getAddress().getCity());
            Assertions.assertEquals(StateEnum.disabled, user.getState());
        }
    }

    @Test
    public void testDelete() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Assertions.assertEquals(1, userMapper.deleteByPrimaryKey(1));

            User user = new User();
            Address address = new Address();
            address.setProvince("Hebei");
            address.setCity("Handan");
            user.setAddress(address);
            user.setState(StateEnum.enabled);
            Assertions.assertEquals(0, userMapper.delete(user));

            user.setState(StateEnum.disabled);
            Assertions.assertEquals(1, userMapper.delete(user));
        }
    }
}
