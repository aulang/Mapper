package tk.mybatis.mapper.generatedvalue;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.entity.Config;

import java.io.IOException;
import java.io.Reader;

/**
 * @author liuzh
 */
@Disabled("这个测试需要使用 MySql 数据库")
public class GeneratedValueTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setOrder("AFTER");
        config.setIDENTITY("MYSQL");
        return config;
    }

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(GeneratedValueTest.class.getResource("mybatis-config-keysql-mysql.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() {
        return null;
    }

    @Test
    public void testUserAutoIncrement() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserAutoIncrementMapper mapper = sqlSession.getMapper(UserAutoIncrementMapper.class);

            UserAutoIncrement user = new UserAutoIncrement();
            user.setName("liuzh");
            Assertions.assertEquals(1, mapper.insert(user));
            Assertions.assertNotNull(user.getId());

            user = mapper.selectByPrimaryKey(user.getId());
            Assertions.assertEquals("liuzh", user.getName());
        }
    }

    @Test
    public void testUserAutoIncrementIdentity() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserAutoIncrementIdentityMapper mapper = sqlSession.getMapper(UserAutoIncrementIdentityMapper.class);

            UserAutoIncrementIdentity user = new UserAutoIncrementIdentity();
            user.setName("liuzh");
            Assertions.assertEquals(1, mapper.insert(user));
            Assertions.assertNotNull(user.getId());

            user = mapper.selectByPrimaryKey(user.getId());
            Assertions.assertEquals("liuzh", user.getName());
        }
    }

    @Test
    public void testUserSqlAfter() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserSqlAfterMapper mapper = sqlSession.getMapper(UserSqlAfterMapper.class);

            UserSqlAfter user = new UserSqlAfter();
            user.setName("liuzh");
            Assertions.assertEquals(1, mapper.insert(user));
            Assertions.assertNotNull(user.getId());

            user = mapper.selectByPrimaryKey(user.getId());
            Assertions.assertEquals("liuzh", user.getName());
        }
    }
}
