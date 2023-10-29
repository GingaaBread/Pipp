package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *  The endnotes node represents the endnotes style configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
@ToString
public class Endnotes extends Node {

    /**
     *  The structure, which needs to appear in the document before the endnotes structure does.
     *  For example, the endnotes structure could only be allowed to appear after the bibliography structure.
     *  If no structure needs to appear first, this is null.
     */
    private String allowBeforeStructure;

    /**
     *  The endnotes node does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
