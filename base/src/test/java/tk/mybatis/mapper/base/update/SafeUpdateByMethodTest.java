package tk.mybatis.mapper.base.update;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;

public class SafeUpdateByMethodTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setSafeUpdate(true);
        //和 SafeUpdateByFieldTest 测试的区别在此，这里将会使后面调用 EntityField.getValue 时，使用 getter 方法获取值
        config.setEnableMethodAnnotation(true);
        return config;
    }

    @Test
    public void testSafeUpdate() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExample(new Country(), new Example(Country.class));
            }
        });
    }

    @Test
    public void testSafeUpdateNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExample(new Country(), null);
            }
        });
    }

    @Test
    public void testSafeUpdateNull2() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExample(null, null);
            }
        });
    }

    @Test
    public void testSafeUpdateByExample() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExampleSelective(new Country(), new Example(Country.class));
            }
        });
    }

    @Test
    public void testSafeUpdateByExampleNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExampleSelective(new Country(), null);
            }
        });
    }
}
