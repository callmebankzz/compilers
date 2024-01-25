package src.lab1;

/**
 * Abstract class representing various operations.
 * Operations uses the visitor pattern to allow different types of operations
 * to be handled differently based on their nature: zero operands (ZeroOp), one 
 * operand (OneOp), two operands (TwoOp), and three operands (ThreeOp).
 *
 * @author Tyra Cole
 * 
 */
abstract class Operations {
    /**
     * Abstract method to accept a visitor. Concrete implementations will 
     * delegate the task to the specific visit method on the visitor.
     *
     * @param <R> The return type of the visitor's methods.
     * @param visitor The visitor that wants to process this operation.
     * @return The result from processing this operation with the visitor.
     */
    abstract <R> R accept(Visitor<R> visitor);

    /**
     * The Visitor interface for operations.
     *
     * @param <R> The return type of the visit methods.
     */
    interface Visitor<R> {

        /**
         * Visit a ZeroOp.
         *
         * @param op The token representing the operation.
         * @return The result from processing a ZeroOp.
         */
        R visitZeroOp(Token op);

        /**
         * Visit a OneOp.
         *
         * @param op The token representing the operation.
         * @param first The first token operand.
         * @return The result from processing a OneOp.
         */
        R visitOneOp(Token op, Token first);

        /**
         * Visit a TwoOp.
         *
         * @param op The token representing the operation.
         * @param first The first token operand.
         * @param second The second token operand.
         * @return The result from processing a TwoOp.
         */
        R visitTwoOp(Token op, Token first, Token second);

        /**
         * Visit a ThreeOp.
         *
         * @param op The token representing the operation.
         * @param first The first token operand.
         * @param second The second token operand.
         * @param third The third token operand.
         * @return The result from processing a ThreeOp.
         */
        R visitThreeOp(Token op, Token first, Token second, Token third);
    }

    /**
     * Represents an operation with no operands.
     */
    static class ZeroOp extends Operations {
        final Token opcode;

        /**
         * Constructs a ZeroOp instance.
         *
         * @param opcode The token representing the operation.
         */
        ZeroOp(Token opcode) {
            this.opcode = opcode;
        }

        /**
         * Accepts a visitor to process this instance of a ZeroOp operation.
         * This method delegates the processing to the visitor's visitZeroOp method 
         * by passing in the opcode that was initialized during the construction of this ZeroOp instance.
         * 
         * @param <R> The return type of the visitor's method.
         * @param visitor The visitor instance that wishes to process this specific ZeroOp operation.
         * @return Returns the computed result after the visitor processes the operation 
         *         using the given opcode.
         */
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitZeroOp(this.opcode);
        }
    }

    /**
     * Represents an operation with one operand.
     */
    static class OneOp extends Operations {
        final Token opcode;
        final Token first;

        /**
         * Constructs a OneOp instance.
         *
         * @param opcode The token representing the operation.
         * @param first The first token operand.
         */
        OneOp(Token opcode, Token first) {
            this.opcode = opcode;
            this.first = first;
        }

        /**
         * Accepts a visitor to process this instance of a OneOp operation.
         * This method delegates the processing to the visitor's visitOneOp method 
         * by passing in the opcode and the one operand (first) 
         * that were initialized during the construction of this OneOp instance.
         * 
         * @param <R> The return type of the visitor's method.
         * @param visitor The visitor instance that wishes to process this specific OneOp operation.
         * @return Returns the computed result after the visitor processes the operation 
         *         with the given opcode and the one operand.
         */
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitOneOp(this.opcode, this.first);
        }
    }

    /**
     * Represents an operation with two operands.
     */
    static class TwoOp extends Operations {
        final Token opcode;
        final Token first;
        final Token second;

        /**
         * Constructs a TwoOp instance.
         *
         * @param opcode The token representing the operation.
         * @param first The first token operand.
         * @param second The second token operand.
         */
        TwoOp(Token opcode, Token first, Token second) {
            this.opcode = opcode;
            this.first = first;
            this.second = second;
        }

        /**
         * Accepts a visitor to process this instance of a TwoOp operation.
         * This method delegates the processing to the visitor's visitTwoOp method 
         * by passing in the opcode and the two operands (first, second) 
         * that were initialized during the construction of this TwoOp instance.
         * 
         * @param <R> The return type of the visitor's method.
         * @param visitor The visitor instance that wishes to process this specific TwoOp operation.
         * @return Returns the computed result after the visitor processes the operation 
         *         with the given opcode and the two operands.
         */
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitTwoOp(this.opcode, this.first, this.second);
        }
    }

    /**
     * Represents an operation with three operands.
     */
    static class ThreeOp extends Operations {
        final Token opcode;
        final Token first;
        final Token second;
        final Token third;

        /**
         * Constructs a ThreeOp instance.
         *
         * @param opcode The token representing the operation.
         * @param first The first token operand.
         * @param second The second token operand.
         * @param third The third token operand.
         */
        ThreeOp(Token opcode, Token first, Token second, Token third) {
            this.opcode = opcode;
            this.first = first;
            this.second = second;
            this.third = third;
        }

        /**
         * Accepts a visitor to process this instance of a ThreeOp operation.
         * This method delegates the processing to the visitor's visitThreeOp method 
         * by passing in the opcode and the three operands (first, second, third) 
         * that were initialized during the construction of this ThreeOp instance.
         * 
         * @param <R> The return type of the visitor's method.
         * @param visitor The visitor instance that wishes to process this specific ThreeOp operation.
         * @return Returns the computed result after the visitor processes the operation 
         *         with the given opcode and the three operands.
         */
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThreeOp(this.opcode, this.first, this.second, this.third);
        }
    }
}