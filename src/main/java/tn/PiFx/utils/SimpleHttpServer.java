package tn.PiFx.utils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080); // Listen on port 8080
        System.out.println("Listening for connection on port 8080 ....");
        while (true) {
            try (Socket socket = serverSocket.accept()) {
                InputStream is = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder request = new StringBuilder();
                String line;
                while (!(line = reader.readLine()).isEmpty()) {
                    request.append(line + "\n");
                }

                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "Authorization code has been received. You can close this window now.";
                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
                // Here, you should extract the authorization code from the request and handle it
                break;
            }
        }
    }
}

