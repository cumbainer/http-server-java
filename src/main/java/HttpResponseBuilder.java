import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseBuilder {

    public HttpResponse build200(String responseBody) {
        return build200(responseBody, Map.of());
    }

    public HttpResponse build200(String responseBody, Map<String, String> overrideHeaders) {
        Charset bodyCharset = isGzipHeaders(overrideHeaders)
                ? StandardCharsets.ISO_8859_1
                : StandardCharsets.UTF_8;

        return build200(responseBody, overrideHeaders, bodyCharset);
    }

    private HttpResponse build200(
            String responseBody,
            Map<String, String> overrideHeaders,
            Charset bodyCharset
    ) {
        String body = responseBody == null ? "" : responseBody;

        Map<String, String> headers = new LinkedHashMap<>();

        if (!body.isBlank()) {
            headers.put("Content-Type", "text/plain");
        }

        headers.put("Content-Length", String.valueOf(body.getBytes(bodyCharset).length));

        headers.putAll(overrideHeaders);

        return new HttpResponse(toHeaderList(headers), body, HttpStatus.OK);
    }

    public HttpResponse build404() {
        return new HttpResponse(
                List.of("Content-Length: 0"),
                "",
                HttpStatus.NOT_FOUND
        );
    }

    private boolean isGzipHeaders(Map<String, String> headers) {
        return "gzip".equalsIgnoreCase(headers.get("Content-Encoding"));
    }

    private List<String> toHeaderList(Map<String, String> headers) {
        return headers.entrySet()
                .stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .toList();
    }
}