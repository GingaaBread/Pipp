package processing.constant;

/**
 * Determines the amount of spacing used for chapters.
 *
 * @version 1.0
 * @since 1.0
 */
public enum ChapterSpacingType {

    /**
     * If used, the default leading spacing is used after chapter texts.
     * There is no further spacing before or after the text.
     */
    LEADING,

    /**
     * If used, the default leading as well as a single empty line before and after the chapter text is used.
     * The standard sentence font data is used for the empty line height calculation.
     */
    LEADING_AND_EMPTY_LINE_BEFORE_AND_AFTER

}
