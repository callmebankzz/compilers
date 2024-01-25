package src.lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static src.lab1.Category.*; // allows me to refer to static variables without prefixing them with Category

/**
 * Representation of a parser. The Parser class is responsible for translating a 
 * list of tokens into a set of executable operations. This parser follows ILOC 
 * grammar rules to interpret tokens as operations and subsequently builds a list
 * of these operations for later execution. Operations can vary in their complexity, 
 * from zero operand operations to operations with up to three operands.
 * 
 * The parser is designed to capture syntactic and certain semantic errors. If the 
 * parser encounters an error, the hasError attribute is set to true and an error 
 * message is reported.
 * 
 * @author Tyra Cole
 * 
 */
class Parser {
     private final List<Token> tokens;  // The list of tokens to be parsed.
    private final List<Operations> opList;  // List of parsed operations.
    private int current = 0;  // Current position in the token list.
    private boolean hasError = false;  // Flag to indicate if any parsing errors occurred.
    private Token currTokenLine;

    /**
     * Constructs a new parser with the given list of tokens.
     *
     * @param tokens The list of tokens to be parsed.
     */
    Parser(List<Token> tokens) {
        this.tokens = tokens;
        //System.out.println("Token list: " + tokens);
        opList = new ArrayList<>();
    }

    /**
     * @return true if a parsing error was encountered, false otherwise.
     */
    boolean hasError() {
        return hasError;
    }

    /**
     * Parses the provided tokens into operations.
     *
     * @return The list of parsed operations.
     */
    List<Operations> parse() {
        // Continue parsing until all tokens are processed or an end-of-file token is encountered.
        while (!isAtEnd()) {
            currTokenLine = peek();
            //System.out.println("CurrTokenLine: " + currTokenLine.category);
            switch (peek().category) {
                // If the token represents an arithmetic operation, parse it as such.
                case ADD:
                case SUB:
                case MULT:
                case RSHIFT:
                case LSHIFT:
                    addInstruction(arithmetic_op());
                    break;

                // If the token represents a load or store operation, parse it accordingly.
                case LOAD:
                case STORE:
                    addInstruction(load_store_op());
                    break;

                // If the token represents a LOADI operation, parse it.
                case LOADI:
                    //.println("This is where loadi_op() should be called.");
                    addInstruction(loadi_op());
                    break;

                // If the token represents an OUTPUT operation, parse it.
                case OUTPUT:
                    addInstruction(output_op());
                    break;

                // If the token represents a NOP (no operation), parse it.
                case NOP:
                    addInstruction(nop());
                    break;

                // If the token doesn't match any of the expected operation types, report an error.
                default:
                    reportError("Unexpected token");
                    break;
            }
        }

        // Return the list of successfully parsed operations.
        return opList;
    }


    /**
     * Parses arithmetic operations.
     *
     * This method expects a sequence of tokens that match the structure of an arithmetic operation. 
     * Typically, this is: an operator, followed by two source registers, an arrow token, 
     * and then a destination register.
     *
     * @return The parsed arithmetic operation or null if there's a parsing error.
     */
    private Operations arithmetic_op() {
        // Capture the arithmetic operator token (e.g., ADD, SUB, etc.)
        Token operator = advance();

        // Expect and capture the first source register token.
        Token first = consumeToken(REGISTER, "Expected source register 1 but found " + peek().lexeme);

        // Expect a comma token separating the source registers.
        consumeToken(COMMA, "Expected a comma after register name");

        // Expect and capture the second source register token.
        Token second = consumeToken(REGISTER, "Expected source register 2 but found " + peek().lexeme);

        // Expect the arrow token (=>) that precedes the destination register.
        consumeToken(ARROW, "Expected => after register name");

        // Expect and capture the destination register token.
        Token third = consumeToken(REGISTER, "Expected destination register but found " + peek().lexeme);

        // If any of the captured tokens are error tokens, return null.
        if (first.category == ERROR || second.category == ERROR || third.category == ERROR) return null;

        // Return a new instance of the ThreeOp operation using the captured tokens.
        return new Operations.ThreeOp(operator, first, second, third);
    }


    /**
     * Parses NOP (No Operation) operations.
     * 
     * The NOP operation is a stand-alone operation that does not expect any other token 
     * to follow. After reading the NOP operator, the method creates and returns an operation 
     * that takes zero operands.
     *
     * @return The parsed NOP operation.
     */
    private Operations nop() {
        // Read the NOP operator token.
        Token operator = advance();
        
        // Return a new instance of the ZeroOp operation using the NOP operator.
        return new Operations.ZeroOp(operator);
    }


    /**
     * Parses load/store operations.
     *
     * The expected format for these operations is: 
     * OPERATOR REGISTER => REGISTER. 
     * The method starts by reading the operator, then expects to find two REGISTER tokens
     * separated by an ARROW. If any token is not as expected, an error is reported.
     *
     * @return The parsed load/store operation or null if there's a parsing error.
     */
    private Operations load_store_op() {
        // Read the operator token (e.g., LOAD or STORE).
        Token operator = advance();
        
        // Expect and capture the source register token.
        Token first = consumeToken(REGISTER, "Expected source register but found " + peek().lexeme);
        
        // Expect the arrow token (=>) that separates source and destination registers.
        consumeToken(ARROW, "Expected => after register name");
        
        // Expect and capture the destination register token.
        Token second = consumeToken(REGISTER, "Expected destination register but found " + peek().lexeme);
        
        // If any of the captured tokens are error tokens, return null to indicate a parsing error.
        if (first.category == ERROR || second.category == ERROR) return null;
        
        // Return a new instance of the TwoOp operation using the captured tokens.
        return new Operations.TwoOp(operator, first, second);
    }

    /**
     * Parses LOADI operations.
     * 
     * The expected format for LOADI operations is:
     * LOADI NUMBER => REGISTER.
     * The method starts by reading the LOADI operator, then expects to find a NUMBER token
     * followed by an ARROW and then a REGISTER token. If any token is not as expected,
     * an error is reported.
     *
     * @return The parsed LOADI operation or null if there's a parsing error.
     */
    private Operations loadi_op() {
        // Read the LOADI operator token.
        Token operator = advance();
        
        // Expect and capture the number token which represents the value to be loaded.
        Token first = consumeToken(NUMBER, "Expected number but found " + peek().lexeme);
        
        // Expect the arrow token (=>) that separates the value and the destination register.
        consumeToken(ARROW, "Expected => after number value");
        
        // Expect and capture the destination register token where the value will be loaded into.
        Token second = consumeToken(REGISTER, "Expected destination register but found" + peek().lexeme);
        
        // If any of the captured tokens are error tokens, return null to indicate a parsing error.
        if (first.category == ERROR || second.category == ERROR) return null;
        
        // Return a new instance of the TwoOp operation using the captured tokens.
        return new Operations.TwoOp(operator, first, second);
    }

    /**
     * Parses the OUTPUT operation.
     * 
     * The expected format for OUTPUT operations is:
     * OUTPUT NUMBER.
     * The method starts by reading the OUTPUT operator, then expects to find a NUMBER token
     * which represents the value to be output. If the token is not as expected, an error is reported.
     *
     * @return The parsed OUTPUT operation or null if there's a parsing error.
     */
    private Operations output_op() {
        // Read the OUTPUT operator token.
        Token operator = advance();
        
        // Expect and capture the number token which represents the value to be output.
        Token first = consumeToken(NUMBER, "Expected number but found " + peek().lexeme);
        
        // If the captured number token is an error token, return null to indicate a parsing error.
        if (first.category == ERROR) return null;
        
        // Return a new instance of the OneOp operation using the captured tokens.
        return new Operations.OneOp(operator, first);
    }

    /**
     * Advances to the next token in the list if not at the end.
     * 
     * @return The previous token.
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * Consumes and returns the next token if it matches the expected type. 
     * Otherwise, reports an error.
     * 
     * @param type The expected token type.
     * @param msg The error message to report if the token type doesn't match.
     * @return The token consumed or an error token.
     */
    private Token consumeToken(Category type, String msg) {
        //System.out.println(type);
        if (checkType(type)) return advance();
        reportError(msg);
        return new Token(ERROR, peek().lexeme, peek().line);
    }

    /**
     * Checks if the type of the next token matches the specified type.
     *
     * @param type The expected type of the next token.
     * @return true if the next token's type matches the specified type, false otherwise.
     */
    private boolean checkType(Category type) {
        if (isAtEnd()) return false;
        //System.out.println("Lexeme: " + peek().lexeme);
        //System.out.println("Peek Category: " + peek().category + " Current type: " + type);
        return peek().category == type;
    }

    /**
     * Determines if the parser has reached the end of file.
     *
     * @return true if the current token is of type EOF (End of File), false otherwise.
     */
    private boolean isAtEnd() {
        return peek().category == EOF;
    }

    /**
     * Retrieves the token at the current position without advancing the current position.
     * 
     * @return The token at the current position.
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Retrieves the token immediately before the current position.
     * 
     * @return The token before the current position.
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

    /**
     * Adds a parsed operation to the list of operations if it's valid.
     * 
     * @param op The parsed operation to be added.
     */
    private void addInstruction(Operations op) {
        if (op != null) opList.add(op);
    }

    /**
     * Skips tokens until the next line is reached or until the end of the tokens list.
     */
    private void skipToNextLine() {
        int errorLine = currTokenLine.line; // Current line where the error occurred.

        while (!isAtEnd() && peek().line == errorLine) {
            advance();
        }
    }

    /**
     * Flags that a parsing error has occurred, reports the error, and then attempts 
     * to continue parsing by advancing to the next token.
     * 
     * @param msg The error message to be reported.
     */
    private void reportError(String msg) {
        hasError = true;
        Main.error(currTokenLine, msg);
        skipToNextLine();
    }
}