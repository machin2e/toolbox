package camp.computer.util;

import java.util.UUID;

public class CouchDB {

    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
