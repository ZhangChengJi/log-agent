package com.log.agent.remote;

import com.log.agent.exception.FrameworkException;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyClientBootstrap  implements RemotingBootstrap{
    private final Bootstrap bootstrap = new Bootstrap();
    private final EventLoopGroup eventLoopGroupWorker;

    public NettyClientBootstrap(){
        this.eventLoopGroupWorker = new NioEventLoopGroup();
    }

    @SneakyThrows
    public Channel getNewChannel(InetSocketAddress address) {
        Channel channel;
        ChannelFuture f = this.bootstrap.connect(address);

        try {
            f.await(10000, TimeUnit.MILLISECONDS);
            if (f.isCancelled()) {
                throw new FrameworkException("connect cancelled, can not connect to log-transfer.");
            } else if (!f.isSuccess()) {
                throw new FrameworkException( "❌connect failed, can not connect to log-transfer.");
            } else {
                channel = f.channel();
            }
        } catch (Exception e) {
            throw new FrameworkException(e, "can not connect to log-transfer.");

        }
        return channel;
    }

    public Channel reconnect(InetSocketAddress address){
        Channel channel = null;
        ChannelFuture f = this.bootstrap.connect(address);

        try {
            f.await(10000, TimeUnit.MILLISECONDS);
            if (f.isCancelled()) {
                throw new FrameworkException("connect cancelled, can not connect to log-transfer.");
            } else if (!f.isSuccess()) {
                log.error("⚠️connect failed ✘︎ can not connect to log-transfer.️");
            } else {
                channel = f.channel();
            }
        } catch (Exception ignored) {


        }
        return channel;
    }
    @Override
    public void start() {
        this.bootstrap.group(this.eventLoopGroupWorker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)//如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                 .handler(new ClientInitialize());

    }

    public void destroyObject( Channel channel)  {
        if (channel != null) {
            channel.disconnect();
            channel.close();
            }
    }
    @Override
    public void shutdown() {
        try {
            this.eventLoopGroupWorker.shutdownGracefully();

        } catch (Exception exx) {
            log.error("Failed to shutdown: {}", exx.getMessage());
        }
    }
}
