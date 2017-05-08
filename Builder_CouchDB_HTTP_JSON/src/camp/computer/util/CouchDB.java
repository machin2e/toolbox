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

    public static String DEFAULT_URI = "http://localhost:5984";

    private String baseUri = "http://localhost:5984";

    // <DANGER>
    public static String ADMIN_USERNAME = "admin";
    public static String ADMIN_PASSWORD = "password";
    // </DANGER>

    // <DANGER>
    public String username = null;
    public String password = null;
    // </DANGER>

    public CouchDB() {
        this.baseUri = CouchDB.DEFAULT_URI;
    }

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
            url = new URL(baseUri + "/_users/org.couchdb.user:" + username);
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

            // Configure PUT request
            url = new URL(baseUri + "/" + username);
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
            out.write("{ \"name\": \"" + username + "\", \"password\": \"" + password + "\", \"roles\": [], \"type\": \"user\" }");
            out.close();

            // Read PUT response (from CouchDB server)
            httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 201) {
                System.out.println("Created.");

                System.out.println(httpResponseCode);
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

            System.out.println("Setting up security... ");

            // Configure PUT request
            url = new URL(baseUri + "/" + username + "/_security");
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
            URL url = new URL(baseUri + "/_session");
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            // httpConnection.setRequestProperty("Accept", "application/json");
            // httpConnection.setRequestProperty("Content-Type", "application/json");
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

    public String listDocuments(String type) {

        HttpURLConnection httpConnection = null;

        try {

            // HTTP Basic Authentication
            // References:
            // - RFC 1945 https://tools.ietf.org/html/rfc1945#section-11.1
            // - Wikipedia https://en.wikipedia.org/wiki/Basic_access_authentication
            final String basicAuthorizationEncoding = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
            final String authorizationHeaderField = "Basic " + basicAuthorizationEncoding;

            URL url = new URL(baseUri + "/" + username + "/_find?include_docs=true");
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
            // System.out.println(httpConnection.getResponseMessage());
            if (httpResponseCode == 200) {

                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

                // Buffer response data
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();

                /*
                // Deserialize response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.toString());
                JsonNode documentsNode = rootNode.path("docs");
                for (Iterator<JsonNode> it = documentsNode.elements(); it.hasNext(); ) {
                    JsonNode documentNode = it.next();

                    String documentType = null;
                    if (documentNode.has("type")) {
                        documentType = documentNode.path("type").asText();
                    }

                    if (documentType != null) {
                        if (type != null && documentType.equals("port")) {

                            Port port = Port.deserialize(documentNode.toString());

                            // TODO: Cache/Manage the deserialized Port entity!
                            System.out.println(port.id + "\t" + port.type);

                        } else if (type != null && documentType.equals("device")) {

                            Device device = Device.deserialize(documentNode.toString());

                            // TODO: Cache/Manage the deserialized Port entity!
                            System.out.println(device.id + "\t" + device.type);

                        } else if (type != null && documentType.equals("project")) {

                            Project project = Project.deserialize(documentNode.toString());

                            // TODO: Cache/Manage the deserialized Port entity!
                            System.out.println(project.id + "\t" + project.type);

                        } else if (type != null && documentType.equals(type)) {
                            Object jsonObject = objectMapper.readValue(documentNode.toString(), Object.class);
                            String perttyStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
                            System.out.println(perttyStr);
                            System.out.println();
                        }
                    }
                }
                */

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

        return null;
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
            URL serverUri = new URL(baseUri + "/" + username + "/" + documentUuid);
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
            URL serverUri = new URL(baseUri + "/" + username + "/" + documentUuid);
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
            URL serverUri = new URL(baseUri + "/" + username + "/" + documentUuid);
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
            url = new URL(baseUri + "/" + username + "/_find?include_docs=true");
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

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(response.toString());

                JsonNode rowsNode = rootNode.path("docs");
                for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                    JsonNode docNode = it.next();

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
            url = new URL(baseUri + "/" + username + "/_find?include_docs=true");
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
            out.write("{ \"selector\": { \"type\": { \"$eq\": \"device\" }, \"$and\": [ { \"ports\": { \"$elemMatch\": { \"modes\": \"spi(mosi)\" } } }, { \"ports\": { \"$elemMatch\": { \"modes\": \"uart(rx)\" } } }, { \"ports\": { \"$elemMatch\": { \"modes\": \"power\" } } } ] } }");
            out.close();

            // Read PUT response (from CouchDB server)
            int httpResponseCode = httpConnection.getResponseCode();

            if (httpResponseCode == 200) {

                System.out.println(httpResponseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //create ObjectMapper instance
                ObjectMapper objectMapper = new ObjectMapper();

                //read JSON like DOM Parser
                JsonNode rootNode = objectMapper.readTree(response.toString());

                JsonNode rowsNode = rootNode.path("docs");
                for (Iterator<JsonNode> it = rowsNode.elements(); it.hasNext(); ) {
                    JsonNode docNode = it.next();

                    JsonNode docType = docNode.path("type");

                    String type = "device";
                    if (type != null && docType.asText().equals(type)) {
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
