package frontend.ast.config;

import frontend.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

/**
 *  The work node contains text containing a work that should be emphasised in the document.
 *  An example use case for this could be referencing other works in titles.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@ToString
public class Work extends Node {

    /**
     *  The work that should be referenced
     */
    @Getter
    private final String emphasisedWork;

    public Work(@NonNull final String emphasisedWork) {
        this.emphasisedWork = emphasisedWork + " ";
    }

    /**
     *  The Work node does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
