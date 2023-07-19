package creation;

/**
 *  The TextAlignment enumeration determines how text is rendered in a document.
 *  For example, text can be rendered flushed with the left margin or centered on the page.
 *
 * @author Gino Glink
 * @since 1.0
 * @since 1.0
 */
public enum TextAlignment {

    /**
     *  The LEFT alignment aligns the rendered text to the left side of the x-dimension.
     *  Most style guides determine that paragraphs should be aligned in this way, flushed with the left margin.
     */
    LEFT,

    /**
     *  The CENTER alignment aligns the rendered text in the center of the page.
     *  This can particularly useful when rendering titles or other texts that should stand out.
     */
    CENTER,

    /**
     *  The RIGHT alignment aligns the rendered text to the right side of the x-dimension.
     *  This can be very useful for rendering the page numbers.
     */
    RIGHT

}
