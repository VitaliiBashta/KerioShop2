package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;

class DynamicHandler implements HttpHandler {
    private final byte[] buf = new byte[16 * 1024];


    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();

        OutputStream os = he.getResponseBody();

        if (he.getRequestMethod().equals("GET")) {
            System.out.println("uri>" + requestedUri);
            System.out.println("query=" + he.getRequestURI().getQuery());
            if (requestedUri.getPath().equals("/"))
                sendFile("index.html", he);
            else
                sendFile(requestedUri.getRawPath(), he);
        }

        if (he.getRequestMethod().equals("POST")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();

            String response= " ";
            if (query.equals("getCompanies"))
                response = Raynet.getCompanies();
            if (query.equals("getPersons"))
                response = Raynet.getPersons();
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
            int n;
            try (InputStream is = new FileInputStream(file.getAbsolutePath())) {
                while ((n = is.read(buf)) > 0)
                    os.write(buf, 0, n);
            }
        }
    }
}