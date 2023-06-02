package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Numeration extends Node {
    private String numerationType;
    private String position;
    private String margin;
    private String skippedPages;

    @Override
    public String toString() {
        return "\nNumeration{" +
                "numerationType='" + numerationType + '\'' +
                ", position='" + position + '\'' +
                ", margin='" + margin + '\'' +
                ", skippedPages='" + skippedPages + '\'' +
                '}';
    }
}
