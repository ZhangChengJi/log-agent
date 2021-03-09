package com.log.agent.aspect;

import com.log.agent.constant.LogAgentProperties;
import com.log.agent.domain.SysLog;
import com.log.agent.event.SysLogEvent;
import com.log.agent.utils.LogTypeEnum;
import com.log.agent.utils.SpringContextHolder;
import com.log.agent.utils.SysLogUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Slf4j
@Aspect
public class SysLogAspect {

   private final LogAgentProperties logAgentProperties;
   public SysLogAspect(LogAgentProperties logAgentProperties){
       this.logAgentProperties = logAgentProperties;
   }

    @Pointcut("@annotation(io.swagger.annotations.ApiOperation)")
    public void logPointCut() {
    }
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String strClassName = point.getTarget().getClass().getName();
        String strMethodName = point.getSignature().getName();
        log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);
        SysLog logVo = SysLogUtils.getSysLog(point.getArgs());
            logVo.setClassName(strClassName);
            logVo.setMethodName(strMethodName);
        logVo.setServiceId(logAgentProperties.getAppName());
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        ApiOperation annotation = method.getAnnotation(ApiOperation.class);
        if(annotation != null){
            //注解上的描述
            logVo.setTitle(annotation.value());
        }
        // 发送异步日志事件
        Long startTime = System.currentTimeMillis();
        Object obj;

        try {
            obj = point.proceed();
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            Long endTime = System.currentTimeMillis();
            logVo.setTime(endTime - startTime); //
            SpringContextHolder.publishEvent(new SysLogEvent(logVo));
        }

        return obj;

    }
}
