package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

/// TODO If date = null take current date. If date = None don't

/**
 *  The publication node represents the publication configuration
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
public class Publication extends Node {

    /**
     *  The title node contains the title of the publication.
     *  For example, this could be the name of a course.
     *  The title is its own node, instead of a string, because the user can use citations within the title.
     */
    private final Title title = new Title();

    /**
     *  The date of the publication describing when the document was published.
     *  If the date is left out, the date of document creation will be used automatically, unless
     *  the date is set to "None".
     */
    @Setter
    private String date;

    /**
     *  A textual representation of the publication node, which contains all formatted properties
     *
     * @return - the publication node as a string
     */
    @Override
    public String toString() {
        return "\n\tPublication{" +
                "title=" + title +
                ", date='" + date + '\'' +
                '}';
    }

    /**
     *  The publication node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        title.checkForWarnings();
    }

}
