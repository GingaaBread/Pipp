package processing.bibliography;

import lombok.Getter;
import lombok.Setter;
import processing.person.Author;
import warning.MissingMemberWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

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

    /**
     * If this method is called, it means that the type does not have its own overridden implementation and is therefore
     * not a supported bibliography source type.
     * Note that all source types must implement this method to add their creation behaviour
     *
     * @param bibliographyEntry the AST node that needs to be converted
     */
    public void createFromBibliographyEntry(frontend.ast.bibliography.BibliographySource bibliographyEntry) {
        WarningQueue.enqueue(new MissingMemberWarning(
                "2: The bibliography entry type '" + bibliographyEntry.getType() + "' is not supported.",
                WarningSeverity.HIGH));

        setBasicValues(bibliographyEntry);
    }

    protected void setBasicValues(frontend.ast.bibliography.BibliographySource bibliographyEntry) {
        id = bibliographyEntry.getId().trim();
        title = bibliographyEntry.getTitle().trim();
        authors = bibliographyEntry
                .getAuthors()
                .getAuthorList()
                .stream()
                .map(author -> {
                    final Author newAuthor;
                    if (author.getFirstname() == null) newAuthor = new Author(author.getName().trim());
                    else newAuthor = new Author(author.getFirstname().trim(), author.getLastname().trim());

                    if (author.getTitle() != null) newAuthor.setTitle(author.getTitle().trim());

                    return newAuthor;
                })
                .toArray(Author[]::new);
    }

}
