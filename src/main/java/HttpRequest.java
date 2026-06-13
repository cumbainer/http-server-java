import java.util.Map;

public record HttpRequest(HttpMethod method, Map<String, String> headers, String requestBody, String url) {

    public HttpRequest() {
        this(HttpMethod.GET, Map.of(), null, "/");
    }

    public String getBaseUrl() {
        String[] parts = this.url.split("/");
        return parts.length >= 2 ? parts[1] : "/";
    }
}
