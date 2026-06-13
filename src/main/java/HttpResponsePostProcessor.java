import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class HttpResponsePostProcessor {
    private static final Set<String> SUPPORTED_COMPRESSIONS = Set.of("gzip");
    private static final String COMPRESSION_HEADER = "Accept-Encoding";

    public HttpResponse postProcess(HttpRequest request, HttpResponse response) {
        if (ParserHelper.shouldClose(request)) {
            response.responseHeaders().add("Connection: close");
        }
        return response;
    }

    private boolean shouldCompress(HttpRequest request) {
        String compressions = request.headers().getOrDefault(COMPRESSION_HEADER, "");
        boolean hasCompression = request.headers().containsKey(COMPRESSION_HEADER);
        boolean isValidCompression = SUPPORTED_COMPRESSIONS.stream().anyMatch(compressions::contains);
        return hasCompression && isValidCompression;
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
