package com.log.agent.utils;

import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpUtil;
import com.log.agent.constant.LogAgentProperties;
import com.log.agent.domain.SysLog;
import lombok.experimental.UtilityClass;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
@UtilityClass
public class SysLogUtils {


    public SysLog getSysLog(Object[] point) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects
                .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        SysLog sysLog = new SysLog();
       // sysLog.setCreateBy(Objects.requireNonNull(getUsername()));
        sysLog.setRemoteAddr(ServletUtil.getClientIP(request));
        sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
        sysLog.setMethod(request.getMethod());
        sysLog.setUserAgent(request.getHeader("user-agent"));
        sysLog.setParams(buildRequestParams(request.getParameterMap(), point));
        return sysLog;
    }
    private String buildRequestParams(Map<String, String[]> paramMap, Object[] args) {
        StringBuilder params = new StringBuilder();
        // post 请求体json参数
        if (CollectionUtils.isEmpty(paramMap)) {
            for (Object obj : args) {
                if(! (obj instanceof Serializable)) {
                    continue;
                }
                params.append(JacksonUtils.obj2jsonIgnoreNull(obj));
            }
            // get 请求参数
        } else {
            for (Map.Entry<String, String[]> param : ((Map<String, String[]>)paramMap).entrySet()){
                params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
                String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
                params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
            }
        }
        return params.toString();
    }
    /**
     * 获取客户端
     * @return clientId
     */
//    private String getClientId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof OAuth2Authentication) {
//            OAuth2Authentication auth2Authentication = (OAuth2Authentication) authentication;
//            return auth2Authentication.getOAuth2Request().getClientId();
//        }
//        return null;
//    }
//
//    /**
//     * 获取用户名称
//     * @return username
//     */
//    private String getUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null) {
//            return null;
//        }
//        return authentication.getName();
//    }

}
