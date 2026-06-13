import java.util.Collections;
import java.util.List;

public class HttpResponseBuilder {

    public HttpResponse build200(String responseBody) {
        List<String> headers =  getResponseBodyHeaders(responseBody);

        return new HttpResponse(headers, responseBody, HttpStatus.OK);
    }

    public HttpResponse build404() {
        return new HttpResponse(Collections.emptyList(), "", HttpStatus.NOT_FOUND);
    }

    private List<String> getResponseBodyHeaders(String responseBody) {
        if (responseBody.isBlank()) {
            return Collections.emptyList();
        }
        String contentLengthH = "Content-Length: " + responseBody.length();
        String contentTypeH = "Content-Type: text/plain";
        return List.of(contentLengthH, contentTypeH);
    }


}
