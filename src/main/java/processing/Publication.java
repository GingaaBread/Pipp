package processing;

import lombok.Data;
import processing.Role;
import processing.work.WebPage;
import processing.work.Work;

import java.util.Calendar;

/**
 *  Specifies information about the publication of a work
 *  The most essential information are the publication name (for example, the name of the publisher of a book),
 *  the date (for example, the year it was published), the location (for example, in which country it has been
 *  published), and roles of other people than the author (for example, editors, directors, screenwriters, etc.)
 *
 *  @see Work
 *
 *  @since 1.0
 *  @version 1.0
 */
@Data
public class Publication {

    /**
     *  The name of the publication.
     *  For a book, this would be the name of the publisher, whereas for a song this could be the name of the
     *  record label.
     */
    private String name;

    /**
     *  The date of the publication.
     *  For a book, video game, song, etc. this would be the date it was published.
     */
    private Calendar date;

    /**
     *  The name of the location of the publication.
     *  For a book, this would be the country it was published in.
     */
    private String location;

    /**
     *  A link to a website, especially useful for Webpage works.
     *
     *  @see WebPage
     */
    private String www;

    /**
     *  The identification of the publication.
     *  For a journal, this would be the edition of the journal (for example, #2)
     */
    private String id;

    /**
     *  The issue of the publication, especially useful for periodicals like journals.
     */
    private String issue;

    /**
     *  The unedited span of pages used from the work (for example, 259-284).
     *  Pipp will edit this page span according to the style guide (for example, add a trailing zero, etc.)
     */
    private String page;

    /**
     *  Roles of other people having worked on the work other than the author.
     *  This could include editors, screenwriters, producers, audio mixers, etc.
     */
    private Role[] roles;

}
