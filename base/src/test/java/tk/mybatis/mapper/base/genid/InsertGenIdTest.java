package tk.mybatis.mapper.base.genid;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tk.mybatis.mapper.base.BaseTest;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;

/**
 * @author liuzh
 */
public class InsertGenIdTest extends BaseTest {
    private final String[][] countries = new String[][]{
            {"Angola", "AO"},
            {"Afghanistan", "AF"},
            {"Albania", "AL"},
            {"Algeria", "DZ"},
            {"Andorra", "AD"},
            {"Anguilla", "AI"},
            {"Antigua and Barbuda", "AG"},
            {"Argentina", "AR"},
            {"Armenia", "AM"},
            {"Australia", "AU"},
            {"Austria", "AT"},
            {"Azerbaijan", "AZ"},
            {"Bahamas", "BS"},
            {"Bahrain", "BH"},
            {"Bangladesh", "BD"},
            {"Barbados", "BB"},
            {"Belarus", "BY"},
            {"Belgium", "BE"},
            {"Belize", "BZ"},
            {"Benin", "BJ"},
            {"Bermuda Is.", "BM"},
            {"Bolivia", "BO"},
            {"Botswana", "BW"},
            {"Brazil", "BR"},
            {"Brunei", "BN"},
            {"Bulgaria", "BG"},
            {"Burkina-faso", "BF"},
            {"Burma", "MM"},
            {"Burundi", "BI"},
            {"Cameroon", "CM"},
            {"Canada", "CA"},
            {"Central African Republic", "CF"},
            {"Chad", "TD"},
            {"Chile", "CL"},
            {"China", "CN"}
    };

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
    public void testGenId() {
        try (SqlSession sqlSession = getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            for (String[] strings : countries) {
                Country country = new Country(strings[0], strings[1]);
                Assertions.assertEquals(1, mapper.insert(country));
                Assertions.assertNotNull(country.getId());
                System.out.println(country.getId());
            }
        }
    }

    @Test
    public void testGenIdWithExistsId() {
        try (SqlSession sqlSession = getSqlSession()) {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Country country = new Country("test", "T");
            country.setId(9999L);
            Assertions.assertEquals(1, mapper.insert(country));
            Assertions.assertNotNull(country.getId());
            Assertions.assertEquals(Long.valueOf(9999), country.getId());
            System.out.println(country.getId());
        }
    }


    @Test
    public void testUUID() {
        try (SqlSession sqlSession = getSqlSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            for (String[] country : countries) {
                User user = new User(country[0], country[1]);
                Assertions.assertEquals(1, mapper.insert(user));
                Assertions.assertNotNull(user.getId());
                System.out.println(user.getId());
            }
        }
    }
}
