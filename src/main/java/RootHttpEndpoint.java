public class RootHttpEndpoint implements HttpEndpoint {
    private final HttpResponseBuilder responseBuilder;

    public RootHttpEndpoint(HttpResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        return responseBuilder.build200("");
    }

    @Override
    public String getUrl() {
        return "/";
    }

    @Override
    public boolean matches(HttpRequest request) {
        return request.getBaseUrl().equals("/");
    }
}