package camp.computer.construct;

import java.util.UUID;

public class Identifier {

    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)
    // TODO: version-uuid

    public long uid = -1L; // Manager_v1.elementCounter++; // manager/cache UID
    // TODO: version-uid

    public String tag = null; // label/identifier(s)

}
