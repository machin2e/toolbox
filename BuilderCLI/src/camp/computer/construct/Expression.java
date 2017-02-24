package camp.computer.construct;

public class Expression {

    // Matches expressions identifying constructs such as "port(id: 34)" and reasonable
    // equivalents expressions.
    public static boolean isConstruct(String expression) {
        return expression.matches("([a-z]+)[ ]*\\([ ]*(id|uid|uuid)[ ]*:[ ]*[0-9]+[ ]*\\)");
    }

}
