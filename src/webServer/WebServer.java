package webServer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import logic.Methods;
import logic.Utils;
import logic.handlers.*;
import logic.jsonObjects.JsonCompany;
import logic.objects.Company;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

public class WebServer {
    public static final List<Company> companies = new LinkedList<>();
    private static final int HOPS = 5;

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        int port = 81;
        webServer.init();
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        webServer.start(port);
    }

    private void start(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server started at " + port);
            server.createContext("/businessCase", new BusinessCaseHandler());
            server.createContext("/offer", new OffersHandler());
            server.createContext("/person", new PersonHandler());
            server.createContext("/Pdf", new PdfHandler());
            server.createContext("/companyList", new CompanyListHandler());
            server.createContext("/companyId", new CompanyIdHandler());
            server.createContext("/licenseInfo", new LicenseInfoHandler());
            server.createContext("/", new StaticHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        String response = Methods.sendGet(Utils.RAYNET_API_URL + "/company/?limit=1");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonCompany jsonCompany = gson.fromJson(response, JsonCompany.class);
        int bulk = jsonCompany.totalCount / HOPS;
        System.out.print("Loading companies " + jsonCompany.totalCount + "\t");
        for (int i = 0; i < HOPS + 1; i++) {
            response = Methods.sendGet(Utils.RAYNET_API_URL + "/company/?limit=" + bulk + "&offset=" + i * bulk);
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
        System.out.println("\tloaded:" + companies.size() + " (" + (jsonCompany.totalCount - companies.size()) + " skipped)");
    }
}
