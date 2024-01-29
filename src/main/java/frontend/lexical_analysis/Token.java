package frontend.lexical_analysis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Creates a token used by the scanner to divide input into tokens.
 * Consists of a {@link TokenType} and a token value.
 *
 * @version 1.0
 * @since 1.0
 */
@ToString
@EqualsAndHashCode
public class Token {

    /**
     * Used to include debug information for debugging purposes and error messages
     */
    @Getter
    private final DebugTokenInfo debugInfo;

    /**
     * The constant type of the token used by the parser to parse input
     */
    public TokenType type;
    
    /**
     * The value of the token.
     * Depending on the token type this may be the name of the keyword, the content of a text, etc.
     */
    public String value;

    /**
     * Instantiates a new token using the desired token type and value and automatically creates an
     * empty debug info object.
     *
     * @param type  the type of the token (text, keyword, etc.)
     * @param value the content value of the token
     */
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
        debugInfo = new DebugTokenInfo();
    }

}
