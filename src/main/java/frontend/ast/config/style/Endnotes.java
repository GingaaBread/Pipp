package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The endnotes node represents the endnotes style configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class Endnotes extends Node {

    /**
     *  The structure, which needs to appear in the document before the endnotes structure does.
     *  For example, the endnotes structure could only be allowed to appear after the bibliography structure.
     *  If no structure needs to appear first, this is null.
     */
    private String allowBeforeStructure;

    /**
     *  A textual representation of the "endnotes" node, which contains the formatted properties
     *
     * @return - the properties of the "endnotes" node as a string
     */
    @Override
    public String toString() {
        return "\n\tEndnotes{" +
                "allowBeforeStructure='" + allowBeforeStructure + '\'' +
                '}';
    }

    /**
     *  The endnotes node does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
