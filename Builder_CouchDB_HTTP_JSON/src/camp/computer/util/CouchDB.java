package camp.computer.util;

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
import java.util.UUID;

import camp.computer.model.Device;
import camp.computer.model.Port;
import camp.computer.model.Project;

public class CouchDB {


    public static String ADMIN_USERNAME = "admin";
    public static String ADMIN_PASSWORD = "password";

    public String username = null;
    public String password = null;

    // <UTIL>
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    // </UTIL>

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

    public void savePortDocument(Port port) {

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

            // Serialize
            String jsonString = (new ObjectMapper()).writeValueAsString(Port.serialize(port, Serialize.Policy.TEMPLATE));

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

    public void saveDeviceDocument(Device device) {

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

            // Serialize
            String jsonString = (new ObjectMapper()).writeValueAsString(Device.serialize(device, Serialize.Policy.TEMPLATE));

            // Write PUT request body (i.e., the JSON data to PUT)
            OutputStreamWriter out = new OutputStreamWriter(httpConnection.getOutputStream());
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

    public void saveProjectDocument(Project project) {

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

            // Serialize object
            String jsonString = (new ObjectMapper()).writeValueAsString(Project.serialize(project, Serialize.Policy.STATE));
            // Object jsonObject = new ObjectMapper().readValue(jsonString, Object.class);
            // String prettyString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            // System.out.println(prettyString);

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

}
