package com.gmh.utils;

import com.gmh.entity.common.OperationLog;
import com.gmh.enums.LogTypeEnum;

import javax.servlet.http.HttpServletRequest;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-01-23 15:05
 * @description: 日志相关操作
 */
public class LogUtil {

  public static OperationLog createOperationLog(
          HttpServletRequest request, LogTypeEnum logType, String logName) {
    OperationLog log = new OperationLog();
    log.setIp(WebUtils.getIpAddr(request));
    log.setUserId(WebUtils.loginUser().getId());
    log.setBusiness(logName);
    log.setType(logType.ordinal());
    return log;
  }
}
