public interface HttpEndpoint {
    HttpResponse processRequest(HttpRequest request);

    boolean matches(HttpRequest request);
}
