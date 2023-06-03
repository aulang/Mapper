# mapper-weekend

可以在 `Example.Criteria` 的条件方法里传 lambada

列子：

```java
UserMapper    userMapper = sqlSession.getMapper(UserMapper.class);
Weekend<User> weekend    = Weekend.of(User.class);
weekend.weekendCriteria()
      .andIsNull(User::getId)
      .andBetween(User::getId,0,10)
      .andIn(User::getUserName, Arrays.asList("a","b","c"));
```

和

```java
CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
List<Country> selectByWeekendSql = mapper.selectByExample(new Example.Builder(Country.class)
        .where(WeekendSqls.<Country>custom().andLike(Country::getCountryname, "China")).build());
```