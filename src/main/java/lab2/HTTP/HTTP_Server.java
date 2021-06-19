package lab2.HTTP;
import javax.crypto.spec.SecretKeySpec;

import java.security.Key;

import com.sun.net.httpserver.*;
import io.jsonwebtoken.*;
import java.util.Date;


import com.fasterxml.jackson.databind.ObjectMapper;
import lab2.Database.DBCommands;
import lab2.Database.Product;
import lab2.Database.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class HTTP_Server {



    static Key signingKey;
    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        HTTP_Server.port = port;
    }

    private static int port = 1234;
    public static void main(String [] args) throws IOException   {
        DBCommands comm = new DBCommands();
        comm.Create();
       // comm.InsertUser(new User("login3","password"));
       // comm.InsertUser(new User("login4","password"));
        String jwt = createJWT("login");
        System.out.println(findUserByJWT(jwt));
        ObjectMapper myMapper = new ObjectMapper();
        ///byte[] response = "{\"login\": \"ok\",\"password\" : \"password\" }".getBytes(StandardCharsets.UTF_8);
        //User user = myMapper.readValue(response, User.class);
       // System.out.println(user);
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
            }
        });
        server.createContext("/login",exchange -> {
            if(exchange.getRequestMethod().equals("POST")){
               // byte[] response = "{\"status\": \"ok\"}".getBytes(StandardCharsets.UTF_8);
                byte[] response = "{\"login\": \"ok\",\"password\" : \"password\" }".getBytes(StandardCharsets.UTF_8);
               User user =  myMapper.readValue(response, User.class);
                try {
                    User find = comm.getUserByLogin(user.getLogin());
                    if(find!=null){
                     if (find.getPassword().equals(user.getPassword())){
                      exchange.getResponseHeaders().set("Authorization", createJWT(find.getLogin()));
                      exchange.sendResponseHeaders(200,0);
                   //   String jwt = createJWT(find.getLogin());
                     //System.out.println(findUserByJWT(jwt));
                     }
                     else   exchange.sendResponseHeaders(401,0);

                    }
                    else{

                        exchange.sendResponseHeaders(401,0);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            else{
                exchange.sendResponseHeaders(405, 0);
            }
        });


        HttpContext context = server.createContext("/api/good", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                System.out.println(exchange.getPrincipal());
                if(exchange.getRequestMethod().equals("GET")) {

                    Product prod = new Product("milk",10,10);

                    byte[] response = myMapper.writeValueAsBytes(prod);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                }
                else{
                    exchange.sendResponseHeaders(404, 0);
                }
            }
        });
        context.setAuthenticator(new Authenticator() {
            @Override
            public Result authenticate(HttpExchange exch) {
               String jwt =  exch.getRequestHeaders().getFirst("Authorization");
               if(jwt!=null){

                   String login = findUserByJWT(jwt);
                   try {
                       User user =  comm.getUserByLogin(login);
                       if(user!=null){
                       return new Success(new HttpPrincipal(login,"user"));

                       }

                           return new Failure(403);
                   } catch (SQLException throwables) {
                       throwables.printStackTrace();
                   }


               }
               System.out.println("kk");
             //   return new Failure(403);
                return null;
            }
        });

    }


    private static String createJWT(String login) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = "secretlongbesthowruimfinetahks".getBytes();
        signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());


        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+ TimeUnit.HOURS.toMillis(14)))
                .setSubject(login)
                .signWith(signatureAlgorithm, signingKey);



        return builder.compact();
    }
    private static String  findUserByJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(jwt).getBody();
      //  System.out.println("ID: " + claims.getId());
     //   System.out.println("Subject: " + claims.getSubject());
     //   System.out.println("Issuer: " + claims.getIssuer());
       // System.out.println("Expiration: " + claims.getExpiration());
        return claims.getSubject();
    }

    }
