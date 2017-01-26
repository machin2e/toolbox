package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.List;

public class PortConfigurationConstraint {
    // TODO: Consider renaming to PortConfigurationConstraint for general use in constructs?

    // TODO: Current Mode: [ AC, DC ]

    // VariableStateSet = [ none, digital, analog, power, pwm, ... ]
    // Variable variable = new Variable(VariableState("digital"), VariableStateSet);

    // <VARIABLES>
    // TODO: Replace with Class + searchable manager
    // Per-Port Modes
    public enum Mode {

        NONE,
        DIGITAL,
        ANALOG,
        POWER, // Level (COMMON/0V, TTL/3.3V, CMOS/5V)

        PWM, // Period (time unit); Duty-Cycle ("on" ratio per period)

        RESISTIVE_TOUCH,

        // I2C Bus
        I2C_SDA,
        I2C_SCL,

        // SPI Bus
        SPI_SCLK,
        SPI_MOSI,
        SPI_MISO,
        SPI_SS,

        // UART Bus
        UART_RX,
        UART_TX

    }

    // TODO: Replace with Class + searchable manager
    public enum Direction {
        NONE,
        INPUT,
        OUTPUT,
        BIDIRECTIONAL // Supporting bidirectional variables is different than supporting both the input and output configurations because in the latter case, only one can be specified.
    }

    // TODO: Replace with Class + searchable manager
    public enum Voltage {
        NONE,
        TTL, // 3.3V
        CMOS, // 5V
        COMMON // 0V
    }
    // </VARIABLES>

    // "generate iasm device a device b" (or ask/generate after adding path)

    // <CONFIGURATION_STATE_ASSIGNMENT_CONSTRAINTS>
    // TODO: public ValueSet<Mode> modes = null;
    public Mode mode = null;

    /**
     * {@code directions} is set to {@code null} when it is not applicable to the {@code PortConfigurationConstraint}. This is
     * distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
//    public ValueSet<Direction> directions = null;
    public ValueSet directions = null;


    /**
     * {@code voltages} is set to {@code null} when assigning the voltage is not applicable (like with
     * {@code directions}). This is distinct from creating a {@code ValueSet} with no elements (i.e., an empty set).
     */
//    public ValueSet<Voltage> voltages = null;
    public ValueSet voltages = null;
    // </CONFIGURATION_STATE_ASSIGNMENT_CONSTRAINTS>

    // TODO: Bus dependencies (device-device interface level)

    // TODO: PortConfigurationConstraint(State... values)/(variable.values.get(<title>), ...)
//    public PortConfigurationConstraint(Mode mode, ValueSet<Direction> directions, ValueSet<Voltage> voltages) {
    public PortConfigurationConstraint(Mode mode, ValueSet directions, ValueSet voltages) {

        this.mode = mode;
        this.directions = directions;
        this.voltages = voltages;

    }

    // TODO: discoverCompatiblePortSet(<device-a>, <device-b>)

    /**
     * Computes/resolves the pair of "minimal compatible" {@code PortConfigurationConstraint}s
     * given a pair of two {@code PortConfigurationConstraint}s. The resulting {@code PortConfigurationConstraint}s
     * will remove values that cannot be assigned to variables in the {@code PortConfigurationConstraint}.
     *
     * @param sourceConstraint
     * @param targetConstraint
     * @return
     */
    public static List<Constraint> computeCompatibleConfigurationSet(Constraint sourceConstraint, Constraint targetConstraint) {

        // <VERBOSE_LOG>
        // Source Port
        boolean ENABLE_VERBOSE_OUTPUT = false;
//        if (ENABLE_VERBOSE_OUTPUT) {
//
//            System.out.print("? (source) " + sourceConfiguration.mode + ";");
//
//            if (sourceConfiguration.directions == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < sourceConfiguration.directions.values.size(); i++) {
//                    System.out.print("" + sourceConfiguration.directions.values.get(i));
//                    if ((i + 1) < sourceConfiguration.directions.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//            System.out.print(";");
//
//            if (sourceConfiguration.voltages == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < sourceConfiguration.voltages.values.size(); i++) {
//                    System.out.print("" + sourceConfiguration.voltages.values.get(i));
//                    if ((i + 1) < sourceConfiguration.voltages.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//            System.out.println();
//
//            // Target Port(s)
//            System.out.print("  (target) " + targetConfiguration.mode + ";");
//
//            if (targetConfiguration.directions == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < targetConfiguration.directions.values.size(); i++) {
//                    System.out.print("" + targetConfiguration.directions.values.get(i));
//                    if ((i + 1) < targetConfiguration.directions.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//            System.out.print(";");
//
//            if (targetConfiguration.voltages == null) {
//                System.out.print("null");
//            } else {
//                for (int i = 0; i < targetConfiguration.voltages.values.size(); i++) {
//                    System.out.print("" + targetConfiguration.voltages.values.get(i));
//                    if ((i + 1) < targetConfiguration.voltages.values.size()) {
//                        System.out.print(",");
//                    }
//                }
//            }
//        }
        // </VERBOSE_LOG>

        // <INITIALIZE_CONSTRAINTS>
//        PortConfigurationConstraint compatibleSourcePortConfigurationConstraint = new PortConfigurationConstraint(null, null, null); // populate with valid subset given variables constraints
//        PortConfigurationConstraint compatibleTargetPortConfigurationConstraint = new PortConfigurationConstraint(null, null, null); // populate with valid subset given variables constraints
        Constraint compatibleSourceConstraint = new Constraint(); // populate with valid subset given variables constraints
        Constraint compatibleTargetConstraint = new Constraint(); // populate with valid subset given variables constraints

        // Add source port Constraint with empty variable state sets
        for (int i = 0; i < sourceConstraint.variableValueSets.size(); i++) {
            String variableTitle = sourceConstraint.variableValueSets.get(i).title;
            compatibleSourceConstraint.variableValueSets.add(new VariableValueSet(variableTitle));
        }

        // Add target port Constraint with empty variable state sets
        for (int i = 0; i < targetConstraint.variableValueSets.size(); i++) {
            String variableTitle = targetConstraint.variableValueSets.get(i).title;
            compatibleTargetConstraint.variableValueSets.add(new VariableValueSet(variableTitle));
        }
        // </INITIALIZE_CONSTRAINTS>

        // <MODE_CONSTRAINT_CHECKS>
//        if (sourceConstraint.mode == targetConstraint.mode) {
//            compatibleSourceConstraints.mode = sourceConstraint.mode;
//            compatibleTargetConstraints.mode = targetConstraint.mode;
//        }

        ValueSet sourceConstraintValues = Constraint.getValues(sourceConstraint, "mode");
        ValueSet targetConstraintValues = Constraint.getValues(targetConstraint, "mode");

        if (sourceConstraintValues.values.get(0).equals(targetConstraintValues.values.get(0))) {
            Constraint.getValues(compatibleSourceConstraint, "mode").values.add(sourceConstraintValues.values.get(0));
            Constraint.getValues(compatibleTargetConstraint, "mode").values.add(targetConstraintValues.values.get(0));
        }

        // TODO: I2C, SPI, UART, etc.

        // </MODE_CONSTRAINT_CHECKS>

        // TODO: compute the "compatible intersection" configurations of the mode constraints of the two configurations
        // TODO: compute the "compatible intersection" configurations of the directions constraints of the two configurations
        // TODO: compute the "compatible intersection" configurations of the voltages constraints of the two configurations

        //---

        // CHECK FOR THESE:
        // x TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" variables
        // x TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" variables
        // CHECK FOR THESE:
        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" variables
        // TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" variables
        // CHECK FOR THESE:
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(INPUT) // add to the "compatible intersection" variables
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(OUTPUT) // add to the "compatible intersection" variables
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
        // TODO: AddSourceDirection(INPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" variables

        // <DIRECTION_CONSTRAINT_CHECKS>
        sourceConstraintValues = Constraint.getValues(sourceConstraint, "direction");
        targetConstraintValues = Constraint.getValues(targetConstraint, "direction");

        if (sourceConstraintValues.values.contains("input") && targetConstraintValues.values.contains("output")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("input");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("output");

        }

        if (sourceConstraintValues.values.contains("output") && targetConstraintValues.values.contains("input")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("output");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("input");

        }

        if (sourceConstraintValues.values.contains("bidirectional") && targetConstraintValues.values.contains("input")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("bidirectional");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("input");

        }

        if (sourceConstraintValues.values.contains("input") && targetConstraintValues.values.contains("bidirectional")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("input");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("bidirectional");

        }

        if (sourceConstraintValues.values.contains("bidirectional") && targetConstraintValues.values.contains("output")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("bidirectional");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("output");

        }

        if (sourceConstraintValues.values.contains("output") && targetConstraintValues.values.contains("bidirectional")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("output");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("bidirectional");

        }

        if (sourceConstraintValues.values.contains("bidirectional") && targetConstraintValues.values.contains("bidirectional")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("bidirectional");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("bidirectional");

        }

//        if (sourceConstraint.directions == null || targetConstraint.directions == null) {
//
//            // TODO:
//
//        } else {
//
//            if (sourceConstraint.directions.values.contains(Direction.INPUT) && targetConstraint.directions.values.contains(Direction.OUTPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.INPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.OUTPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (1)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.OUTPUT) && targetConstraint.directions.values.contains(Direction.INPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.OUTPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.INPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (2)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.BIDIRECTIONAL) && targetConstraint.directions.values.contains(Direction.INPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetConstraints.directions.values.add(Direction.INPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.INPUT) && targetConstraint.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.INPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.BIDIRECTIONAL) && targetConstraint.directions.values.contains(Direction.OUTPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetConstraints.directions.values.add(Direction.OUTPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.OUTPUT) && targetConstraint.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.OUTPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.BIDIRECTIONAL) && targetConstraint.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//        }


        // Check direction compatibility
//        if (sourceConstraint.directions == null || targetConstraint.directions == null) {
//
//            // TODO:
//
//        } else {
//
//            if (sourceConstraint.directions.values.contains(Direction.INPUT) && targetConstraint.directions.values.contains(Direction.OUTPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.INPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.OUTPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (1)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.OUTPUT) && targetConstraint.directions.values.contains(Direction.INPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.OUTPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.INPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (2)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.BIDIRECTIONAL) && targetConstraint.directions.values.contains(Direction.INPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetConstraints.directions.values.add(Direction.INPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.INPUT) && targetConstraint.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.INPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.BIDIRECTIONAL) && targetConstraint.directions.values.contains(Direction.OUTPUT)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetConstraints.directions.values.add(Direction.OUTPUT);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.OUTPUT) && targetConstraint.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.OUTPUT);
//                compatibleTargetConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//            if (sourceConstraint.directions.values.contains(Direction.BIDIRECTIONAL) && targetConstraint.directions.values.contains(Direction.BIDIRECTIONAL)) {
//
//                compatibleSourceConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//                compatibleTargetConstraints.directions.values.add(Direction.BIDIRECTIONAL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (3)");
//                }
//            }
//
//        }
        // </DIRECTION_CONSTRAINT_CHECKS>

        // TODO: null, NONE

        // <VOLTAGE_CONSTRAINT_CHECKS>
        sourceConstraintValues = Constraint.getValues(sourceConstraint, "voltage");
        targetConstraintValues = Constraint.getValues(targetConstraint, "voltage");

        if (sourceConstraintValues.values.contains("ttl") && targetConstraintValues.values.contains("ttl")) {

            Constraint.getValues(compatibleSourceConstraint, "voltage").values.add("ttl");
            Constraint.getValues(compatibleTargetConstraint, "voltage").values.add("ttl");

        }

        if (sourceConstraintValues.values.contains("cmos") && targetConstraintValues.values.contains("cmos")) {

            Constraint.getValues(compatibleSourceConstraint, "voltage").values.add("cmos");
            Constraint.getValues(compatibleTargetConstraint, "voltage").values.add("cmos");

        }

        if (sourceConstraintValues.values.contains("common") && targetConstraintValues.values.contains("common")) {

            Constraint.getValues(compatibleSourceConstraint, "voltage").values.add("common");
            Constraint.getValues(compatibleTargetConstraint, "voltage").values.add("common");

        }

        //---

//        if (sourceConstraint.voltages == null || targetConstraint.voltages == null) {
//
//            // TODO:
//
//        } else {
//
//            // Check voltage compatibility
//            if (sourceConstraint.voltages.values.contains(Voltage.TTL) && targetConstraint.voltages.values.contains(Voltage.TTL)) {
//
//                compatibleSourceConstraints.voltages.values.add(Voltage.TTL);
//                compatibleTargetConstraints.voltages.values.add(Voltage.TTL);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (4)");
//                }
//            }
//
//            if (sourceConstraint.voltages.values.contains(Voltage.CMOS) && targetConstraint.voltages.values.contains(Voltage.CMOS)) {
//
//                compatibleSourceConstraints.voltages.values.add(Voltage.CMOS);
//                compatibleTargetConstraints.voltages.values.add(Voltage.CMOS);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (5)");
//                }
//            }
//
//            if (sourceConstraint.voltages.values.contains(Voltage.COMMON) && targetConstraint.voltages.values.contains(Voltage.COMMON)) {
//
//                compatibleSourceConstraints.voltages.values.add(Voltage.COMMON);
//                compatibleTargetConstraints.voltages.values.add(Voltage.COMMON);
//
//                if (ENABLE_VERBOSE_OUTPUT) {
//                    System.out.println("  > Match (6)");
//                }
//            }
//
//        }

        // TODO: COMMON - TTL
        // TODO: COMMON - CMOS

        // TODO: null, NONE
        // </VOLTAGE_CONSTRAINT_CHECKS>

        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.println("\n");
        }

        // TODO: Verify this logic for returning "null"
//        if ((compatibleSourceConstraints.mode == null || compatibleTargetConstraints.mode == null)
//                || (compatibleSourceConstraints.directions.values.size() == 0 || compatibleTargetConstraints.directions.values.size() == 0)
//                || (compatibleSourceConstraints.voltages.values.size() == 0 || compatibleTargetConstraints.voltages.values.size() == 0)) {
//            return null;
//        } else {
//            return new ValueSet<>(compatibleSourceConstraints, compatibleTargetConstraints);
//        }

        if ((Constraint.getValues(compatibleSourceConstraint, "mode").values.size() == 0 || Constraint.getValues(compatibleTargetConstraint, "mode").values.size() == 0)
                || (Constraint.getValues(compatibleSourceConstraint, "direction").values.size() == 0 || Constraint.getValues(compatibleTargetConstraint, "direction").values.size() == 0)
                || (Constraint.getValues(compatibleSourceConstraint, "voltage").values.size() == 0 || Constraint.getValues(compatibleTargetConstraint, "voltage").values.size() == 0)) {
            return null;
        } else {
            List<Constraint> compatibleConstraints = new ArrayList<>();
            compatibleConstraints.add(compatibleSourceConstraint);
            compatibleConstraints.add(compatibleTargetConstraint);
            return compatibleConstraints;
//            return new ValueSet<>(compatibleSourceConstraint, compatibleTargetConstraint);
        }
    }

    // TODO: rankCompatibleConfigurations(<compatible-variables-list>)

}
