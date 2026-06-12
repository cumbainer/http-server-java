import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EndpointRegistry {
    private static final List<HttpEndpoint> endpoints = new ArrayList<>();
    {
        endpoints.add(new HttpEndpoint(HttpMethod.GET, "/"));
        endpoints.add(new HttpEndpoint(HttpMethod.GET, "/user-agent"));
        endpoints.add(new HttpEndpoint(HttpMethod.GET, "/echo/**"));
        endpoints.add(new HttpEndpoint(HttpMethod.GET, "/files/{filename}"));
    }

    public static List<HttpEndpoint> getEndpoints() {
        return endpoints;
    }
}
