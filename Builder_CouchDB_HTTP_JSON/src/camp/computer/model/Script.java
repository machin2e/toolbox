package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Script {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("type")
    public String type;

    @JsonProperty("code")
    public String code;

    public Script() {
        this.type = "script";
    }

}
