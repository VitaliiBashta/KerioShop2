package webServer;

import com.sun.net.httpserver.HttpServer;
import logic.Raynet;
import logic.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

class WebServer {
    private final Raynet raynet = new Raynet();

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        int port = 81;
        webServer.raynet.init();
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        webServer.start(port);
    }

    private void start(int port) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server started at " + port);
            server.createContext("/businessCase", new BusinessCaseHandler(raynet));
            server.createContext("/companyList", new CompanyListHandler(raynet));
            server.createContext("/licenseInfo", new LicenseInfoHandler());
            server.createContext("/info", new DynamicHandler(raynet));
            server.createContext("/", new StaticHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
