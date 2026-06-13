public class EchoHttpEndpoint implements HttpEndpoint {
    private final HttpResponseBuilder responseBuilder;

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        String[] parts = request.url().split("/");
        String echoPart = parts[2];
        return responseBuilder.build200(echoPart);
    }

    @Override
    public String getUrl() {
        return "/echo/**";
    }

    @Override
    public boolean matches(HttpRequest request) {
        return request.getBaseUrl().contains("echo");
    }

    public EchoHttpEndpoint(HttpResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }
}
