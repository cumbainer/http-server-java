public interface HttpEndpoint {
    HttpResponse processRequest(HttpRequest request);

    String getUrl();

    boolean matches(HttpRequest request);
}
