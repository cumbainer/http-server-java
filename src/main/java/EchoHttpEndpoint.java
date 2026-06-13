import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

        if (shouldCompress(request)) {
            byte[] gzipBytes = compressToGzipBytes(echoPart);
            String gzipBody = new String(gzipBytes, StandardCharsets.ISO_8859_1);

            return responseBuilder.build200(
                    gzipBody,
                    Map.of("Content-Encoding", "gzip")
            );
        }

        return responseBuilder.build200(echoPart);
    }

    private boolean shouldCompress(HttpRequest request) {
        String compressions = request.headers().getOrDefault(COMPRESSION_HEADER, "");
        boolean hasCompression = request.headers().containsKey(COMPRESSION_HEADER);
        boolean isValidCompression = SUPPORTED_COMPRESSIONS.stream().anyMatch(compressions::contains);
        return hasCompression && isValidCompression;
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

    public static byte[] compressToGzipBytes(String str) {
        if (str == null || str.isEmpty()) {
            return new byte[0];
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(str.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }
}
