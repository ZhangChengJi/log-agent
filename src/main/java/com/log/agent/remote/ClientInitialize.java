package com.log.agent.remote;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ClientInitialize extends ChannelInitializer<SocketChannel> {
    private static final int READ_IDEL_TIME_OUT = 0; // 读超时
    private static final int WRITE_IDEL_TIME_OUT = 60;// 写超时
    private static final int ALL_IDEL_TIME_OUT = 0; // 所有超时
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //60秒钟没有写事件，则断开连接
        pipeline.addLast(new IdleStateHandler(READ_IDEL_TIME_OUT,
                WRITE_IDEL_TIME_OUT, ALL_IDEL_TIME_OUT, TimeUnit.SECONDS));
         pipeline.addLast(new LengthFieldBasedFrameDecoder(2048       ,0,2));

        pipeline.addLast("encoder",new LogToByteEncoder());
        pipeline.addLast(new HeartbeatServerHandler());
    }
}
