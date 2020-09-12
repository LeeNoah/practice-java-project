package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(workGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture f = b.connect("127.0.0.1", 8765).sync();

        //buf
        f.channel().write(Unpooled.copiedBuffer("test".getBytes()));
        f.channel().flush();

        f.channel().closeFuture().sync();
        workGroup.shutdownGracefully();
    }
}
