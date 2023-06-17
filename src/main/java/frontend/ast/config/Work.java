package frontend.ast.config;

import frontend.ast.Node;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *  The work node contains text containing a work that should be emphasised in the document.
 *  An example use case for this could be referencing other works in titles.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@AllArgsConstructor
public class Work extends Node {

    /**
     *  The work that should be referenced
     */
    @Getter
    private String emphasisedWork;

    /**
     *  A textual representation of the "work" node, which contains the work that should be referenced
     *
     * @return - the "work" node as a string
     */
    @Override
    public String toString() {
        return "\n\t\tWork{" +
                "emphasisedWork=" + emphasisedWork +
                '}';
    }

    /**
     *  The Work node does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
