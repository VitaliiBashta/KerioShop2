package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;

public class Utils {
    public static final String RAYNET_API_URL = "https://app.raynet.cz/api/v2";
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

    private class JsonResponse {
        public Dial data;
        boolean success;

        private class Dial {
            public int id;
            private String code01;
        }
    }
}

