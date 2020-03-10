package com.gmh.controller.common;

import com.gmh.config.JwtTokenUtil;
import com.gmh.entity.common.User;
import com.gmh.enums.CacheEnum;
import com.gmh.enums.ReturnCodeEnum;
import com.gmh.utils.CacheUtils;
import com.gmh.utils.ReturnUtil;
import com.gmh.enums.SystemStringEnum;
import com.gmh.utils.WebUtils;
import com.gmh.vo.LoginUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-10-29 16:39
 * @description: 登录
 */
@RestController
public class LoginController {

  private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
  @Value("${server.port}")
  private String servicePort;

  @Autowired
  @Qualifier("jwtUserDetailsService")
  private UserDetailsService userDetailsService;

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @GetMapping("getCsrf")
  @Cacheable(
          value = SystemStringEnum.RedisValueEnum.CSRF_VALUE,
          key = "#token.token",
          unless = "#result == null")
  public String getCsrf(CsrfToken token, HttpServletRequest request, HttpSession httpSession) {
    LoginUserVO loginUserVO = WebUtils.loginUser();
    if (loginUserVO != null) {
      String tokenToken = token.getToken();
      httpSession.setAttribute(WebUtils.HTTP_SESSION_CSRF, tokenToken);
      CacheUtils.putString(
              CacheEnum.LOGIN_USER_JSON.getCode(),
              tokenToken,
              loginUserVO.getId().toString());
    }
    return ReturnUtil.success(token);
  }

  @PostMapping("/login")
  public String login(User user, HttpServletRequest request){
    final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLoginName());
    final String token = jwtTokenUtil.generateToken(userDetails);
    return ReturnUtil.success(token);
  }

  @RequestMapping("loginError")
  public String loginError() {
    return ReturnUtil.error(ReturnCodeEnum.LOGIN_ERROR);
  }

  @RequestMapping("loginMsg")
  public String loginMsg() {
    return ReturnUtil.error(ReturnCodeEnum.LOGIN_ERROR);
  }


  @GetMapping("loginSuccess")
  public String loginSuccess() {
    LOG.info("登录服务器,端口号：" + servicePort);
    return ReturnUtil.success(ReturnCodeEnum.LOGIN_SUCCESS);
  }
  @GetMapping("jwt")
  public String jwt() {
    UserDetails userDetails = (UserDetails) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return "jwt:"+userDetails.getUsername()+","+userDetails.getPassword();
  }
}
