package processing.work;

import processing.StyleSheet;

/**
 *  A work represents any kind of work done by someone other than the author, which needs to be referenced.
 *  Libraries can inherit from this abstract class to create custom work types.
 *
 *  @since 1.0
 *  @version 1.0
 */
public abstract class Work {

    /**
     *  Determines the main title of the work. Different work types can interpret this differently.
     */
    protected String title;

    /**
     *  Determines the authors of the work. Every work needs to have at least one credited author.
     */
    protected Author[] authors;

    /**
     *  Determines information about the publication of the work. Note that not all fields must be used.
     */
    protected Publication publication;

    /**
     *  Turns the work into a string, adhering to the bibliography (or works-cited) formalities of the
     *  specified style sheet.
     * @param styleSheet - the style sheet that determines the formal aspects of the conversion
     * @return - the work as a String ready to be rendered in the bibliography section of a document
     */
    public abstract String toBibliography(StyleSheet styleSheet);

}
