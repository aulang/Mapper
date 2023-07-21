/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2017 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.mapper.test.example;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.model.CountryExample;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.model.Country2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liuzh
 */
public class TestSelectByExample {

    @Test
    public void testSelectByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleException() {
        Assertions.assertThrows(Exception.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country2.class);
                example.createCriteria().andGreaterThan("id", 100);
                mapper.selectByExample(example);
            } finally {
                sqlSession.close();
            }
        });
    }

    @Test
    public void testSelectByExampleForUpdate() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.setForUpdate(true);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAndExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria()
                    .andCondition("countryname like 'C%' and id < 100")
                    .andCondition("length(countryname) = ", 5);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(3, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleInNotIn() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            Set<Integer> set = new HashSet<Integer>();
            set.addAll(Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}));
            example.createCriteria().andIn("id", set)
                    .andNotIn("id", Arrays.asList(new Object[]{11}));
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(10, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExampleInNotIn2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andIn("id", Arrays.asList(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}))
                    .andNotIn("id", Arrays.asList(new Object[]{11}));
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(10, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample2() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andLike("countryname", "A%");
            example.or().andGreaterThan("id", 100);
            example.setDistinct(true);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(true, countries.size() > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample3() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            CountryExample example = new CountryExample();
            example.createCriteria().andCountrynameLike("A%");
            example.or().andIdGreaterThan(100);
            example.setDistinct(true);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(true, countries.size() > 83);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByExample4() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            Country ct = new Country();
            ct.setCountryname("China");

            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 20).andEqualTo(ct);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            System.out.println(countries.get(0).toString());
            Assertions.assertEquals(1, countries.size());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectColumnsByExample() {
        Assertions.assertThrows(MapperException.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country.class);
                example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
                example.or().andLessThan("id", 41);
                example.selectProperties("id", "countryname", "hehe");
                List<Country> countries = mapper.selectByExample(example);
                //查询总数
                Assertions.assertEquals(90, countries.size());
            } finally {
                sqlSession.close();
            }
        }, "类 Country 不包含属性 'hehe'，或该属性被@Transient注释！");
    }

    @Test
    public void testExcludeColumnsByExample() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            example.excludeProperties("id");
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(90, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testAndOr() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.createCriteria().andGreaterThan("id", 100).andLessThan("id", 151);
            example.or().andLessThan("id", 41);
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(90, countries.size());

            //当不使用条件时，也不能出错
            example = new Example(Country.class);
            example.createCriteria();
            example.or();
            example.and();
            countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(183, countries.size());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testOrderBy() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
//            example.setOrderByClause("id desc");
            example.orderBy("id").desc().orderBy("countryname").orderBy("countrycode").asc();
            List<Country> countries = mapper.selectByExample(example);
            //查询总数
            Assertions.assertEquals(183, (int) countries.get(0).getId());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 指定查询字段正确
     */
    @Test
    public void testSelectPropertisCheckCorrect() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = new Example(Country.class);
            example.selectProperties(new String[]{"countryname"});
            example.createCriteria().andEqualTo("id", 35);
            List<Country> country1 = mapper.selectByExample(example);
            Assertions.assertEquals(null, country1.get(0).getId());
            Assertions.assertEquals("China", country1.get(0).getCountryname());
            Assertions.assertEquals(null, country1.get(0).getCountrycode());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 指定查询字段拼写错误或不存在
     */
    @Test
    public void testSelectPropertisCheckSpellWrong() {
        Assertions.assertThrows(MapperException.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country.class);
                example.selectProperties(new String[]{"countrymame"});
                example.createCriteria().andEqualTo("id", 35);
                List<Country> country2 = mapper.selectByExample(example);
                Assertions.assertEquals(null, country2.get(0).getId());
                Assertions.assertEquals("China", country2.get(0).getCountryname());
                Assertions.assertEquals(null, country2.get(0).getCountrycode());
            } finally {
                sqlSession.close();
            }
        }, "类 Country 不包含属性 'countrymame'，或该属性被@Transient注释！");
    }

    /**
     * 指定查询字段为@Transient注释字段
     */
    @Test
    public void testSelectPropertisCheckTransient1() {
        Assertions.assertThrows(MapperException.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country.class);
                example.selectProperties(new String[]{"name"});
                example.createCriteria().andEqualTo("id", 35);
                List<Country> country = mapper.selectByExample(example);
            } finally {
                sqlSession.close();
            }
        }, "类 Country 不包含属性 'name'，或该属性被@Transient注释！");
    }

    /**
     * 指定查询字段为@Transient注释字段
     */
    @Test
    public void testSelectPropertisCheckTransient2() {
        Assertions.assertThrows(MapperException.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country.class);
                example.selectProperties(new String[]{"dynamicTableName123"});
                example.createCriteria().andEqualTo("id", 35);
                List<Country> country = mapper.selectByExample(example);
            } finally {
                sqlSession.close();
            }
        }, "类 Country 不包含属性 'dynamicTableName123'，或该属性被@Transient注释！");
    }

    /**
     * 指定排除的查询字段不存在或拼写错误
     */
    @Test
    public void testExcludePropertisCheckWrongSpell() {
        Assertions.assertThrows(MapperException.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country.class);
                example.excludeProperties(new String[]{"countrymame"});
                example.createCriteria().andEqualTo("id", 35);
                List<Country> country = mapper.selectByExample(example);
            } finally {
                sqlSession.close();
            }
        }, "类 Country 不包含属性 'countrymame'，或该属性被@Transient注释！");

    }

    /**
     * 指定排除的查询字段为@Transient注释字段
     */
    @Test
    public void testExcludePropertisCheckTransient() {
        Assertions.assertThrows(MapperException.class, () -> {
            SqlSession sqlSession = MybatisHelper.getSqlSession();
            try {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Example example = new Example(Country.class);
                example.excludeProperties(new String[]{"dynamicTableName123"});
                example.createCriteria().andEqualTo("id", 35);
                List<Country> country = mapper.selectByExample(example);
            } finally {
                sqlSession.close();
            }
        }, "类 Country 不包含属性 'dynamicTableName123'，或该属性被@Transient注释！");
    }
}
