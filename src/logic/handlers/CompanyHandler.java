package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;
import logic.objects.Company;

import java.util.LinkedList;
import java.util.List;

import static logic.Utils.*;

public class CompanyHandler implements HttpHandler {
    private static final List<Company> companies = new LinkedList<>();
    private static final int HOPS = 5;

    public CompanyHandler() {
        String response = sendRequest(Utils.RAYNET_URL + "/company/?limit=1");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonCompany jsonCompany = gson.fromJson(response, JsonCompany.class);
        int bulk = jsonCompany.totalCount / HOPS;
        System.out.print("Loading companies " + jsonCompany.totalCount + "\t");
        for (int i = 0; i < HOPS + 1; i++) {
            response = sendRequest(Utils.RAYNET_URL + "/company/?limit=" + bulk + "&offset=" + i * bulk);
            jsonCompany = gson.fromJson(response, JsonCompany.class);
            for (JsonCompany.Company company : jsonCompany.data) {
                String name = company.name;
                Integer id = company.id;
//                name = name.replace("&", "\\u0026");
//                name = name.replace("\"", "\\\"");
//                name = name.replace("\'", "\\u0027");
                companies.add(new Company(company.id, company.name));
            }
            System.out.print(".");
        }
        System.out.println("\tloaded:" + companies.size());
    }

    @Override
    public void handle(HttpExchange he) {
        String companyName = he.getRequestURI().getQuery();
        if (companyName != null) {
            String id = "";
            for (Company comp : companies) {
                if (comp.value.equals(companyName)) {
                    id = comp.data;
                    break;
                }
            }
            writeResponse(he, id);
        } else
            writeResponse(he, objectToJson(companies));
    }

    public class JsonCompany {
        int totalCount;
        private List<Company> data;

        public class Company {
            public int id;
            public String name;   //required
        }
    }
}