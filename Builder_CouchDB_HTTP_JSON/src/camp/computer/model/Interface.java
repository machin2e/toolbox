package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Interface {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("type")
    public String type;

    @JsonProperty("source")
    public Device source;

    @JsonProperty("target")
    public Device target;

    @JsonProperty("channels")
    public List<Channel> channels;

    @JsonProperty("controller")
    public Controller controller;

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
}

