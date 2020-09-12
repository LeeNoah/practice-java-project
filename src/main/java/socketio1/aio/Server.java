package socketio1.aio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    final static int PORT = 8765;

    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(PORT);
            System.out.println("server start ...");

            HandlerExecutorPool handlerExecutorPool = new HandlerExecutorPool(50, 1000);
            while (true) {
                Socket socket = server.accept();
                handlerExecutorPool.execute(new SocketHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
