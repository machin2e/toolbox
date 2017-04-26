package camp.computer.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class PortSerializer extends StdSerializer<Port> {

    public PortSerializer() {
        this(null);
    }

    public PortSerializer(Class<Port> t) {
        super(t);
    }

    @Override
    public void serialize(Port port, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("_id", port.id);
        jsonGenerator.writeStringField("_rev", port.rev);
        jsonGenerator.writeStringField("instance_id", port.instance_id);
        jsonGenerator.writeStringField("type", port.type);
        jsonGenerator.writeObjectField("modes", port.modes);
        jsonGenerator.writeObjectField("directions", port.directions);
        jsonGenerator.writeObjectField("voltages", port.voltages);

        jsonGenerator.writeEndObject();

    }
}

