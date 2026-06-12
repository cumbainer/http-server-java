import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            Socket clientSocket = serverSocket.accept();

            byte[] buffer = new byte[1024];
            InputStream inputStream = clientSocket.getInputStream();

            int readBytes = inputStream.read(buffer);
            buffer = trimTrailingZeroBytes(buffer);
            System.out.println(Arrays.toString(buffer));
            System.out.printf("Read Bytes %d%n", readBytes);
            System.out.println("Is Keep Alive %s%n".formatted(clientSocket.getKeepAlive()));

            char[] output = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(buffer)).array();
            String request = String.valueOf(output).trim();
            System.out.println(request);

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
            outputStream.flush();


        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public static byte[] trimTrailingZeroBytes(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        int end = bytes.length;

        while (end > 0 && bytes[end - 1] == 0) {
            end--;
        }

        return Arrays.copyOf(bytes, end);
    }


}
