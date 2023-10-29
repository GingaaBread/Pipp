package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sentence extends Node {
    private String prefix;
    private String allowWhitespace;
    private String allowBoldText;
    private String allowItalicText;

    @Override
    protected void checkForWarnings() { }

}
