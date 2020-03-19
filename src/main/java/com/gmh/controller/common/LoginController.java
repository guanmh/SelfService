package com.gmh.controller.common;

import com.gmh.enums.CacheEnum;
import com.gmh.enums.ReturnCodeEnum;
import com.gmh.enums.SystemStringEnum;
import com.gmh.utils.CacheUtils;
import com.gmh.utils.ReturnUtil;
import com.gmh.utils.WebUtils;
import com.gmh.vo.LoginUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
