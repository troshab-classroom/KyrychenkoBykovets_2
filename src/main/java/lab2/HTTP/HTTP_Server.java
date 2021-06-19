package lab2.HTTP;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

public class HTTP_Server {
    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        HTTP_Server.port = port;
    }

    private static int port = 1234;
    public static void main(String [] args) throws IOException   {
        HttpServer server = HttpServer.create(new InetSocketAddress(port),0);
        server.start();
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if(exchange.getRequestMethod().equals("GET")) {
                    byte[] response = "{\"status\": \"ok\"}".getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                }
                else{

                    exchange.sendResponseHeaders(405, 0);


                }


             //   exchange.sendResponseHeaders();
            }
        });

    }
    }
