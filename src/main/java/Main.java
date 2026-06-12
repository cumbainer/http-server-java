import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Main {
    static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            Socket clientSocket = serverSocket.accept();

            byte[] buffer = new byte[1024];
            InputStream inputStream = clientSocket.getInputStream();
            int readBytes = inputStream.read(buffer);
            buffer = trimTrailingZeroBytes(buffer);

            char[] output = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(buffer)).array();
            String request = String.valueOf(output).trim();


            int status = 200;
            String reason = "OK";
            String filePath = request.split("\r\n")[0].split(" ")[1];
            if (!filePath.isBlank()) {
                status = 404;
                reason = "Not Found";
            }

            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(buildOutput(status, reason));

            //todo what does this do ?
            outputStream.flush();


        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static byte[] buildOutput(int status, String reason) {
        String b = "HTTP/1.1" +
                " " +
                status +
                " " +
                reason +
                "\r\n\r\n";
        return b.getBytes();
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
