package com.example.todoapp.config.security;

import javax.sql.DataSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * This class will keep all non-security Spring Configuration as far as almost everything is autoconfigured
 */
@Configuration
@EnableWebMvc
public class SpringConfiguration {
    @Value("${spring.datasource.driverClassName}")
    private String datasourceClassName;
    @Value("${spring.datasource.url}")
    private String datasourceUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    /**
     * ModelMapper will be used to convert entities to dto and vice versa
     * @return ModelMapper Bean
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public DataSource getDataSource()
    {
        DriverManagerDataSource dataSource =  new DriverManagerDataSource();
        dataSource.setDriverClassName(datasourceClassName);
        dataSource.setUrl(datasourceUrl);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

}
