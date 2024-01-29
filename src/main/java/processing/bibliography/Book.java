package processing.bibliography;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}
