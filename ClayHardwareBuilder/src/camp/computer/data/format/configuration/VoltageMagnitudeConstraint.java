package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

public class VoltageMagnitudeConstraint {

    public List<PortConfigurationConstraint.Voltage> voltages = new ArrayList<>();

    public VoltageMagnitudeConstraint(PortConfigurationConstraint.Voltage... voltages) {

        for (int voltageIndex = 0; voltageIndex < voltages.length; voltageIndex++) {
            if (!this.voltages.contains(voltages[voltageIndex])) {
                this.voltages.add(voltages[voltageIndex]);
            }
        }

    }

}
