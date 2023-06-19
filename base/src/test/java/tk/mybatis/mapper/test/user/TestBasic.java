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
import tk.mybatis.mapper.mapper.UserInfoMapper;
import tk.mybatis.mapper.model.UserInfo;

import java.util.List;

/**
 * 测试增删改查
 *
 * @author liuzh
 */
public class TestBasic {


    /**
     * 新增
     */
    @Test
    public void testInsert() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername("abel533");
            userInfo.setPassword("123456");
            userInfo.setUsertype("2");
            userInfo.setEmail("abel533@gmail.com");

            Assertions.assertEquals(1, mapper.insert(userInfo));

            Assertions.assertNotNull(userInfo.getId());
            Assertions.assertTrue(userInfo.getId() >= 6);

            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(userInfo));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    /**
     * 主要测试删除
     */
    @Test
    public void testDelete() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            //查询总数
            Assertions.assertEquals(5, mapper.selectCount(new UserInfo()));
            //查询100
            UserInfo userInfo = mapper.selectByPrimaryKey(1);


            //根据主键删除
            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(1));


            //查询总数
            Assertions.assertEquals(4, mapper.selectCount(new UserInfo()));
            //插入
            Assertions.assertEquals(1, mapper.insert(userInfo));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    /**
     * 查询
     */
    @Test
    public void testSelect() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = new UserInfo();
            userInfo.setUsertype("1");
            List<UserInfo> userInfos = mapper.select(userInfo);
            Assertions.assertEquals(3, userInfos.size());
        }
    }

    /**
     * 根据主键全更新
     */
    @Test
    public void testUpdateByPrimaryKey() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = mapper.selectByPrimaryKey(2);
            Assertions.assertNotNull(userInfo);
            userInfo.setUsertype(null);
            userInfo.setEmail("abel533@gmail.com");
            //不会更新username
            Assertions.assertEquals(1, mapper.updateByPrimaryKey(userInfo));

            userInfo = mapper.selectByPrimaryKey(userInfo);
            Assertions.assertNull(userInfo.getUsertype());
            Assertions.assertEquals("abel533@gmail.com", userInfo.getEmail());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    /**
     * 根据主键更新非null
     */
    @Test
    public void testUpdateByPrimaryKeySelective() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            UserInfoMapper mapper = sqlSession.getMapper(UserInfoMapper.class);
            UserInfo userInfo = mapper.selectByPrimaryKey(1);
            Assertions.assertNotNull(userInfo);
            userInfo.setUsertype(null);
            userInfo.setEmail("abel533@gmail.com");
            //不会更新username
            Assertions.assertEquals(1, mapper.updateByPrimaryKeySelective(userInfo));

            userInfo = mapper.selectByPrimaryKey(1);
            Assertions.assertEquals("1", userInfo.getUsertype());
            Assertions.assertEquals("abel533@gmail.com", userInfo.getEmail());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


}
