package tk.mybatis.mapper.additional.delete;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.additional.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DeleteByPropertyMapperTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    }

    /**
     * 获取初始化 sql
     *
     * @return
     */
    protected Reader getSqlFileAsReader() throws IOException {
        URL url = getClass().getResource("CreateDB.sql");
        return toReader(url);
    }

    @Test
    public void deleteByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

            Course beforeDelete = mapper.selectByPrimaryKey(2);
            Assertions.assertNotNull(beforeDelete);
            Assertions.assertEquals("JavaStarter2", beforeDelete.getName());

            int deletedCount = mapper.deleteByProperty(Course::getName, "JavaStarter2");
            Assertions.assertEquals(1, deletedCount);

            Course afterDelete = mapper.selectByPrimaryKey(2);
            Assertions.assertNull(afterDelete);
        }
    }

    @Test
    public void deleteInByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

            List<Course> beforeDelete = mapper.selectAll();
            Assertions.assertEquals(4, beforeDelete.size());

            int deletedCount = mapper.deleteInByProperty(Course::getPrice, Arrays.asList(50, 80, 100));

            Assertions.assertEquals(4, deletedCount);

            List<Course> afterDelete = mapper.selectAll();
            Assertions.assertEquals(0, afterDelete.size());
        }
    }

    @Test
    public void deleteBetweenByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);

            List<Course> beforeDelete = mapper.selectAll();
            Assertions.assertEquals(4, beforeDelete.size());

            int deletedCount = mapper.deleteBetweenByProperty(Course::getPrice, 80, 100);

            Assertions.assertEquals(2, deletedCount);

            List<Course> afterDelete = mapper.selectAll();
            Assertions.assertEquals(2, afterDelete.size());
        }
    }
}
