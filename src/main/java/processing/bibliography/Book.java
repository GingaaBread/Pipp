package processing.bibliography;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import warning.SelfCheckWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

@Getter
@Setter
@ToString
public class Book extends BibliographySource {

    /**
     * The year the book was published.
     * Not an integer in case the users cites an ancient source.
     */
    protected String publicationYear;

    /**
     * The name of the publication, for example a book publisher.
     */
    protected String publicationName;

    @Override
    public void createFromBibliographyEntry(frontend.ast.bibliography.BibliographySource bibliographyEntry) {
        if (bibliographyEntry.getPublication().getName() != null ||
                bibliographyEntry.getPublication().getYear() != null) {
            final var year = bibliographyEntry.getPublication().getYear();
            if (year != null) publicationYear = year.trim();

            final var name = bibliographyEntry.getPublication().getName();
            if (name != null) publicationName = name.trim();
        } else {
            WarningQueue.enqueue(new SelfCheckWarning(
                    "3: The bibliography entry with the ID '" + bibliographyEntry.getId() +
                            "' does not have a publication name or year.", WarningSeverity.CRITICAL));
        }
        setBasicValues(bibliographyEntry);
    }
}
