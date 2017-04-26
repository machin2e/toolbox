package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("type")
    public String type;

    @JsonProperty("tasks")
    public List<Task> tasks;

    public Controller() {
        tasks = new ArrayList<>();
    }
}

