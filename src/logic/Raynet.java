package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.dials.Dial;
import logic.jsonDials.JsonDial;
import logic.jsonObjects.JsonBusinessCase;
import logic.jsonObjects.JsonCompany;
import logic.jsonObjects.JsonOffer;
import logic.jsonObjects.JsonPerson;
import logic.objects.*;
import webAccess.Methods;

import java.io.UnsupportedEncodingException;
import java.util.*;


public class Raynet {
    public static final String RAYNET_URL = "https://app.raynet.cz";
    private static final int BULK_COUNT = 500;

    Map<String, Company> companies = new TreeMap<>();
    private Map<String, Dial> businessCaseCategories = new TreeMap<>();
    private Map<String, Dial> businessCasePhases = new HashMap<>();
    private Map<String, Dial> contactSources = new HashMap<>();
    private Map<Integer, Person> persons = new HashMap<>();
    private Map<Integer, BusinessCase> businessCases = new HashMap<>();
    private Map<Integer, Offer> offers = new HashMap<>();


    public void init() {
        initBusinessCaseCategory();
        initBusinessCasePhase();
        initContactSource();
        initCompanies();
        initPersons();
//        initBusinessCases();
//        initOffers();

        linkCompanyAndPerson();
//        linkBusinessCaseAndOwnerAndPerson();
//        LinkBusinessCaseAndOffer();
        System.out.println("------------ Raynet initiated -----------");
    }


    private void initBusinessCaseCategory() {
        String response;
        response = Methods.sendGet("/api/v2/businessCaseCategory/");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonDial json = gson.fromJson(response, JsonDial.class);
        for (int j = 0; j < json.data.size(); j++) {
            businessCaseCategories.put(json.data.get(j).code01, json.data.get(j));
        }
        System.out.println("Loading business categories. Loaded:" + businessCaseCategories.size() + ":" + json.totalCount);
    }

    private void initBusinessCasePhase() {
        String response;
        response = Methods.sendGet("/api/v2/businessCasePhase/");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonDial json = gson.fromJson(response, JsonDial.class);
        for (int j = 0; j < json.data.size(); j++) {
            businessCasePhases.put(json.data.get(j).code01, json.data.get(j));
        }
        System.out.println("Loading business phases. Loaded:" + businessCasePhases.size() + ":" + json.totalCount);
    }

    private void initContactSource() {
        String response;
        response = Methods.sendGet("/api/v2/contactSource/");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonDial json = gson.fromJson(response, JsonDial.class);
        for (int j = 0; j < json.data.size(); j++) {
            contactSources.put(json.data.get(j).code01, json.data.get(j));
        }
        System.out.println("Loading contact sources. Loaded:" + contactSources.size() + ":" + json.totalCount);
    }


    public String getBusinessCaseCategory() {
        StringBuilder result = new StringBuilder();
        result.append("<option value=\"\"></option>");
        for (Dial item : businessCaseCategories.values()) {
            result.append(item.asHtmlOption());
        }
        return result.toString();
    }

    public String getBusinessCasePhase() {
        StringBuilder result = new StringBuilder();
        result.append("<option value=\"\"></option>");
        for (Dial item : businessCasePhases.values()) {
            result.append(item.asHtmlOption());
        }
        return result.toString();
    }

    public String getContactSource() {
        StringBuilder result = new StringBuilder();
        return result.toString();

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
//                System.out.println(person + " works in " + companies.get(person.primaryRelationship.company.name));
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

    public String getPersons(String companyName) {
        StringBuilder result = new StringBuilder();
        Company company = companies.get(companyName);
        for (Person person : company.employees) {
            result.append(person.asHTML());
        }
        return result.toString();
    }

    public String getCompanyList() {
        return listToJson(companies.values());
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

//        List<NameValuePair> urlParameters = new ArrayList<>();
//        urlParameters.add(new BasicNameValuePair("name", "testtest"));
//        urlParameters.add(new BasicNameValuePair("company", "1"));
//        urlParameters.add(new BasicNameValuePair("owner", "3"));
//        urlParameters.add(new BasicNameValuePair("currency", "16"));

//        String jsonString = "{" +
//                "  \"name\": \"testtest\"," +
//                "  \"company\": 1," +
//                "  \"owner\": 3," +
//                "  \"currency\": 15"
//                + "}";
//        HttpEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
//
//        String response = Methods.sendPut("/api/v2/businessCase/", entity);
//        System.out.println(response);

        String asB64 = Base64.getEncoder().encodeToString("vitalii.bashta@zebra.cz:V1596italii".getBytes("utf-8"));

        System.out.println(asB64);
    }

    public <T> String listToJson(Collection<T> list) {

        StringBuilder result = new StringBuilder();
        for (T item : list) {
            result.append(item.toString()).append(",");
        }
        result.insert(0, "[");
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");
        return result.toString();
    }



}
