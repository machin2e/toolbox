package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import camp.computer.util.JSON;

public class Device {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("instance_id")
    public String instance_id = UUID.randomUUID().toString();

    @JsonProperty("type")
    public String type;

    @JsonProperty("ports")
    public List<Port> ports;

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

    public String serializeConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Device.class, new DeviceSerializer());
        objectMapper.registerModule(module);

        String serialized = null;
        try {
            serialized = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return serialized;
    }

    @JsonValue
    @JsonSerialize(using = DeviceSerializer.class)
    public Device getConfiguration() {
        return this;
    }

    public static ObjectNode serialize(Device device, JSON.SerializationPolicy serializationPolicy) {

        if (serializationPolicy == JSON.SerializationPolicy.STRUCTURE) {

            ObjectNode deviceNode = JsonNodeFactory.instance.objectNode();
            deviceNode.put("_id", device.id);
            deviceNode.put("_rev", device.rev);
            deviceNode.put("type", device.type);

            ArrayNode portsNode = deviceNode.putArray("ports");
            for (int i = 0; i < device.ports.size(); i++) {
                portsNode.add(Port.serialize(device.ports.get(i), serializationPolicy));
            }

            return deviceNode;

        } else if (serializationPolicy == JSON.SerializationPolicy.INSTANCE) {

            // TODO:

        } else if (serializationPolicy == JSON.SerializationPolicy.STATE) {

            ObjectNode deviceNode = JsonNodeFactory.instance.objectNode();
            deviceNode.put("_id", device.id);
            deviceNode.put("_rev", device.rev);
            deviceNode.put("type", device.type);

            ArrayNode portsNode = deviceNode.putArray("ports");
            for (int i = 0; i < device.ports.size(); i++) {
                portsNode.add(Port.serialize(device.ports.get(i), serializationPolicy));
            }

            return deviceNode;

        }

        return null;

    }
}

