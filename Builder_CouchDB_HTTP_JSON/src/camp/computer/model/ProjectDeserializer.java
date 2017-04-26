package camp.computer.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by mokogobo on 4/26/2017.
 */
public class ProjectDeserializer extends StdDeserializer<Project> {

    public ProjectDeserializer() {
        this(null);
    }

    public ProjectDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Project deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        JsonNode rootNode = jp.getCodec().readTree(jp);
        String id = rootNode.get("_id").asText();
        // TODO: String rev = rootNode.get("_rev").asText();
        // TODO: String instanceId = rootNode.get("instance_id").asText();
        String type = rootNode.get("type").asText();
        JsonNode assetsNode = rootNode.path("assets"); // <assets></assets>
        JsonNode devicesNode = assetsNode.path("devices"); // <devices></devices>
        for (Iterator<JsonNode> it = devicesNode.elements(); it.hasNext(); ) {
            JsonNode deviceNode = it.next();
            String instanceId = deviceNode.get("type").asText();
        }
//        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();

        // Print document values
        System.out.println("PROJECT _id: " + id);
        System.out.println("PROJECT type: " + type);

        Project project = new Project();
        // TODO: Populate project

        return project;
    }
}
