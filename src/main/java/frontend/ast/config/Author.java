package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The author node represents an author of the document
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Author extends Node {

    /**
     * The non-blank title of the author specified by the user (Prof., Dr., etc.)
     */
    private String title;

    /**
     * The non-blank name of the author specified by the user.
     * If used, the first and last name properties may not be used as the name property is used to
     * generate these automatically
     */
    private String name;

    /**
     * The non-blank first name of the author specified by the user.
     * If used, the last name property must be used, and the name property may not be used.
     */
    private String firstname;

    /**
     * The non-blank last name of the author specified by the user.
     * If used, the first name property must be used, and the name property may not be used.
     */
    private String lastname;

    /**
     * The non-blank identification number/string specified by the user.
     * This can be used to identify the author via the use of an ID field
     */
    private String id;

    /**
     * The author node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
    }

}
