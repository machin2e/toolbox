package camp.computer.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import camp.computer.util.Serialize;

public class Channel extends Entity {

    public String id;

    public String rev;

    public String type;

    public Port source;

    public Port target;

    private Channel() {
        this.type = "channel";
    }

    public Channel(Port source, Port target) {
        this.type = "channel";
        this.source = source;
        this.target = target;
    }

    public static ObjectNode serialize(Channel channel, Serialize.Policy serializationPolicy) {

        if (serializationPolicy == Serialize.Policy.TEMPLATE) {

            // TODO:

        } else if (serializationPolicy == Serialize.Policy.INSTANCE) {

            // TODO:

        } else if (serializationPolicy == Serialize.Policy.STATE) {

            ObjectNode channelNode = JsonNodeFactory.instance.objectNode();

            channelNode.put("_id", channel.id);
            channelNode.put("_rev", channel.rev);

            channelNode.put("type", channel.type);

//        ObjectNode sourceNode = channelNode.putObject("source");
//        sourceNode.put("type", channel.source.type);
//        sourceNode.put("instance_id", channel.source.instance_id);
//        sourceNode.put("mode", channel.source.mode);
//        sourceNode.put("direction", channel.source.direction);
//        sourceNode.put("voltage", channel.source.voltage);
            channelNode.set("source", Port.serialize(channel.source, serializationPolicy));

//        ObjectNode targetNode = channelNode.putObject("target");
//        targetNode.put("type", channel.target.type);
//        targetNode.put("instance_id", channel.target.instance_id);
            channelNode.set("target", Port.serialize(channel.target, serializationPolicy));

//        if (serializationPolicy == JSON.Policy.INSTANCE) {
//            channelNode.put("instance_id", channel.instance_id);
//        }

//        if (serializationPolicy == JSON.Policy.TEMPLATE
//                || serializationPolicy == JSON.Policy.INSTANCE) {
//
//            ArrayNode portsNode = channelNode.putArray("ports");
//            for (int i = 0; i < channel.ports.size(); i++) {
//                portsNode.add(Port.serialize(channel.ports.get(i), serializationPolicy));
//            }
//
//        }
//
//        if (serializationPolicy == JSON.Policy.STATE) {
//
//            ArrayNode portsNode = channelNode.putArray("ports");
//            for (int i = 0; i < channel.ports.size(); i++) {
//                portsNode.add(Port.serialize(channel.ports.get(i), serializationPolicy));
//            }
//
//        }

            return channelNode;

        }

        return null;

    }

    public static Channel deserialize(String json) {

        try {

//            Interface iface = Interface.create();
            Channel channel = new Channel();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json.toString());

            if (rootNode.has("_id")) {
                channel.id = rootNode.path("_id").asText();
            }

            if (rootNode.has("type")) {
                channel.type = rootNode.path("type").asText();
            }

//            if (rootNode.has("instance_id")) {
//                iface.instance_id = rootNode.path("instance_id").asText();
//            }

            if (rootNode.has("source")) {
                Port sourcePort = Port.deserialize(rootNode.path("source").toString());
                channel.source = sourcePort;
            }

            if (rootNode.has("target")) {
                Port targetPort = Port.deserialize(rootNode.path("target").toString());
                channel.target = targetPort;
            }

            return channel;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}


