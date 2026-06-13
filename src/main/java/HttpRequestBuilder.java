import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestBuilder {

    public HttpRequest build(String req) {
        if (req == null || req.isBlank()) {
            return new HttpRequest();
        }

        String[] parts = req.split("\\r\\n\\r\\n", 2);

        String head = parts[0];
        String body = parts.length > 1 ? parts[1] : "";

        String[] lines = head.split("\\r\\n");

        if (lines.length == 0) {
            return new HttpRequest();
        }

        String[] requestLine = lines[0].split(" ");

        if (requestLine.length < 3) {
            throw new IllegalArgumentException("Invalid HTTP request line: " + lines[0]);
        }

        HttpMethod method = HttpMethod.valueOf(requestLine[0]);
        String url = requestLine[1];

        Map<String, String> headers = new LinkedHashMap<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];

            if (line.isBlank()) {
                break;
            }

            int colonIndex = line.indexOf(':');

            if (colonIndex == -1) {
                continue;
            }

            String headerName = line.substring(0, colonIndex).trim();
            String headerValue = line.substring(colonIndex + 1).trim();

            headers.put(headerName, headerValue);
        }

        return new HttpRequest(method, headers, body.isEmpty() ? null : body, url);
    }
}