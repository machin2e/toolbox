package camp.computer.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import camp.computer.util.CouchDB;
import camp.computer.util.Serialize;

public class Device {

    // <COUCHDB>
    public String id;

    public String rev;
    // </COUCHDB>

    // <TEMPLATE/STATE>
    public String type;

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

