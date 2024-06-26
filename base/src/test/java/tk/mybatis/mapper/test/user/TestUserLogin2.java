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

package tk.mybatis.mapper.test.user;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserLogin2Mapper;
import tk.mybatis.mapper.model.UserLogin2;
import tk.mybatis.mapper.model.UserLogin2Key;

import java.util.Date;
import java.util.List;

/**
 * 通过主键删除
 *
 * @author liuzh
 */
public class TestUserLogin2 {


    /**
     * 新增
     */
    @Test
    public void testInsert() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);

            UserLogin2 userLogin = new UserLogin2();
            userLogin.setUsername("abel533");
            userLogin.setLogindate(new Date());
            userLogin.setLoginip("192.168.123.1");

            Assertions.assertEquals(1, mapper.insert(userLogin));

            Assertions.assertNotNull(userLogin.getLogid());
            Assertions.assertTrue(userLogin.getLogid() > 10);
            //这里测了实体类入参的删除
            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(userLogin));
        }
    }

    /**
     * 主要测试删除
     */
    @Test
    public void testDelete() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);
            //查询总数
            Assertions.assertEquals(10, mapper.selectCount(new UserLogin2()));
            //根据主键查询
            UserLogin2Key key = new UserLogin2();
            key.setLogid(1);
            key.setUsername("test1");
            UserLogin2 userLogin = mapper.selectByPrimaryKey(key);
            //根据主键删除
            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(key));

            //查询总数
            Assertions.assertEquals(9, mapper.selectCount(new UserLogin2()));
            //插入
            Assertions.assertEquals(1, mapper.insert(userLogin));
        }
    }


    /**
     * 查询
     */
    @Test
    public void testSelect() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);
            UserLogin2 userLogin = new UserLogin2();
            userLogin.setUsername("test1");
            List<UserLogin2> userLogins = mapper.select(userLogin);
            Assertions.assertEquals(5, userLogins.size());
        }
    }

    /**
     * 根据主键全更新
     */
    @Test
    public void testUpdateByPrimaryKey() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);
            UserLogin2Key key = new UserLogin2();
            key.setLogid(2);
            key.setUsername("test1");
            UserLogin2 userLogin = mapper.selectByPrimaryKey(key);
            Assertions.assertNotNull(userLogin);
            userLogin.setLoginip("1.1.1.1");
            userLogin.setLogindate(null);
            //不会更新username
            Assertions.assertEquals(1, mapper.updateByPrimaryKey(userLogin));

            userLogin = mapper.selectByPrimaryKey(userLogin);
            Assertions.assertNull(userLogin.getLogindate());
            Assertions.assertEquals("1.1.1.1", userLogin.getLoginip());
        }
    }

    /**
     * 根据主键更新非null
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserLogin2Mapper mapper = sqlSession.getMapper(UserLogin2Mapper.class);

            UserLogin2Key key = new UserLogin2();
            key.setLogid(1);
            key.setUsername("test1");

            UserLogin2 userLogin = mapper.selectByPrimaryKey(key);
            Assertions.assertNotNull(userLogin);
            userLogin.setLogindate(null);
            userLogin.setLoginip("1.1.1.1");
            //不会更新username
            Assertions.assertEquals(1, mapper.updateByPrimaryKeySelective(userLogin));

            userLogin = mapper.selectByPrimaryKey(key);
            Assertions.assertNotNull(userLogin.getLogindate());
            Assertions.assertEquals("1.1.1.1", userLogin.getLoginip());
        }
    }
}
