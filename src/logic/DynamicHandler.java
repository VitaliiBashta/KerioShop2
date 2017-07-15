package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;

class DynamicHandler implements HttpHandler {
    private final byte[] buf = new byte[16 * 1024];
    private final Raynet raynet;

    public DynamicHandler(Raynet raynet) {
        this.raynet = raynet;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();

        OutputStream os = he.getResponseBody();

        if (he.getRequestMethod().equals("GET")) {

            String query = requestedUri.getQuery();
            System.out.println("GET parameters>\t" + query);
            String response = "";
            if (query == null) {
                if (requestedUri.getPath().equals("/"))
                    sendFile("index.html", he);
                else
                    sendFile(requestedUri.getRawPath(), he);
            } else {
                if (query.equals("getCompanyList"))
                    response = raynet.companiesToJson(raynet.companies.values());
                if (query.startsWith("getList")) {
                    String className = query.split("=")[1];
                    response = raynet.getDialHtml(className);
                }
                if (query.startsWith("getPersonsFor")) {
                    String companyName = query.split("=")[1];
                    response = raynet.getPersons(companyName);
                }
                if (query.startsWith("getIdFor")) {
                    String companyName = query.split("=")[1];
                    System.out.println("response for getIdFor " + companyName + raynet.companies.get(companyName).id);
                    response = String.valueOf(raynet.companies.get(companyName).id);
                }
                if (query.startsWith("getBusinessCasesFor")) {
                    String companyName = query.split("=")[1];
                    response = raynet.getBusinessCases(companyName);
                }
                if (query.startsWith("getPdfUrl")) {
                    Integer businessCaseId = Integer.valueOf(query.split("=")[1]);
                    response = raynet.getPdfUrl(businessCaseId);
                }
            }
            if (!response.equals("")) {
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }
        os.close();
    }

    private void sendFile(String target, HttpExchange he) throws IOException {
        File file = new File(new File("wwwRoot"), target);
        if (file.exists()) {
            OutputStream os = he.getResponseBody();
            he.sendResponseHeaders(200, file.length());
            System.out.println("Request file: " + file + " mime type: " + Files.probeContentType(file.toPath()));

            int n;
            try (InputStream is = new FileInputStream(file.getAbsolutePath())) {
                while ((n = is.read(buf)) > 0)
                    os.write(buf, 0, n);
            }
        }
    }
}