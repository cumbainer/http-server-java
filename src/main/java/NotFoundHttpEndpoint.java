import java.util.Collections;

public class NotFoundHttpEndpoint implements HttpEndpoint {
    public NotFoundHttpEndpoint() {
    }

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        return new HttpResponse(Collections.emptyList(), null, HttpStatus.NOT_FOUND);
    }

    @Override
    public String getUrl() {
        return "";
    }
}
