package processing.work;

import lombok.Builder;
import lombok.NonNull;
import processing.StyleSheet;

import java.util.Calendar;

/**
 *  A Book is a traditional type of work, which is usually referenced using its title, author(s), and publication
 *  details (usually the publisher and publication date).
 *
 *  @since 1.0
 *  @version 1.0
 */
public class Book extends Work {

    @NonNull
    @Builder
    public Book(Author[] authors, String title, Publication publication) {
        super.authors = authors;
        super.title = title;
        super.publication = publication;
    }

    @Override
    public String toBibliography(StyleSheet styleSheet) {
        switch (styleSheet) {
            case MLA9 -> {
                if (publication.getDate() == null)
                    throw new IllegalArgumentException("Book has no publication date");

                // todo: handle multiple authors

                return super.authors[0].getLastName() + ", " + super.authors[0].getFirstName() + ". _" +
                    title + "_. " + publication.getName() + ", " + publication.getDate().get(Calendar.YEAR) + ".";
            }
            default -> throw new UnsupportedOperationException("A 'Book' work type does not support the specified " +
                    styleSheet + " stylesheet.");
        }
    }
}
