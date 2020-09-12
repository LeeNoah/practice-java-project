package netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

public class ServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //((ByteBuf)msg).release();
        try {
            ByteBuf buf = (ByteBuf) msg;
            //netty自己封装的Bytebuf,不需要flip
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            String request = new String(bytes, "utf-8");
            System.out.println("server:" + request);

            // 写回数据给客户端
            String res = "resp from server";
            ChannelFuture cf = ctx.writeAndFlush(Unpooled.copiedBuffer(res.getBytes()));
            cf.addListener(ChannelFutureListener.CLOSE);
            // 调用write的时候msg会自动刷新释放，不用再release
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
