package netty2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.ServerHandler;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();      // 用于处理服务端接收客户端连接的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();    // 进行网路通信（网络读写）
        ServerBootstrap b = new ServerBootstrap();                  // 创建辅助工具类，用于服务器通道的一系列配置
        b.group(bossGroup, workerGroup)                             // 绑定两个线程组
                .channel(NioServerSocketChannel.class)              // 指定nio的模式
                .option(ChannelOption.SO_BACKLOG, 1024)      // 设置tcp缓冲区
                .option(ChannelOption.SO_SNDBUF, 32 * 1024)  // 设置发送缓冲大小
                .option(ChannelOption.SO_RCVBUF, 32 * 1024)  // 设置接收缓冲大小
                .option(ChannelOption.SO_KEEPALIVE, true)    // 保持连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {         //
                        sc.pipeline().addLast(new ServerHandler()); // 这里配置具体数据接收方法的处理
                    }
                });


        ChannelFuture f = b.bind(8765).sync();              // 绑定指定的端口  进行监听

        f.channel().closeFuture().sync();                           // 等待关闭

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}
