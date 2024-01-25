package processing.bibliography;

import lombok.Getter;
import lombok.Setter;

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
     * Determines the main title of the work. Different work types can interpret this differently.
     */
    protected String title;

    /**
     * Marks whether this bibliography source has also been cited by the user.
     * This is used to warn the user about unused sources and enabling this source to appear in the bibliography.
     */
    protected boolean hasBeenCited;

}
