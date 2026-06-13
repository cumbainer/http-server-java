import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FilesHttpEndpoint implements HttpEndpoint {
    private final HttpResponseBuilder responseBuilder;

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        String[] parts = request.url().split("/");
        String filename;
        if (parts.length > 2) {
            filename = DirectoryContainer.getBaseDirectory() + "/" + parts[2];
        } else {
            return responseBuilder.build404();
        }

        if (request.method() == HttpMethod.GET) {
            return handleGet(filename);
        }
        return handlePost(filename, request.requestBody());
    }

    private HttpResponse handlePost(String filename, String content) {
        Path filePath = Path.of(filename);

        try {
            Files.writeString(
                    filePath,
                    content == null ? "" : content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

            return new HttpResponse(
                    List.of("Content-Length: 0"),
                    "",
                    HttpStatus.CREATED
            );
        } catch (IOException e) {
            e.printStackTrace();

            return new HttpResponse(
                    List.of("Content-Length: 0"),
                    "",
                    HttpStatus.INTERNAL_ERROR
            );
        }
    }

    private HttpResponse handleGet(String filename) {
        StringBuilder fileContent = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNext()) {
                fileContent.append(scanner.nextLine());
            }
            return responseBuilder.build200(fileContent.toString(), Map.of("Content-Type", "application/octet-stream"));
        } catch (FileNotFoundException ex) {
            return responseBuilder.build404();
        }
    }

    @Override
    public String getUrl() {
        return "/files/{filename}";
    }

    @Override
    public boolean matches(HttpRequest request) {
        return request.getBaseUrl().contains("files");
    }

    public FilesHttpEndpoint(HttpResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }
}
