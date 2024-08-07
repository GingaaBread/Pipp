package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@ToString
public class Style extends Node {

    /**
     *  The layout node contains information about the general layout of the document.
     *  That includes the margin to the sides of the document, for example.
     */
    private final Layout layout = new Layout();

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
     *  The style node does not produce warnings
     */
    @Override
    public void checkForWarnings() {
        layout.checkForWarnings();
        structure.checkForWarnings();
        numeration.checkForWarnings();
    }

}
