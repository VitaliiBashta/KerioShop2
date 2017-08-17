package logic.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import logic.Utils;

import java.util.LinkedList;
import java.util.List;

import static logic.Utils.*;

public class CompanyHandler implements HttpHandler {
    private static final List<Company> companies = new LinkedList<>();
    private static final List<String> companiesName = new LinkedList<>();
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
            for (Company company : jsonCompany.data) {
//                name = name.replace("&", "\\u0026");
                company.name = company.name.replace("\"", "\\\"");
//                name = name.replace("\'", "\\u0027");
                companies.add(company);
                companiesName.add(company.name);
            }
            System.out.print(".");
        }
        System.out.println("\tloaded:" + companies.size());
    }

    static Company getCompany(Integer id) {
        for (Company company : companies) {
            if (company.id == id) return company;
        }
        return companies.get(0);
    }

    @Override
    public void handle(HttpExchange he) {
        String companyName = he.getRequestURI().getQuery();
        if (companyName != null) {
            String id = "";
            String margin = "0";
            for (Company comp : companies) {
                if (comp.name.equals(companyName)) {
                    if (comp.customFields != null && comp.customFields.Marze_Keri_5288b != null) {
                        margin = comp.customFields.Marze_Keri_5288b.replace("%", "");
                    }
                    id = comp.id + "," + comp.owner.id + "," + margin + "," + comp.regNumber;
                    break;
                }
            }
            writeResponse(he, id);
        } else {
            String jsonCompanies = objectToJson(companiesName);
            writeResponse(he, jsonCompanies);
        }
    }

    private class JsonCompany {
        private int totalCount;
        private List<Company> data;
    }

    public class Company {
        private int id;
        public String name;
        String regNumber;
        Owner owner;
        CustomFields customFields;
    }

    private class Owner {
        Integer id;
        String fullName;
    }

    private class CustomFields {
        String Marze_GFI_0b8ec;
        String Marze_Keri_5288b;
        String Marze_Acro_77f17;
    }

}