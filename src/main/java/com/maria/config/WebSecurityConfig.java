package com.maria.config;

import com.maria.authentication.TokenAuthenticationProvider;
import com.maria.authentication.TokenFilter;
import com.maria.service.api.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created   on 8/9/2017.
 */
@Configuration
@EnableWebSecurity
//@EnableTransactionManagement
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;
    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .logout().disable()
                .httpBasic().disable()
                .formLogin().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter(authenticationService, tokenAuthenticationProvider);
    }
}