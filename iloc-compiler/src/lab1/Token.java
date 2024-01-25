package src.lab1;

/**
 * Java class to represent tokens. A token is a representation of a 
 * word, characterized by a category (type) and its 
 * actual lexeme (word). Additionally, each token is associated with 
 * a line number.
 * 
 * @author Tyra Cole
 * 
 */
public class Token {

    // The category (or type) of the token (e.g., Keyword, Identifier, etc.)
    final Category category;

    // The actual word or text (lexeme) represented by this token
    final String lexeme;

    // The line number in the source where this token was found
    final int line;

    /**
     * Constructor for the Token class.
     * 
     * @param category The category or type of the token
     * @param lexeme   The actual word or text (lexeme) of the token
     * @param line     The line number where this token was found
     */
    Token(Category category, String lexeme, int line) {
        this.category = category;
        this.lexeme = lexeme;
        this.line = line;
    }

    /**
     * Provides a custom string representation of the Token object.
     * 
     * @return A string representing the token in the format:
     *         "Category Lexeme Line: lineNumber"
     */
    public String toString() {
        return category + " " + lexeme + " Line: " + line;
    }
}
