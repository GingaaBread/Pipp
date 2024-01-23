package creation;

import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.Author;
import processing.NumerationAuthorName;
import processing.Processor;

import java.util.List;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Used to add a stamp to the current page rendering the current page number and author, if desired.
 *
 * @version 1.0
 * @since 1.0
 */
public class PageNumberStamp {

    /**
     * Contains a reference to all page instances, which have already been stamped.
     * If trying to stamp a page object, which exists on the stack, an exception is thrown.
     */
    private static final Stack<PDPage> stampedPages = new Stack<>();

    /**
     * Contains the page number that should be rendered next.
     * If a page is skipped, the value is NOT incremented, which means that the next page after a skipped page
     * receives the page number, the skipped page would have received.
     */
    private static int nextNumber = 1;

    /**
     * The number index is ALWAYS incremented, even if the page is skipped.
     * This is used to address and identify the individual pages.
     */
    private static int numberIndex = 1;

    /**
     * Adds a page number stamp to the current page.
     * Throws an exception if there is no current page, the page has already been stamped or if the stamp
     * could not be applied caused by an IOException.
     */
    @NonNull
    public static void stampCurrentPage() {
        // Use the current page in the page factory as the stamped page
        final var page = PageCreator.getCurrent();

        // Check if there is no page to stamp
        if (page == null)
            throw new IllegalStateException("Cannot stamp the current page because it does not exist yet.");

        // If a page has been stamped, it should not be stamped again.
        if (stampedPages.contains(page)) throw new IllegalStateException("Trying to re-stamp an already stamped page");

        // Do not stamp the page if the "actual" page is included in the skipped pages list
        if (!Processor.getSkippedPages().contains(numberIndex)) {
            // Contains the displayed name of the author(s), which is added before the page number
            final var authorNamePrefixBuilder = new StringBuilder();

            String firstAuthorName = null;

            // The names should only be displayed if there are authors configured in the first place
            if (Processor.getNumerationAuthorName() != NumerationAuthorName.NONE) {
                Author[] authors = Processor.getAuthors();
                for (int i = 0; i < authors.length; i++) {
                    var author = authors[i];
                    final String authorPrefix = switch (Processor.getNumerationAuthorName()) {
                        case FIRST_NAME -> author.getFirstname();
                        case LAST_NAME -> author.getLastname();
                        case NAME -> author.getFirstname() + " " + author.getLastname();
                        case FULL_NAME -> author.nameToString();
                        case NONE -> throw new IllegalStateException();
                    };

                    authorNamePrefixBuilder.append(authorPrefix);

                    if (i != authors.length - 1) authorNamePrefixBuilder.append(", ");

                    if (i == 0) firstAuthorName = authorPrefix;
                }
            }

            // Contains the page number as a string in the desired numeral system
            final String pageString = switch (Processor.getNumerationType()) {
                case ARABIC -> String.valueOf(nextNumber);
                case ROMAN -> arabicToRoman(nextNumber);
            };

            // Contains the entire text as one string (the author texts and page number)
            final String content = authorNamePrefixBuilder + " " + pageString;

            // Numerations use the default "sentence" font style
            var asText = new Text(content, Processor.getSentenceFont(),
                    Processor.getSentenceFontSize(), Processor.getSentenceFontColour());

            // Calculates the starting x position of the text
            final ContentAlignment alignment = switch (Processor.getNumerationPosition()) {
                case TOP_LEFT, BOTTOM_LEFT -> ContentAlignment.LEFT;
                case TOP_RIGHT, BOTTOM_RIGHT -> ContentAlignment.RIGHT;
                case TOP, BOTTOM -> ContentAlignment.CENTER;
            };

            // Calculates the starting y position of the text
            final float y = switch (Processor.getNumerationPosition()) {
                // If the numeration should be rendered at the top, start at the numeration margin from the top
                case TOP, TOP_LEFT, TOP_RIGHT -> page.getMediaBox().getHeight() - Processor.getNumerationMargin();

                // Else, start at the numeration margin from the bottom
                case BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> Processor.getNumerationMargin();
            };

            var pageText = new Text(pageString, asText.getFont(), asText.getFontSize(), asText.getFontColour());

            if (Processor.getNumerationLimit() == null) {
                if (TextRenderer.textFitsInOneLine(asText)) {
                    TextRenderer.renderNoContentText(List.of(asText), alignment, y, null);
                } else if (Processor.getAuthors().length > 1 &&
                        Processor.getNumerationAuthorName() != NumerationAuthorName.NONE) {
                    var firstAuthorOnlyText = new Text(firstAuthorName + " et al. " + pageString,
                            asText.getFont(), asText.getFontSize(), asText.getFontColour());

                    // Check if the first name and et al. would fit in one line
                    if (TextRenderer.textFitsInOneLine(firstAuthorOnlyText))
                        TextRenderer.renderNoContentText(List.of(firstAuthorOnlyText), alignment, y, null);
                        // If not, only the page number is rendered
                    else {
                        TextRenderer.renderNoContentText(List.of(pageText), alignment, y, null);
                    }
                }
            } else {
                if (Processor.getAuthors().length > Processor.getNumerationLimit()) {
                    var firstAuthorOnlyText = new Text(firstAuthorName + " et al. " + pageString,
                            asText.getFont(), asText.getFontSize(), asText.getFontColour());

                    // Check if the first name and et al. would fit in one line
                    if (TextRenderer.textFitsInOneLine(firstAuthorOnlyText))
                        TextRenderer.renderNoContentText(List.of(firstAuthorOnlyText), alignment, y, null);
                        // If not, only the page number is rendered
                    else {
                        TextRenderer.renderNoContentText(List.of(pageText), alignment, y, null);
                    }
                } else if (TextRenderer.textFitsInOneLine(asText)) {
                    TextRenderer.renderNoContentText(List.of(asText), alignment, y, null);
                } else {
                    TextRenderer.renderNoContentText(List.of(pageText), alignment, y, null);
                }
            }

            // Increment the page number (only if this page was not skipped)
            nextNumber++;
        }

        // Even if the page was skipped, increment the number index, which is needed for addressing the pages
        numberIndex++;

        // Add this page to the stamped pages, which does not allow them to be stamped again
        PageNumberStamp.stampedPages.add(page);
    }

    /**
     * Translates a number using arabic numerals into a number using Roman numerals
     *
     * @param number - the number in arabic numerals (1,2,5,...)
     * @return - the number in Roman numerals (I, II, V, ...)
     */
    private static String arabicToRoman(int number) {
        var map = new TreeMap<Integer, String>();

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

        int floored = map.floorKey(number);
        return number == floored
                ? map.get(number)
                : map.get(floored) + arabicToRoman(number - floored);
    }

}
