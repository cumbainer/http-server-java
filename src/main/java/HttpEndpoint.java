public class HttpEndpoint {
    private HttpMethod method;
    private String url;

    public HttpEndpoint(HttpMethod method, String url) {
        this.method = method;
        this.url = url;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
