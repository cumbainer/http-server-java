import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;

public class Main2 {
    static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            while (true) {
                Runnable runnable = () -> handleRequest(serverSocket);
                runnable.run();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
