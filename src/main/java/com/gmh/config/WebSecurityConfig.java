package com.gmh.config;

import com.gmh.impl.JwtUserDetailsService;
import com.gmh.utils.ApplicationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
  private JwtUserDetailsService jwtUserDetailsService;

  @Autowired
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Autowired
  JwtAuthenticationFilter jwtAuthenticationFilter;

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

  @Override
  protected void configure(HttpSecurity http) throws Exception {
//    http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//        .and()
//        .authorizeRequests()
//        .antMatchers("/*")
//        .permitAll()
//        .anyRequest()
//        .authenticated()
//        .and()
//        .formLogin()
//        .loginPage("/loginMsg")
//        .loginProcessingUrl("/login")
//        .failureForwardUrl("/loginError")
//        .defaultSuccessUrl("/loginSuccess", true)
//        .permitAll()
//        .and()
//        .sessionManagement()
//        .invalidSessionUrl("/timeout")
//        .and()
//        .rememberMe()
//        .tokenValiditySeconds(1209600)
//        .and()
//        .logout()
//        .logoutSuccessUrl("/logoutSuccess")
//        .and()
//        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//        .csrf().disable()// 禁用 Spring Security 自带的跨域处理
//        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);// 定制我们自己的session策略:调整为让SpringSecurity不创建和使用 session
    http
            .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/*").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/**").anonymous()
            .anyRequest().authenticated()       // 剩下所有的验证都需要验证
            .and()
            .csrf().disable()                      // 禁用 Spring Security 自带的跨域处理
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 定制我们自己的 session 策略：调整为让 Spring Security 不创建和使用 session、
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jwtUserDetailsService);
  }
}
