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

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;

/**
 * 通过主键查询
 *
 * @author liuzh
 */
public class TestExistsWithPrimaryKey {

    /**
     * 根据PK进行查询
     */
    @Test
    public void testDynamicExistsWithPrimaryKey() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country();
            country.setId(35);
            Assertions.assertTrue(mapper.existsWithPrimaryKey(country));

            country.setId(0);
            Assertions.assertFalse(mapper.existsWithPrimaryKey(country));
        }
    }
}
