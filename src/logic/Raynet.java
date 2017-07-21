package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logic.jsonObjects.*;
import logic.objects.BusinessCaseRead;
import logic.objects.Company;
import logic.objects.Offer;
import logic.objects.Person;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import webAccess.Methods;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Raynet {
    public static final String RAYNET_URL = "https://app.raynet.cz";
    public static final Double DISTRIBUTOR_MARGIN = 0.45;
    private static final int HOPS = 5;
    public final Map<Integer, BusinessCaseRead> businessCases = new HashMap<>();
    public final Map<Integer, Offer> offers = new HashMap<>();
    final Map<String, Company> companies = new TreeMap<>();
    private final Map<Integer, Person> persons = new HashMap<>();

    public static void main(String[] args) throws ParseException {
        String oldDate = "July 18, 2018";
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        Date startDate = df.parse(oldDate);

        DateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        String value = newFormat.format(startDate);
        System.out.println(value);
    }

    public Company getCompanyById(int id) {
        for (Company company: companies.values()             ) {
            if (company.id == id) return company;
        }
        return null;
    }

    void init() {
        initCompanies();
        initPersons();
        initAllBusinessCases();
        initAllOffers();

        linkFields();
        System.out.println("------------ Raynet initiated -----------");
    }

    public String getPdfUrl(Integer businessCaseId) throws UnsupportedEncodingException {
        Integer offerId = this.businessCases.get(businessCaseId).offer.id;
        String request = "/api/v2/offer/" + offerId + "/pdfExport";
        String response = Methods.sendGet(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonOfferPdfExport json = gson.fromJson(response, JsonOfferPdfExport.class);

        String url1 = json.getRequest();
        System.out.println(url1);
        return url1;

    }

    public void linkFields() {
        linkCompanyAndPerson();
        linkBusinessCaseAndOwnerAndPerson();
        LinkBusinessCaseAndCompany();
        LinkBusinessCaseAndOffer();
    }

    public void LinkBusinessCaseAndOffer() {
        for (Offer offer : offers.values()) {
            if (offer.businessCase.id > 0) {
                BusinessCaseRead businessCase = businessCases.get(offer.businessCase.id);
                offer.businessCase = businessCase;
                businessCase.offer = offer;
            }
        }
    }

    public void LinkBusinessCaseAndCompany() {
        for (BusinessCaseRead businessCase : businessCases.values()) {
            if (businessCase.company.id > 0) {
//                System.out.println("linking businessCase: " + businessCase.id + ":" + businessCase.name);
                Company company = companies.get(businessCase.company.name);
                if (company != null) {
                    company.businessCases.add(businessCase);
                    businessCase.company = company;
                }
            }
        }
    }

    private void linkBusinessCaseAndOwnerAndPerson() {
        for (BusinessCaseRead businessCase : businessCases.values()) {
            if (businessCase.owner.id > 0) {
                businessCase.owner = persons.get(businessCase.owner.id);
            }
            if (businessCase.person != null) {
                businessCase.person = persons.get(businessCase.person.id);
            }
        }
    }

    private void initAllOffers() {
        String response;
        response = Methods.sendGet("/api/v2/offer/?limit=1");
        System.out.print("Loading offers\t");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonOffer json = gson.fromJson(response, JsonOffer.class);
        int bulk = json.totalCount / HOPS;
        for (int i = 0; i < json.totalCount / bulk + 1; i++) {
            initOffers(i * bulk, bulk);
            System.out.print(".");
        }
        System.out.println(" Loaded:" + offers.size() + ":" + json.totalCount);
    }

    private void initOffers(int offset, int limit) {
        String response;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        response = Methods.sendGet("/api/v2/offer/?limit=" + limit + "&offset=" + offset);
        JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
        for (int j = 0; j < jsonOffer.data.size(); j++) {
            offers.put(jsonOffer.data.get(j).id, jsonOffer.data.get(j));
        }
    }

    public void initOffers(int companyId) {
        String response;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        response = Methods.sendGet("/api/v2/offer/?company[EQ]=" + companyId);
        JsonOffer jsonOffer = gson.fromJson(response, JsonOffer.class);
        for (int j = 0; j < jsonOffer.data.size(); j++) {
            offers.put(jsonOffer.data.get(j).id, jsonOffer.data.get(j));
        }
    }

    private void initAllBusinessCases() {
        String response;
        response = Methods.sendGet("/api/v2/businessCase/?limit=1");
        System.out.print("Loading business cases\t");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        JsonBusinessCase json = gson.fromJson(response, JsonBusinessCase.class);
        int bulk = json.totalCount / HOPS;
        for (int i = 0; i < json.totalCount / bulk + 1; i++) {
            initBusinessCases(i * bulk, bulk);
            System.out.print(".");
        }
        System.out.println("\tLoaded:" + businessCases.size() + ":" + json.totalCount);
    }

    private void initBusinessCases(int offset, int limit) {
        String response;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        response = Methods.sendGet("/api/v2/businessCase/?limit=" + limit + "&offset=" + offset);
        JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
        for (int j = 0; j < jsonBusinessCase.data.size(); j++) {
            businessCases.put(jsonBusinessCase.data.get(j).id, jsonBusinessCase.data.get(j));
        }
    }

    public void initBusinessCases(int companyID) {
        String response;
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        response = Methods.sendGet("/api/v2/businessCase/?company[EQ]=" + companyID);
        JsonBusinessCase jsonBusinessCase = gson.fromJson(response, JsonBusinessCase.class);
        for (int j = 0; j < jsonBusinessCase.data.size(); j++) {
            businessCases.put(jsonBusinessCase.data.get(j).id, jsonBusinessCase.data.get(j));
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
        result.append("<option selected disabled value=\"\"></option>");
        result.append("<option value=\"0\">(nový)</option>");
        for (BusinessCaseRead businessCase : company.businessCases) {
            result.append(businessCase.asHTML());
        }
        return result.toString();
    }

    public String getLicKeyInfo(String licenseNumber) {
        String params = "licence=" + licenseNumber + "&next=Continue";
        String url = "https://secure.kerio.com/order/upgrWizIndex.php";
        String response = Methods.sendPost2(url, params);
        Document doc = Jsoup.parse(response);
        Element table = doc.body().select(".tableVertical ").get(0);
        Elements rows = table.select("tr");
        List<String> licInfo = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            String key = row.select("th").get(0).text();
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
        Gson gson = new Gson();
        String json = gson.toJson(licInfo);
        System.out.println(json);
        return json;
    }
}
