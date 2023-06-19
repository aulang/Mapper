package tk.mybatis.mapper.additional.update.differ;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.additional.BaseTest;
import tk.mybatis.mapper.additional.Country;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

public class UpdateByDifferMapperTest extends BaseTest {

    /**
     * 获取 mybatis 配置
     *
     * @return
     */
    protected Reader getConfigFileAsReader() throws IOException {
        URL url = getClass().getResource("mybatis-config.xml");
        return toReader(url);
    }

    @Test
    public void testUpdateByDiffer() {
        try (SqlSession sqlSession = getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country old = mapper.selectByPrimaryKey(1L);
            //(1, 'Angola', 'AO', 1)
            Country newer = new Country();
            newer.setId(1L);
            newer.setCountryname("Newer");
            newer.setCountrycode("AO");
            int count = mapper.updateByDiffer(old, newer);
            Assertions.assertEquals(1, count);
            old = mapper.selectByPrimaryKey(1L);
            Assertions.assertEquals(1L, old.getId().longValue());
            Assertions.assertEquals("Newer", old.getCountryname());
            Assertions.assertEquals("AO", old.getCountrycode());
        }
    }
}
