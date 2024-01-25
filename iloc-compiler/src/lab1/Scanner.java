package src.lab1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static src.lab1.Category.*; // allows me to refer to static variables without prefixing them with Category

/**
 * Representation of a scanner. The Scanner class is responsible for taking a 
 * source string and turning it into a list of Tokens, which are <Category, lexeme> 
 * pairings representing words of the ILOC language.
 * 
 * @author Tyra Cole
 * 
 */
public class Scanner {

    // A static map to hold the keyword to Category associations.
    private static final Map<String, Category> keywords;
    static {
        keywords = new HashMap<>();
        keywords.put("load", LOAD);
        keywords.put("loadI", LOADI);
        keywords.put("store", STORE);
        keywords.put("add", ADD);
        keywords.put("sub", SUB);
        keywords.put("mult", MULT);
        keywords.put("lshift", LSHIFT);
        keywords.put("rshift", RSHIFT);
        keywords.put("output", OUTPUT);
        keywords.put("nop", NOP);
    }

    // The string of the source file that is being read.
    private final String source;
    // List to hold the tokens as they are scanned.
    private final List<Token> tokens = new ArrayList<>();
    // Markers to keep track of the portion of the source string being examined.
    private int start = 0;
    private int curr = 0;
    // Line counter.
    private int line = 1;

    /**
     * Constructor that initializes the Scanner with source string, source.  
     */ 
    Scanner(String source) {
        this.source = source;
    }

    /**
     * Creates a new Token and adds it to the list of Tokens created, tokens.
     */
    List<Token> addTokens() {
        while (!isAtEnd()) {
            start = curr;
            charToToken();
        }
        tokens.add(new Token(EOF, "", line));
        return tokens;
    }

    /**
     * Skips all characters in the source until reaching the end of the current line or the end of the file.
     * This method is useful for gracefully recovering from an error and skipping the rest of a line to
     * prevent multiple error messages for a single erroneous line.
     */
    private void skipToNextLine() {
        // Keep advancing characters while we're not at the end of the file
        // and haven't yet encountered a newline character.
        while (!isAtEnd() && peek() != '\n') {
            advance();
        }
        
        // If we're not at the end of the file, then we're currently positioned
        // on a newline character. Move past it and increment the line counter.
        if (!isAtEnd()) {
            advance();  // Move past the newline character.
            line++;     // Increment the line counter to track our new line position.
        }
    }


    /**
     * Transforms the current character from the source code into its corresponding token.
     * It recognizes various characters and sequences to classify them into token categories.
     */
    private void charToToken() {
        // Advances to the next character in the source and assigns it to 'c'
        char c = advance();

        // Use a switch statement to check the character and determine its Category.
        switch (c) {
            case ',':
                // If the character is a comma, add a COMMA token
                createToken(COMMA);
                break;
            case '=':
                // If the character is an '=', check if the next character is '>'
                if (isMatch('>')) createToken(ARROW);  // If so, it's an ARROW token
                else {
                    Main.error(line, "Expected > but found " + peek());  // Otherwise, raise an error expecting '>'
                    skipToNextLine();
                }
                break;
            case '/':
                // If the character is a '/', check if the next character is also '/'
                if (isMatch('/')) {
                    // If it's a comment starting with '//', skip till the end of the line
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    // If it's not a recognized sequence, raise an error
                    Main.error(line, "Unexpected character");
                    skipToNextLine();
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace characters (spaces, carriage returns, tabs)
                break;
            case '\n':
                // For newline characters, increment the line count
                line++;
                break;
            default:
                // If the character isn't one of the above
                if (isDigit(c)) {
                    // Process numbers if the character is a digit
                    number();
                } else if (isAlpha(c)) {
                    // Process identifiers or keywords if the character is alphabetical
                    identifier();
                } else {
                    // If it's neither a digit nor an alphabetical character, raise an error
                    Main.error(line, "Unexpected character");
                    skipToNextLine();
                }
                break;
        }
    }


    /** 
     * Checks if the current character is alphabetic
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

    /** 
     * Identifies words and classifies which Category they belong to.
     */ 
    private void identifier() {
        // Checks to see if the next character is also alphanumeric
        // If so, advance the current pointer to the next character
        // Continue this until you reach the end of alphanumeric characters in this sequence
        while (isAlphaNumeric(peek())) advance();
        // Make a string of all the alphanumeric characters found above
        String text = source.substring(start, curr);
        // Find the Category of the word. Default is REGISTER.
        Category category = keywords.getOrDefault(text, REGISTER);
        // REGISTER tokens must begin with character 'r'. Return ERROR if not.
        if (category == REGISTER && text.charAt(0) != 'r') {
            Main.error(line, "Register expected");
            skipToNextLine();
            return;
        }
        // Create a Token for word of Category type found.
        createToken(category);
    }

    /**
     * Checks if a character is alphanumeric.
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Checks that a character is a number.
     */
    private void number() {
        while (isDigit(peek())) advance();
        createToken(NUMBER);
    }

    /**
     * Checks that a character is a digit (number between 0 and 9, inclusive).
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Takes a peek at the next character without moving the pointer at the current character.
     */
    private char peek() {
        if (isAtEnd()) return 0;
        return source.charAt(curr);
    }

    /**
     * Checks to see if the curr character is equal to c.
     */
    private boolean isMatch(char c) {
        if (isAtEnd()) return false;
        if (source.charAt(curr) != c) return false;
        curr++;
        return true;
    }

    /**
     * Creates a new token and adds it to a list of Token objects,
     */
    private void createToken(Category category) {
        String text = source.substring(start, curr);
        tokens.add(new Token(category, text, line));
    }

    /**
     * Moves the current pointer to the next character.
     */
    private char advance() {
        curr++;
        return source.charAt(curr - 1);
    }

    /**
     * Checks to see if at end of file.
     */
    private boolean isAtEnd() {
        return curr >= source.length();
    }
}