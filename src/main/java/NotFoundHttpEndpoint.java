import java.util.Collections;

public class NotFoundHttpEndpoint implements HttpEndpoint {
    public NotFoundHttpEndpoint() {
    }

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        return new HttpResponse(Collections.emptyList(), null, HttpStatus.NOT_FOUND);
    }

    @Override
    public boolean matches(HttpRequest request) {
        return false;
    }
}
