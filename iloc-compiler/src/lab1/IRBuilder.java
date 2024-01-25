package src.lab1;

/**
 * Builds the intermediate representation. The IRBuilder class implements the Visitor pattern 
 * for operations and provides functionality to format the operations into a specific string 
 * representation.
 * 
 * @author Tyra Cole
 */
public class IRBuilder implements Operations.Visitor<String> {

    /**
     * Invokes the appropriate visit method on the given operation.
     *
     * @param expr The operation to be formatted.
     * @return The formatted string representation of the operation.
     */
    String print(Operations expr) {
        return expr.accept(this);
    }

    /**
     * Formats a zero-operand operation.
     *
     * @param op The operation token.
     * @return The formatted string representation.
     */
    @Override
    public String visitZeroOp(Token op) {
        return formatOperation(op.lexeme);
    }

    /**
     * Formats a single-operand operation.
     *
     * @param op    The operation token.
     * @param first The operand token.
     * @return The formatted string representation.
     */
    @Override
    public String visitOneOp(Token op, Token first) {
        return formatOperation(op.lexeme, first);
    }

    /**
     * Formats a two-operand operation.
     *
     * @param op     The operation token.
     * @param first  The first operand token.
     * @param second The second operand token.
     * @return The formatted string representation.
     */
    @Override
    public String visitTwoOp(Token op, Token first, Token second) {
        return formatOperation(op.lexeme, first, second);
    }

    /**
     * Formats a three-operand operation.
     *
     * @param op     The operation token.
     * @param first  The first operand token.
     * @param second The second operand token.
     * @param third  The third operand token.
     * @return The formatted string representation.
     */
    @Override
    public String visitThreeOp(Token op, Token first, Token second, Token third) {
        return formatOperation(op.lexeme, first, second, third);
    }

    /**
     * Formats an operation and its operands into a specific string representation.
     * 
     * Given the operation name and a varying number of operands, this method 
     * will create a string representation of the format:
     * [name {type1 lexeme1} {type2 lexeme2} ...]
     *
     * @param name The name of the operation.
     * @param ops  The varargs of tokens representing operands (can be zero or more).
     * @return The formatted string representation.
     */
    private String formatOperation(String name, Token... ops) { 
        StringBuilder builder = new StringBuilder();

        builder.append("[").append(name);
        
        // Iterating through each operand token and appending to the string.
        for (Token op : ops) {
            builder.append(" {")
                    .append(op.category)
                    .append(" ")
                    .append(op.lexeme)
                    .append("}");
        }
        builder.append("]");

        return builder.toString();
    }
}