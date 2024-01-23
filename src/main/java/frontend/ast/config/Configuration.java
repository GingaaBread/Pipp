package frontend.ast.config;

import frontend.ast.Node;
import frontend.ast.config.style.Style;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * The configuration node groups together all configurations
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Configuration extends Node {

    /**
     * The "assessors" node groups together all assessors specified by the user
     */
    private Assessors assessors = new Assessors();

    /**
     * The "authors" node groups together all authors specified by the user
     */
    private Authors authors = new Authors();

    /**
     * The "publication" node contains information about the publication of the document.
     * This includes the date of publication, for example.
     */
    private Publication publication = new Publication();

    /**
     * The "style" node contains information about the used style guide and its overridden values
     */
    private Style style = new Style();

    /**
     * The title node is used to generate the title of the document.
     * It is its own branch, instead of a string, because the user can use citations within the title.
     */
    private Title title = new Title();

    /**
     * Determines the type of the document.
     * For example, "Paper" determines that the document is a scientific paper.
     */
    private String documentType;

    /**
     * The configuration node does not produce warnings
     */
    @Override
    public void checkForWarnings() {
        assessors.checkForWarnings();
        authors.checkForWarnings();
        publication.checkForWarnings();
        style.checkForWarnings();
        title.checkForWarnings();
    }

}
