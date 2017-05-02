package camp.computer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

public class JSON {


    public static String toPrettyString(String json) {

        try {
            Object jsonObject = new ObjectMapper().readValue(json, Object.class);
            String prettyString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
            return prettyString;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
