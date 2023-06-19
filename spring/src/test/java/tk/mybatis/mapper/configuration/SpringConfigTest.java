package tk.mybatis.mapper.configuration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

/**
 * @author liuzh
 */
public class SpringConfigTest {

    @Test
    public void testCountryMapper() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("tk/mybatis/mapper/configuration/spring.xml");
        CountryMapper countryMapper = context.getBean(CountryMapper.class);

        List<Country> countries = countryMapper.selectAll();
        Assertions.assertNotNull(countries);
        Assertions.assertEquals(183, countries.size());
    }
}
