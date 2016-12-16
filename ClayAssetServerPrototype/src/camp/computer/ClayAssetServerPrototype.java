package camp.computer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClayAssetServerPrototype {

    public static int PORT = 8001;

    public ClayAssetServerPrototype() {


    }

    public void start() throws IOException {

        // Create HTTP server.
        InetSocketAddress inetSocketAddress = new InetSocketAddress(PORT);
        HttpServer httpServer = HttpServer.create(inetSocketAddress, 0);
        System.out.println("\tServer listening on " + inetSocketAddress.getAddress().getHostAddress() + ":" + PORT + ".");

        // Create HTTP request handlers
        //
        // HTTP REST API:
        //
        // GET /assets/actions
        // GET /assets/action/<uuid>/scripts
        // GET /assets/action/<uuid>/script/<uuid>
        //
        // POST /assets/action
        // POST /assets/action/<uuid>/script

        System.out.print("\tSetting up HTTP request handlers... ");
        httpServer.createContext("/", new RootHandler());
        httpServer.createContext("/echoHeader", new EchoHeaderHandler());
        httpServer.createContext("/echoGet", new EchoGetHandler());
        httpServer.createContext("/echoPost", new EchoPostHandler());

        httpServer.createContext("/jsonGet", new JsonGetHandler());

        // GET
        httpServer.createContext("/assets/actions", new GetActionsHandler());

        // POST
        httpServer.createContext("/assets/process", new PostProcessHandler());
        System.out.println("Success.");

        // Configure how requests are processed in threads.
        // Example 1
        httpServer.setExecutor(null); // JavaDoc: https://docs.oracle.com/javase/8/docs/jre/api/net/httpserver/spec/com/sun/net/httpserver/HttpServer.html#setExecutor-java.util.concurrent.Executor-
        // Example 2: httpServer.setExecutor(Executors.newCachedThreadPool());

        // Start HTTP server.
        httpServer.start();
    }

    public class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "<h1>Server start success if you see this message</h1>" + "<h1>Port: " + PORT + "</h1>";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    public class EchoHeaderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Headers headers = httpExchange.getRequestHeaders();
            Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
            String response = "";
            for (Map.Entry<String, List<String>> entry : entries) {
                response += entry.toString() + "\n";
            }
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public class EchoGetHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange he) throws IOException {
            // parse request
            Map<String, Object> parameters = new HashMap<String, Object>();
            URI requestedUri = he.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);

            // send response
            String response = "";
            for (String key : parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";
            he.sendResponseHeaders(200, response.length());
            OutputStream os = he.getResponseBody();
            os.write(response.toString().getBytes());

            os.close();
        }
    }

    public class EchoPostHandler implements HttpHandler {

        @Override

        public void handle(HttpExchange httpExchange) throws IOException {
            // parse request
            Map<String, Object> parameters = new HashMap<String, Object>();
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);

            // send response
            String response = "";
            for (String key : parameters.keySet()) {
                response += key + " = " + parameters.get(key) + "\n";
            }
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    // Parses data in "key1=value1&key2=value2" format.
    public static void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");
            for (String pair : pairs) {
                String param[] = pair.split("[=]");
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0],
                            System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1],
                            System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);

                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }

    public class JsonGetHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            // parse request
            Map<String, Object> parameters = new HashMap<>();
            URI requestedUri = httpExchange.getRequestURI();
            String query = requestedUri.getRawQuery();
            parseQuery(query, parameters);

            // send response
            String response = "";
            for (String key : parameters.keySet())
                response += key + " = " + parameters.get(key) + "\n";
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());

            os.close();
        }
    }

    public class PostProcessHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            // parse request
            Map<String, Object> parameters = new HashMap<>();
            InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);

            String requestBody = "";
            for (String queryRequestBodyLine = br.readLine(); queryRequestBodyLine != null; queryRequestBodyLine = br.readLine()) {
                System.out.println("JSON POST query: " + queryRequestBodyLine);
                requestBody += queryRequestBodyLine;
            }

            // <JSON_PARSER>
            // Create JSON Parser (with Jackson)
            JsonFactory f = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(f);

            /*
            JsonParser jp = f.createParser(requestBody);
            jp.nextToken();
            // and then each time, advance to opening START_OBJECT
            while (jp.nextToken() == JsonToken.START_OBJECT) {
//                Foo foobar = mapper.readValue(jp, Foo.class);
                String value = jp.getValueAsString();
                System.out.println("value: " + value);
                // process
                // after binding, stream points to closing END_OBJECT
            }
            */

            String type = null;
            List<String> actionTitles = new ArrayList<>();
            List<String> actionScripts = new ArrayList<>();

            JsonNode rootNode = mapper.readTree(requestBody);

            Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
            while (fieldsIterator.hasNext()) {

                Map.Entry<String, JsonNode> field = fieldsIterator.next();

                // Parse Process
                if (field.getKey().equals("actions")) { // Array

                    if (field.getValue().isArray()) {

                        ArrayNode arrayNode = (ArrayNode) field.getValue();

                        Iterator<JsonNode> arrayIterator = arrayNode.elements();
                        while (arrayIterator.hasNext()) {
                            JsonNode arrayElementNode = arrayIterator.next();

                            // Parse Node (e.g., { "title": "set color", "script": "var function = setColor(color) { ... })
                            // TODO: Recursive call to generic JSON parser...
                            if (arrayElementNode.isObject()) {
                                System.out.println("array element is object");
                                JsonNode objectNode = arrayElementNode;
                                Iterator<Map.Entry<String, JsonNode>> objectFieldsIterator = objectNode.fields();

                                String actionTitle = null;
                                String actionScript = null;

                                while (objectFieldsIterator.hasNext()) {
                                    Map.Entry<String, JsonNode> objectField = objectFieldsIterator.next();
                                    System.out.println("\tKey: " + objectField.getKey() + "\tValue:" + objectField.getValue());

                                    if (objectField.getKey().equals("title")) {
                                        actionTitle = objectField.getValue().toString();
                                    } else if (objectField.getKey().equals("script")) {
                                        actionScript = objectField.getValue().toString();
                                    }
                                }

                                actionTitles.add(actionTitle);
                                actionScripts.add(actionScript);
                            }
                        }

//                        Iterator<Map.Entry<String, JsonNode>> objectFieldsIterator = arrayNode.fields();
//
//                        for (final JsonNode objNode : arrayNode) {
//                            System.out.println(objNode);
//                        }
//
//                        String actionTitle = null;
//                        String actionScript = null;
//
//                        while (objectFieldsIterator.hasNext()) {
//                            Map.Entry<String, JsonNode> objectField = objectFieldsIterator.next();
//                            System.out.println("\tKey: " + objectField.getKey() + "\tValue:" + objectField.getValue());
//
//                            if (objectField.getKey().equals("title")) {
//                                actionTitle = objectField.getValue().toString();
//                            } else if (objectField.getKey().equals("script")) {
//                                actionScript = objectField.getValue().toString();
//                            }
//                        }
//
//                        actionScripts.put(actionTitle, actionScript);

                    }

                } else if (field.getKey().equals("position")) { // Object

                    if (field.getValue().isObject()) {
                        JsonNode objectNode = field.getValue();
                        Iterator<Map.Entry<String, JsonNode>> objectFieldsIterator = objectNode.fields();
                        while (objectFieldsIterator.hasNext()) {
                            Map.Entry<String, JsonNode> field2 = objectFieldsIterator.next();
                            System.out.println("\tKey: " + field2.getKey() + "\tValue:" + field2.getValue());
                        }
                    }

                } else if (field.getKey().equals("type")) { // Value

                    type = field.getValue().toString();

                } else {
                    System.out.println("Key: " + field.getKey() + "\tValue:" + field.getValue());
                }
            }

            System.out.println("---");
            System.out.println("Received Process:");
            System.out.println("type: " + type);
            for (int i = 0; i < actionTitles.size(); i++) {
                System.out.println("\ttitle: " + actionTitles.get(i));
                System.out.println("\tscript: " + actionScripts.get(i));
            }
            System.out.println("---");
            // </JSON_PARSER>

            // <EVALUATE_PROCESS_ACTION_JAVASCRIPTS>
            for (int i = 0; i < actionScripts.size(); i++) {
                String script = actionScripts.get(i);
                Application.javaScriptEngine.eval(script);
            }
            // </EVALUATE_PROCESS_ACTION_JAVASCRIPTS>

            // send response
            String response = "";
            for (String key : parameters.keySet()) {
                response += key + " = " + parameters.get(key) + "\n";
            }
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.toString().getBytes());
            os.close();
        }
    }

    public static String parseValue(String jsonString, String key) {

        // Create JSON Parser (with Jackson)
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);

        String value = null;

        JsonNode rootNode = null;
        try {
            rootNode = objectMapper.readTree(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<Map.Entry<String, JsonNode>> fieldsIterator = rootNode.fields();
        while (fieldsIterator.hasNext()) {
            Map.Entry<String, JsonNode> field = fieldsIterator.next();

            // Parse Field
            if (field.getKey().equals(key)) { // Value
                value = field.getValue().toString();
            }
        }

        return value;
    }


    // Returns JSON
    public class GetActionsHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            // parse request
//            Map<String, Object> parameters = new HashMap<>();
//            URI requestedUri = httpExchange.getRequestURI();
//            String query = requestedUri.getRawQuery();
//            parseQuery(query, parameters);

            Map<String, String> virtualRedisStore = new HashMap<>();
            virtualRedisStore.put("get ports", "var action = function(data) { clay.getPorts(); return data; }");
            virtualRedisStore.put("set brightness", "var action = function(data) { print('setBrightness'); print(data); return data; }");
            virtualRedisStore.put("set color", "var action = function(data) { print('setColor');  print(data); return data; }");
            virtualRedisStore.put("set rotation direction", "var action = function(data) { print('setRotationDirection'); print(data); return data; }");
            virtualRedisStore.put("set rotation speed", "var action = function(data) { print('setRotationSpeed'); print(data); return data; }");
            virtualRedisStore.put("send message", "var action = function(data) { print('sendMessage'); print(data); return data; }");
            virtualRedisStore.put("get sensor data", "var action = function(data) { print('getSernsorData'); print(data); return data; }");
//            virtualRedisStore.put("get ports", "var action = function(data) {\n\tclay.getPorts();\n\treturn data;\n}");
//            virtualRedisStore.put("set brightness", "var action = function(data) {\n\tprint('setBrightness');\n\tprint(data);\n\treturn data;\n}");
//            virtualRedisStore.put("set color", "var action = function(data) {\n\tprint('setColor');\n\tprint(data);\n\treturn data;\n}");
//            virtualRedisStore.put("set rotation direction", "var action = function(data) {\n\tprint('setRotationDirection');\n\tprint(data);\n\treturn data;\n}");
//            virtualRedisStore.put("set rotation speed", "var action = function(data) {\n\tprint('setRotationSpeed');\n\tprint(data);\n\treturn data;\n}");
//            virtualRedisStore.put("send message", "var action = function(data) {\n\tprint('sendMessage');\n\tprint(data);\n\treturn data;\n}");
//            virtualRedisStore.put("get sensor data", "var action = function(data) {\n\tprint('getSernsorData');\n\tprint(data);\n\treturn data;\n}");

            // Set content type
            httpExchange.getResponseHeaders().set("Content-Type", "application/json");

            // Configure HTTP response
            String httpResponseBody = "{ \"actions\": [";
            int i = 0;
            for (String key : virtualRedisStore.keySet()) {
                httpResponseBody += "{ "
                        + "\"type\": \"action\", "
                        + "\"title\": \"" + key + "\","
                        + "\"script\": \"" + virtualRedisStore.get(key) + "\""
                        + "}";

                if (i < (virtualRedisStore.size() - 1)) {
                    httpResponseBody += ", ";
                }
                i++;
            }
            httpResponseBody += "] }";
            System.out.println("Response: " + httpResponseBody);

            httpExchange.sendResponseHeaders(200, httpResponseBody.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(httpResponseBody.toString().getBytes());

            outputStream.close();
        }
    }
}
