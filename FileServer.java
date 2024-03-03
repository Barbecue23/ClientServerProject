import java.io.*;
import java.net.*;

public class FileServer {
    int port = 12345; // Port on which the server listens
    String directoryPath = "./files"; // Path to the directory containing the files
    public FileServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started. Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                FileServerApp sa = new FileServerApp(clientSocket, directoryPath);
                sa.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new FileServer();
    }
}
