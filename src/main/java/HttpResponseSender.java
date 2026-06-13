import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpResponseSender {

    public boolean sendHttpResponse(Socket clientSocket, HttpResponse response) {
        if (clientSocket == null || response == null) {
            return false;
        }

        try {
            OutputStream outputStream = clientSocket.getOutputStream();

            String body = response.responseBody() == null
                    ? ""
                    : response.responseBody();

            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

            StringBuilder rawResponse = new StringBuilder();

            rawResponse.append("HTTP/1.1 ")
                    .append(response.responseStatus().getCode())
                    .append(" ")
                    .append(response.responseStatus().getReason())
                    .append("\r\n");

            boolean hasContentLength = false;

            List<String> headers = response.responseHeaders();

            if (headers != null) {
                for (String header : headers) {
                    if (header.toLowerCase().startsWith("content-length:")) {
                        hasContentLength = true;
                    }

                    rawResponse.append(header).append("\r\n");
                }
            }

            if (!hasContentLength) {
                rawResponse.append("Content-Length: ")
                        .append(bodyBytes.length)
                        .append("\r\n");
            }

            rawResponse.append("\r\n");

            outputStream.write(rawResponse.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.write(bodyBytes);
            outputStream.flush();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}