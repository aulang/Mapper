package tk.mybatis.mapper.autoconfigure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tk.mybatis.mapper.page.plugin.PagePlugin;

/**
 * 分页自动配置
 *
 * @author wulang
 */
@Configuration
@EnableConfigurationProperties(MapperProperties.class)
@ConditionalOnProperty(prefix = "mapper.page", name = "enabled", matchIfMissing = true)
public class PageAutoConfiguration {

    private final MapperProperties properties;

    @Autowired
    public PageAutoConfiguration(MapperProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public PagePlugin pagePlugin() {
        return new PagePlugin(properties.getPage().getDialect());
    }
}
