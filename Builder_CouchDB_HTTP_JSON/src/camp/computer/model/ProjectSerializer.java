package camp.computer.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ProjectSerializer extends StdSerializer<Project> {

    public ProjectSerializer() {
        this(null);
    }

    public ProjectSerializer(Class<Project> t) {
        super(t);
    }

    @Override
    public void serialize(Project project, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("_id", project.id);
//        jsonGenerator.writeStringField("_rev", project.rev);
        jsonGenerator.writeStringField("type", "project");

        // ./assets/devices
        jsonGenerator.writeObjectFieldStart("assets");
        jsonGenerator.writeArrayFieldStart("devices");
        for (int i = 0; i < project.devices.size(); i++) {
//            jsonGenerator.writeObject(project.devices.get(i).serializeConfiguration());
            jsonGenerator.writeObject(project.devices.get(i).getConfiguration());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();

        // ./interfaces/interface
        jsonGenerator.writeArrayFieldStart("interfaces");
        for (int i = 0; i < project.interfaces.size(); i++) {
//            jsonGenerator.writeObject(project.interfaces.get(i));

            Interface iface = project.interfaces.get(i);

            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("_id", iface.id);
            jsonGenerator.writeStringField("_rev", iface.rev);
            jsonGenerator.writeStringField("type", "interface");
            // Interface source
            jsonGenerator.writeObjectFieldStart("source");
            jsonGenerator.writeStringField("type", "device");
            jsonGenerator.writeStringField("instance_id", iface.source.instance_id);
            jsonGenerator.writeEndObject();
            // Interface target
            jsonGenerator.writeObjectFieldStart("target");
            jsonGenerator.writeStringField("type", "target");
            jsonGenerator.writeStringField("instance_id", iface.target.instance_id);
            jsonGenerator.writeEndObject();
            jsonGenerator.writeArrayFieldStart("channels");
            for (int j = 0; j < iface.channels.size(); j++) {
                Channel channel = iface.channels.get(j);
                jsonGenerator.writeStartObject();
//                jsonGenerator.writeStringField("_id", channel.id);
//                jsonGenerator.writeStringField("_rev", channel.rev);
                jsonGenerator.writeStringField("type", "channel");
//                jsonGenerator.writeFieldName("source");
//                jsonGenerator.writeRawValue(Port.serializeState(channel.source));
                jsonGenerator.writeObjectFieldStart("source");
                jsonGenerator.writeStringField("type", "port");
                jsonGenerator.writeStringField("instance_id", channel.source.instance_id);
                jsonGenerator.writeStringField("mode", channel.source.mode);
                jsonGenerator.writeStringField("direction", channel.source.direction);
                jsonGenerator.writeStringField("voltage", channel.source.voltage);
                jsonGenerator.writeEndObject();
                jsonGenerator.writeObjectFieldStart("target");
                jsonGenerator.writeStringField("type", "port");
                jsonGenerator.writeStringField("instance_id", channel.target.instance_id);
                jsonGenerator.writeStringField("mode", channel.target.mode);
                jsonGenerator.writeStringField("direction", channel.target.direction);
                jsonGenerator.writeStringField("voltage", channel.target.voltage);
                jsonGenerator.writeEndObject();
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();
            jsonGenerator.writeObjectFieldStart("controller"); // <controller>
            jsonGenerator.writeStringField("_id", "<uuid>");
            jsonGenerator.writeStringField("_rev", "<uuid>");
            jsonGenerator.writeArrayFieldStart("tasks"); // <tasks>
            jsonGenerator.writeStartObject(); // <task>
            jsonGenerator.writeStringField("type", "task");
            jsonGenerator.writeStringField("_id", "<uuid>");
            jsonGenerator.writeStringField("_rev", "<uuid>");
            jsonGenerator.writeObjectFieldStart("script"); // <script>
            jsonGenerator.writeStringField("_id", "<uuid>");
            jsonGenerator.writeStringField("_rev", "<uuid>");
            jsonGenerator.writeStringField("type", "script");
            jsonGenerator.writeStringField("code", "javascript:task(data) { /* task function body */ }");
            jsonGenerator.writeEndObject(); // </script>
            jsonGenerator.writeEndObject(); // </task>
            jsonGenerator.writeEndArray(); // </tasks>
            jsonGenerator.writeEndObject(); // </controller>
            jsonGenerator.writeEndObject(); // </interface>
        }
        jsonGenerator.writeEndArray(); // </interfaces>

        jsonGenerator.writeEndObject();



//        jsonGenerator.writeObjectFieldStart("devices");
    }
}

