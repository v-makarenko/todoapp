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

/**
 * All security configuration is here
 */
// Candidate should keep all configuration structured and in one place, to be sure it is not scattered all around
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${spring.queries.users-query}")
    private String usersQuery;
    @Value("${spring.queries.roles-query}")
    private String rolesQuery;                                          // We have only one role, so keeping it simple

    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder encoder; // We should not keep our passwords as plain text

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

    /**
     * This custom filter is used to process not only form-based authentication requests, but also JSON-based
      * @return CustomUsernamePasswordAuthenticationFilter Bean instance
     * @throws Exception
     */
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

    /**
     * In case of success authentication this handler will return 200, not 3xx
     */
    @Bean
    public CustomAuthenticationSuccessHandler customSuccessHandler() {
        CustomAuthenticationSuccessHandler customSuccessHandler = new CustomAuthenticationSuccessHandler();
        return customSuccessHandler;
    }

    /**
     * In case of success authentication this handler will return FORBIDDEN, not 3xx
     */
    @Bean
    public SimpleUrlAuthenticationFailureHandler customFailureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
        return failureHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
            .successHandler(customSuccessHandler())     // Adding custom handlers
            .failureHandler(customFailureHandler())
            .and()
            .headers()  // Used for H2 console, could be disabled for prod
            .frameOptions()
            .sameOrigin()
            .and()
            .csrf()
            .disable()
            .addFilter(customUsernamePasswordAuthenticationFilter())   // will replcase default UsernamePasswordAuthenticationFilter
            .authorizeRequests()
            .antMatchers("/login")
            .permitAll()
            .antMatchers("/users/register")
            .permitAll()
            .anyRequest()
            .authenticated();
    }
}