package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class ProjectConstruct extends Construct {

    // TODO: public boolean isPublished = true; // For publishing projects into portfolio.

    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();

    public List<PathConstruct> pathConstructs = new ArrayList<>();

    public ProjectConstruct() {
        super();
    }

}
