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

    private static int port = 1234;
    public static void main(String [] args) throws IOException {
        DBCommands comm = new DBCommands();
        comm.Create();
        Product prod = new Product("meet", 23, 45);
        comm.Insert(prod);
        // comm.InsertUser(new User("login5","5f4dcc3b5aa765d61d8327deb882cf99"));
        ObjectMapper myMapper = new ObjectMapper();
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.start();
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equals("GET")) {
                    byte[] response = "{\"status\": \"ok\"}".getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length);
                    exchange.getResponseBody().write(response);
                    exchange.close();
                } else {
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        });
        server.createContext("/login", exchange -> {
            if (exchange.getRequestMethod().equals("POST")) {
                User user = myMapper.readValue(exchange.getRequestBody(), User.class);
                try {
                    User find = comm.getUserByLogin(user.getLogin());
                    if (find != null) {
                        String ps=user.getPassword();
                        if (find.getPassword().equals(hashMD(ps))) {
                            exchange.getResponseHeaders().set("Authorization", createJWT(find.getLogin()));
                            exchange.sendResponseHeaders(200, 0);
                        } else exchange.sendResponseHeaders(401, 0);

                    } else {

                        exchange.sendResponseHeaders(401, 0);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                exchange.sendResponseHeaders(405, 0);
            }
        });


        HttpContext context = server.createContext("/api/good/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                if (exchange.getRequestMethod().equals("GET")) {
                    String s = String.valueOf(exchange.getRequestURI());
                    int id = 0;
                    Product responseProd = null;
                    try {
                        id = Integer.parseInt(s.substring("/api/good/".length(), s.length()));
                    } catch (Exception ex) {
                        exchange.sendResponseHeaders(404, 0);
                        ex.printStackTrace();

                    }
                    try {
                        responseProd = comm.getProductByID(id);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (responseProd != null) {


                        byte[] response = myMapper.writeValueAsBytes(responseProd);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, response.length);
                        exchange.getResponseBody().write(response);
                        exchange.close();
                    }
                    else{
                        exchange.sendResponseHeaders(404, 0);
                    }
                } else if (exchange.getRequestMethod().equals("DELETE")) {
                    String s = String.valueOf(exchange.getRequestURI());
                    int id = 0;
                    Product responseProd = null;
                    try {
                        id = Integer.parseInt(s.substring("/api/good/".length(), s.length()));
                    } catch (Exception ex) {
                        exchange.sendResponseHeaders(404, 0);
                        ex.printStackTrace();
                    }
                    try {
                        responseProd = comm.DeleteId(id);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (responseProd != null) {
                        byte[] response = "{\"status\": \"No Content\"}".getBytes(StandardCharsets.UTF_8);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(204, response.length);
                        exchange.getResponseBody().write(response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                } else if (exchange.getRequestMethod().equals("POST")) {

                    String s = String.valueOf(exchange.getRequestURI());
                    int id = 0;
                    Product productReceived = myMapper.readValue(exchange.getRequestBody(), Product.class);
                    if (productReceived.getPrice() < 0 || productReceived.getAmount() < 0)
                        exchange.sendResponseHeaders(409, 0);

                    Product responseProd = productReceived;
                    try {
                        id = Integer.parseInt(s.substring("/api/good/".length(), s.length()));
                    } catch (Exception ex) {
                        exchange.sendResponseHeaders(404, 0);
                        ex.printStackTrace();
                    }
                    try {
                        responseProd = comm.updateProduct(id, productReceived);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if (responseProd != null) {
                        byte[] response = "{\"status\": \"No Content\"}".getBytes(StandardCharsets.UTF_8);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(204, response.length);
                        exchange.getResponseBody().write(response);
                        exchange.close();
                    } else {
                        exchange.sendResponseHeaders(404, 0);
                    }
                }
                if (exchange.getRequestMethod().equals("PUT")) {

                    Product product = myMapper.readValue(exchange.getRequestBody(), Product.class);
                    if (product != null) {
                        try {
                            if (product.getAmount() <= 0 || product.getPrice() <= 0) {
                                exchange.sendResponseHeaders(409, 0);
                            }
                            Product prod = comm.Insert(product);
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            exchange.sendResponseHeaders(201, prod.getId().toString().getBytes().length);
                            exchange.getResponseBody().write(prod.getId().toString().getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                            exchange.sendResponseHeaders(409, 0);
                        }

                    }
                    exchange.sendResponseHeaders(409, 0);
                } else {
                    exchange.sendResponseHeaders(404, 0);
                }
            }
        });


        context.setAuthenticator(new Authenticator() {
            @Override
            public Result authenticate(HttpExchange exch) {
                String jwt = exch.getRequestHeaders().getFirst("Authorization");
                if (jwt != null) {

                    String login = findUserByJWT(jwt);
                    try {
                        User user = comm.getUserByLogin(login);
                        if (user != null) {
                            return new Success(new HttpPrincipal(login, "user"));
                        }
                        return new Failure(403);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }


                }
                return new Failure(403);
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
    public static String hashMD(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    private static String  findUserByJWT(String jwt) {
        Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }

    }
