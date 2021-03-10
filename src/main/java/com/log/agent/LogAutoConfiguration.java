package com.log.agent;

import com.log.agent.aspect.SysLogAspect;
import com.log.agent.constant.LogAgentProperties;
import com.log.agent.event.SysLogListener;
import com.log.agent.remote.AbstractNettyRemotingClient;
import com.log.agent.remote.NettyClientBootstrap;
import com.log.agent.remote.NettyPoolableFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
@ConditionalOnWebApplication
@Configuration(proxyBeanMethods = false) //proxyBeanMethods为flase 不会被代理，如果为true会被CGLIB代理，如果只是普通类的话建议设置为 flase ,这样能提升性能
@ConditionalOnProperty(name = {"spring.log-agent.enabled"}, matchIfMissing = false)
public class LogAutoConfiguration {



   @Bean
   public LogAgentProperties logAgentProperties(ApplicationContext context) {
       log.info("logAgent Log collection plug in start 🚗️  🚗  🚗 .....");
       if (context.getParent() != null
               && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
               context.getParent(), LogAgentProperties.class).length > 0) {
           return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(),
                   LogAgentProperties.class);
       }
       return new LogAgentProperties();
   }
    @Bean
   public NettyClientBootstrap nettyClientBootstrap(){
       return new NettyClientBootstrap();
   }
   @Bean
   public AbstractNettyRemotingClient abstractNettyRemotingClient(LogAgentProperties logAgentProperties, NettyClientBootstrap nettyClientBootstrap){
       return new AbstractNettyRemotingClient(logAgentProperties,nettyClientBootstrap);
   }
    @Bean
   public NettyPoolableFactory nettyPoolableFactory(AbstractNettyRemotingClient abstractNettyRemotingClient){
       return new NettyPoolableFactory(abstractNettyRemotingClient);
   }
    @Bean
    public SysLogListener sysLogListener(AbstractNettyRemotingClient abstractNettyRemotingClient) {
        return new SysLogListener(abstractNettyRemotingClient);
    }

    @Bean
    public SysLogAspect sysLogAspect(LogAgentProperties logAgentProperties) {
        return new SysLogAspect(logAgentProperties);
    }
}
