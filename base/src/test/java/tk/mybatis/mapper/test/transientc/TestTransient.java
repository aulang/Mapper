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

package tk.mybatis.mapper.test.transientc;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.mapper.CountryTMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.CountryT;

import java.util.List;

/**
 * Created by liuzh on 2014/11/21.
 */
public class TestTransient {
    /**
     * 插入完整数据
     */
    @Test
    public void testDynamicInsert() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryTMapper mapper = sqlSession.getMapper(CountryTMapper.class);
            CountryT country = new CountryT();
            country.setId(10086);
            country.setCountrycode("CN");
            country.setCountryname("天朝");
            Assertions.assertEquals(1, mapper.insert(country));

            //查询CN结果
            country = new CountryT();
            country.setCountrycode("CN");
            List<CountryT> list = mapper.select(country);

            Assertions.assertEquals(2, list.size());
            //屏蔽的数据是null
            Assertions.assertNull(list.get(0).getCountrycode());
            //删除插入的数据,以免对其他测试产生影响
            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(10086));
        }
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicUpdateByPrimaryKey() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryTMapper mapper = sqlSession.getMapper(CountryTMapper.class);
            CountryT country = new CountryT();
            country.setId(174);
            country.setCountryname("美国");
            country.setCountrycode("US");
            Assertions.assertEquals(1, mapper.updateByPrimaryKey(country));

            country = mapper.selectByPrimaryKey(174);
            Assertions.assertNotNull(country);
            Assertions.assertEquals(174, (int) country.getId());
            Assertions.assertEquals("美国", country.getCountryname());
            Assertions.assertNull(country.getCountrycode());
        }
    }

    /**
     * 根据查询条件进行查询
     */
    @Test
    public void testDynamicSelect() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryTMapper mapper = sqlSession.getMapper(CountryTMapper.class);
            CountryT country = new CountryT();
            country.setId(174);
            country.setCountrycode("US");
            List<CountryT> countryList = mapper.select(country);

            Assertions.assertEquals(1, countryList.size());
            Assertions.assertEquals(174, (int) countryList.get(0).getId());
            Assertions.assertNotNull(countryList.get(0).getCountryname());
            Assertions.assertNull(countryList.get(0).getCountrycode());
        }
    }
}
