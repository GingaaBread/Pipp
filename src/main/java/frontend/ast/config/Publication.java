package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The publication node represents the publication configuration
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Publication extends Node {

    /**
     * The title node contains the title of the publication.
     * For example, this could be the name of a course.
     * The title is its own node, instead of a string, because the user can use citations within the title.
     */
    private final Title title = new Title();

    /**
     * The institution of the publication.
     * For example, the name of a university.
     */
    @Setter
    private String institution;

    /**
     * The name of the branch of the institution.
     * For example, "Computer Science".
     */
    @Setter
    private String chair;

    /**
     * The semester date used for some academic papers.
     * If used, this replaces the date.
     */
    @Setter
    private String semester;

    /**
     * The date of the publication describing when the document was published.
     * If the date is left out, the date of document creation will be used automatically, unless
     * the date is set to "None".
     */
    @Setter
    private String date;

    /**
     * The publication node does not produce warnings
     */
    @Override
    protected void checkForWarnings() {
        title.checkForWarnings();
    }

}
