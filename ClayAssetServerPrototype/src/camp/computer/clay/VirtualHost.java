package camp.computer.clay;

// Simulates firmware (not exposed to Users by default)
public class VirtualHost {

    // This shouldn't be exposed directly through the API. It should only be passed as a parameter to action scripts.
    public String getDataNamespace() {
        String data = "{ \"color\": \"blue\", \"brightness\": \"0.8\" }";
        return data;
    }

    // TODO: Write functions to generate synthetic data for different types of ports (modes, directions, protocols)
}
