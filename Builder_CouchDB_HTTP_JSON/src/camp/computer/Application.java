package camp.computer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;
import java.util.Scanner;
import java.util.UUID;

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

    public void saveDocument(String type) {
        if (type.equals("port")) {
            savePortDocument("digital", "output", "cmos");
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
            String documentUuid = UUID.randomUUID().toString().replace("-", "");
            URL serverUri = new URL("http://localhost:5984/" + username + "/" + documentUuid);
            httpConnection = (HttpURLConnection) serverUri.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("PUT");
            httpConnection.setUseCaches(false);
            httpConnection.setRequestProperty("Accept", "application/json");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setRequestProperty("Authorization", authorizationHeaderField);
            httpConnection.connect();

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
            out.write("{ \"type\": \"port\", \"mode\": \"digital\", \"direction\": \"input\", \"voltage\": \"ttl\" }");
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
}
