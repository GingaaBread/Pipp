package creation.content;

/**
 * This enumeration determines how content (text, images, etc.) is rendered in a document.
 * For example, text can be rendered flushed with the left margin or centered on the page.
 *
 * @since 1.0
 * @since 1.0
 */
public enum ContentAlignment {

    /**
     * The LEFT alignment aligns the rendered content to the left side of the x-dimension.
     */
    LEFT,

    /**
     * The CENTER alignment aligns the rendered content in the center of the page.
     * This can particularly useful when rendering titles or other texts that should stand out.
     */
    CENTER,

    /**
     * The RIGHT alignment aligns the rendered text to the right side of the x-dimension.
     */
    RIGHT

}
