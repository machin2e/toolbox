package camp.computer.construct;

public class PathConstruct extends Construct {

    public PortConstruct sourcePortConstruct = null;

    public PortConstruct targetPortConstruct = null;

    // TODO: public String portConfiguration; // Used to determine configurations of contained ports.

    public PathConstruct() {
        super();
        this.type = "path";
    }

}
