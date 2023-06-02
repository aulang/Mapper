package tk.mybatis.mapper.base.delete;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.Example;

public class SafeDeleteByFieldTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setSafeDelete(true);
        return config;
    }

    @Test
    public void testSafeDelete() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.delete(new Country());
            }
        });

    }

    @Test
    public void testSafeDeleteNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.delete(null);
            }
        });

    }

    @Test
    public void testSafeDeleteByExample() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.deleteByExample(new Example(Country.class));
            }
        });

    }

    @Test
    public void testSafeDeleteByExampleNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.deleteByExample(null);
            }
        });
    }
}
