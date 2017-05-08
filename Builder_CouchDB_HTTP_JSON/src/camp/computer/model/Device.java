package camp.computer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import camp.computer.util.CouchDB;
import camp.computer.util.Serialize;

public class Device extends Entity {

    // <COUCHDB>
    public String id;
    public String rev;
    // </COUCHDB>

    public String type;

    // <TEMPLATE/STATE>
    public List<Port> ports;
    // </TEMPLATE/STATE>

    // <INSTANCE>
    public String instance_id = null; // UUID.randomUUID().toString();
    // </INSTANCE>

    private Device() {
        type = "device";
        ports = new ArrayList<>();
    }

    public static Device create() {
        return new Device();
    }

    public void addPort(Port port) {
        ports.add(port);
    }

    public static ObjectNode serialize(Device device, Serialize.Policy serializationPolicy) {

        ObjectNode deviceNode = JsonNodeFactory.instance.objectNode();

        deviceNode.put("_id", device.id);
//        deviceNode.put("_rev", device.rev);

        deviceNode.put("type", device.type);

        if (serializationPolicy == Serialize.Policy.INSTANCE) {
            deviceNode.put("instance_id", device.instance_id);
        }

        if (serializationPolicy == Serialize.Policy.TEMPLATE
                || serializationPolicy == Serialize.Policy.INSTANCE) {

            ArrayNode portsNode = deviceNode.putArray("ports");
            for (int i = 0; i < device.ports.size(); i++) {
                portsNode.add(Port.serialize(device.ports.get(i), serializationPolicy));
            }

        }

        if (serializationPolicy == Serialize.Policy.STATE) {

            ArrayNode portsNode = deviceNode.putArray("ports");
            for (int i = 0; i < device.ports.size(); i++) {
                portsNode.add(Port.serialize(device.ports.get(i), serializationPolicy));
            }

        }

        return deviceNode;

    }

    public static Device deserialize(String json) {

        try {

            Device device = Device.create();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            if (rootNode.has("_id")) {
                device.id = rootNode.path("_id").asText();
            }

            if (rootNode.has("type")) {
                device.type = rootNode.path("type").asText();
            }

            if (rootNode.has("instance_id")) {
                device.instance_id = rootNode.path("instance_id").asText();
            }

            if (rootNode.has("ports")) {
                JsonNode portsNode = rootNode.path("ports");
                for (Iterator<JsonNode> it = portsNode.elements(); it.hasNext(); ) {
                    JsonNode portNode = it.next();
                    Port port = Port.deserialize(portNode.toString());
                    device.ports.add(port);
                }
            }

            return device;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Device generateRandom() {

        Device device = Device.create();
        device.id = CouchDB.generateUuid();

        int portCount = (new Random()).nextInt(12);
        for (int i = 0; i < portCount; i++) {
            device.addPort(Port.generateRandom());
        }

        return device;

    }
}

