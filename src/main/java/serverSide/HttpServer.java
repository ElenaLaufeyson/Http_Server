package serverSide;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) {
        int port = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Сервер запущен на: "
                    + serverSocket.getLocalPort() + " порту\n");
        } catch (IOException e) {
            System.out.println("Порт " + port + " заблокирован.");
            System.exit(-1);
        }
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientHandler session = new ClientHandler(clientSocket);
                new Thread(session).start();
            } catch (IOException e) {
                System.out.println("Невозможно установить соединение. ");
                System.out.println(e.getMessage());
                System.exit(-1);
            }
        }
    }
}
