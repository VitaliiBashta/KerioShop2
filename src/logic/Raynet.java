package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.jsonObjects.JsonBusinessCase;
import logic.jsonObjects.JsonCompany;
import logic.jsonObjects.JsonOffer;
import logic.jsonObjects.JsonPerson;
import logic.objects.*;
import webAccess.Methods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Raynet {
    public static final String RAYNET_URL = "https://app.raynet.cz";
    private static final int BULK_COUNT = 500;
    Map<String, Company> companies = new HashMap<>();
    Map<Integer, Person> persons = new HashMap<>();
    Map<Integer, BusinessCase> businessCases = new HashMap<>();
    Map<Integer, Offer> offers = new HashMap<>();



    public void init() {

//        initDials("/api/v2/companyCategory/", CompanyCategory.class);
        initCompanies();
        initPersons();
        initBusinessCases();
        initOffers();

        linkCompanyAndPerson();
        linkBusinessCaseAndOwnerAndPerson();
        LinkBusinessCaseAndOffer();
        System.out.println("------------ Raynet initiated -----------");
    }

    private void LinkBusinessCaseAndOffer() {
        for (Offer offer : offers.values()) {
            if (offer.businessCase.id > 0) {
                BusinessCase businessCase = businessCases.get(offer.businessCase.id);
                offer.businessCase = businessCase;
                businessCase.offer = offer;
            }
        }
    }

    private void linkBusinessCaseAndOwnerAndPerson() {
        for (BusinessCase businessCase : businessCases.values()) {
            if (businessCase.owner.id > 0) {
                businessCase.owner = persons.get(businessCase.owner.id);
            }
            if (businessCase.person != null) {
                businessCase.person = persons.get(businessCase.person.id);
            }
        }
    }

    private void initOffers() {
        String response;
        response = Methods.sendGet("/api/v2/offer/?limit=1");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonOffer json = gson.fromJson(response, JsonOffer.class);

        for (int i = 0; i < json.totalCount / BULK_COUNT + 1; i++) {
            response = Methods.sendGet("/api/v2/offer/?limit=" + BULK_COUNT + "&offset=" + i * BULK_COUNT);
            JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
            for (int j = 0; j < jsonOffer.data.size(); j++) {
                offers.put(jsonOffer.data.get(j).id, jsonOffer.data.get(j));
            }
            System.out.println("Loading offer cases. Loaded:" + offers.size() + ":" + json.totalCount);
        }
    }

    private void initBusinessCases() {
        String response;
        response = Methods.sendGet("/api/v2/businessCase/?limit=1");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonBusinessCase json = gson.fromJson(response, JsonBusinessCase.class);

        for (int i = 0; i < json.totalCount / BULK_COUNT + 1; i++) {
            response = Methods.sendGet("/api/v2/businessCase/?limit=" + BULK_COUNT + "&offset=" + i * BULK_COUNT);
            JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
            for (int j = 0; j < jsonBusinessCase.data.size(); j++) {
                businessCases.put(jsonBusinessCase.data.get(j).id, jsonBusinessCase.data.get(j));
            }
            System.out.println("Loading business cases. Loaded:" + businessCases.size() + ":" + json.totalCount);
        }
    }

//    private <T> void initDials(String url, T clazz){
//        String response;
//        response = Methods.sendGet(url);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        JsonResponse json = gson.fromJson(response, JsonResponse.class);
//        for (int i = 0; i < json.totalCount; i++) {
//            if (clazz instanceof CompanyCategory) {
//                companyCategories.put((CompanyCategory) json.data.get(i)).id,(CompanyCategory)json.data.get(i));
//
//            }
//        }
//    }

    private void linkCompanyAndPerson() {
        for (Person person : persons.values()) {
            if (person.primaryRelationship != null) {
                Company company = companies.get(person.primaryRelationship.company.name);
                if (company != null) {
                    company.employees.add(person);
                    person.primaryRelationship.company = company;
                } else {
                    System.out.println("===error in person : " + person);
                }
                System.out.println(person + " works in " + companies.get(person.primaryRelationship.company.name));
            }
        }
    }

    private void initPersons() {
        String response;
        response = Methods.sendGet("/api/v2/person/?limit=1");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonPerson json = gson.fromJson(response, JsonPerson.class);
        int i = 0;
        while (persons.size() < json.totalCount) {
            response = Methods.sendGet("/api/v2/person/?limit=" + BULK_COUNT + "&offset=" + i * (BULK_COUNT - 1));


            JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
            for (int j = 0; j < jsonPerson.data.size(); j++) {
                persons.put(jsonPerson.data.get(j).id, jsonPerson.data.get(j));
            }
            i++;
            System.out.println("Loading persons. Loaded:" + persons.size() + ":" + json.totalCount);

        }

//        for (Person person : persons.values()) {
//            System.out.println(person);
//        }

    }


    private void initCompanies() {
        String response;
        response = Methods.sendGet("/api/v2/company/?limit=1");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonCompany json = gson.fromJson(response, JsonCompany.class);

        for (int i = 0; i < json.totalCount / BULK_COUNT + 1; i++) {
            response = Methods.sendGet("/api/v2/company/?limit=" + BULK_COUNT + "&offset=" + i * BULK_COUNT);
            JsonCompany jsonCompany = gson.fromJson(response, JsonCompany.class);
            for (int j = 0; j < jsonCompany.data.size(); j++) {
                companies.put(jsonCompany.data.get(j).name, jsonCompany.data.get(j));
            }
            System.out.println("Loading companies. Loaded:" + companies.size() + ":" + json.totalCount);
        }
    }


    public String getCompanies() {
        StringBuilder result = new StringBuilder();
        result.append("<option value=\"\"></option>");
        for (Company company : companies.values()) {
            result.append(company.asHTML());
        }
        return result.toString();
    }

    public String getPersons() {
        StringBuilder result = new StringBuilder();
        for (Person person : persons.values()) {
            result.append(person.asHTML());
        }
        return result.toString();
    }

    public String getPersons(String companyName) {
        StringBuilder result = new StringBuilder();
        Company company = companies.get(companyName);
        for (Person person : company.employees) {
            result.append(person.asHTML());
        }
        return result.toString();
    }

    public String getCompanyList() {
        Gson gson = new Gson();

        System.out.println("companies total:" + companies.size());
        List<CompanySimple> companiesSimple = new ArrayList<>();
        for (Company company : companies.values()) {
            companiesSimple.add(new CompanySimple(company.id, company.name));
        }
        String result = gson.toJson(companiesSimple);
        System.out.println("{\" companies\": "+ result+ "}");
        return result;
    }
}
