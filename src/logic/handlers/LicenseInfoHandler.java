package logic.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Methods;
import logic.Utils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LicenseInfoHandler implements HttpHandler {

    private static String getLicKeyInfo(String licenseNumber) {
        String params = "licence=" + licenseNumber + "&next=Continue";
        String response = Methods.sendPost(Utils.KERIO_URL, params);
        Document doc = Jsoup.parse(response);
        Elements tables = doc.body().select(".tableVertical ");
        List<String> licInfo = new ArrayList<>();
        if (tables.size() > 0) {
            Element table = tables.get(0);
            Elements rows = table.select("tr");
            for (int i = 0; i < rows.size(); i++) {
                Element row = rows.get(i);
                String value = row.select("td").get(0).text();
                if (i == 4) {
                    DateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
                    Date startDate = null;
                    try {
                        startDate = df.parse(value);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                    value = newFormat.format(startDate);
                }
                licInfo.add(value);
            }
        } else {
            licInfo.add("Neplatné lic. číslo!");
        }
        Gson gson = new Gson();
        String json = gson.toJson(licInfo);
        System.out.println(json);
        return json;
    }

    @Override
    public void handle(HttpExchange he) throws IOException {
        URI requestedUri = he.getRequestURI();
        OutputStream os = he.getResponseBody();
        if (he.getRequestMethod().equals("GET")) {
            String query = requestedUri.getQuery();
            String response = "";
            if (query != null) {
                System.out.println(">>> GETTING info about: " + query);
                response = getLicKeyInfo(query);
            }
            if (!response.equals("")) {
                System.out.println("<<<: \t" + response);
                he.sendResponseHeaders(200, response.getBytes().length);
                os.write(response.getBytes());
            }
            os.flush();
        }
        os.close();
    }
}