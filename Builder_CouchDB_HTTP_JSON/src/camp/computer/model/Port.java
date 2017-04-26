package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

import camp.computer.util.JSON;
import camp.computer.util.List;

public class Port {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("instance_id")
    public String instance_id = UUID.randomUUID().toString();

    @JsonProperty("type")
    public String type;

    // <DESCRIPTOR>
    @JsonProperty("modes")
    public List<String> modes;

    @JsonProperty("directions")
    public List<String> directions;

    @JsonProperty("voltages")
    public List<String> voltages;
    // </DESCRIPTOR>

    // <STATE>
    @JsonProperty("mode")
    public String mode;

    @JsonProperty("direction")
    public String direction;

    @JsonProperty("voltage")
    public String voltage;
    // </STATE>

    private Port() {
        type = "port";
        modes = new List<>();
        directions = new List<>();
        voltages = new List<>();
    }

    public static Port create() {
        return new Port();
    }

    public static Port create(List modes, List directions, List voltages) {
        Port port = new Port();
        port.modes.addAll(modes);
        port.directions.addAll(directions);
        port.voltages.addAll(voltages);
        return port;
    }

    public void set(String mode, String direction, String voltage) {
        this.mode = mode;
        this.direction = direction;
        this.voltage = voltage;
    }

    public static String serializeConfiguration(Port port) {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Port.class, new PortSerializer());
        objectMapper.registerModule(module);

        String serialized = null;
        try {
            serialized = objectMapper.writeValueAsString(port);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return serialized;
    }

    public static String serializeState(Port port) {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(Port.class, new PortStateSerializer());
        objectMapper.registerModule(module);

        String serialized = null;
        try {
            serialized = objectMapper.writeValueAsString(port);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return serialized;
    }

    public static ObjectNode serialize(Port port, JSON.SerializationPolicy serializationPolicy) {

        if (serializationPolicy == JSON.SerializationPolicy.STRUCTURE) {

            ObjectNode portNode = JsonNodeFactory.instance.objectNode();
            portNode.put("_id", port.id);
            portNode.put("_rev", port.rev);
            portNode.put("type", port.type);

            ArrayNode modesNode = portNode.putArray("modes");
            for (int i = 0; i < port.modes.size(); i++) {
               modesNode.add(port.modes.get(i));
            }

            ArrayNode directionsNode = portNode.putArray("directions");
            for (int i = 0; i < port.directions.size(); i++) {
                directionsNode.add(port.directions.get(i));
            }

            ArrayNode voltagesNode = portNode.putArray("voltages");
            for (int i = 0; i < port.voltages.size(); i++) {
                voltagesNode.add(port.voltages.get(i));
            }

            return portNode;

        } else if (serializationPolicy == JSON.SerializationPolicy.INSTANCE) {

            // TODO:

        } else if (serializationPolicy == JSON.SerializationPolicy.STATE) {

            ObjectNode portNode = JsonNodeFactory.instance.objectNode();
            portNode.put("_id", port.id);
            portNode.put("_rev", port.rev);
            portNode.put("type", port.type);

            portNode.put("mode", port.mode);

            portNode.put("direction", port.direction);

            portNode.put("voltage", port.voltage);

            return portNode;

        }


        return null;

    }

}
