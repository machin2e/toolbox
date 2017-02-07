package camp.computer.construct;

import java.util.UUID;

import camp.computer.workspace.Manager;

public abstract class Construct {

    public long uid = Manager.elementCounter++;

    // Identity:
    public UUID uuid = UUID.randomUUID();

    // <COMPONENT>
    public String title = null; // title/tag(s)
    // </COMPONENT>

    // TODO: type (generic type identifier so can use single construct)
    // TODO: HashMap<?> variables (can be Construct or other values)
    // Construct (Prototype) -> Container (must specify type or is anonymous) -> Container Revision

    // Structure/Links:
    // TODO: parent
    // TODO: siblings
    // TODO: connections (per-construct links)
    // TODO: children

    // TODO: previousVersion
    // TODO: nextVersions (maybe just use previousVersion)

    // TODO: variables/features/properties/states
    // TODO: configuration(s) : assign state to multiple variables <-- do this for _Container_ not Construct


    public Construct() {
        Manager.elements.put(uid, this);
    }

}
