package camp.computer.construct;

import java.util.UUID;

public class Construct {
    public long uid = -1L; // Manager_v1.elementCounter++; // manager/cache UID
    public UUID uuid = UUID.randomUUID(); // universal identifier (unique among all in central repo)

    public String tag = null; // label/tag(s)
}
