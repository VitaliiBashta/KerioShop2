package logic;


import java.util.LinkedHashMap;
import java.util.Map;

public class Splitter {
    public static Map<String, String> split(String query) {
        System.out.print("Enterring splitter >>>>>>");
        Map<String, String> result = new LinkedHashMap<>();
        String[] params = query.split("&");
        for (String param : params) {
            String[] pairs = param.split("=");
            result.put(pairs[0], pairs[1]);
        }
        System.out.println("<<<<<<<<  Leaving  splitter");
        return result;
    }
}

