package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

public class CurrentDirectionConstraint {

    public List<PortConfiguration.Direction> directions = new ArrayList<>();

    public CurrentDirectionConstraint(PortConfiguration.Direction... directions) {

        for (int directionIndex = 0; directionIndex < directions.length; directionIndex++) {
            if (!this.directions.contains(directions[directionIndex])) {
                this.directions.add(directions[directionIndex]);
            }
        }

    }

}
