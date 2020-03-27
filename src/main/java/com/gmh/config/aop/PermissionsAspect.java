package com.gmh.config.aop;

import com.gmh.enums.ReturnCodeEnum;
import com.gmh.utils.ReturnUtil;
import com.gmh.utils.WebUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.omg.CORBA.NO_PERMISSION;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

/**
 * @author GMH
 * @title: PermissionsAspect
 * @projectName SelfProject
 * @description:
 * @date 2020/3/27 23:19
 */
@Aspect
@Component
public class PermissionsAspect {

    /**
     * 定义切入点，切入点为加了OperationLogDetail注解的方法
     * 也可用execution(public * com.mmys.service..*.save*(..)) || execution(public * com.mmys.service..*.delete*(..))的方式打到相同目的
     */
    @Pointcut("@annotation(com.gmh.config.aop.HasPermissions)")
    public void webLog(){}

    @Around("webLog()")
    public Object access(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature)point.getSignature();
        HasPermissions annotation = signature.getMethod().getAnnotation(HasPermissions.class);
        Object res;
        try {
            res = point.proceed();
            if (!handleAuth(annotation)) {
                res = ReturnUtil.error(ReturnCodeEnum.NO_PERMISSION);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            res = ReturnUtil.error(e.getMessage());
        }
        return res;
    }

    /**
     * 判断权限
     * @param annotation
     * @return
     */
    private Boolean handleAuth(HasPermissions annotation) {
        if(WebUtils.hasAnyRole(annotation.permissions())){
            return true;
        }
        return false;
    }
}
