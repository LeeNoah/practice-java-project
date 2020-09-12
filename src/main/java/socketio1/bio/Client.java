package socketio1.bio;

import java.io.*;
import java.net.Socket;

public class Client {
    final static String ADDRESS = "127.0.0.1";
    final static int PORT = 8765;

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(ADDRESS, PORT);
            InputStream inputStream = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));
            out = new PrintWriter(socket.getOutputStream(), true);

            //向服务端发送数据
            out.println("接受到客户端的请求数据。。。");
            String response = in.readLine();
            System.out.println("client: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
