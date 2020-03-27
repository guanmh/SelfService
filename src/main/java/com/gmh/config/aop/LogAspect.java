package com.gmh.config.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gmh.entity.common.OperationLog;
import com.gmh.enums.ReturnCodeEnum;
import com.gmh.service.common.IOperationLogService;
import com.gmh.utils.ReturnUtil;
import com.gmh.utils.WebUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author GMH
 * @title: PermissionsAspect
 * @projectName SelfProject
 * @description:
 * @date 2020/3/27 23:19
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    IOperationLogService logService;

    /**
     * 定义切入点，切入点为加了OperationLogDetail注解的方法
     * 也可用execution(public * com.mmys.service..*.save*(..)) || execution(public * com.mmys.service..*.delete*(..))的方式打到相同目的
     */
    @Pointcut("@annotation(com.gmh.config.aop.LogDetail)")
    public void webLog(){}

    @Around("webLog()")
    public Object access(ProceedingJoinPoint point) {
        Object res = null;
        try {
            res = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return res;
    }

    @AfterReturning(value = "webLog()",returning = "retVal")
    public void AfterReturning(JoinPoint joinPoint,Object retVal){
        //保存日志
        insertLog(joinPoint,retVal);
    }

    private void insertLog(JoinPoint joinPoint,Object retVal){
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        LogDetail annotation = signature.getMethod().getAnnotation(LogDetail.class);
        if(annotation != null){
            JSONObject jsonObject = (JSONObject) JSONObject.parse(retVal.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            OperationLog log = new OperationLog();
            log.setUserId(WebUtils.loginUser().getId());
            log.setType(annotation.logType().name());
            log.setBusiness(annotation.business());
            log.setCreateTime(new Date());
            getLogContent(log,((MethodSignature)joinPoint.getSignature()).getParameterNames(),joinPoint.getArgs(),annotation);
            log.setUserId(data.getLong("id"));
            logService.save(log);
        }
    }

    /**
     * 对当前登录用户和占位符处理
     * @param argNames 方法参数名称数组
     * @param args 方法参数数组
     * @param annotation 注解信息
     * @return 返回处理后的描述
     */
    private void getLogContent(OperationLog log,String[] argNames, Object[] args, LogDetail annotation){
        Map<Object, Object> map = new HashMap<>(4);
        for(int i = 0;i < argNames.length;i++){
            map.put(argNames[i],args[i]);
        }
        String content = annotation.content();
        try {
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                content = content.replace("{" + k + "}", JSON.toJSONString(v));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        log.setContent(content);
    }
}
