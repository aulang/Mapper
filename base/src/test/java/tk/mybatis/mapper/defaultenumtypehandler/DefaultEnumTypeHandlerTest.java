package tk.mybatis.mapper.defaultenumtypehandler;

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
public class DefaultEnumTypeHandlerTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setEnumAsSimpleType(true);
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(DefaultEnumTypeHandlerTest.class.getResource("mybatis-config-defaultenumtypehandler.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(DefaultEnumTypeHandlerTest.class.getResource("CreateDB.sql"));
    }

    @Test
    public void testSelect() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = userMapper.selectAll();
            Assertions.assertNotNull(users);
            Assertions.assertEquals(2, users.size());

            Assertions.assertEquals("abel533", users.get(0).getName());
            Assertions.assertEquals(LockDictEnum.unlocked, users.get(0).getLock());
            Assertions.assertEquals(StateDictEnum.enabled, users.get(0).getState());

            Assertions.assertEquals("isea533", users.get(1).getName());
            Assertions.assertEquals(LockDictEnum.locked, users.get(1).getLock());
            Assertions.assertEquals(StateDictEnum.disabled, users.get(1).getState());

            User user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals(LockDictEnum.unlocked, users.get(0).getLock());
            Assertions.assertEquals(StateDictEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            User user = new User();
            user.setId(3);
            user.setName("liuzh");
            user.setLock(LockDictEnum.unlocked);
            user.setState(StateDictEnum.enabled);

            Assertions.assertEquals(1, userMapper.insert(user));

            user = userMapper.selectByPrimaryKey(3);
            Assertions.assertEquals("liuzh", user.getName());
            Assertions.assertEquals(LockDictEnum.unlocked, user.getLock());
            Assertions.assertEquals(StateDictEnum.enabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testUpdate() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals(LockDictEnum.unlocked, user.getLock());
            Assertions.assertEquals(StateDictEnum.enabled, user.getState());

            user.setLock(LockDictEnum.locked);
            user.setState(StateDictEnum.disabled);
            Assertions.assertEquals(1, userMapper.updateByPrimaryKey(user));

            user = userMapper.selectByPrimaryKey(1);
            Assertions.assertEquals("abel533", user.getName());
            Assertions.assertEquals(LockDictEnum.locked, user.getLock());
            Assertions.assertEquals(StateDictEnum.disabled, user.getState());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDelete() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Assertions.assertEquals(1, userMapper.deleteByPrimaryKey(1));

            User user = new User();
            user.setState(StateDictEnum.enabled);
            Assertions.assertEquals(0, userMapper.delete(user));

            user = new User();
            user.setLock(LockDictEnum.unlocked);
            Assertions.assertEquals(0, userMapper.delete(user));

            user = new User();
            user.setLock(LockDictEnum.locked);
            user.setState(StateDictEnum.disabled);
            Assertions.assertEquals(1, userMapper.delete(user));
        } finally {
            sqlSession.close();
        }
    }

}
