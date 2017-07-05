package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.Objects.BusinessCase;
import logic.Objects.Company;
import logic.Objects.Offer;
import logic.Objects.Person;
import logic.jsonObjects.JsonCompany;
import logic.jsonObjects.JsonPerson;
import webAccess.Methods;

import java.util.HashMap;
import java.util.Map;


public class Raynet {
    public static final String RAYNET_URL = "https://app.raynet.cz";
    private static final int BULK_COUNT = 500;
    static Map<Integer, Company> companies = new HashMap<>();
    static Map<Integer, Person> persons = new HashMap<>();
    static Map<Integer, BusinessCase> businessCases = new HashMap<>();
    static Map<Integer, Offer> offers = new HashMap<>();


    public Raynet() {
        companies = new HashMap<>();
        persons = new HashMap<>();
        businessCases = new HashMap<>();
        offers = new HashMap<>();

    }

    public static void main(String[] args) {
        Raynet raynet = new Raynet();
        raynet.init();

    }

    public void init() {
        initCompanies();
        initPersons();
        initEmployees();
    }

    private void initEmployees() {
        for (Person person : persons.values()) {
            if (person.primaryRelationship != null) {
                Company company = companies.get(person.primaryRelationship.company.id);
                if (company != null) {
                    company.employees.add(person);
                    person.primaryRelationship.company = company;
                } else {
                    System.out.println("===error in person : " + person);
                }
                System.out.println(person + " works in " + companies.get(person.primaryRelationship.company.id));
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
                companies.put(jsonCompany.data.get(j).id, jsonCompany.data.get(j));
            }
            System.out.println("Loading companies. Loaded:" + companies.size() + ":" + json.totalCount);
        }

//        for (Company company:companies.values()) {
//            System.out.println(company);
//        }
    }


    public static String getCompanies() {
        StringBuilder result = new StringBuilder();
        for (Company company : companies.values()) {
            result.append(company.asHTML());
        }
        return result.toString();
    }

    public static String getPersons() {
        StringBuilder result = new StringBuilder();
        for (Person person : persons.values()) {
            result.append(person.asHTML());
        }
        return result.toString();
    }
}
