package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The layout node determines the general layout properties of the document
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class Layout extends Node {

    /**
     *  The width of the document provided by the user.
     *  The width needs to be a positive floating point number.
     */
    private String width;

    /**
     *  The height of the document provided by the user.
     *  The height needs to be a positive floating point number.
     */
    private String height;

    /**
     *  The margin of elements to the four sides of the document provided by the user.
     *  The margin needs to be a positive floating point number.
     */
    private String margin;

    /**
     *  The spacing of lines in paragraphs and text components provided by the user.
     *  Must be "1", "1.5" or "2".
     */
    private String spacing;

    /**
     *  A textual representation of the layout node, which contains all formatted properties
     *
     * @return - the properties of the layout node as a string
     */
    @Override
    public String toString() {
        return "\n\tLayout{" +
                "width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", margin='" + margin + '\'' +
                ", spacing='" + spacing + '\'' +
                '}';
    }

    /**
     *  The layout node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {}

}
