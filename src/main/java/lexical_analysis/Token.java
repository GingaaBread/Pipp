package lexical_analysis;

import lombok.Data;

@Data
public class Token {
    public TokenType type;
    public String value;
}
