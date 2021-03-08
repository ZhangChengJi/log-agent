package com.log.agent.remote;

import com.log.agent.exception.FrameworkErrorCode;
import com.log.agent.exception.FrameworkException;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class NettyClientChannelManager {
    private final ConcurrentMap<String, Channel> channels = new ConcurrentHashMap<>();
    private final AbstractNettyRemotingClient abstractNettyRemotingClient;
    private final NettyClientBootstrap clientBootstrap;


    NettyClientChannelManager(AbstractNettyRemotingClient abstractNettyRemotingClient){
        this.abstractNettyRemotingClient = abstractNettyRemotingClient;
        this.clientBootstrap =abstractNettyRemotingClient.getClientBootstrap();

    }
    ConcurrentMap<String, Channel> getChannels() {
        return channels;
    }

    Channel reconnect(String serverAddress) {

            try {
             return   acquireChannel(serverAddress);
            } catch (Exception e) {
                log.error("{} can not connect to {} cause:{}", FrameworkErrorCode.NetConnect.getErrCode(), serverAddress, e.getMessage(), e);
                throw new FrameworkException(e, "can not connect to services-server.");
            }
        }

    Channel acquireChannel(String serverAddress) {
        Channel channelToServer = channels.get(serverAddress);
        if (channelToServer != null  && channelToServer.isActive()) {
                return channelToServer;
        }
        synchronized(this)  {
            if (channelToServer != null){
                clientBootstrap.destroyObject(channelToServer);
                channels.remove(serverAddress);
            }
            return doConnect(serverAddress);
        }
    }

    public Channel doConnect(String serverAddress) {
        Channel channelToServer = channels.get(serverAddress);

        if (channelToServer != null && channelToServer.isActive()) {
            return channelToServer;
        }
        Channel channelFromPool;
        InetSocketAddress address =   toInetSocketAddress(serverAddress);
       Channel channel= clientBootstrap.getNewChannel(address);
       channels.put(serverAddress,channel);
       return channel;


    }
    void registerChannel(final String serverAddress, final Channel channel) {
        Channel channelToServer = channels.get(serverAddress);
        if (channelToServer != null && channelToServer.isActive()) {
            return;
        }
        channels.put(serverAddress, channel);
    }
    public static InetSocketAddress toInetSocketAddress(String address) {
        int i = address.indexOf(':');
        String host;
        int port;
        if (i > -1) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = 0;
        }
        return new InetSocketAddress(host, port);
    }


}
