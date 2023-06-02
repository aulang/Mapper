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

public class SafeDeleteByMethodTest extends BaseTest {

    @Override
    protected Config getConfig() {
        Config config = super.getConfig();
        config.setSafeDelete(true);
        //和 SafeDeleteByFieldTest 测试的区别在此，这里将会使后面调用 EntityField.getValue 时，使用 getter 方法获取值
        config.setEnableMethodAnnotation(true);
        return config;
    }

    @Test
    public void testSafeDelete() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.delete(new Country());
            } finally {
                sqlSession.close();
            }
        });

    }

    @Test
    public void testSafeDeleteNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.delete(null);
            } finally {
                sqlSession.close();
            }
        });
    }


    @Test
    public void testSafeDeleteByExample() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.deleteByExample(new Example(Country.class));
            } finally {
                sqlSession.close();
            }
        });
    }

    @Test
    public void testSafeDeleteByExampleNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            SqlSession sqlSession = getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.deleteByExample(null);
            } finally {
                sqlSession.close();
            }
        });

    }

}
