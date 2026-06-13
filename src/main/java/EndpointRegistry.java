import java.util.ArrayList;
import java.util.List;

public class EndpointRegistry {
    private static final List<HttpEndpoint> endpoints = new ArrayList<>();

    public EndpointRegistry() {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();

        endpoints.add(new FilesHttpEndpoint(responseBuilder));
        endpoints.add(new RootHttpEndpoint(responseBuilder));
        endpoints.add(new UserAgentHttpEndpoint(responseBuilder));
        endpoints.add(new EchoHttpEndpoint(responseBuilder));
    }

    public HttpEndpoint getEndpoint(HttpRequest request) {
        return endpoints.stream()
                .filter(endpoint -> endpoint.getUrl().contains(request.getBaseUrl()))
                .findAny()
                .orElseGet(NotFoundHttpEndpoint::new);
    }
}
