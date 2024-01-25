package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The paragraph node represents the structural paragraph style configuration
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Paragraph extends Node {

    /**
     * Determines the amount of space each paragraph is indented.
     */
    private String indentation;

    /**
     * The paragraph configuration throws an error if the indentation field is blank
     */
    @Override
    public void checkForWarnings() {
        if (indentation != null && indentation.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
