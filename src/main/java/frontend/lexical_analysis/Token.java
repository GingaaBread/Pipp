package frontend.lexical_analysis;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    public TokenType type;
    public String value;
}
