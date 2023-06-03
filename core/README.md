# MyBatis 通用 Mapper

## 通用 Mapper - 简单用法示例

全部针对单表操作，每个实体类都需要继承通用Mapper接口来获得通用方法。

示例代码：

    CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
    //查询全部
    List<Country> countryList = mapper.select(new Country());
    //总数
    Assert.assertEquals(183, countryList.size());

    //通用Example查询
    Example example = new Example(Country.class);
    example.createCriteria().andGreaterThan("id", 100);
    countryList = mapper.selectByExample(example);
    Assert.assertEquals(83, countryList.size());

    //MyBatis-Generator生成的Example查询
    CountryExample example2 = new CountryExample();
    example2.createCriteria().andIdGreaterThan(100);
    countryList = mapper.selectByExample(example2);
    Assert.assertEquals(83, countryList.size());

CountryMapper代码如下：

    public interface CountryMapper extends Mapper<Country> {
    }

这里不说更具体的内容，如果您有兴趣，可以查看下面的<b>项目文档</b>

## 实体类注解

从上面效果来看也能感觉出这是一种类似hibernate的用法，因此也需要实体和表对应起来，因此使用了JPA注解。更详细的内容可以看下面的<b>项目文档</b>。

Country代码：

    public class Country {
        @Id
        private Integer id;
        @Column
        private String countryname;
        private String countrycode;
        //省略setter和getter方法
    }

可以方便的生成这些（带注解的）实体类。