import java.util.ArrayList;
import java.util.List;

public class HttpResponsePostProcessor {
    public HttpResponse postProcess(HttpRequest request, HttpResponse response) {
        if (ParserHelper.shouldClose(request)) {
            List<String> newHeaders = new ArrayList<>(response.responseHeaders());
            newHeaders.add("Connection: close");
            return new HttpResponse(newHeaders, response.responseBody(), response.responseStatus());
        }
        return response;
    }

}
