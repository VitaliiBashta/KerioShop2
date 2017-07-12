package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.jsonObjects.JsonResponse;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.util.Map;

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
                    String companyName = query.substring(query.indexOf('?') + 1);
                    response = raynet.getBusinessCases(companyName);
                }
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
            while ((line = br.readLine()) != null) {
                query.append(line);
            }
            String response;
            response = getProductHtml(query.toString());
            if (!response.equals("")) {
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }
        os.close();
    }

    private String getProductHtml(String query) {
        StringBuilder result = new StringBuilder();

        Map<String, String> query_pairs = Utils.split(query);
        String product = query_pairs.get("product");
        if (product.equals("Kerio Connect") ||
                product.equals("Kerio Connect") ||
                product.equals("Kerio Connect"))
            result.append("New license for ");
        result.append(query_pairs.get("product"));
        if (query_pairs.get("goods_type") != null)
            result.append(query_pairs.get("goods_type"));

        if (query_pairs.get("AntiSpam") != null)
            result.append(", AntiSpam");
        if (query_pairs.get("antivirus") != null)
            result.append(", Kerio Antivirus");

        if (query_pairs.get("activeSync") != null)
            result.append(", ActiveSync");

        if (query_pairs.get("ExtWarranty") != null)
            result.append(", incl. Ex Warr");
        if (query_pairs.get("WebFilter") != null)
            result.append(", Kerio Web Filter");

        String users = query_pairs.get("users");
        if (users != null) result.append(", ").append(users).append(" users");

        String swm = query_pairs.get("swm");
        if (swm != null && swm.equals("2"))
            result.append(", 2 years SWM");
        return  result.toString() ;
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


    private Integer getCreatedId(String response){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonResponse json = gson.fromJson(response, JsonResponse.class);
        return json.data.id;
    }
}