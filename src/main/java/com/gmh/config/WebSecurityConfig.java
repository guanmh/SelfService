package com.gmh.config;

import com.gmh.impl.LoginServiceImpl;
import com.gmh.utils.ApplicationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-01-21 16:34
 * @description: 安全框架配置
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  ApplicationParams params;

  @Autowired
  private LoginServiceImpl jwtUserDetailsService;

  /**
   * 不需要登录就能访问的URL
   *
   * @param web
   */
  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(params.getAntMatchers());
  }

  @Bean
  public PasswordEncoder passwordEncoder(
      @Value("${self-params.user.userSuperPassword}") String userSuperPassword) {
    return SelfPasswordEncoder.create(userSuperPassword);
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.headers()
            .and()
            .authorizeRequests()
            .antMatchers("/*")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/loginMsg")
            .loginProcessingUrl("/login")
            .failureForwardUrl("/loginError")
            .defaultSuccessUrl("/loginSuccess", true)
            .permitAll()
            .and()
            .sessionManagement()
            .invalidSessionUrl("/timeout")
            .and()
            .rememberMe()
            .tokenValiditySeconds(1209600)
            .and()
            .logout()
            .logoutSuccessUrl("/logoutSuccess")
            .and()
            .csrf();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jwtUserDetailsService);
  }
}
