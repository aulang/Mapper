package tk.mybatis.mapper.cache;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.Country;
import tk.mybatis.mapper.base.CountryMapper;

import java.io.IOException;
import java.io.Reader;

/**
 * @author liuzh
 */
public class CacheTest extends BaseTest {

    @Override
    protected Reader getConfigFileAsReader() throws IOException {
        return toReader(CacheTest.class.getResource("mybatis-config-cache.xml"));
    }

    @Test
    public void testNoCache() {
        try (SqlSession sqlSession = getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
            //由于 CountryMapper 没有使用二级缓存，因此下面的设置不会影响下次（不同的 SqlSession）查询
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        }

        //下面获取新的 sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = mapper.selectByPrimaryKey(35);

            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());

            Assertions.assertNotEquals("中国", country.getCountryname());
            Assertions.assertNotEquals("ZH", country.getCountrycode());
        }
    }

    @Test
    public void testSingleInterfaceCache() {
        //利用二级缓存的脏数据特性来验证二级缓存
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheMapper mapper = sqlSession.getMapper(CountryCacheMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        }

        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheMapper mapper = sqlSession.getMapper(CountryCacheMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("中国", country.getCountryname());
            Assertions.assertEquals("ZH", country.getCountrycode());
        }

        //下面清空缓存再试
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheMapper mapper = sqlSession.getMapper(CountryCacheMapper.class);
            //调用 update 清空缓存
            mapper.updateByPrimaryKey(new Country());
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
        }
    }

    @Test
    public void testCountryCacheRefMapper() {
        //--------------------selectByPrimaryKey---------------------
        //利用二级缓存的脏数据特性来验证二级缓存
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        }

        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("中国", country.getCountryname());
            Assertions.assertEquals("ZH", country.getCountrycode());
        }

        //--------------------selectById---------------------
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectById(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        }

        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            Country country = mapper.selectById(35);
            Assertions.assertEquals("中国", country.getCountryname());
            Assertions.assertEquals("ZH", country.getCountrycode());
        }

        //下面清空缓存再试
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheRefMapper mapper = sqlSession.getMapper(CountryCacheRefMapper.class);
            //调用 update 清空缓存
            mapper.updateByPrimaryKey(new Country());
            Country country = mapper.selectById(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
        }
    }

    @Test
    @Disabled("MyBatis 有 Bug，这种方式目前行不通")
    public void testCountryCacheWithXmlMapper() {
        //--------------------selectByPrimaryKey---------------------
        //利用二级缓存的脏数据特性来验证二级缓存
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        }

        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectByPrimaryKey(35);
            Assertions.assertEquals("中国", country.getCountryname());
            Assertions.assertEquals("ZH", country.getCountrycode());
        }

        //--------------------selectById---------------------
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectById(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
            //这里修改会产生脏数据，这么做只是为了验证二级缓存
            country.setCountryname("中国");
            country.setCountrycode("ZH");
        }

        //前面 sqlSession.close() 后就会缓存，下面获取新的 sqlSession
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            Country country = mapper.selectById(35);
            Assertions.assertEquals("中国", country.getCountryname());
            Assertions.assertEquals("ZH", country.getCountrycode());
        }

        //下面清空缓存再试
        try (SqlSession sqlSession = getSqlSession()) {
            CountryCacheWithXmlMapper mapper = sqlSession.getMapper(CountryCacheWithXmlMapper.class);
            //调用 update 清空缓存
            mapper.updateByPrimaryKey(new Country());
            Country country = mapper.selectById(35);
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());
        }
    }
}
