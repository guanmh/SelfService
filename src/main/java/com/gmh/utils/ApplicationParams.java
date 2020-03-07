package com.gmh.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @project: self
 * @author: GMH
 * @create: 2019-06-10 10:47
 * @description: 配置参数
 */
@Component
@ConfigurationProperties(prefix = "self-params")
@Data
public class ApplicationParams {

  private String[] antMatchers;
}
