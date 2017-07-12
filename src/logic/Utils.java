package logic;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.jsonObjects.JsonResponse;

import java.util.LinkedHashMap;
import java.util.Map;

public class Utils {
    public static Map<String, String> split(String query) {
//        System.out.print("Entering splitter >>>>>>");
//        System.out.print(query);
        Map<String, String> result = new LinkedHashMap<>();
        String[] params = query.split("&");
        for (String param : params) {
            String[] pairs = param.split("=");
            if (pairs.length==2)result.put(pairs[0], pairs[1]);
        }
//        System.out.println("<<<<<<<<  Leaving  splitter");
        return result;
    }


    public static Integer getCreatedId(String response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonResponse json = gson.fromJson(response, JsonResponse.class);
        if (json.success) return json.data.id;
        else return -1;
    }
}

