package frontend.lexical_analysis;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *  Creates a token used by the scanner to divide input into tokens.
 *  Consists of a {@link TokenType} and a token value.
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Token {

    /**
     *  The constant type of the token used by the parser to parse input
     */
    public TokenType type;

    /**
     *  The value of the token.
     *  Depending on the token type this may be the name of the keyword, the content of a text, etc.
     */
    public String value;

}
