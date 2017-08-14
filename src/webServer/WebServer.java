package webServer;

import com.sun.net.httpserver.HttpServer;
import logic.handlers.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;


class WebServer {
    private static final Logger logger = Logger.getLogger(WebServer.class);
    public static void main(String[] args) {

        WebServer webServer = new WebServer();
        int port = 81;
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        webServer.start(port);
        logger.info(" ------------- server started -----------");
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
            server.createContext("/kerio", new KerioHandler());
            server.createContext("/gfi", new StaticHandler());
            server.createContext("/acronis", new StaticHandler());
            server.createContext("/", new StaticHandler());
            server.setExecutor(null);
            server.start();
            System.out.println("server started at " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
