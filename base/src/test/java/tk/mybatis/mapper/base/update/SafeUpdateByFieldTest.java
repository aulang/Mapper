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

public class SafeUpdateByFieldTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setSafeUpdate(true);
        return config;
    }

    @Test
    public void testSafeUpdate() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExample(new Country(), new Example(Country.class));
            } finally {
                sqlSession.close();
            }
        });
    }

    @Test
    public void testSafeUpdateNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExample(new Country(), null);
            } finally {
                sqlSession.close();
            }
        });
    }

    @Test
    public void testSafeUpdateNull2() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExample(null, null);
            } finally {
                sqlSession.close();
            }
        });
    }

    @Test
    public void testSafeUpdateByExample() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExampleSelective(new Country(), new Example(Country.class));
            } finally {
                sqlSession.close();
            }
        });
    }

    @Test
    public void testSafeUpdateByExampleNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.updateByExampleSelective(new Country(), null);
            } finally {
                sqlSession.close();
            }
        });
    }

}
