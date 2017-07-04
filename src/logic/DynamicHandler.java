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

        System.out.println("uri>" + requestedUri);
        if (he.getRequestMethod().equals("GET")) {

            if (requestedUri.getPath().equals("/"))
                sendFile("kerioLogin.html", he);
            else
                sendFile(requestedUri.getRawPath(), he);
        }

        if (he.getRequestMethod().equals("POST")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(he.getRequestBody(), "utf-8"));
            String query = br.readLine();
            System.out.println("query: " +  query);
            String params[] = query.split("[,]");
            if (params[0].equalsIgnoreCase("off")) System.exit(0);



            String response = "";
            if (!response.equals("")) {
                he.sendResponseHeaders(200, response.length());
                os.write(response.getBytes());
            }
            os.flush();
        }
        os.close();

    }

    private void sendFile(String target, HttpExchange he) {
        File file = new File(new File("wwwRoot"), target);

        if (file.exists()) {
            try {
                OutputStream os = he.getResponseBody();
                he.sendResponseHeaders(200, file.length());
                int n;
                try (InputStream is = new FileInputStream(file.getAbsolutePath())) {
                    while ((n = is.read(buf)) > 0)
                        os.write(buf, 0, n);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}