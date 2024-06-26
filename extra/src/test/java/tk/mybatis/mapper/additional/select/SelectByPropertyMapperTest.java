package tk.mybatis.mapper.additional.select;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.additional.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SelectByPropertyMapperTest extends BaseTest {

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
    public void selectOneByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            Book book = mapper.selectOneByProperty(Book::getName, "JavaStarter1");
            Assertions.assertNotNull(book);
            Assertions.assertEquals("JavaStarter1", book.getName());
        }
    }

    @Test
    public void selectByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            List<Book> books = mapper.selectByProperty(Book::getPrice, 50);
            Assertions.assertEquals(2, books.size());
        }
    }

    @Test
    public void selectInByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            List<Book> books = mapper.selectInByProperty(Book::getPrice, Arrays.asList(50, 80));
            Assertions.assertEquals(3, books.size());
        }
    }

    @Test
    public void selectBetweenByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            List<Book> books = mapper.selectBetweenByProperty(Book::getPublished, LocalDate.of(2015, 11, 11),
                    LocalDate.of(2019, 11, 11));
            Assertions.assertEquals(4, books.size());
        }
    }

    @Test
    public void selectCountByPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            int count = mapper.selectCountByProperty(Book::getPrice, 50);
            Assertions.assertEquals(2, count);
        }
    }

    @Test
    public void existsWithPropertyTest() {
        try (SqlSession sqlSession = getSqlSession()) {
            BookMapper mapper = sqlSession.getMapper(BookMapper.class);
            boolean exist = mapper.existsWithProperty(Book::getPrice, 50);
            Assertions.assertTrue(exist);
        }
    }
}
