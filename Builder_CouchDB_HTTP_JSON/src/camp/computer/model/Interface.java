package camp.computer.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

import camp.computer.util.Serialize;

public class Interface {

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
}

