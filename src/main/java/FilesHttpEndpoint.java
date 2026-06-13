import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FilesHttpEndpoint implements HttpEndpoint {
    private final HttpResponseBuilder responseBuilder;

    @Override
    public HttpResponse processRequest(HttpRequest request) {
        String[] parts = request.url().split("/");
        String filename;
        if (parts.length > 2) {
            filename = parts[3];
        } else {
            return responseBuilder.build404();
        }

        StringBuilder fileContent = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNext()) {
                fileContent.append(scanner.nextLine());
            }
            return responseBuilder.build200(fileContent.toString());
        } catch (FileNotFoundException ex) {
            return responseBuilder.build404();
        }
    }

    @Override
    public String getUrl() {
        return "/files/{filename}";
    }

    public FilesHttpEndpoint(HttpResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }
}
