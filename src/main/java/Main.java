public static final int PORT = 4221;

void main(String[] args) {
    DirectoryContainer.setDir(getFileDir(args));

    try (ServerSocket serverSocket = new ServerSocket(PORT)) {
        serverSocket.setReuseAddress(true);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            Thread thread = new Thread(() -> handleHttpRequest(clientSocket));
            thread.start();
        }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
}

private String getFileDir(String[] args) {
    for (int i = 0; i < args.length; i++) {
        if (args[i].equals("--directory")) {
            return args[i + 1];
        }
    }
    return null;
}
private void handleHttpRequest(Socket clientSocket) {
    var registry = new EndpointRegistry();
    var httpReqBuilder = new HttpRequestBuilder();
    var httpResponseSender = new HttpResponseSender();
    var postProcessor = new HttpResponsePostProcessor();

    System.out.println("Accepted new connection from " + clientSocket.getInetAddress().getHostName());

    String requestStr = ParserHelper.parseReqToString(clientSocket);
    HttpRequest request = httpReqBuilder.build(requestStr);

    HttpEndpoint endpoint = registry.getEndpoint(request);
    HttpResponse response = endpoint.processRequest(request);
    HttpResponse processedResponse = postProcessor.postProcess(request, response);

    boolean isSent = httpResponseSender.sendHttpResponse(clientSocket, processedResponse);
    if (isSent) {
        System.out.printf("Response sent with status %s %n", processedResponse.responseStatus());
    }

}

