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
       // convertByteBufToString(byteBuf);
    }
    public String convertByteBufToString(ByteBuf buf) {
        String str;
        if(buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        System.out.println(str);
        return str;
    }
    public static void main(String[] args) {
        String a="SysLog(title=1ne 使用 根据用户名字查询不等于当前名字的数据, createBy=null, createTime=null, remoteAddr=127.0.0.1, userAgent=Apache-HttpClient/4.5.12 (Java/1.8.0_271), requestUri=/user/ne/admin, method=GET, methodName=ne, className=com.nq.cloud.user.controller.UserController, params=\"admin\", time=5, serviceId=scaffold-user)\n";
        System.out.println(a.length());
    }
}
