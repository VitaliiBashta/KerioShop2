package logic;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;

class ProductHandler implements HttpHandler {


    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();

        OutputStream os = he.getResponseBody();

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
            result.append(", +year SWM");
        return result.toString();
    }
}