package camp.computer.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import camp.computer.util.JSON;
import camp.computer.util.Serialize;

public class Channel {

    public String id;

    public String rev;

    public String type;

    public Port source;

    public Port target;

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

}


