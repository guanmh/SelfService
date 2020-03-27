package com.gmh.config.aop;

import com.gmh.enums.LogTypeEnum;

import java.lang.annotation.*;

/**
 * @author GMH
 * @title: HasFunction
 * @projectName SelfProject
 * @description: 日志注解
 * @date 2020/3/27 23:15
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogDetail {

    String business();

    String content();

    LogTypeEnum logType();
}
