package camp.computer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Iterator;

import camp.computer.util.CouchDB;
import camp.computer.util.List;
import camp.computer.util.Serialize;

public class Port {

    // <COUCHDB>
    public String id;

    public String rev;
    // </COUCHDB>

    // <INSTANCE>
    public String instance_id = null; // UUID.randomUUID().toString();
    // </INSTANCE>

    // <TEMPLATE>
    public String type;

    public List<String> modes;

    public List<String> directions;

    public List<String> voltages;
    // </TEMPLATE>

    // <STATE>
    public String mode;

    public String direction;

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

    public static ObjectNode serialize(Port port, Serialize.Policy serializePolicy) {

        ObjectNode portNode = JsonNodeFactory.instance.objectNode();

//        portNode.put("_id", port.id);
//        portNode.put("_rev", port.rev);

        portNode.put("type", port.type);

        if (serializePolicy == Serialize.Policy.INSTANCE
                || serializePolicy == Serialize.Policy.STATE) {
            portNode.put("instance_id", port.instance_id);
        }

        if (serializePolicy == Serialize.Policy.TEMPLATE
                || serializePolicy == Serialize.Policy.INSTANCE) {

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

        }

        if (serializePolicy == Serialize.Policy.STATE) {

            portNode.put("mode", port.mode);
            portNode.put("direction", port.direction);
            portNode.put("voltage", port.voltage);

            return portNode;

        }

        return portNode;

    }

    public static Port deserialize(String json) {

        try {

            Port port = Port.create();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            if (rootNode.has("_id")) {
                port.id = rootNode.path("_id").asText();
            }

            if (rootNode.has("type")) {
                port.type = rootNode.path("type").asText();
            }

            if (rootNode.has("instance_id")) {
                port.instance_id = rootNode.path("instance_id").asText();
            }

            if (rootNode.has("modes")) {
                JsonNode modesNode = rootNode.path("modes");
                for (Iterator<JsonNode> it = modesNode.elements(); it.hasNext(); ) {
                    JsonNode modeNode = it.next();
                    port.modes.add(modeNode.asText());
                }
            }

            if (rootNode.has("directions")) {
                JsonNode directionsNode = rootNode.path("directions");
                for (Iterator<JsonNode> it = directionsNode.elements(); it.hasNext(); ) {
                    JsonNode directionNode = it.next();
                    port.directions.add(directionNode.asText());
                }
            }

            if (rootNode.has("voltages")) {
                JsonNode voltagesNode = rootNode.path("voltages");
                for (Iterator<JsonNode> it = voltagesNode.elements(); it.hasNext(); ) {
                    JsonNode voltageMode = it.next();
                    port.voltages.add(voltageMode.asText());
                }
            }

            if (rootNode.has("mode")) {
                port.mode = rootNode.path("mode").asText();
            }

            if (rootNode.has("direction")) {
                port.direction = rootNode.path("direction").asText();
            }

            if (rootNode.has("voltage")) {
                port.voltage = rootNode.path("voltage").asText();
            }

            return port;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    public static Port generateRandom() {

//        String mode = List.selectRandomElement("digital", "analog", "pulse-width-modulation", "resistive-touch", "power", "i2c(scl)", "i2c(sda)", "spi(mosi)", "spi(miso)", "spi(ss)", "uart(rx)", "uart(tx)");
//        String direction = List.selectRandomElement("input", "output", "bidirectional");
//        String voltage = List.selectRandomElement("0v", "3.3v", "5v");

        List<String> modes = List.randomSublist("digital", "analog", "pulse-width-modulation", "resistive-touch", "power", "i2c(scl)", "i2c(sda)", "spi(mosi)", "spi(miso)", "spi(ss)", "uart(rx)", "uart(tx)");
        List<String> directions = List.randomSublist("input", "output", "bidirectional");
        List<String> voltages = List.randomSublist("0v", "3.3v", "5v");

        String mode = List.selectRandomElement(modes);
        String direction = List.selectRandomElement(directions);
        String voltage = List.selectRandomElement(voltages);

        Port port = Port.create(modes, directions, voltages);
        port.id = CouchDB.generateUuid();

        port.set(mode, direction, voltage);

        return port;

    }

}
