package camp.computer.construct;

import java.util.ArrayList;
import java.util.List;

public class ProjectConstruct {

    public long uid = DeviceConstruct.constructCounter++;

    public String title = null;

    public List<DeviceConstruct> deviceConstructs = new ArrayList<>();

    public List<PathConstruct> pathConstructs = new ArrayList<>();

}
