package camp.computer.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import camp.computer.util.CouchDB;
import camp.computer.util.List;
import camp.computer.util.Serialize;

public class Port {

    // <COUCHDB>
    public String id;

    public String rev;
    // </COUCHDB>

    // <TEMPLATE>
    public String type;

    public List<String> modes;

    public List<String> directions;

    public List<String> voltages;
    // </TEMPLATE>

    // <INSTANCE>
    public String instance_id = null; // UUID.randomUUID().toString();
    // </INSTANCE>

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

        portNode.put("_id", port.id);
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
