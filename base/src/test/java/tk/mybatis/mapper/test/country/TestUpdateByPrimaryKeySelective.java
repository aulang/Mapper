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

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;

/**
 * 通过PK更新实体类非null属性
 *
 * @author liuzh
 */
public class TestUpdateByPrimaryKeySelective {

    @Test
    public void testDynamicUpdateByPrimaryKeySelectiveAll() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Assertions.assertEquals(0, mapper.updateByPrimaryKeySelective(new Country()));
            }
        });
    }

    @Test
    public void testDynamicUpdateByPrimaryKeySelectiveAllByNull() {
        Assertions.assertThrows(PersistenceException.class, () -> {
            try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
                CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
                Assertions.assertEquals(0, mapper.updateByPrimaryKeySelective(null));
            }
        });
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setId(173);
            country.setCountryname("英国");
            Assertions.assertEquals(1, mapper.updateByPrimaryKeySelective(country));

            country = mapper.selectByPrimaryKey(173);
            Assertions.assertNotNull(country);
            Assertions.assertEquals(173, (int) country.getId());
            Assertions.assertEquals("英国", country.getCountryname());
            Assertions.assertNotNull(country.getCountrycode());
            Assertions.assertEquals("GB", country.getCountrycode());
        }
    }

    /**
     * 继承类可以使用,但多出来的属性无效
     */
    @Test
    public void testDynamicUpdateByPrimaryKeySelectiveNotFoundKeyProperties() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Key key = new Key();
            key.setId(173);
            key.setCountrycode("CN");
            key.setCountrytel("+86");
            Assertions.assertEquals(1, mapper.updateByPrimaryKeySelective(key));
        }
    }

    static class Key extends Country {
        private String countrytel;

        public String getCountrytel() {
            return countrytel;
        }

        public void setCountrytel(String countrytel) {
            this.countrytel = countrytel;
        }
    }
}
