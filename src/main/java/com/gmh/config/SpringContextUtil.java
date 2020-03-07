package com.gmh.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
　* @description:获取spring的对象
　* @param
　* @return
　* @author GMH
　* @date 2019/11/7 0:25
　*/
@Component
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  /**
   * 获取上下文
   *
   * @return
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 设置上下文
   *
   * @param applicationContext
   */
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    SpringContextUtil.applicationContext = applicationContext;
  }

  /**
   * 通过名字获取上下文中的bean
   *
   * @param name
   * @return
   */
  public static Object getBean(String name) {
    return applicationContext.getBean(name);
  }

  /**
   * 通过类型获取上下文中的bean
   *
   * @param requiredType
   * @return
   */
  public static <T> T getBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }
}
