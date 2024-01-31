package processing.numeration;

/**
 * Used to determine how the name of an author should be rendered in the page number stamps of the document.
 *
 * @version 1.0
 * @since 1.0
 */
public enum NumerationAuthorName {

    /**
     * If used, only the first name of the author will be rendered in the page number stamp.
     * Example: "John 1"
     */
    FIRST_NAME,

    /**
     * If used, only the last name of the author will be rendered in the page number stamp.
     * Example: "Doe 1"
     */
    LAST_NAME,

    /**
     * If used, the first and last name of the author will be rendered in the page number stamp, but
     * if the author has got a title like Dr., Prof., etc., it will not be rendered.
     * Example: "John Doe 1"
     */
    NAME,

    /**
     * If used, the full name, including the title of the author, will be rendered in the page number stamp.
     * Example: "Prof. Dr. John Doe 1"
     */
    FULL_NAME,

    /**
     * If used, the author's name will not be rendered in the page number stamp.
     * Example: "1"
     */
    NONE

}
