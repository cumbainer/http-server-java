public class HttpRequestBuilder {

    public static HttpRequest build(String req) {


    }

    public static HttpRequest build(byte[] inputStream) {
        try {
            return new HttpRequest();
        } catch (Exception ex) {
            System.out.printf("Exception while building HttpRequest %s%n", ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
