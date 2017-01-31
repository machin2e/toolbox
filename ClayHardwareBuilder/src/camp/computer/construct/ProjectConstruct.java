package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class ProjectConstruct extends Construct {

    public String title = null;

    // TODO: public boolean isPublished = true; // For publishing projects into portfolio.

    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();

    public List<PathConstruct> pathConstructs = new ArrayList<>();

    public ProjectConstruct() {
        super();
    }

}
