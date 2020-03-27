package com.gmh.config.aop;

import java.lang.annotation.*;

/**
 * @author GMH
 * @title: HasFunction
 * @projectName SelfProject
 * @description: 权限注解
 * @date 2020/3/27 23:15
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HasPermissions{

    /**
     * 权限列表，以“,”分割的权限编号
     *
     * @return
     */
    String permissions();
}
