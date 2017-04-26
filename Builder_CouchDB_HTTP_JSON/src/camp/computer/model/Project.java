package camp.computer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonSerialize(using = ProjectSerializer.class)
public class Project {

    // <INTERNAL>
    @JsonIgnore
    public List<Interface> interfaces;
    // </INTERNAL>

    @JsonProperty("_id")
    public String id;

    @JsonProperty("_rev")
    public String rev;

    @JsonProperty("type")
    public String type;

    @JsonProperty("devices")
    public List<Device> devices;

//    @JsonProperty("interfaces")
//    public List<Interface> interfaces;

    public Project() {
        type = "project";
        devices = new ArrayList<>();
        interfaces = new ArrayList<>();
    }

    public Interface getInterface(Device source, Device target) {
        for (int i = 0; i < interfaces.size(); i++) {
            if (interfaces.get(i).source == source && interfaces.get(i).target == target) {
                return interfaces.get(i);
            }
        }
        return null;
    }

    public Interface addInterface(Device source, Device target) {
        if (source == target) {
            return null;
        }

        if (!this.devices.contains(source)) {
            this.devices.add(source);
        }
        if (!this.devices.contains(target)) {
            this.devices.add(target);
        }

        Interface iface = getInterface(source, target);
        if (iface == null) {
            iface = new Interface(source, target);
            interfaces.add(iface);
        }
        return iface;
    }

    public static Project deserialize(String json) {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Project.class, new ProjectDeserializer());
        mapper.registerModule(module);

        Project project = null;
        try {
            project = mapper.readValue(json, Project.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return project;

    }
}

