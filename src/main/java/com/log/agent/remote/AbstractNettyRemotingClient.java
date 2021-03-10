package com.log.agent.remote;

import cn.hutool.json.JSON;
import com.log.agent.constant.LogAgentProperties;
import com.log.agent.domain.SysLog;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class AbstractNettyRemotingClient extends AbstractNettyRemoting {
    private static final int MAX_MERGE_SEND_THREAD = 1;
    private static final long KEEP_ALIVE_TIME = Integer.MAX_VALUE;
    private static final long SCHEDULE_DELAY_MILLS = 60 * 1000L;
    private static final long SCHEDULE_INTERVAL_MILLS = 10 * 1000L;
    private final NettyClientBootstrap clientBootstrap;
    private final NettyClientChannelManager nettyClientChannelManager;
    private final LogAgentProperties logAgentProperties;
    private final String logTransfer;
    private Channel channel;
    public AbstractNettyRemotingClient(LogAgentProperties logAgentProperties, NettyClientBootstrap nettyClientBootstrap){
        super();
         this.logAgentProperties = logAgentProperties;
        this.clientBootstrap = nettyClientBootstrap;
        this.clientBootstrap.start();
        this.nettyClientChannelManager=new NettyClientChannelManager(this);
        this.logTransfer=this.logAgentProperties.getUrl();
    }



    public void init() {
              this.channel=  nettyClientChannelManager.connect(this.logTransfer);
    }

    public NettyClientBootstrap getClientBootstrap() {
        return clientBootstrap;
    }

    public synchronized void send(JSON log) {
        synchronized(this) {
            if (this.channel != null) {
                if (!this.channel.isOpen()) {
                    this.channel = nettyClientChannelManager.reConnect(this.logTransfer);
                }

            } else {
                this.channel = nettyClientChannelManager.reConnect(this.logTransfer);
            }

            if (this.channel != null && this.channel.isOpen()) {
                this.channel.writeAndFlush(log.toString());
            }
        }
        }
}
