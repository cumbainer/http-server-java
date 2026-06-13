import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class EchoHttpEndpoint implements HttpEndpoint {
    private static final Set<String> SUPPORTED_COMPRESSIONS = Set.of("gzip");
    private static final String COMPRESSION_HEADER = "Accept-Encoding";

    private final HttpResponseBuilder responseBuilder;

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        String[] parts = request.url().split("/");
        String echoPart = parts[2];

        boolean hasCompression = request.headers().containsKey(COMPRESSION_HEADER);
        boolean isValidCompression = SUPPORTED_COMPRESSIONS.contains(request.headers().get(COMPRESSION_HEADER));
        if (hasCompression && isValidCompression) {
            echoPart = compress(echoPart);
            return responseBuilder.build200(echoPart, Map.of("Content-Encoding", "gzip"));
        }

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

    public static String compress(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        ByteArrayOutputStream obj = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(obj)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(obj.toByteArray());
    }
}
