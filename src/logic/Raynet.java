package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.jsonObjects.*;
import logic.objects.Company;
import logic.objects.Person;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class Raynet {

    private static final int HOPS = 5;
    public final Map<String, Company> companies = new TreeMap<>();
    private final Map<Integer, Person> persons = new HashMap<>();

    public String getPdfUrl(Integer offerId) throws UnsupportedEncodingException {
        String request = Utils.RAYNET_API_URL + "/offer/" + offerId + "/pdfExport";
        String response = Methods.sendGet(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonOfferPdfExport json = gson.fromJson(response, JsonOfferPdfExport.class);
        String url = json.getRequest();
        System.out.println(url);
        return url;
    }

    public String getOffers(Integer businessCaseId) {
//        StringBuilder result = new StringBuilder();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//        int companyID = companies.get(businessCaseId).id;
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/offer/?businessCase[EQ]=" + businessCaseId);
        JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
//        for (int j = 0; j < jsonOffer.data.size(); j++) {
//            result.append(jsonOffer.data.get(j).asHTML());
//        }
        return jsonOffer.asHTML();
    }

    public String getBusinessCases(String companyName) {
        System.out.println("response for getIdFor (" + companyName + ")=" + companies.get(companyName).id);
        int companyID = companies.get(companyName).id;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/businessCase/?company[EQ]=" + companyID);
        JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
        return jsonBusinessCase.asHTML();
    }

    public void init() {
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/company/?limit=1");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonCompany jsonCompany = gson.fromJson(response, JsonCompany.class);
        int bulk = jsonCompany.totalCount / HOPS;
        System.out.print("Loading companies " + jsonCompany.totalCount + "\t");
        for (int i = 0; i < HOPS + 1; i++) {
            response = Methods.sendGet(Utils.RAYNET_API_URL + "/company/?limit=" + bulk + "&offset=" + i * bulk);
            jsonCompany = gson.fromJson(response, JsonCompany.class);
            for (int j = 0; j < jsonCompany.data.size(); j++) {
                companies.put(jsonCompany.data.get(j).name, jsonCompany.data.get(j));
            }
            System.out.print(".");
        }
        System.out.println("\tloaded:" + companies.size() + " (" + (jsonCompany.totalCount - companies.size()) + " skipped)");
        System.out.println("------------ Raynet initiated -----------");
    }



    public String getPersons(String companyName) {
        StringBuilder result = new StringBuilder();
        Integer companyID = this.companies.get(companyName).id;
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/person/?primaryRelationship-company-id=" + companyID);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
        persons.clear();
        for (int j = 0; j < jsonPerson.data.size(); j++) {
            persons.put(jsonPerson.data.get(j).id, jsonPerson.data.get(j));
        }
        for (Person person : persons.values()) {
            result.append(person.asHTML());
        }
        return result.toString();
    }

}
