package webServer;

import com.sun.net.httpserver.HttpServer;
import logic.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

class WebServer {

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        int port = 81;
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        webServer.start(port);
    }

    private void start(int port) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/company", new CompanyHandler());
            server.createContext("/businessCase", new BusinessCaseHandler());
            server.createContext("/offer", new OffersHandler());
            server.createContext("/person", new PersonHandler());
            server.createContext("/Pdf", new PdfHandler());
            server.createContext("/MailTo", new MailHandler());
            server.createContext("/licenseInfo", new LicenseInfoHandler());
            server.createContext("/", new StaticHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("server started at " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
