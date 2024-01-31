package processing.bibliography;

import lombok.Getter;
import lombok.Setter;
import processing.person.Author;

/**
 * A work represents any kind of work done that is referenced in the bibliography, which needs to be referenced.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class BibliographySource {

    /**
     * Determines the identifier of the work.
     * Must be unique.
     */
    protected String id;

    /**
     * Determines the main title of the work. Different work types can interpret this differently.
     */
    protected String title;

    /**
     * Determines the authors of the work.
     */
    protected Author[] authors;

    /**
     * Marks whether this bibliography source has also been cited by the user.
     * This is used to warn the user about unused sources and enabling this source to appear in the bibliography.
     */
    protected boolean hasBeenCited;

}
