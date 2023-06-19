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

package tk.mybatis.mapper.test.country;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;

/**
 * 通过实体类属性进行查询
 *
 * @author liuzh
 */
public class TestSelectOne {

    /**
     * 查询全部
     */
    @Test
    public void testDynamicSelectAll() {
        Assertions.assertThrows(TooManyResultsException.class, () -> {
            try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Country country = mapper.selectOne(new Country());
            }
        });

    }

    /**
     * 入参为null时查询全部
     */
    @Test
    public void testDynamicSelectAllByNull() {
        Assertions.assertThrows(TooManyResultsException.class, () -> {
            try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                mapper.selectOne(null);
            }
        });
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicSelect() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountrycode("CN");
            Country result = mapper.selectOne(country);

            Assertions.assertEquals(35, (int) result.getId());
            Assertions.assertEquals("China", result.getCountryname());
        }
    }

    /**
     * 查询不存在的结果
     */
    @Test
    public void testDynamicSelectZero() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setCountrycode("CN");
            country.setCountryname("天朝");//实际上是 China
            Country result = mapper.selectOne(country);

            Assertions.assertNull(result);
        }
    }
}
