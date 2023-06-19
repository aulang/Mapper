package tk.mybatis.mapper.base.delete;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;
import tk.mybatis.mapper.base.CountryMapper;

public class DeleteByPrimaryKeyMapperTest extends BaseTest {

    @Test
    public void testDeleteByPrimaryKey() {
        try (SqlSession sqlSession = getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Assertions.assertEquals(183, mapper.selectAll().size());
            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(1L));
            Assertions.assertEquals(182, mapper.selectAll().size());

            Assertions.assertEquals(1, mapper.deleteByPrimaryKey(2));
            Assertions.assertEquals(181, mapper.selectAll().size());

            Assertions.assertEquals(1, mapper.deleteByPrimaryKey("3"));
            Assertions.assertEquals(180, mapper.selectAll().size());

            Assertions.assertEquals(0, mapper.deleteByPrimaryKey(1));
            Assertions.assertEquals(180, mapper.selectAll().size());
        }
    }
}
