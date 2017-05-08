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

import camp.computer.util.Serialize;

public class Interface extends Entity {

    // <COUCHDB>
    public String id;

    public String rev;
    // </COUCHDB>

    // <TEMPLATE/STATE>
    public String type;

    public Device source;

    public Device target;

    public List<Channel> channels;

    public Controller controller;
    // </TEMPLATE/STATE>

    private Interface() {
        this.type = "interface";
        this.channels = new ArrayList<>();
    }

    public Interface(Device source, Device target) {
        this.type = "interface";
        this.source = source;
        this.target = target;
        this.channels = new ArrayList<>();
    }

    public Channel getChannel(Port source, Port target) {
        if (channels != null) {
            for (int i = 0; i < channels.size(); i++) {
                if (channels.get(i).source == source && channels.get(i).target == target) {
                    return channels.get(i);
                }
            }
        }
        return null;
    }

    public Channel addChannel(Port source, Port target) {
        if (source == target) {
            return null;
        }

        Channel channel = getChannel(source, target);
        if (channel == null) {
            channel = new Channel(source, target);
            channels.add(channel);
        }
        return channel;
    }

    public static ObjectNode serialize(Interface iface, Serialize.Policy serializationPolicy) {

        if (serializationPolicy == Serialize.Policy.TEMPLATE) {

            // TODO:

        } else if (serializationPolicy == Serialize.Policy.INSTANCE) {

            // TODO:

        } else if (serializationPolicy == Serialize.Policy.STATE) {

            ObjectNode ifaceNode = JsonNodeFactory.instance.objectNode();
//            ifaceNode.put("_id", iface.id);
//            ifaceNode.put("_rev", iface.rev);
            ifaceNode.put("type", iface.type);

            ObjectNode sourceNode = ifaceNode.putObject("source");
            sourceNode.put("type", iface.source.type);
            sourceNode.put("instance_id", iface.source.instance_id);

            ObjectNode targetNode = ifaceNode.putObject("target");
            targetNode.put("type", iface.target.type);
            targetNode.put("instance_id", iface.target.instance_id);

            ArrayNode channelsNode = ifaceNode.putArray("channels");
            for (int i = 0; i < iface.channels.size(); i++) {
                channelsNode.add(Channel.serialize(iface.channels.get(i), serializationPolicy));
            }

            ifaceNode.set("controller", Controller.serialize(iface.controller, serializationPolicy));

            return ifaceNode;

        }

        return null;

    }

    public static Interface deserialize(String json) {

        try {

//            Interface iface = Interface.create();
            Interface iface = new Interface();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            if (rootNode.has("_id")) {
                iface.id = rootNode.path("_id").asText();
            }

            if (rootNode.has("type")) {
                iface.type = rootNode.path("type").asText();
            }

//            if (rootNode.has("instance_id")) {
//                iface.instance_id = rootNode.path("instance_id").asText();
//            }

            if (rootNode.has("source")) {
                Device sourceDevice = Device.deserialize(rootNode.path("source").toString());
                iface.source = sourceDevice;
            }

            if (rootNode.has("target")) {
                Device targetDevice = Device.deserialize(rootNode.path("target").toString());
                iface.target = targetDevice;
            }

            if (rootNode.has("channels")) {
                JsonNode channelsNode = rootNode.path("channels");
                for (Iterator<JsonNode> it = channelsNode.elements(); it.hasNext(); ) {
                    JsonNode channelNode = it.next();
                    Channel channel = Channel.deserialize(channelNode.toString());
                    iface.channels.add(channel);
                }
            }

            if (rootNode.has("controller")) {
                Controller controller = Controller.deserialize(rootNode.path("controller").toString());
                iface.controller = controller;
            }

            return iface;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}

