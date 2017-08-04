package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Utils {
    public static final String RAYNET_URL = "https://app.raynet.cz/api/v2";
    public static final String KERIO_URL = "https://secure.kerio.com/order/upgrWizIndex.php";
    public static final Double DISTRIBUTOR_MARGIN = 0.45;

    public static Integer getCreatedId(String response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonResponse json = gson.fromJson(response, JsonResponse.class);
        if (json.success) return json.data.id;
        else return -1;
    }

    public static <T> String objectToJson(T obj) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        return gson.toJson(obj);
    }

    public static void writeResponse(HttpExchange he, String response) {
        try (OutputStream os = he.getResponseBody()) {
            he.sendResponseHeaders(200, response.getBytes().length);
            os.write(response.getBytes());
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendRequest(String url) {
        return sendRequest(url, SendMethod.GET, null);
    }

    private static class JsonResponse {
        private Dial data;
        private boolean success;

        private class Dial {
            private int id;
        }
    }

    public static String sendRequest(String url, SendMethod method, HttpEntity entity) {
        HttpClient client = HttpClientBuilder.create().build();

        HttpRequestBase request;
        switch (method) {
            case PUT:
                request = new HttpPut(url);
                if (entity != null) ((HttpPut) request).setEntity(entity);
                break;
            case POST:
                request = new HttpPost(url);
                if (entity != null) ((HttpPost) request).setEntity(entity);
                break;
            default:
                request = new HttpGet(url);
                break;
        }
        request.addHeader("X-Instance-Name", "zebra");
        request.addHeader("Authorization", "Basic dml0YWxpaS5iYXNodGFAemVicmEuY3o6VjE1OTZpdGFsaWk=");

        StringBuilder result = new StringBuilder();
        try {
            HttpResponse response = client.execute(request);

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()))) {
                String line;
                while ((line = reader.readLine()) != null) result.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}

