package frontend.lexical_analysis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    public TokenType type;
    public String value;

    public String getErrorMessage() {
        return type == TokenType.KEYWORD ? value : type.toString();
    }
}
