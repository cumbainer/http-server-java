import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    private static final List<String> SUPPORTED_PATHS = List.of("", "echo", "user-agent");
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
            System.out.println(request);

            OutputStream outputStream = clientSocket.getOutputStream();

            int responseStatus = getStatus(request);
            if (!isSuccessStatus(responseStatus)) {
                outputStream.write(buildOutput(responseStatus, null, ""));
                outputStream.flush();
                return;

            }
            String responseBody = getResponseBody(request);
            List<String> headers = getResponseBodyHeaders(responseBody);

            outputStream.write(buildOutput(responseStatus, headers, responseBody));

            //todo what does this do ?
            outputStream.flush();


        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private static boolean isSuccessStatus(int status) {
        List<Integer> successStatuses = List.of(200);
        return successStatuses.contains(status);
    }

    private static int getStatus(String reqPart) {
        String url = ParserHelper.getUrlFromRequestPart(reqPart);
        String baseAbsolutePath = url.split("/")[0].trim();
        if (SUPPORTED_PATHS.contains(url) || SUPPORTED_PATHS.contains(baseAbsolutePath)) {
            return 200;
        }
        return 404;
    }


    private static String getResponseBody(String httpRequestPart) {
        String url = ParserHelper.getUrlFromRequestPart(httpRequestPart);
        String baseUrlPart = url.split("/")[0].trim();
        return switch (url) {
            case "echo" -> getEchoResponseBody(httpRequestPart);
            case "user-agent" -> getUserAgentResponse(httpRequestPart);
            default -> "";
        };

    }

    private static String getUserAgentResponse(String reqPart) {
        String targetHeader = "User-Agent";
        String[] headers = reqPart.split("\n");
        for (int i=0; i < headers.length; i++) {
            String header = headers[i];
            if (header.contains(targetHeader)) {
                return header.split(":")[1].trim();
            }
        }
        return "";
    }

    private static String getEchoResponseBody(String reqPart) {
        String url = ParserHelper.getUrlFromRequestPart(reqPart);
        if (url.isBlank()) {
            return "";
        }
        String[] parts = url.split("/");
        return parts[1].trim();
    }

    private static List<String> getResponseBodyHeaders(String responseBody) {
        String contentLengthH = "Content-Length: " + responseBody.length();
        String contentTypeH = "Content-Type: text/plain";
        return List.of(contentLengthH, contentTypeH);
    }

    private static String getReason(int status){
        Map<Integer, String> status2Reason = new HashMap<>();
        status2Reason.put(200, "OK");
        status2Reason.put(404, "Not Found");
        status2Reason.put(400, "Bad Request");

        return status2Reason.get(status);
    }

    private static byte[] buildOutput(int status, List<String> headers, String responseBody) {
        if (headers == null) {
            headers = Collections.EMPTY_LIST;
        }
        StringBuilder headerB = new StringBuilder();
        for (String h : headers) {
            headerB.append(h).append("\r\n");
        }
        String reason = getReason(status);
        String request = "HTTP/1.1" + " " + status + " " + reason;
        String header = headerB.toString();


        String b = request +
                "\r\n" +
                header +
                "\r\n" +
                responseBody + "\r\n";
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
