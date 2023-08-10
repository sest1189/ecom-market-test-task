package org.ecom.market.test.task.ecommarkettesttask.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "org.ecom.market.test.task.ecommarkettesttask")
@PropertySource("classpath:application.properties")
public class SpringConfig {
    public static final String APP_PROPERTIES = "application.properties";

    @ConfigurationProperties
    @Bean(name = APP_PROPERTIES)
    public Properties getProperties() {
        return new Properties();
    }

}
