package camp.computer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;

import camp.computer.model.Port;
import camp.computer.util.Cache;
import camp.computer.util.CouchDB;
import camp.computer.util.List;

public class Builder {

    Cache cache = null;
    CouchDB couchDB = null;

    public Builder(Cache cache, CouchDB couchDB) {
        this.cache = cache;
        this.couchDB = couchDB;
    }

    public List<Port> requestPorts(CouchDB couchDB) {

        String documentJson = couchDB.listDocuments("port");

        try {

            List<Port> ports = new List<>();

            // Deserialize response
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(documentJson.toString());
            JsonNode documentsNode = rootNode.path("docs");
            for (Iterator<JsonNode> it = documentsNode.elements(); it.hasNext(); ) {
                JsonNode documentNode = it.next();

                String documentType = null;
                if (documentNode.has("type")) {
                    documentType = documentNode.path("type").asText();
                }

                if (documentType != null) {

                    Port port = Port.deserialize(documentNode.toString());
                    ports.add(port);

                    // TODO: Cache/Manage the deserialized Port entity!

                }
            }

            return ports;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
