package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// todo: check numeration type exists, position exists, margin is legal, skipped pages are legal
public class Numeration extends Node {
    private String numerationType;
    private String position;
    private String margin;
    private String skippedPages;

    @Override
    public String toString() {
        return "\n\tNumeration{" +
                "numerationType='" + numerationType + '\'' +
                ", position='" + position + '\'' +
                ", margin='" + margin + '\'' +
                ", skippedPages='" + skippedPages + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {

    }

    @Override
    protected void checkForErrors() {

    }
}
