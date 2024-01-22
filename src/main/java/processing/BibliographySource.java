package processing;

/**
 * A work represents any kind of work done that is referenced in the bibliography, which needs to be referenced.
 *
 * @version 1.0
 * @since 1.0
 */
public class BibliographySource {

    /**
     * Determines the main title of the work. Different work types can interpret this differently.
     */
    protected String title;

    /**
     * Determines the authors of the work. Every work needs to have at least one credited author.
     */
    protected Author[] authors;

}
