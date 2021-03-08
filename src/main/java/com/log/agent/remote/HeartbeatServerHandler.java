package com.log.agent.remote;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

    // Return a unreleasable view on the given ByteBuf
    // which will just ignore release and retain calls.
    private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
            .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
                    CharsetUtil.UTF_8));  // 1

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {


        if (evt instanceof IdleStateEvent) {  // 2
            log.info("🕞长时间没有日志 传输，暂时关闭通道连接。");

            ctx.channel().disconnect();
            ctx.channel().close();
//            ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate()).addListener(
//                    ChannelFutureListener.CLOSE_ON_FAILURE);  // 3
//
//            System.out.println( ctx.channel().remoteAddress()+"超时类型：" + type);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}