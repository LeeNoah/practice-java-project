package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //((ByteBuf)msg).release();
        try {
            ByteBuf buf = (ByteBuf) msg;
            //netty自己封装的Bytebuf,不需要flip
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String request = new String(bytes, "utf-8");
            System.out.println("client:" + request);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
