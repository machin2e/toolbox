package camp.computer.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class DeviceSerializer extends StdSerializer<Device> {

    public DeviceSerializer() {
        this(null);
    }

    public DeviceSerializer(Class<Device> t) {
        super(t);
    }

    @Override
    public void serialize(Device device, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("_id", device.id);
        jsonGenerator.writeStringField("_rev", device.rev);
        jsonGenerator.writeStringField("instance_id", device.instance_id);
        jsonGenerator.writeStringField("type", device.type);
        jsonGenerator.writeArrayFieldStart("ports");
        for (int i = 0; i < device.ports.size(); i++) {
            jsonGenerator.writeRawValue(Port.serializeConfiguration(device.ports.get(i)));
        }
        jsonGenerator.writeEndArray();

        jsonGenerator.writeEndObject();

    }
}

