import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ParserHelper {

    public static String getUrlFromRequestPart(String part) {
        return part.split("\r\n")[0].split(" ")[1].substring(1);
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

    public static String parseReqToString(Socket clientSocket) {
        try {
            byte[] buffer = new byte[1024];
            InputStream inputStream = clientSocket.getInputStream();
            int readBytes = inputStream.read(buffer);
            System.out.printf("Read %d bytes%n", readBytes);

            buffer = ParserHelper.trimTrailingZeroBytes(buffer);
            char[] reqChars = StandardCharsets.US_ASCII.decode(ByteBuffer.wrap(buffer)).array();

            return String.valueOf(reqChars).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
