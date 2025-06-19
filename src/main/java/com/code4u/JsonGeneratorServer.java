package com.code4u;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.json.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;

public class JsonGeneratorServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/generate-json", new JsonGeneratorHandler());
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }

    static class JsonGeneratorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes());
            System.out.println("Received payload: " + body);

            try {
                JSONObject input = new JSONObject(body);
                String filename = input.getString("filename");
                int count = input.getInt("count");
                boolean randomize = input.optBoolean("randomize", false);

                JSONObject base;
                Object baseObj = input.get("baseJson");
                if (baseObj instanceof JSONObject) {
                    base = (JSONObject) baseObj;
                } else if (baseObj instanceof String) {
                    base = new JSONObject((String) baseObj);
                } else {
                    throw new JSONException("Invalid baseJson format");
                }

                JSONArray output = new JSONArray();
                for (int i = 0; i < count; i++) {
                    JSONObject copy = new JSONObject(base.toString());
                    if (randomize) {
                        applyRandomization(copy);
                    } else {
                        assignIds(copy, i);
                    }
                    output.put(copy);
                }

                byte[] fileBytes = output.toString(2).getBytes();
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.getResponseHeaders().add("Content-Disposition", "attachment; filename=" + filename);
                exchange.sendResponseHeaders(200, fileBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(fileBytes);
                }

                System.out.println("Generated and sent: " + filename);
            } catch (Exception e) {
                e.printStackTrace();
                exchange.sendResponseHeaders(400, -1);
            }
        }

        private void assignIds(JSONObject obj, int id) {
            for (String key : obj.keySet()) {
                Object value = obj.get(key);
                if (value instanceof JSONObject) {
                    assignIds((JSONObject) value, id);
                } else if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    for (int i = 0; i < array.length(); i++) {
                        Object element = array.get(i);
                        if (element instanceof JSONObject) {
                            assignIds((JSONObject) element, id + i);
                        }
                    }
                } else if (key.equalsIgnoreCase("id")) {
                    obj.put(key, id);
                }
            }
        }

        private void applyRandomization(JSONObject obj) {
            for (String key : obj.keySet()) {
                Object value = obj.get(key);
                if (value instanceof JSONObject) {
                    applyRandomization((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    JSONArray array = (JSONArray) value;
                    for (int i = 0; i < array.length(); i++) {
                        Object element = array.get(i);
                        if (element instanceof JSONObject) {
                            applyRandomization((JSONObject) element);
                        } else {
                            array.put(i, generateRandomValue(element));
                        }
                    }
                } else {
                    obj.put(key, generateRandomValue(value));
                }
            }
        }

        private Object generateRandomValue(Object value) {
            if (value instanceof Boolean) {
                return Math.random() < 0.5;
            } else if (value instanceof Integer) {
                return (int) (Math.random() * 100000);
            } else if (value instanceof Long) {
                return (long) (Math.random() * 100000000L);
            } else if (value instanceof Double || value instanceof Float) {
                return Math.round(Math.random() * 100000.0 * 100.0) / 100.0;
            } else if (value instanceof String) {
                return UUID.randomUUID().toString().substring(0, 8);
            }
            return value;
        }
    }


    static class StaticFileHandler implements HttpHandler {
        private static final Map<String, String> contentTypes = new HashMap<>();

        static {
            contentTypes.put("html", "text/html");
            contentTypes.put("js", "application/javascript");
            contentTypes.put("css", "text/css");
            contentTypes.put("json", "application/json");
            contentTypes.put("ico", "image/x-icon");
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

            File file = new File("frontend" + path);
            if (!file.exists()) {
                System.out.println("Static file not found: " + path);
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            String ext = path.substring(path.lastIndexOf(".") + 1);
            String contentType = contentTypes.getOrDefault(ext, "application/octet-stream");

            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, bytes.length);
            exchange.getResponseBody().write(bytes);
            exchange.getResponseBody().close();
        }
    }
}
