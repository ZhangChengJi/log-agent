package com.log.agent.remote;

import cn.hutool.json.JSONUtil;
import com.log.agent.domain.SysLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class LogToByteEncoder extends MessageToByteEncoder<String> {
  private final int  DefaultHeadLength = 8;

    private final int DefaultProtocolVersion = 0x8001; // test protocol version

    private final int ActionPing = 0x0001; // ping
    private final int ActionPong = 0x0002; // pong
    private final int  ActionData = 0x00F0; // business
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String sysLog, ByteBuf out) throws Exception {
        //写入开头的标志
        byte[] bytes = sysLog.getBytes();
        out.writeShort(bytes.length);
        out.writeBytes(bytes);
    }


}
