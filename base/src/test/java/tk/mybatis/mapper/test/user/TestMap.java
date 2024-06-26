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
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.mapper.UserInfoMapMapper;
import tk.mybatis.mapper.model.UserInfoMap;

import java.util.List;

/**
 * 测试增删改查 - 不在直接支持Map
 *
 * @author liuzh
 */
public class TestMap {


    /**
     * 新增
     */
    // @Test
    public void testInsert() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = new UserInfoMap();
            userInfoMap.setUserName("abel533");
            userInfoMap.setPassword("123456");
            userInfoMap.setUserType("2");

            Assertions.assertEquals(1, mapper.insert(userInfoMap));

            Assertions.assertNotNull(userInfoMap.getId());
            Assertions.assertEquals(6, (int) userInfoMap.getId());

            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(userInfoMap));
        }
    }

    /**
     * 主要测试删除
     */
    // @Test
    public void testDelete() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            //查询总数
            Assertions.assertEquals(5, mapper.selectCount(new UserInfoMap()));
            //查询100
            UserInfoMap userInfoMap = mapper.selectByPrimaryKey(1);

            //根据主键删除
            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(1));

            //查询总数
            Assertions.assertEquals(4, mapper.selectCount(new UserInfoMap()));
            //插入
            Assertions.assertEquals(1, mapper.insert(userInfoMap));
        }
    }


    /**
     * 查询
     */
    // @Test
    public void testSelect() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = new UserInfoMap();
            userInfoMap.setUserType("1");
            List<UserInfoMap> UserInfoMaps = mapper.select(userInfoMap);
            Assertions.assertEquals(3, UserInfoMaps.size());
        }
    }

    /**
     * 根据主键全更新
     */
    // @Test
    public void testUpdateByPrimaryKey() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = mapper.selectByPrimaryKey(2);
            Assertions.assertNotNull(userInfoMap);
            userInfoMap.setUserType(null);
            userInfoMap.setRealName("liuzh");
            //不会更新user_type
            Assertions.assertEquals(1, mapper.updateByPrimaryKey(userInfoMap));

            userInfoMap = mapper.selectByPrimaryKey(userInfoMap);
            Assertions.assertNull(userInfoMap.getUserType());
            Assertions.assertEquals("liuzh", userInfoMap.getRealName());
        }
    }

    /**
     * 根据主键更新非null
     */
    // @Test
    public void testUpdateByPrimaryKeySelective() {
        try (SqlSession sqlSession = MybatisHelper.getSqlSession()) {
            UserInfoMapMapper mapper = sqlSession.getMapper(UserInfoMapMapper.class);
            UserInfoMap userInfoMap = mapper.selectByPrimaryKey(1);
            Assertions.assertNotNull(userInfoMap);
            userInfoMap.setUserType(null);
            userInfoMap.setRealName("liuzh");
            //不会更新user_type
            Assertions.assertEquals(1, mapper.updateByPrimaryKeySelective(userInfoMap));

            userInfoMap = mapper.selectByPrimaryKey(1);
            Assertions.assertEquals("1", userInfoMap.getUserType());
            Assertions.assertEquals("liuzh", userInfoMap.getRealName());
        }
    }
}
