package com.mamun72.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/userlogin/**").permitAll()
                .antMatchers("/errorPage/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().disable()
                .logout().logoutUrl("logout").deleteCookies("JAVA_COOKIE")
                .permitAll();
    }
}

