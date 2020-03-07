package com.gmh.utils;

import com.gmh.vo.LoginUserVO;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-01-23 15:05
 * @description: Web相关操作
 */
public class WebUtils {

  /** 放到HttpSession中的csrf的键key */
  public static final String HTTP_SESSION_CSRF = "HTTP_SESSION_CSRF";

  private static Boolean isWindows;

  private static String SERVICE_PORT_LOG;

  private static final String UNKNOWN = "unknown";

  /** 本地IP地址 */
  private static final String LOCAL_HOST_IP = "127.0.0.1";

  /**
   * 判断浏览器类型，firefox浏览器做特殊处理，否则下载文件名乱码
   *
   * @param request
   * @param response
   * @param fileName
   */
  private static void compatibleFileName(
          HttpServletRequest request, HttpServletResponse response, String fileName) {
    String agent = request.getHeader("USER-AGENT").toLowerCase();
    String codedFileName = null;
    try {
      codedFileName = URLEncoder.encode(fileName, CharEncoding.UTF_8);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      codedFileName = fileName;
    }
    if (agent.contains("firefox")) {
      response.setCharacterEncoding(CharEncoding.UTF_8);
      response.setHeader("content-disposition", "attachment;filename=" + encode(fileName));
    } else {
      response.setHeader("content-disposition", "attachment;filename=" + codedFileName);
    }
  }
  /**
   * 乱码
   *
   * @param message
   * @return
   */
  public static String encode(String message) {
    return new String(message.getBytes(), StandardCharsets.ISO_8859_1);
  }

  /**
   * 文件导出
   *
   * @param request
   * @param response
   * @param workbook
   * @param fileName
   */
  public static synchronized void exportExcel(
      HttpServletRequest request,
      HttpServletResponse response,
      Workbook workbook,
      String fileName) {
    synchronized (response) {
      // 告诉浏览器用什么软件可以打开此文件
      response.setHeader("content-Type", "application/vnd.ms-excel");
      WebUtils.compatibleFileName(request, response, fileName);
      try {
        workbook.write(response.getOutputStream());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 是有拥有权限
   *
   * @param powerCodes
   * @return
   */
  private static boolean hasAnyPower(String... powerCodes) {
    Assert.isTrue(CollectionUtils.isNotEmpty(powerCodes), "没有获取到正确的权限比对！");
    LoginUserVO loginUser = loginUser();
    if (loginUser.getIsAdmin()) {
      return true;
    }
    Collection<GrantedAuthority> authorities = loginUser.getAuthorities();
    if (CollectionUtils.isNotEmpty(authorities)) {
      for (GrantedAuthority authority : authorities) {
        if (CollectionUtils.containsAny(powerCodes, authority.getAuthority())) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean hasAnyRole(String... roles) {
    Assert.isTrue(CollectionUtils.isNotEmpty(roles), "没有获取到正确的权限比对！");
    int length = roles.length;
    String[] powerCodes = new String[length];
    for (int i = 0; i < length; i++) {
      powerCodes[i] = roles[i];
    }
    return hasAnyPower(roles);
  }

  public static Boolean isWindows() {
    if (isWindows == null) {
      isWindows = System.getProperty("os.name").indexOf("Windows") >= 0;
    }
    return isWindows;
  }

  /**
   * 当前登录人
   *
   * @return
   */
  public static LoginUserVO loginUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return Objects.isNull(authentication) || !(authentication.getPrincipal() instanceof LoginUserVO)
        ? null
        : (LoginUserVO) authentication.getPrincipal();
  }

  /**
   * 操作人IP地址
   *
   * @param request
   * @return
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (StringUtils.isEmpty(ip) || StringUtils.equalsIgnoreCase(UNKNOWN, ip)) {
      ip = request.getRemoteAddr();
      if (LOCAL_HOST_IP.equals(ip)) {
        // 根据网卡取本机配置的IP
        InetAddress inet = null;
        try {
          inet = InetAddress.getLocalHost();
        } catch (Exception e) {
          e.printStackTrace();
        }
        ip = inet.getHostAddress();
      }
    }
    // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    if (StringUtils.length(ip) > 15) {
      int i = ip.indexOf(",");
      if (i > 0) {
        ip = ip.substring(0, i);
      }
    }
    return ip;
  }

  public static String getTimerLog() {
    return SERVICE_PORT_LOG;
  }
}
