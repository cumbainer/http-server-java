import java.util.Map;

public record HttpRequest(HttpMethod method, Map<String, String> headers, String requestBody, String url) {

    public HttpRequest() {
        this(HttpMethod.GET, Map.of(), null, "/");
    }

    public String getBaseUrl() {
        return this.url.split("/")[0];


    }
}
