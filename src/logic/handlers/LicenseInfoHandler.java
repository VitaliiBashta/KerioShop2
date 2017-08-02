package logic.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static logic.SendMethod.POST;
import static logic.Utils.sendRequest;

public class LicenseInfoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) {
        if (he.getRequestMethod().equals("GET")) {
            String query = he.getRequestURI().getQuery();
            if (query != null) {
                System.out.println(">>> GETTING info about: " + query);
                String response = getLicKeyInfo(query);
                System.out.println("<<<: \t" + response);
                Utils.writeResponse(he, response);
            }
        }
    }

    private static String getLicKeyInfo(String licenseNumber) {
        String params = "licence=" + licenseNumber + "&next=Continue";
        HttpEntity entity = new StringEntity(params, ContentType.APPLICATION_FORM_URLENCODED);
        String response = sendRequest(Utils.KERIO_URL, POST, entity);
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
            licInfo.add("License key error!");
        }
        Gson gson = new Gson();
        String json = gson.toJson(licInfo);
        System.out.println(json);
        return json;
    }
}