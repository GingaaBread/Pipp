package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmphasisStructure extends Node {

    /**
     *  The font node contains information about a structure font used for that structure throughout the document.
     */
    private final Font font = new Font();

    /**
     *  Determines if the user is allowed to emphasise sentences or not
     */
    private String allowEmphasis;

    @Override
    protected void checkForWarnings() {
        font.checkForWarnings();
    }
}
