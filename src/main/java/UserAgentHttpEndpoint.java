public class UserAgentHttpEndpoint implements HttpEndpoint {
    private final HttpResponseBuilder responseBuilder;

    public UserAgentHttpEndpoint(HttpResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        String userAgent = request.headers().get("User-Agent");
        return responseBuilder.build200(userAgent);
    }

    @Override
    public String getUrl() {
        return "/user-agent";
    }

    @Override
    public boolean matches(HttpRequest request) {
        return request.getBaseUrl().contains("user-agent");
    }
}
