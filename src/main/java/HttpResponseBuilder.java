import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseBuilder {

    public HttpResponse build200(String responseBody) {
        return build200(responseBody, Map.of());
    }

    public HttpResponse build200(String responseBody, Map<String, String> overrideHeaders) {
        Map<String, String> headers = new LinkedHashMap<>();

        if (responseBody != null && !responseBody.isBlank()) {
            headers.put("Content-Type", "text/plain");
        }
        headers.put("Content-Length", responseBody == null ? "0" : String.valueOf(responseBody.length()));

        headers.putAll(overrideHeaders);

        return new HttpResponse(toHeaderList(headers), responseBody, HttpStatus.OK);
    }

    public HttpResponse build404() {
        return new HttpResponse(List.of(), "", HttpStatus.NOT_FOUND);
    }

    private List<String> toHeaderList(Map<String, String> headers) {
        return headers.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .toList();
    }
}