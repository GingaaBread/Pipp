package processing.work;

import lombok.Builder;
import lombok.NonNull;
import processing.Author;
import processing.Publication;

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
    public String toBibliography() {
                if (publication.getDate() == null)
                    throw new IllegalArgumentException("Book has no publication date");

                // todo: handle multiple authors

                return super.authors[0].getLastname() + ", " + super.authors[0].getFirstname() + ". _" +
                    title + "_. " + publication.getName() + ", " + publication.getDate().get(Calendar.YEAR) + ".";
    }
}
