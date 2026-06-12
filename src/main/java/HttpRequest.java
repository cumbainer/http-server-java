import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private HttpMethod method;
    private Map<String, String> headers;
    private String requestBody;
    private String url;

    public HttpRequest(HttpMethod method, Map<String, String> headers, String requestBody, String url) {
        this.method = method;
        this.headers = headers;
        this.requestBody = requestBody;
        this.url = url;
    }

    public HttpRequest() {
        this.url = "/";
        this.requestBody = null;
        this.headers = Map.of();
        this.method = HttpMethod.GET;
    }

    public String getBaseUrl() {
        return this.url.split("/")[0];
    }
}
