package camp.computer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import camp.computer.model.Device;
import camp.computer.model.Interface;
import camp.computer.model.Port;
import camp.computer.model.Project;
import camp.computer.model.Script;
import camp.computer.model.Task;
import camp.computer.util.CouchDB;
import camp.computer.util.List;

public class Application {

    public static String ADMIN_USERNAME = "admin";
    public static String ADMIN_PASSWORD = "password";

    public String username = null;
    public String password = null;

    public Application() {

    }

    public void start() {

        // Authenticate
        System.out.println("1. new user");
        System.out.println("2. existing user");
        System.out.println("3. exit");
        System.out.print("~ ");

        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();

        if (name.equals("1")) {
            System.out.print("username: ");
            String username = scanner.next();
            System.out.print("password: ");
            String password = scanner.next();
            createUser(username, password);
        } else if (name.equals("2")) {
            System.out.print("username: ");
            String username = scanner.next();
            System.out.print("password: ");
            String password = scanner.next();
            boolean isAuthenticated = authenticateUser(username, password);
            if (isAuthenticated) {
                // Cache the user's username and password for later requests
                this.username = username;
                this.password = password;
                run();
            }
        } else if (name.equals("3")) {
            System.exit(0);
        }

    }

    private void run() {

        Scanner scanner = new Scanner(System.in);
        String inputLine = null;

        while (true) {
            System.out.print("~ ");
            inputLine = scanner.nextLine();
            interpretLine(inputLine);
        }

    }

    public void interpretLine(String inputLine) {

        // TODO: create user (if not exists)
        // TODO: create user database (if not exists)
        // TODO: add user as member of their database

        if (inputLine.matches("^(list)($|[ ]+.*)")) {
            // list [port|device|project|...]
            String[] inputTokens = inputLine.split("[ ]+");
            if (inputTokens.length == 1) {
                listDocuments(null);
            } else if (inputTokens.length == 2) {
                listDocuments(inputTokens[1]);
            }
        } else if (inputLine.matches("^(save)($|[ ]+.*)")) {
            // save [port|device|project|...]
            System.out.println("saving...");
            String[] inputTokens = inputLine.split("[ ]+");
            if (inputTokens.length == 1) {
                // TODO: Ask user what to save. Then start interactive construction of object.
            } else if (inputTokens.length == 2) {
                saveDocument(inputTokens[1]);
            }
        } else if (inputLine.matches("^(find)($|[ ]+.*)")) {
            String[] inputTokens = inputLine.split("[ ]+");
            if (inputTokens.length == 5 && inputTokens[1].equals("port")) {
                findPortDocument(inputTokens[2], inputTokens[3], inputTokens[4]);
            } else if (inputTokens[1].equals("device")) {
//                findDeviceDocument(inputTokens[2], inputTokens[3], inputTokens[4]);
                findDeviceDocument("", "", "");
            } else {
                System.out.println("Error: Invalid number of arguments.");
            }
        } else if (inputLine.matches("^(load)($|[ ]+.*)")) {
            System.out.println("loading...");
        } else if (inputLine.matches("^(exit)($|[ ]+.*)")) {
            System.out.println("exiting...");
            System.exit(0);
        }

    }

    public void listDocuments(String type) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            URL url = null;
            url = new URL("http://localhost:5984/" + username + "/_find?include_docs=true");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("{ \"selector\": { \"type\": { \"$eq\": \"" + type + "\" } } }");
            out.close();

            // Read PUT response (from CouchDB server)
            int httpResponseCode = httpConnection.getResponseCode();
            System.out.println(httpResponseCode);
            if (httpResponseCode == 200) {

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

//                //print result
//                boolean prettyPrint = true;
//                if (!prettyPrint) {
//                    System.out.println(response.toString());
//                } else {
//                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
//                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
//                    System.out.println(perttyStr);
//                }

                //read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(response.toString());
                //            JsonNode idNode = rootNode.path("id");
                //            System.out.println("id = "+idNode.asInt());
//                            JsonNode totalRows = rootNode.path("total_rows");
//                            System.out.println("id = " + totalRows.asInt());

                System.out.println("-----------------------------------------------------------");

                JsonNode rowsNode = rootNode.path("docs");
                for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                    JsonNode docNode = it.next();

                    //                JsonNode idNode = node.path("id");
                    //                JsonNode revNode = node.path("rev");
                    //                System.out.println("id = " + idNode);
                    //                System.out.println("rev = " + revNode);

//                    JsonNode docNode = node.path("doc");
                    JsonNode docIdNode = docNode.path("_id");
                    JsonNode docType = docNode.path("type");

                    if (type != null && docType.asText().equals(type)) {
//                        System.out.println("doc  = " + docNode);
//                        System.out.println("_id  = " + docIdNode);
//                        System.out.println("type = " + docType);

                        Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                        String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                        System.out.println(perttyStr);
                        System.out.println();
                    }

                }


            } else if (httpResponseCode == 403) {
                System.out.println("Forbidden.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public void listDocuments2(String type) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            URL url = null;
            url = new URL("http://localhost:5984/" + username + "/_all_docs?include_docs=true");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(false);
            httpConnection.setRequestMethod("GET");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);

            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 200) {

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                /*
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                }
                */

                //read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(response.toString());
    //            JsonNode idNode = rootNode.path("id");
    //            System.out.println("id = "+idNode.asInt());
    //            JsonNode totalRows = rootNode.path("total_rows");
    //            System.out.println("id = " + totalRows.asInt());

                System.out.println("-----------------------------------------------------------");

                JsonNode rowsNode = rootNode.path("rows");
                for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                    JsonNode node = it.next();

    //                JsonNode idNode = node.path("id");
    //                JsonNode revNode = node.path("rev");
    //                System.out.println("id = " + idNode);
    //                System.out.println("rev = " + revNode);

                    JsonNode docNode = node.path("doc");
                    JsonNode docIdNode = docNode.path("_id");
                    JsonNode docType = docNode.path("type");

                    if (type != null && docType.asText().equals(type)) {
                        System.out.println("doc  = " + docNode);
                        System.out.println("_id  = " + docIdNode);
                        System.out.println("type = " + docType);

                        Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                        String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                        System.out.println(perttyStr);
                        System.out.println();
                    }

                }
            } else if (httpResponseCode == 403) {
                System.out.println("Forbidden.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public void findPortDocument(String mode, String direction, String voltage) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            URL url = null;
            url = new URL("http://localhost:5984/" + username + "/_find?include_docs=true");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("{ \"selector\": { \"type\": { \"$eq\": \"port\" }, \"modes\": \"" + mode + "\", \"directions\": \"" + direction + "\", \"voltages\": \"" + voltage + "\" } }");
            out.close();

            // Read PUT response (from CouchDB server)
            int httpResponseCode = httpConnection.getResponseCode();
            System.out.println(httpResponseCode);
            if (httpResponseCode == 200) {

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

//                //print result
//                boolean prettyPrint = true;
//                if (!prettyPrint) {
//                    System.out.println(response.toString());
//                } else {
//                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
//                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
//                    System.out.println(perttyStr);
//                }

                //read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(response.toString());
                //            JsonNode idNode = rootNode.path("id");
                //            System.out.println("id = "+idNode.asInt());
//                            JsonNode totalRows = rootNode.path("total_rows");
//                            System.out.println("id = " + totalRows.asInt());

                System.out.println("-----------------------------------------------------------");

                JsonNode rowsNode = rootNode.path("docs");
                for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                    JsonNode docNode = it.next();

                    //                JsonNode idNode = node.path("id");
                    //                JsonNode revNode = node.path("rev");
                    //                System.out.println("id = " + idNode);
                    //                System.out.println("rev = " + revNode);

//                    JsonNode docNode = node.path("doc");
                    JsonNode docIdNode = docNode.path("_id");
                    JsonNode docType = docNode.path("type");

                    String type = "port";
                    if (type != null && docType.asText().equals(type)) {
//                        System.out.println("doc  = " + docNode);
//                        System.out.println("_id  = " + docIdNode);
//                        System.out.println("type = " + docType);

                        Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                        String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                        System.out.println(perttyStr);
                        System.out.println();
                    }

                }
            } else if (httpResponseCode == 403) {
                System.out.println("Forbidden.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public void findDeviceDocument(String mode, String direction, String voltage) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            URL url = null;
            url = new URL("http://localhost:5984/" + username + "/_find?include_docs=true");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
//            out.write("{ \"selector\": { \"type\": { \"$eq\": \"device\" }, \"modes\": \"" + modes + "\", \"directions\": \"" + directions + "\", \"voltages\": \"" + voltages + "\" } }");
//            out.write("{ \"selector\": { \"type\": { \"$eq\": \"device\" }, \"ports[0].modes\": \"spi(mosi)\" } }");
//            out.write("{ \"selector\": { \"type\": { \"$eq\": \"device\" }, \"ports\": { \"$elemMatch\": { \"modes\": \"spi(mosi)\" } } } }");
            out.write("{ \"selector\": { \"type\": { \"$eq\": \"device\" }, \"$and\": [ { \"ports\": { \"$elemMatch\": { \"modes\": \"spi(mosi)\" } } }, { \"ports\": { \"$elemMatch\": { \"modes\": \"uart(rx)\" } } }, { \"ports\": { \"$elemMatch\": { \"modes\": \"power\" } } } ] } }");
            out.close();

            // Read PUT response (from CouchDB server)
            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 200) {

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

//                //print result
//                boolean prettyPrint = true;
//                if (!prettyPrint) {
//                    System.out.println(response.toString());
//                } else {
//                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
//                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
//                    System.out.println(perttyStr);
//                }

                //read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(response.toString());
                //            JsonNode idNode = rootNode.path("id");
                //            System.out.println("id = "+idNode.asInt());
//                            JsonNode totalRows = rootNode.path("total_rows");
//                            System.out.println("id = " + totalRows.asInt());

                JsonNode rowsNode = rootNode.path("docs");
                for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                    JsonNode docNode = it.next();

//                    JsonNode docIdNode = docNode.path("_id");
                    JsonNode docType = docNode.path("type");

                    String type = "device";
                    if (type != null && docType.asText().equals(type)) {
//                        System.out.println("doc  = " + docNode);
//                        System.out.println("_id  = " + docIdNode);
//                        System.out.println("type = " + docType);

                        Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                        String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                        System.out.println(perttyStr);
                        System.out.println();
                    }

                }
            } else if (httpResponseCode == 403) {
                System.out.println("Forbidden.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public void saveDocument(String type) {
        if (type.equals("port")) {
            String mode = selectRandomElement("digital", "analog", "pulse-width-modulation", "resistive-touch", "power", "i2c(scl)", "i2c(sda)", "spi(mosi)", "spi(miso)", "spi(ss)", "uart(rx)", "uart(tx)");
            String direction = selectRandomElement("input", "output", "bidirectional");
            String voltage = selectRandomElement("0v", "3.3v", "5v");
            savePortDocument(mode, direction, voltage);
        } else if (type.equals("device")) {
            saveDeviceDocument("component name here", "component description here");
        } else if (type.equals("project")) {
            saveProjectDocument("My Project Name", "My project description here!");
        }
    }

    public void savePortDocument(String mode, String direction, String voltage) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;
            // Configure PUT request
            String documentUuid = CouchDB.generateUuid();
            URL serverUri = new URL("http://localhost:5984/" + username + "/" + documentUuid);
            httpConnection = (HttpURLConnection) serverUri.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Create JSON structure
            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
            ObjectNode rootNode = jsonNodeFactory.objectNode();
            rootNode.put("type", "port");
            rootNode.put("modes", mode);
            rootNode.put("directions", direction);
            rootNode.put("voltages", voltage);

            String jsonString = (new ObjectMapper()).writeValueAsString(rootNode);
            // System.out.println(jsonString);

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            // out.write("{ \"type\": \"port\", \"modes\": \"" + modes + "\", \"directions\": \"" + directions + "\", \"voltages\": \"" + voltages + "\" }");
            out.write(jsonString);
            out.close();

            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 201) {
                System.out.println("Created.");

                System.out.println(httpResponseCode);

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(prettyString);
                }

            } else if (httpResponseCode == 401) {
                System.out.println("Unauthorized.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public void saveDeviceDocument(String name, String description) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            // Configure PUT request
            String documentUuid = CouchDB.generateUuid();
            URL serverUri = new URL("http://localhost:5984/" + username + "/" + documentUuid);
            httpConnection = (HttpURLConnection) serverUri.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Create JSON structure
            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
            ObjectNode rootNode = jsonNodeFactory.objectNode();
            rootNode.put("type", "device");
            rootNode.put("name", name);
            rootNode.put("description", description);
            // Add ports to array:
            ArrayNode portList = jsonNodeFactory.arrayNode();
            // Port 1:
            for (int i = 0; i < 3; i++) {
                // Specify the port configuration
                String mode = selectRandomElement("digital", "analog", "pulse-width-modulation", "resistive-touch", "power", "i2c(scl)", "i2c(sda)", "spi(mosi)", "spi(miso)", "spi(ss)", "uart(rx)", "uart(tx)");
                String direction = selectRandomElement("input", "output", "bidirectional");
                String voltage = selectRandomElement("0v", "3.3v", "5v");

                // Create JSON object for port
                ObjectNode portRootNode = jsonNodeFactory.objectNode();
                portRootNode.put("type", "port");
                portRootNode.put("modes", mode);
                portRootNode.put("directions", direction);
                portRootNode.put("voltages", voltage);
                portList.add(portRootNode);
            }
            rootNode.set("ports", portList);

            String jsonString = (new ObjectMapper()).writeValueAsString(rootNode);
            // System.out.println(jsonString);

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            // out.write("{ \"type\": \"port\", \"modes\": \"" + modes + "\", \"directions\": \"" + directions + "\", \"voltages\": \"" + voltages + "\" }");
            out.write(jsonString);
            out.close();

            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 201) {
                System.out.println("Created.");

                System.out.println(httpResponseCode);

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(prettyString);
                }

            } else if (httpResponseCode == 401) {
                System.out.println("Unauthorized.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public void saveProjectDocument(String name, String description) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            // Configure PUT request
            String documentUuid = CouchDB.generateUuid();
            URL serverUri = new URL("http://localhost:5984/" + username + "/" + documentUuid);
            httpConnection = (HttpURLConnection) serverUri.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

//            // Create JSON structure
//            JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
//            ObjectNode projectRootNode = jsonNodeFactory.objectNode();
//            projectRootNode.put("type", "project");
//            projectRootNode.put("name", name);
//            projectRootNode.put("description", description);
//
//            // Add devices to array:
//            ArrayNode deviceList = jsonNodeFactory.arrayNode();
//            int deviceCount = 3;
//            for (int deviceIndex = 0; deviceIndex < deviceCount; deviceIndex++) {
//                ObjectNode deviceRootNode = jsonNodeFactory.objectNode();
//                deviceRootNode.put("type", "device");
//                deviceRootNode.put("name", name);
//                deviceRootNode.put("description", description);
//                // Add ports to array:
//                ArrayNode portList = jsonNodeFactory.arrayNode();
//                for (int i = 0; i < 3; i++) {
//                    // Specify the port configuration
//                    String mode = selectRandomElement("digital", "analog", "pulse-width-modulation", "resistive-touch", "power", "i2c(scl)", "i2c(sda)", "spi(mosi)", "spi(miso)", "spi(ss)", "uart(rx)", "uart(tx)");
//                    String direction = selectRandomElement("input", "output", "bidirectional");
//                    String voltage = selectRandomElement("0v", "3.3v", "5v");
//
//                    // Create JSON object for port
//                    ObjectNode portRootNode = jsonNodeFactory.objectNode();
//                    portRootNode.put("type", "port");
//                    portRootNode.put("modes", mode);
//                    portRootNode.put("directions", direction);
//                    portRootNode.put("voltages", voltage);
//                    portList.add(portRootNode);
//                }
//                deviceRootNode.set("ports", portList);
//                deviceList.add(deviceRootNode);
//            }
//            projectRootNode.set("devices", deviceList);
//
//            // Add interfaces to array:
//            ArrayNode interfaceList = jsonNodeFactory.arrayNode();
//            int interfaceCount = 2;
//            for (int interfaceIndex = 0; interfaceIndex < interfaceCount; interfaceIndex++) {
//                ObjectNode interfaceRootNode = jsonNodeFactory.objectNode();
//                interfaceRootNode.put("type", "interface");
//                // Add channels to array:
//                ArrayNode channelList = jsonNodeFactory.arrayNode();
//                int channelCount = 3;
//                for (int i = 0; i < channelCount; i++) {
//                    // Create JSON object for channel
//                    ObjectNode channelRootNode = jsonNodeFactory.objectNode();
//                    channelRootNode.put("type", "channel");
//                    // TODO: source and target device/port
//                    channelList.add(channelRootNode);
//                }
//                interfaceRootNode.set("channels", channelList);
//
//                // Add control to array:
//                ObjectNode controlRootNode = jsonNodeFactory.objectNode();
//                ArrayNode taskList = jsonNodeFactory.arrayNode();
//                int taskCount = 3;
//                for (int i = 0; i < taskCount; i++) {
//                    // Create JSON object for task
//                    ObjectNode taskRootNode = jsonNodeFactory.objectNode();
//                    taskRootNode.put("type", "task");
//                    // Create JSON object for script
//                    ObjectNode scriptRootNode = jsonNodeFactory.objectNode();
//                    scriptRootNode.put("type", "script");
//                    scriptRootNode.put("code", "javascript:task(data) { /* function body */ }");
//                    taskRootNode.set("script", scriptRootNode);
//                    taskList.add(taskRootNode);
//                }
//                controlRootNode.set("tasks", taskList);
//                interfaceRootNode.set("control", controlRootNode);
//
//                interfaceList.add(interfaceRootNode);
//            }
//            projectRootNode.set("interfaces", interfaceList);

            Port port = null;

            Project project = new Project();
            project.id = CouchDB.generateUuid();
//            Interface iface = new Interface();
//            iface.controller = new Controller();
            Task task = new Task();
            task.script = new Script();
//            iface.controller.tasks.add(task);
//            project.interfaces.add(iface);
            Device irRangefinderDevice = Device.create();
            port = Port.create(new List("power"), new List("input"), new List("cmos"));
            port.set("power", "input", "cmos");
            irRangefinderDevice.ports.add(port);
            port = Port.create(new List("digital", "analog"), new List("input", "output"), new List("cmos", "ttl"));
            port.set("digital", "output", "cmos");
            irRangefinderDevice.ports.add(port);
            port = Port.create(new List("digital", "analog"), new List("input", "output"), new List("cmos", "ttl"));
            port.set("analog", "input", "cmos");
            irRangefinderDevice.ports.add(port);
            Device servoDevice = Device.create();
            servoDevice.ports.add(Port.create());
            servoDevice.ports.add(Port.create());
            servoDevice.ports.add(Port.create());
//            project.devices.add(device1);

            Interface iface = project.addInterface(irRangefinderDevice, servoDevice);
            iface.addChannel(irRangefinderDevice.ports.get(0), servoDevice.ports.get(1));
            iface.addChannel(irRangefinderDevice.ports.get(1), servoDevice.ports.get(2));
            iface.addChannel(irRangefinderDevice.ports.get(2), servoDevice.ports.get(0));
//            project.addInterface(servoDevice, irRangefinderDevice);
//            iface.addChannel(irRangefinderDevice.ports.get(0), servoDevice.ports.get(1));
//            iface.addChannel(irRangefinderDevice.ports.get(1), servoDevice.ports.get(2));
//            iface.addChannel(irRangefinderDevice.ports.get(2), servoDevice.ports.get(0));
//            System.out.println("interface #: " + project.interfaces.size());
//            System.out.println("channel #: " + iface.channels.size());

//            ObjectMapper mapper = new ObjectMapper();
            //By default all fields without explicit view definition are included, disable this
//            mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
//            mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
//            String json = mapper
//                    .writerWithView(Views.StateOnly.class)
//                    .writeValueAsString(project);

//            String json = new ObjectMapper().writeValueAsString(device);
            String jsonString = new ObjectMapper().writeValueAsString(project);
            Object jsonObject = new ObjectMapper().readValue(jsonString, Object.class);
            String prettyString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            System.out.println(prettyString);
//            System.out.println(json);

//            String jsonString = (new ObjectMapper()).writeValueAsString(projectRootNode);
            // System.out.println(jsonString);

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            // out.write("{ \"type\": \"port\", \"modes\": \"" + modes + "\", \"directions\": \"" + directions + "\", \"voltages\": \"" + voltages + "\" }");
            out.write(jsonString);
            out.close();

            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 201) {
                System.out.println("Created.");

                System.out.println(httpResponseCode);

                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    prettyString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(prettyString);
                }

            } else if (httpResponseCode == 401) {
                System.out.println("Unauthorized.");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }


    public void createUser(String username, String password) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((ADMIN_USERNAME + ":" + ADMIN_PASSWORD).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            // Configure PUT request
            URL url = null;
            url = new URL("http://localhost:5984/_users/org.couchdb.user:" + username);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("{ \"name\": \"" + username + "\", \"password\": \"" + password + "\", \"roles\": [], \"type\": \"user\" }");
            out.close();

            // Read PUT response (from CouchDB server)
            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 201) {
                System.out.println("Created.");

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
    //            System.out.println(httpCon.getResponseCode());
    //            System.out.println(httpCon.getResponseMessage());
    //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                }
            } else if (httpResponseCode == 401) {
                System.out.println("Unauthorized.");
            }

            /*
            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(response.toString());
//            JsonNode idNode = rootNode.path("id");
//            System.out.println("id = "+idNode.asInt());
//            JsonNode totalRows = rootNode.path("total_rows");
//            System.out.println("id = " + totalRows.asInt());

            System.out.println("-----------------------------------------------------------");

            JsonNode rowsNode = rootNode.path("rows");
            for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                JsonNode node = it.next();

//                JsonNode idNode = node.path("id");
//                JsonNode revNode = node.path("rev");
//                System.out.println("id = " + idNode);
//                System.out.println("rev = " + revNode);

                JsonNode docNode = node.path("doc");
                JsonNode docIdNode = docNode.path("_id");
                JsonNode docType = docNode.path("type");

                if (type != null && docType.asText().equals(type)) {
                    System.out.println("doc  = " + docNode);
                    System.out.println("_id  = " + docIdNode);
                    System.out.println("type = " + docType);

                    Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                    System.out.println();
                }

            }
            */

            /* ---------------------------------------------------------------------------------- */

            // Configure PUT request
//            URL url = null;
            url = new URL("http://localhost:5984/" + username);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
//            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("{ \"name\": \"" + username + "\", \"password\": \"" + password + "\", \"roles\": [], \"type\": \"user\" }");
            out.close();

            // Read PUT response (from CouchDB server)
//            int httpResponseCode = httpConnection.getResponseCode();
            httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 201) {
                System.out.println("Created.");

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                }
            } else if (httpResponseCode == 401) {
                System.out.println("Unauthorized.");
            }

            /*
            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(response.toString());
//            JsonNode idNode = rootNode.path("id");
//            System.out.println("id = "+idNode.asInt());
//            JsonNode totalRows = rootNode.path("total_rows");
//            System.out.println("id = " + totalRows.asInt());

            System.out.println("-----------------------------------------------------------");

            JsonNode rowsNode = rootNode.path("rows");
            for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                JsonNode node = it.next();

//                JsonNode idNode = node.path("id");
//                JsonNode revNode = node.path("rev");
//                System.out.println("id = " + idNode);
//                System.out.println("rev = " + revNode);

                JsonNode docNode = node.path("doc");
                JsonNode docIdNode = docNode.path("_id");
                JsonNode docType = docNode.path("type");

                if (type != null && docType.asText().equals(type)) {
                    System.out.println("doc  = " + docNode);
                    System.out.println("_id  = " + docIdNode);
                    System.out.println("type = " + docType);

                    Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                    System.out.println();
                }

            }
            */

            /* ---------------------------------------------------------------------------------- */

            System.out.println("Setting up security... ");

            // Configure PUT request
//            URL url = null;
            url = new URL("http://localhost:5984/" + username + "/_security");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("{ \"admins\": { \"names\": [], \"roles\": [] }, \"members\": { \"names\": [ \"" + username + "\" ], \"roles\": [] } }");
            out.close();

            // Read PUT response (from CouchDB server)
            httpResponseCode = httpConnection.getResponseCode();

            System.out.println("response:");
            System.out.println(httpResponseCode);

            if (httpResponseCode == 200) {
                System.out.println("OK.");

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                }
            } else if (httpResponseCode == 400) {
                System.out.println("Bad Request.");
            }

            /*
            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(response.toString());
//            JsonNode idNode = rootNode.path("id");
//            System.out.println("id = "+idNode.asInt());
//            JsonNode totalRows = rootNode.path("total_rows");
//            System.out.println("id = " + totalRows.asInt());

            System.out.println("-----------------------------------------------------------");

            JsonNode rowsNode = rootNode.path("rows");
            for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                JsonNode node = it.next();

//                JsonNode idNode = node.path("id");
//                JsonNode revNode = node.path("rev");
//                System.out.println("id = " + idNode);
//                System.out.println("rev = " + revNode);

                JsonNode docNode = node.path("doc");
                JsonNode docIdNode = docNode.path("_id");
                JsonNode docType = docNode.path("type");

                if (type != null && docType.asText().equals(type)) {
                    System.out.println("doc  = " + docNode);
                    System.out.println("_id  = " + docIdNode);
                    System.out.println("type = " + docType);

                    Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                    System.out.println();
                }

            }
            */

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
    }

    public boolean authenticateUser(String username, String password) {

        HttpURLConnection httpConnection = null;

        try {

            // Configure PUT request
            URL url = null;
            url = new URL("http://localhost:5984/_session");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
//            httpConnection.setRequestProperty("Accept", "application/json");
//            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("name=" + username + "&password=" + password);
            out.close();

            // Read PUT response (from CouchDB server)
            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 200) {
                // Should respond with a string like the following:
                // {"ok":true,"name":"mokogobo","roles":[]}
                System.out.println("Created.");

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                //            System.out.println(httpCon.getResponseCode());
                //            System.out.println(httpCon.getResponseMessage());
                //            out.close();

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();



                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //print result
                boolean prettyPrint = true;
                if (!prettyPrint) {
                    System.out.println(response.toString());
                } else {
                    Object jsonObject = objectMapper.readValue(response.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                }

                return true;

            } else if (httpResponseCode == 401) {
                System.out.println("Unauthorized.");
            }

            /*
            //read JSON like DOM Parser
            JsonNode rootNode = objectMapper.readTree(response.toString());
//            JsonNode idNode = rootNode.path("id");
//            System.out.println("id = "+idNode.asInt());
//            JsonNode totalRows = rootNode.path("total_rows");
//            System.out.println("id = " + totalRows.asInt());

            System.out.println("-----------------------------------------------------------");

            JsonNode rowsNode = rootNode.path("rows");
            for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                JsonNode node = it.next();

//                JsonNode idNode = node.path("id");
//                JsonNode revNode = node.path("rev");
//                System.out.println("id = " + idNode);
//                System.out.println("rev = " + revNode);

                JsonNode docNode = node.path("doc");
                JsonNode docIdNode = docNode.path("_id");
                JsonNode docType = docNode.path("type");

                if (type != null && docType.asText().equals(type)) {
                    System.out.println("doc  = " + docNode);
                    System.out.println("_id  = " + docIdNode);
                    System.out.println("type = " + docType);

                    Object jsonObject = objectMapper.readValue(docNode.toString(), Object.class);
                    String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                    System.out.println(perttyStr);
                    System.out.println();
                }

            }
            */

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }

        return false;
    }

    public static String selectRandomElement(String... elements) {
        Random random = new Random();
        int randomIndex = random.nextInt(elements.length);
        return elements[randomIndex];
    }
}
