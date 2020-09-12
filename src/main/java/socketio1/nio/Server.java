package socketio1.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable {
    // server实现runnable接口，让selector处于死循环轮询channel 的状态

    // 1 多路复用器（管理所有通告）
    private Selector selector;
    // 2 建立缓冲区
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);

    // 写数据的缓冲区
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public Server(int port) {
        try {
            // 打开多路复用器
            this.selector = Selector.open();
            // 打开服务器通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 设置服务器通道为非阻塞模式
            ssc.configureBlocking(false);
            // 绑定地址
            ssc.bind(new InetSocketAddress(port));
            // 把服务器通道注册到多路复用器上,并且监听阻塞时间
            ssc.register(this.selector, SelectionKey.OP_ACCEPT);
            System.out.println("server start ...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 必须要让多路复用器开始监听
                this.selector.select();
                // 返回已经注册到多路复用器上的结果集
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                // 进行遍历
                while (keys.hasNext()) {
                    // 获取一个选择的元素
                    SelectionKey key = keys.next();
                    // 直接可以移除掉？
                    keys.remove();
                    // 如果是有效的
                    if (key.isValid()) {
                        // 如果为阻塞状态
                        if (key.isAcceptable()) {
                            this.accept(key);
                        }
                        // 如果为可读状态
                        if (key.isReadable()) {
                            this.read(key);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            // 1 获取服务通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            // 2 获取阻塞方法（sc为client的channel）
            SocketChannel sc = ssc.accept();
            // 3 设置阻塞模式
            sc.configureBlocking(false);
            // 4 注册到多路复用器上，并设置读取标识
            sc.register(this.selector, SelectionKey.OP_READ);
            System.out.println("register socket channel");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void read(SelectionKey key) {
        try {
            // 1 清空旧的数据
            this.readBuffer.clear();
            // 2 获取client的channel
            SocketChannel sc = (SocketChannel) key.channel();
            // 3 读取数据
            int count = sc.read(this.readBuffer);
            // 4 如果没有数据
            if (count == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            // 5 有数据则进行读取，读取之前需要进行复位（把position和limin进行复位）
            this.readBuffer.flip();
            // 6 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            byte[] bytes = new byte[this.readBuffer.remaining()];
            // 7 接受缓冲区数据
            this.readBuffer.get(bytes);
            String body = new String(bytes).trim();
            // 8 打印结果
            System.out.println("server:" + body);
            // 9 可以写回数据给客户端。。。

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Server(8765)).start();
    }

}
