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

    /**
     *  The font node contains information about a structure font used for that structure throughout the document.
     */
    private final Font font = new Font();

    @Override
    protected void checkForWarnings() {
        font.checkForWarnings();
    }

}
