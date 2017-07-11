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

import java.util.*;


public class Raynet {
    public static final String RAYNET_URL = "https://app.raynet.cz";
    private static final int HOPS = 5;

    Map<String, Company> companies = new TreeMap<>();
    private Map<String, Map<String, Dial>> dials = new HashMap<>();
    private Map<Integer, Person> persons = new HashMap<>();
    private Map<Integer, BusinessCase> businessCases = new HashMap<>();
    private Map<Integer, Offer> offers = new HashMap<>();


    Raynet() {
        dials.put("businessCaseCategory", new TreeMap<>());
        dials.put("businessCasePhase", new TreeMap<>());
        dials.put("contactSource", new TreeMap<>());
    }

    void init() {
        initDials();
        initCompanies();
        initPersons();
//        initBusinessCases();
//        initOffers();

        linkFields();
        System.out.println("------------ Raynet initiated -----------");
    }

    private void linkFields(){
        linkCompanyAndPerson();
        linkBusinessCaseAndOwnerAndPerson();
        LinkBusinessCaseAndCompany();
        LinkBusinessCaseAndOffer();
    }

    private void initDials() {
        for (Map.Entry<String, Map<String, Dial>> entry : dials.entrySet()) {
            String response = Methods.sendGet("/api/v2/" + entry.getKey() + "/");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonDial json = gson.fromJson(response, JsonDial.class);
            Map<String, Dial> dialList = new TreeMap<>();
            for (int j = 0; j < json.data.size(); j++) {
                dialList.put(json.data.get(j).code01, json.data.get(j));
                entry.setValue(dialList);
            }
            System.out.println("Loading " + entry.getKey() + ". Loaded:" + entry.getValue().size() + ":" + json.totalCount);
        }
    }

    public static void main(String[] args) {
        Raynet r = new Raynet();
        r.initCompanies();
//        r.initPersons();
        for(Company company: r.companies.values()){
            if ( company.primaryAddress.address.countryCode !=null)
                System.out.println("company  "+company.id+" has :" + company.primaryAddress.address.countryCode);

        }
        System.out.println("------------end--------------");
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

//        String asB64 = Base64.getEncoder().encodeToString("vitalii.bashta@zebra.cz:V1596italii".getBytes("utf-8"));

//        System.out.println(asB64);
    }

    String getDialHtml(String dial) {
        StringBuilder result = new StringBuilder();
        result.append("<option value=\"\"></option>");
        Collection<Dial> dialList = dials.get(dial).values();
        for (Dial item : dialList) {
            result.append(item.asHtmlOption());
        }
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

    private void LinkBusinessCaseAndCompany() {
        for (BusinessCase businessCase : businessCases.values()) {
            if (businessCase.company.id > 0) {
                System.out.println("linking businessCase: " + businessCase.id + ":" + businessCase.name);
                Company company = companies.get(businessCase.company.name);
                if (company != null) {
                    company.businessCases.add(businessCase);
                    businessCase.company = company;
                }
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
        System.out.print("Loading offers\t");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonOffer json = gson.fromJson(response, JsonOffer.class);
        int bulk = json.totalCount / HOPS;
        for (int i = 0; i < json.totalCount / bulk + 1; i++) {
            response = Methods.sendGet("/api/v2/offer/?limit=" + bulk + "&offset=" + i * bulk);
            JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
            for (int j = 0; j < jsonOffer.data.size(); j++) {
                offers.put(jsonOffer.data.get(j).id, jsonOffer.data.get(j));
            }

        }
        System.out.println(" Loaded:" + offers.size() + ":" + json.totalCount);
    }

    private void initBusinessCases() {
        String response;
        response = Methods.sendGet("/api/v2/businessCase/?limit=1");
        System.out.print("Loading business cases\t");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonBusinessCase json = gson.fromJson(response, JsonBusinessCase.class);
        int bulk = json.totalCount / HOPS;
        for (int i = 0; i < json.totalCount / bulk + 1; i++) {
            response = Methods.sendGet("/api/v2/businessCase/?limit=" + bulk + "&offset=" + i * bulk);
            JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
            for (int j = 0; j < jsonBusinessCase.data.size(); j++) {
                businessCases.put(jsonBusinessCase.data.get(j).id, jsonBusinessCase.data.get(j));
            }
            System.out.print(".");
        }
        System.out.println("\tLoaded:" + businessCases.size() + ":" + json.totalCount);
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
        System.out.print("Loading persons\t");
        while (persons.size() < json.totalCount) {
            int bulk = json.totalCount / HOPS;
            response = Methods.sendGet("/api/v2/person/?limit=" + bulk + "&offset=" + i * (bulk - 1));
            JsonPerson jsonPerson = gson.fromJson(response, JsonPerson.class);
            for (int j = 0; j < jsonPerson.data.size(); j++) {
                persons.put(jsonPerson.data.get(j).id, jsonPerson.data.get(j));
            }
            i++;
            System.out.print(".");
        }
        System.out.println("\tLoaded:" + persons.size() + ":" + json.totalCount);

//        for (Person person : persons.values()) {
//            System.out.println(person);
//        }

    }

    private void initCompanies() {
        String response;
        response = Methods.sendGet("/api/v2/company/?limit=1");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonCompany json = gson.fromJson(response, JsonCompany.class);
        System.out.print("Loading companies\t");
        int bulk = json.totalCount / HOPS;
        for (int i = 0; i < json.totalCount / bulk + 1; i++) {
            response = Methods.sendGet("/api/v2/company/?limit=" + bulk + "&offset=" + i * bulk);
            JsonCompany jsonCompany = gson.fromJson(response, JsonCompany.class);
            for (int j = 0; j < jsonCompany.data.size(); j++) {
                companies.put(jsonCompany.data.get(j).name, jsonCompany.data.get(j));
            }
            System.out.print(".");
        }
        System.out.println("\tLoaded:" + companies.size() + ":" + json.totalCount);
    }

    public <T> String companiesToJson(Collection<T> list) {

        StringBuilder result = new StringBuilder();
        for (T item : list) {
            result.append(item.toString()).append(",");
        }
        result.insert(0, "[");
        result.deleteCharAt(result.lastIndexOf(","));
        result.append("]");
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

    public String getBusinessCases(String companyName) {
        StringBuilder result = new StringBuilder();
        Company company = companies.get(companyName);
        for (BusinessCase businessCase : company.businessCases) {
            result.append(businessCase.asHTML());
        }
        return result.toString();
    }
}
