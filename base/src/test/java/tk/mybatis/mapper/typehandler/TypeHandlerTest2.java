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
public class TypeHandlerTest2 extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setEnumAsSimpleType(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(TypeHandlerTest2.class.getResource("mybatis-config-typehandler2.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(TypeHandlerTest2.class.getResource("CreateDB.sql"));
    }

    @Test
    public void testSelect() {
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);
            List<User2> users = userMapper.selectAll();
            Assertions.assertNotNull(users);
            Assertions.assertEquals(2, users.size());

            Assertions.assertEquals("abel533", users.get(0).getName());
            Assertions.assertEquals("Hebei", users.get(0).getAddress().getProvince());
            Assertions.assertEquals("Shijiazhuang", users.get(0).getAddress().getCity());
            Assertions.assertEquals(StateEnum.enabled, users.get(0).getState());

            Assertions.assertEquals("isea533", users.get(1).getName());
            Assertions.assertEquals("Hebei/Handan", users.get(1).getAddress().toString());
            Assertions.assertEquals(StateEnum.disabled, users.get(1).getState());

            User2 user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals("Hebei", user.getAddress().getProvince());
            Assertions.assertEquals("Shijiazhuang", user.getAddress().getCity());
            Assertions.assertEquals(StateEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);

            User2 user = new User2();
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate() {
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);
            User2 user = userMapper.selectByPrimaryKey(1);
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
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDelete() {
        SqlSession sqlSession = getSqlSession();
        try {
            User2Mapper userMapper = sqlSession.getMapper(User2Mapper.class);
            Assertions.assertEquals(1, userMapper.deleteByPrimaryKey(1));

            User2 user = new User2();
            Address address = new Address();
            address.setProvince("Hebei");
            address.setCity("Handan");
            user.setAddress(address);
            user.setState(StateEnum.enabled);
            Assertions.assertEquals(0, userMapper.delete(user));

            user.setState(StateEnum.disabled);
            Assertions.assertEquals(1, userMapper.delete(user));
        } finally {
            sqlSession.close();
        }
    }

}
