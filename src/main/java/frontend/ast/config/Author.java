package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/**
 *  The author node represents an author of the document
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@Setter
public class Author extends Node {

    /**
     *  The non-blank name of the author specified by the user.
     *  If used, the first and last name properties may not be used as the name property is used to
     *  generate these automatically
     */
    private String name;

    /**
     *  The non-blank first name of the author specified by the user.
     *  If used, the last name property must be used, and the name property may not be used.
     */
    private String firstname;

    /**
     *  The non-blank last name of the author specified by the user.
     *  If used, the first name property must be used, and the name property may not be used.
     */
    private String lastname;

    /**
     *  The non-blank identification number/string specified by the user.
     *  This can be used to identify the author via the use of an ID field
     */
    private String id;

    /**
     *  The textual representation of the author node, which contains the properties of the node
     *  separated visually
     *
     * @return - the properties of the author node as a string
     */
    @Override
    public String toString() {
        return "\n\tAuthor{" +
                "name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    /**
     *  The author node does not produce warnings
     */
    @Override
    protected void checkForWarnings() { }

}
