package logic;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;


class WebServer {

    public static void main(String[] args) {
        int port = 81;
        if (args.length > 0)
            port = Integer.parseInt(args[0]);
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server started at " + port);
            server.createContext("/", new DynamicHandler());
            server.createContext("/css", new StaticHandler());
            server.createContext("/js", new StaticHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
