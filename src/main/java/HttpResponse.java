import java.util.List;

public record HttpResponse(List<String> responseHeaders, String responseBody, HttpStatus responseStatus) {
}
