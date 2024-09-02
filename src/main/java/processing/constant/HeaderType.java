package processing.constant;

/**
 * Determines how the header is displayed in the document
 * @since 1.0
 * @version 1.0
 */
public enum HeaderType {

    /**
     * There should be no header rendered in the document.
     */
    NONE,

    /**
     * Uses an entire page to display the title.
     * Page numbers are omitted for that first title page.
     */
    FULL_TITLE_PAGE,

    /**
     * Uses a simple header section and then proceeds with the normal
     * page flow.
     */
    SIMPLE_HEADER
}
