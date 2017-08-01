package logic;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.jsonObjects.JsonResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    public static final String RAYNET_API_URL = "https://app.raynet.cz/api/v2";
    public static final String KERIO_URL = "https://secure.kerio.com/order/upgrWizIndex.php";
    public static final Double DISTRIBUTOR_MARGIN = 0.45;

    public static Map<String, String> urlParamsToMap(String query) {
        Map<String, String> result = new LinkedHashMap<>();
        String[] params = query.split("&");
        for (String param : params) {
            String[] pairs = param.split("=");
            if (pairs.length == 2) result.put(pairs[0], pairs[1]);
        }
        return result;
    }

    public static Integer getCreatedId(String response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonResponse json = gson.fromJson(response, JsonResponse.class);
        if (json.success) return json.data.id;
        else return -1;
    }

    public static <T>String objectToEntity(T obj){
//        this.businessCase = businessCase;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String json = gson.toJson(obj);
        System.out.println(json);


        return json;
    }
}

