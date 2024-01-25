package src.lab1;

/**
 * This is an enumeration of the types of words that can be found
 * in the ILOC language.
 */
public enum Category {
    COMMA,
    ARROW,
    REGISTER, NUMBER,
    LOAD, LOADI, STORE, LSHIFT, RSHIFT,
    ADD, MULT, SUB, OUTPUT, NOP,
    ERROR, EOF
}