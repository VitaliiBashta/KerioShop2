package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;

class DynamicHandler implements HttpHandler {
    private final byte[] buf = new byte[16 * 1024];
    private Raynet raynet;

    public DynamicHandler(Raynet raynet) {
        this.raynet = raynet;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();

        OutputStream os = he.getResponseBody();

        if (he.getRequestMethod().equals("GET")) {
            System.out.println("uri>" + requestedUri);
            if (requestedUri.getPath().equals("/"))
                sendFile("index.html", he);
            else
                sendFile(requestedUri.getRawPath(), he);
        }

        if (he.getRequestMethod().equals("POST")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();
            System.out.println("query: " + query);
            String response= " ";
            if (query.equals("getCompanyList"))
                response = raynet.listToJson(raynet.companies.values());
            if (query.equals("getBusinessCaseCategoryList")) {
                response = raynet.getBusinessCaseCategory();
            }
            if (query.equals("getBusinessCasePhaseList")) {
                response = raynet.getBusinessCasePhase();
            }
            if (query.equals("getContactSourceList")) {
                response = raynet.getContactSource();
            }
            if (query.startsWith("setCompany")) {
                String companyName = query.substring(query.indexOf('?')+1);
                response = raynet.getPersons(companyName);
            }

            if (!response.equals("")) {
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }

        if (he.getRequestMethod().equals("PUT")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            StringBuilder query = new StringBuilder();
            String line;
            while ((line = br.readLine()) !=null){
                query.append(line);
            }
            System.out.println("PUT query: " + query + " for URI " + requestedUri);
            String response= " hello  after put ";
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