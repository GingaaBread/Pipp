package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains all configuration options of the emphasis structure
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class EmphasisStructure extends Node {

    /**
     * The font node contains information about a structure font used for that structure throughout the document.
     */
    private final Font font = new Font();

    /**
     * Determines if the user is allowed to emphasise sentences or not
     */
    private String allowEmphasis;

    /**
     * Produces an error if the allow emphasis field is blank
     */
    @Override
    public void checkForWarnings() {
        if (allowEmphasis != null && allowEmphasis.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

        font.checkForWarnings();
    }
}
