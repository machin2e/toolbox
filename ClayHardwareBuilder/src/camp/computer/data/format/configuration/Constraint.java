package camp.computer.data.format.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import camp.computer.util.Pair;

/**
 * Constrains the values (or values) that can be assigned <em>simultaneously</em> to each of the
 * constraints <em>in combination with each other</em> uniquely identified by each
 * {@code variableConstraint[i].title} to the values (or values) specified in {@code variableConstraint[i].values}.
 * <p>
 * Therefore {@code Constraint} specifies a constraint on the valid mutual state assignments among
 * the specified constraints.
 * <p>
 * In other words, this constrains the set of valid <em>combinitoric value permutations</em> that
 * can be assigned to constraints at the same time.
 */
public class Constraint {

    // [ (Variable, CompatibleStateSubset), (Variable, CompatibleStateSubset), ..., (Variable, CompatibleStateSubset) ]

    //    public List<VariableValueSet> variableValueSets = new ArrayList<>();
    public HashMap<String, ValueSet> variables = new HashMap<>();

    // TODO: PortConfigurationConstraint(State... values)/(variable.values.get(<title>), ...)
    //public Constraint(Mode mode, ValueSet<Direction> directions, ValueSet<Voltage> voltages) {
    public Constraint(Pair<String, ValueSet>... variableValueSets) {

        for (int variableIndex = 0; variableIndex < variableValueSets.length; variableIndex++) {
//            this.variableValueSets.add(variableValueSets[variableIndex]);
            this.variables.put(variableValueSets[variableIndex].key, variableValueSets[variableIndex].value);
        }

    }

    public Constraint(List<VariableValueSet> variableValueSets) {

        for (int variableIndex = 0; variableIndex < variableValueSets.size(); variableIndex++) {
//            this.variableValueSets.add(variableValueSets.get(variableIndex));
            this.variables.put(variableValueSets.get(variableIndex).title, variableValueSets.get(variableIndex).values);
        }

    }

    public static ValueSet getValues(Constraint constraint, String variableTitle) {

//        for (int i = 0; i < constraint.variableValueSets.size(); i++) {
//            if (constraint.variableValueSets.get(i).title.equals(variableTitle)) {
//                return constraint.variableValueSets.get(i).values;
//            }
//        }

        for (String currentVariableTitle : constraint.variables.keySet()) {
            if (currentVariableTitle.equals(variableTitle)) {
                return constraint.variables.get(currentVariableTitle);
            }
        }

        return null;
    }

    public ValueSet getValues(String variableTitle) {
//        for (int i = 0; i < variableValueSets.size(); i++) {
//            if (variableValueSets.get(i).title.equals(variableTitle)) {
//                return variableValueSets.get(i).values;
//            }
//        }

        for (String currentVariableTitle : this.variables.keySet()) {
            if (currentVariableTitle.equals(variableTitle)) {
                return this.variables.get(currentVariableTitle);
            }
        }

        return null;
    }

    // TODO: discoverCompatiblePortSet(<device-a>, <device-b>)

    // TODO: computeCompatibleConfigurations/computeCompatibleConfigurationSets(<device-a>, <device-b>)

    /**
     * Computes/resolves the pair of "minimal compatible" {@code PortConfigurationConstraint}s
     * given a pair of two {@code PortConfigurationConstraint}s. The resulting {@code PortConfigurationConstraint}s
     * will remove values that cannot be assigned to constraints in the {@code PortConfigurationConstraint}.
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
        Constraint compatibleSourceConstraint = new Constraint(); // populate with valid subset given constraints constraints
        Constraint compatibleTargetConstraint = new Constraint(); // populate with valid subset given constraints constraints

        // Add source port Constraint with empty variable state sets
//        for (int i = 0; i < sourceConstraint.variableValueSets.size(); i++) {
//            String variableTitle = sourceConstraint.variableValueSets.get(i).title;
//            compatibleSourceConstraint.variableValueSets.add(new VariableValueSet(variableTitle));
//        }
        for (String variableTitle : sourceConstraint.variables.keySet()) {
            compatibleSourceConstraint.variables.put(variableTitle, new ValueSet());
        }

        // Add target port Constraint with empty variable state sets
//        for (int i = 0; i < targetConstraint.variableValueSets.size(); i++) {
//            String variableTitle = targetConstraint.variableValueSets.get(i).title;
//            compatibleTargetConstraint.variableValueSets.add(new VariableValueSet(variableTitle));
//        }
        for (String variableTitle : targetConstraint.variables.keySet()) {
            compatibleTargetConstraint.variables.put(variableTitle, new ValueSet());
        }
        // </INITIALIZE_CONSTRAINTS>

        // <MODE_CONSTRAINT_CHECKS>
        // <TODO>
        // TODO: Update to support multiple "mode" constraints.
//        ValueSet sourceConstraintValues = Constraint.getValues(sourceConstraint, "mode");
//        ValueSet targetConstraintValues = Constraint.getValues(targetConstraint, "mode");
        List<String> sourceConstraintValues = sourceConstraint.variables.get("mode").values;
        List<String> targetConstraintValues = targetConstraint.variables.get("mode").values;

//        if (sourceConstraintValues.values.get(0).equals(targetConstraintValues.values.get(0))) {
//            Constraint.getValues(compatibleSourceConstraint, "mode").values.add(sourceConstraintValues.values.get(0));
//            Constraint.getValues(compatibleTargetConstraint, "mode").values.add(targetConstraintValues.values.get(0));
//        }
        if (sourceConstraintValues.get(0).equals(targetConstraintValues.get(0))) {
//            Constraint.getValues(compatibleSourceConstraint, "mode").values.add(sourceConstraintValues.values.get(0));
//            Constraint.getValues(compatibleTargetConstraint, "mode").values.add(targetConstraintValues.values.get(0));
            compatibleSourceConstraint.variables.get("mode").values.add(sourceConstraintValues.get(0));
            compatibleTargetConstraint.variables.get("mode").values.add(targetConstraintValues.get(0));
        }
        // </TODO>

        // TODO: I2C, SPI, UART, etc.

        // </MODE_CONSTRAINT_CHECKS>

        // computes the "compatible intersection" configurations of the mode constraints of the two configurations

        //---

        // CHECK FOR THESE:
        // x TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" constraints
        // x TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" constraints
        // CHECK FOR THESE:
        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(INPUT) // add to the "compatible intersection" constraints
        // TODO: AddSourceDirection(INPUT); AddTargetDirection(OUTPUT) // add to the "compatible intersection" constraints
        // CHECK FOR THESE:
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(INPUT) // add to the "compatible intersection" constraints
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(OUTPUT) // add to the "compatible intersection" constraints
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" constraints
        // TODO: AddSourceDirection(INPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" constraints
        // TODO: AddSourceDirection(OUTPUT); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" constraints
        // TODO: AddSourceDirection(BIDIRECTIONAL); AddTargetDirection(BIDIRECTIONAL) // add to the "compatible intersection" constraints

        // <DIRECTION_CONSTRAINT_CHECKS>
//        sourceConstraintValues = Constraint.getValues(sourceConstraint, "direction");
//        targetConstraintValues = Constraint.getValues(targetConstraint, "direction");
        sourceConstraintValues = sourceConstraint.variables.get("direction").values;
        targetConstraintValues = targetConstraint.variables.get("direction").values;

        if (sourceConstraintValues.contains("input") && targetConstraintValues.contains("output")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("input");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("output");

        }

        if (sourceConstraintValues.contains("output") && targetConstraintValues.contains("input")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("output");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("input");

        }

        if (sourceConstraintValues.contains("bidirectional") && targetConstraintValues.contains("input")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("bidirectional");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("input");

        }

        if (sourceConstraintValues.contains("input") && targetConstraintValues.contains("bidirectional")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("input");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("bidirectional");

        }

        if (sourceConstraintValues.contains("bidirectional") && targetConstraintValues.contains("output")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("bidirectional");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("output");

        }

        if (sourceConstraintValues.contains("output") && targetConstraintValues.contains("bidirectional")) {

            Constraint.getValues(compatibleSourceConstraint, "direction").values.add("output");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("bidirectional");

        }

        if (sourceConstraintValues.contains("bidirectional") && targetConstraintValues.contains("bidirectional")) {

            compatibleSourceConstraint.getValues("direction").values.add("bidirectional");
            // Constraint.getValues(compatibleSourceConstraint, "direction").values.add("bidirectional");
            Constraint.getValues(compatibleTargetConstraint, "direction").values.add("bidirectional");

        }
        // </DIRECTION_CONSTRAINT_CHECKS>

        // TODO: null, NONE

        // <VOLTAGE_CONSTRAINT_CHECKS>
        sourceConstraintValues = sourceConstraint.variables.get("voltage").values;
        targetConstraintValues = targetConstraint.variables.get("voltage").values;

        if (sourceConstraintValues.contains("ttl") && targetConstraintValues.contains("ttl")) {

            Constraint.getValues(compatibleSourceConstraint, "voltage").values.add("ttl");
            Constraint.getValues(compatibleTargetConstraint, "voltage").values.add("ttl");

        }

        if (sourceConstraintValues.contains("cmos") && targetConstraintValues.contains("cmos")) {

            Constraint.getValues(compatibleSourceConstraint, "voltage").values.add("cmos");
            Constraint.getValues(compatibleTargetConstraint, "voltage").values.add("cmos");

        }

        if (sourceConstraintValues.contains("common") && targetConstraintValues.contains("common")) {

            Constraint.getValues(compatibleSourceConstraint, "voltage").values.add("common");
            Constraint.getValues(compatibleTargetConstraint, "voltage").values.add("common");

        }

        // TODO: COMMON - TTL
        // TODO: COMMON - CMOS

        // TODO: null, NONE
        // </VOLTAGE_CONSTRAINT_CHECKS>

        if (ENABLE_VERBOSE_OUTPUT) {
            System.out.println("\n");
        }

        // TODO: Verify this logic for returning "null"

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

    // TODO: rankCompatibleConfigurations(<compatible-constraints-list>)

}
