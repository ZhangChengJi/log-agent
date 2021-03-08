package com.log.agent.event;

import cn.hutool.json.JSONUtil;
import com.log.agent.domain.SysLog;
import com.log.agent.remote.AbstractNettyRemotingClient;
import com.log.agent.remote.NettyPoolableFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

@Slf4j
@RequiredArgsConstructor
public class SysLogListener {
   private final AbstractNettyRemotingClient remotingClient;


    @Order
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        synchronized(this){
            SysLog sysLog = (SysLog) event.getSource();
            remotingClient.send(JSONUtil.parse(sysLog));
        }

    }
}
