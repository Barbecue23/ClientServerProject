import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class FileServerApp extends Thread {
    Socket clientSocket;
    String directoryPath;
    public FileServerApp(Socket socket, String directoryPath) {
        this.clientSocket = socket;
        this.directoryPath = directoryPath;
    }
    public void run() {
        try {
             // Create input and output streams for client communication
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // List files in the directory
            File directory = new File(directoryPath);
            File[] files = directory.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    out.println(file.getName());
                }
            }
            out.println("END_OF_LIST"); // Signal the end of the file list

            // Read the selected file name from the client
            String selectedFileName = in.readLine();

            // Send the selected file to the client
            File selectedFile = new File(directoryPath, selectedFileName);
            if (selectedFile.exists() && selectedFile.isFile()) {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    clientSocket.getOutputStream().write(buffer, 0, bytesRead);
                }
                fileInputStream.close();
            } else {
                out.println("FILE_NOT_FOUND");
            }

            clientSocket.close();
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }
}
