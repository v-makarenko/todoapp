package com.example.todoapp.config.security;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
        throws Exception {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery(usersQuery)
            .authoritiesByUsernameQuery(rolesQuery)
            .passwordEncoder(encoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter()
        throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter = new CustomUsernamePasswordAuthenticationFilter();
        customUsernamePasswordAuthenticationFilter
            .setAuthenticationManager(authenticationManagerBean());
        customUsernamePasswordAuthenticationFilter
            .setAuthenticationSuccessHandler(customSuccessHandler());
        return customUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public CustomAuthenticationSuccessHandler customSuccessHandler() {
        CustomAuthenticationSuccessHandler customSuccessHandler = new CustomAuthenticationSuccessHandler();
        return customSuccessHandler;
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler customFailureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
        return failureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
            .successHandler(customSuccessHandler())
            .failureHandler(customFailureHandler())
            .and()
            .headers()
            .frameOptions()
            .sameOrigin()
            .and()
            .csrf()
            .disable()
            .addFilter(customUsernamePasswordAuthenticationFilter())
            .authorizeRequests()
            .antMatchers("/login")
            .permitAll()
            .antMatchers("/users/register")
            .permitAll()
            .anyRequest()
            .authenticated();
    }
}