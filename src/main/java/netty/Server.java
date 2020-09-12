package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) throws InterruptedException {
        //1 用来处理I/O操作的线程的多线程时间循环器
        // 用来接收client端连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //2 用来处理已经被接收的连接，用于实际的业务处理
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        // 3 创建一个辅助类BootStrap，就是对Server进行一系列的配置
        ServerBootstrap b = new ServerBootstrap();
        // 把两个工作线程组加入进来
        b.group(bossGroup, workerGroup)
                // 指定使用NioServerSocketChannel这种类型的通道
                .channel(NioServerSocketChannel.class)
                // 一定要使用channelHandler去绑定具体的事件处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)// 默认128
                // 保持连接
                .childOption(ChannelOption.SO_KEEPALIVE, true);


        // 绑定指定的端口  进行监听
        ChannelFuture f = b.bind(8765).sync();

        f.channel().closeFuture().sync();

        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();

    }
}
