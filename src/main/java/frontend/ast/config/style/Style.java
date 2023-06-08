package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The style node contains information about how the document should be styled.
 *  This includes using predetermined style configurations from a style guide or letting the user override
 *  style configurations.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class Style extends Node {

    /**
     *  The layout node contains information about the general layout of the document.
     *  That includes the margin to the sides of the document, for example.
     */
    private final Layout layout = new Layout();

    /**
     *  The font node contains information about the main font used throughout the document.
     */
    private final Font font = new Font();

    /**
     *  The structures node contains information about how structures should be styled.
     */
    private final Structure structure = new Structure();

    /**
     *  The numeration node contains information about how page numbers should be styled.
     */
    private final Numeration numeration = new Numeration();

    /**
     *  The base style guide that should be used for all style configurations.
     *  The user can override any of the preconfigured values.
     */
    private String baseStyle;

    /**
     *  A textual representation of the style node, which contains all formatted properties
     *
     * @return - the properties of the style node as a string
     */
    @Override
    public String toString() {
        return "\n\tStyle{" +
                "baseStyle='" + baseStyle + '\'' +
                ", layout=" + layout +
                ", font=" + font +
                ", structure=" + structure +
                ", numeration=" + numeration +
                '}';
    }

    /**
     *  The style node does not produce warnings
     */
    @Override
    public void checkForWarnings() {
        layout.checkForWarnings();
        font.checkForWarnings();
        structure.checkForWarnings();
        numeration.checkForWarnings();
    }

}
