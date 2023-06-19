package tk.mybatis.mapper.version;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;

/**
 * @author liuzh
 */
public class VersionTest extends BaseTest {

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(VersionTest.class.getResource("mybatis-config-version.xml"));
    }

    @Override
    protected Reader getSqlFileAsReader() throws IOException {
        return toReader(VersionTest.class.getResource("CreateDB.sql"));
    }

    @Test
    public void testInsert() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserTimestampMapper mapper = sqlSession.getMapper(UserTimestampMapper.class);
            UserTimestamp user = new UserTimestamp();
            user.setId(1);
            user.setJoinDate(new Timestamp(System.currentTimeMillis()));
            int count = mapper.insert(user);
            Assertions.assertEquals(1, count);
        }
    }

    @Test
    public void testUpdate() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserTimestampMapper mapper = sqlSession.getMapper(UserTimestampMapper.class);
            UserTimestamp user = mapper.selectByPrimaryKey(999);
            Assertions.assertNotNull(user);
            Timestamp joinDate = user.getJoinDate();
            int count = mapper.updateByPrimaryKey(user);
            Assertions.assertEquals(1, count);

            user = mapper.selectByPrimaryKey(999);
            Assertions.assertFalse(joinDate.equals(user.getJoinDate()));
        }
    }

    @Test
    public void testUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserTimestampMapper mapper = sqlSession.getMapper(UserTimestampMapper.class);
            UserTimestamp user = mapper.selectByPrimaryKey(999);
            Assertions.assertNotNull(user);
            Timestamp joinDate = user.getJoinDate();
            int count = mapper.updateByPrimaryKeySelective(user);
            Assertions.assertEquals(1, count);

            user = mapper.selectByPrimaryKey(999);
            Assertions.assertFalse(joinDate.equals(user.getJoinDate()));
        }
    }

    @Test
    public void testUpdateInt() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserIntMapper mapper = sqlSession.getMapper(UserIntMapper.class);
            UserInt user = mapper.selectByPrimaryKey(999);
            Assertions.assertNotNull(user);
            Integer age = user.getAge();
            int count = mapper.updateByPrimaryKey(user);
            Assertions.assertEquals(1, count);

            user = mapper.selectByPrimaryKey(999);
            Assertions.assertNotEquals(age, user.getAge());
        }
    }

    @Test
    public void testUpdateIntByPrimaryKeySelective() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserIntMapper mapper = sqlSession.getMapper(UserIntMapper.class);
            UserInt user = mapper.selectByPrimaryKey(999);
            Assertions.assertNotNull(user);
            Integer age = user.getAge();
            int count = mapper.updateByPrimaryKeySelective(user);
            Assertions.assertEquals(1, count);

            user = mapper.selectByPrimaryKey(999);
            Assertions.assertNotEquals(age, user.getAge());
        }
    }
}
