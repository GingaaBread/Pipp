package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sentence extends Node {
    private String prefix;
    private String allowWhitespace;
    private String allowBoldText;
    private String allowItalicText;

    @Override
    public String toString() {
        return "\n\tSentence{" +
                "prefix='" + prefix + '\'' +
                ", allowWhitespace='" + allowWhitespace + '\'' +
                ", allowBoldText='" + allowBoldText + '\'' +
                ", allowItalicText='" + allowItalicText + '\'' +
                '}';
    }

    @Override
    protected void checkForWarnings() {

    }

}
