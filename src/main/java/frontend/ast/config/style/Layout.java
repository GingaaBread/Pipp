package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The layout node determines the general layout properties of the document
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Layout extends Node {

    /**
     * The width of the document provided by the user.
     * The width needs to be a positive floating point number.
     */
    private String width;

    /**
     * The height of the document provided by the user.
     * The height needs to be a positive floating point number.
     */
    private String height;

    /**
     * The margin of elements to the four sides of the document provided by the user.
     * The margin needs to be a positive floating point number.
     */
    private String margin;

    /**
     * The spacing of lines in paragraphs and text components provided by the user.
     * Must be "1", "1.5" or "2".
     */
    private String spacing;

    /**
     * The layout node produces a warning if any field is blank
     */
    @Override
    public void checkForWarnings() {
        if (width != null && width.isBlank() ||
                height != null && height.isBlank() ||
                margin != null && margin.isBlank() ||
                spacing != null && spacing.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
