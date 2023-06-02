package tk.mybatis.mapper.test.example;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuyi
 * @date 2017/11/18
 */
public class TestExampleBuilder {

    @Test
    public void testExampleBuilder() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class).build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(183, countries.size());

            // 下面的查询会有缓存
            Example example0 = Example.builder(Country.class)
                    .select().build();
            List<Country> countries0 = mapper.selectByExample(example0);
            Assertions.assertEquals(183, countries0.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testDistinct() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .distinct()
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(183, countries.size());

            // distinct和order by冲突问题
            Example example0 = Example.builder(Country.class)
                    .selectDistinct("id", "countryname").build();
            List<Country> countries0 = mapper.selectByExample(example0);
            Assertions.assertEquals(183, countries0.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testForUpdate() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .select("countryname")
                    .where(Sqls.custom().andGreaterThan("id", 100))
                    .orderByAsc("countrycode")
                    .forUpdate()
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(83, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testEqualTo() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andEqualTo("id", "35"))
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country = countries.get(0);
            Assertions.assertEquals(Integer.valueOf(35), country.getId());
            Assertions.assertEquals("China", country.getCountryname());
            Assertions.assertEquals("CN", country.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testBetween() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andBetween("id", 34, 35))
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country35 = countries.get(0);
            Assertions.assertEquals(Integer.valueOf(35), country35.getId());
            Assertions.assertEquals("China", country35.getCountryname());
            Assertions.assertEquals("CN", country35.getCountrycode());

            Country country34 = countries.get(1);
            Assertions.assertEquals(Integer.valueOf(34), country34.getId());
            Assertions.assertEquals("Chile", country34.getCountryname());
            Assertions.assertEquals("CL", country34.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testIn() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andIn("id", new ArrayList<Integer>(Arrays.asList(35, 183))))
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country35 = countries.get(1);
            Assertions.assertEquals(Integer.valueOf(35), country35.getId());
            Assertions.assertEquals("China", country35.getCountryname());
            Assertions.assertEquals("CN", country35.getCountrycode());

            Country country183 = countries.get(0);
            Assertions.assertEquals(Integer.valueOf(183), country183.getId());
            Assertions.assertEquals("Zambia", country183.getCountryname());
            Assertions.assertEquals("ZM", country183.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    /*
     * @description: 单个where组合查询测试
     * 直接把example的构造放到selectByExample()函数里
     * */
    @Test
    public void testWhereCompound0() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            List<Country> countries = mapper.selectByExample(
                    Example.builder(Country.class)
                            .where(Sqls.custom()
                                    .andEqualTo("countryname", "China")
                                    .andEqualTo("id", 35)
                                    .orIn("id", new ArrayList<Integer>(Arrays.asList(35, 183)))
                                    .orLike("countryname", "Ye%")
                            )
                            .build());
            Country country35 = countries.get(2);
            Assertions.assertEquals(Integer.valueOf(35), country35.getId());
            Assertions.assertEquals("China", country35.getCountryname());
            Assertions.assertEquals("CN", country35.getCountrycode());

            Country country183 = countries.get(0);
            Assertions.assertEquals(Integer.valueOf(183), country183.getId());
            Assertions.assertEquals("Zambia", country183.getCountryname());
            Assertions.assertEquals("ZM", country183.getCountrycode());

            Country country179 = countries.get(1);
            Assertions.assertEquals(Integer.valueOf(179), country179.getId());
            Assertions.assertEquals("Yemen", country179.getCountryname());
            Assertions.assertEquals("YE", country179.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    /*
     * @description: 单个where组合查询测试
     * */
    @Test
    public void testWhereCompound1() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom()
                            .andBetween("id", 35, 50)
                            .orLessThan("id", 40)
                            .orIsNull("countryname")
                    )
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(50, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    /*
     *   @description: 多个where连接的查询语句测试
     * */
    @Test
    public void testWhereAndWhereCompound() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom()
                            .andEqualTo("countryname", "China")
                            .andEqualTo("id", 35)
                    )
                    .andWhere(Sqls.custom()
                            .andEqualTo("id", 183)
                    )
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(0, countries.size());

        } finally {
            sqlSession.close();
        }
    }

    /*
     *   @description: 多个where连接的查询语句测试
     * */
    @Test
    public void testWhereOrWhereCompound() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom()
                            .andEqualTo("countryname", "China")
                            .andEqualTo("id", 35)
                    )
                    .orWhere(Sqls.custom()
                            .andEqualTo("id", 183)
                    )
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(2, countries.size());

        } finally {
            sqlSession.close();
        }
    }

    /*
     *   @description: 多个where连接的查询语句测试
     * */
    @Test
    public void testMultiWhereCompound() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .selectDistinct()
                    .where(Sqls.custom()
                            .andEqualTo("countryname", "China")
                            .andEqualTo("id", 35)
                    )
                    .orWhere(Sqls.custom()
                            .andBetween("countryname", 'C', 'H')
                            .andNotLike("countryname", "Co%")
                    )
                    .andWhere(Sqls.custom()
                            .andLessThan("id", "100")
                            .orGreaterThan("id", "55")
                    )
                    .orWhere(Sqls.custom()
                            .andEqualTo("countryname", "Cook Is.")
                    )
                    .orderByAsc("id", "countryname")
                    .orderByDesc("countrycode")
                    .forUpdate()
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assertions.assertEquals(35, countries.size());

        } finally {
            sqlSession.close();
        }
    }

    /*
     *  @description: 测试order by
     *  orderBy()默认为Asc（升序），与orderByAsc()一样
     * */
    @Test
    public void testOrderBy() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andBetween("id", 50, 55))
                    .orderBy("id").orderByAsc("countryname").orderByDesc("countrycode")
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            for (Country country : countries) {
                System.out.println(country.getId() + " " + country.getCountryname() + " " + country.getCountrycode());
            }
            Assertions.assertEquals(6, countries.size());
        } finally {
            sqlSession.close();
        }
    }
}
