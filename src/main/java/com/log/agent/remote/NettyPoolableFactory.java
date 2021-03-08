package com.log.agent.remote;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import io.netty.channel.Channel;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class NettyPoolableFactory {
    private final AbstractNettyRemotingClient rpcRemotingClient;
    private final NettyClientBootstrap clientBootstrap;
    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap();

    /**
     * Instantiates a new Netty key poolable factory.
     *
     * @param rpcRemotingClient the rpc remoting client
     */
    public NettyPoolableFactory(AbstractNettyRemotingClient rpcRemotingClient) {
        this.rpcRemotingClient = rpcRemotingClient;
        this.clientBootstrap = rpcRemotingClient.getClientBootstrap();
        rpcRemotingClient.init();
    }



}
