package socketio1.aio;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable {

    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            InputStream inputStream = socket.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));
            out = new PrintWriter(socket.getOutputStream(), true);
            String body = null;
            while (true) {
                body = in.readLine();
                if (body == null) break;
                System.out.println("server: " + body);
                out.println("server response message...");
            }
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
            socket = null;
        }

    }
}
