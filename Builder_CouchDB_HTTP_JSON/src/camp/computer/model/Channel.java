package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Channel {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("type")
    public String type;

    @JsonProperty("source")
    public Port source;

    @JsonProperty("target")
    public Port target;

    public Channel(Port source, Port target) {
        this.type = "channel";
        this.source = source;
        this.target = target;
    }
}

