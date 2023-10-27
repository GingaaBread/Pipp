package creation;

import lombok.NonNull;
import org.apache.pdfbox.pdmodel.PDPage;
import processing.*;

import java.util.List;
import java.util.TreeMap;

import java.util.Stack;

/**
 *  Used to add a stamp to the current page rendering the current page number and author
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
public class PageNumberStamp {

    /**
     *  Contains a reference to all page instances, which have already been stamped.
     *  If trying to stamp a page object, which exists on the stack, an exception is thrown.
     */
    private static final Stack<PDPage> stampedPages = new Stack<>();

    /**
     *  Contains the page number that should be rendered next.
     *  If a page is skipped, the value is NOT incremented, which means that the next page after a skipped page
     *  receives the page number, the skipped page would have received.
     */
    private static int nextNumber = 1;

    /**
     *  The number index is ALWAYS incremented, even if the page is skipped.
     *  This is used to address and identify the individual pages.
     */
    private static int numberIndex = 1;

    /**
     *  Adds a page number stamp to the current page.
     *  Throws an exception if there is no current page, the page has already been stamped or if the stamp
     *  could not be applied caused by an IOException.
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
        if (!Processor.skippedPages.contains(numberIndex)) {
                // Contains the displayed name of the author(s), which is added before the page number
                final var authorNamePrefixBuilder = new StringBuilder();

                // The names should only be displayed if there are authors configured in the first place
                if (Processor.authors.length > 0 && Processor.numerationAuthorName != NumerationAuthorName.NONE) {
                    // Sets the prefix to the name of the FIRST author, unless the user does not want to
                    // TODO: Consider at least up to four authors?
                    final String authorPrefix = switch (Processor.numerationAuthorName) {
                        case FIRST_NAME -> Processor.authors[0].getFirstname();
                        case LAST_NAME -> Processor.authors[0].getLastname();
                        case NAME -> Processor.authors[0].getFirstname() + " " + Processor.authors[0].getLastname();
                        case FULL_NAME -> Processor.authors[0].nameToString();
                        default -> throw new IllegalStateException("Unexpected value: " + Processor.numerationAuthorName);
                    };

                    // The name is now part of the builder
                    authorNamePrefixBuilder.append(authorPrefix);

                    // If there are multiple authors, also append "et al." if the names should be displayed
                    if (Processor.authors.length > 1 && Processor.numerationAuthorName != NumerationAuthorName.NONE)
                        authorNamePrefixBuilder.append(" et al. ");

                    // TODO
                    // if does not fit in one line
                    // use first and append et. al.
                }

                // Contains the page number as a string in the desired numeral system
                final String pageString = switch (Processor.numerationType) {
                    case ARABIC -> String.valueOf(nextNumber);
                    case ROMAN -> arabicToRoman(nextNumber);
                };

                // Contains the entire text as one string (the author texts and page number)
                final String content = authorNamePrefixBuilder + " " + pageString;

                // Calculates the starting x position of the text
                final TextAlignment alignment = switch (Processor.numerationPosition) {
                    case TOP_LEFT, BOTTOM_LEFT -> TextAlignment.LEFT;
                    case TOP_RIGHT, BOTTOM_RIGHT -> TextAlignment.RIGHT;
                    case TOP, BOTTOM -> TextAlignment.CENTER;
                };

                // Calculates the starting y position of the text
                final float y = switch (Processor.numerationPosition) {
                    // If the numeration should be rendered at the top, start at the numeration margin from the top
                    case TOP, TOP_LEFT, TOP_RIGHT -> page.getMediaBox().getHeight() - Processor.numerationMargin;

                    // Else, start at the numeration margin from the bottom
                    case BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> Processor.numerationMargin;
                };

                // Renders the page numeration with a normal text style
                TextRenderer.renderNoContentText(List.of(new Text(content, TextStyle.NORMAL)), alignment, y);

                // Increment the page number (only if this page was not skipped)
                nextNumber++;
        }

        // Even if the page was skipped, increment the number index, which is needed for addressing the pages
        numberIndex++;

        // Add this page to the stamped pages, which does not allow them to be stamped again
        PageNumberStamp.stampedPages.add(page);
    }

    /**
     *  Translates a number using arabic numerals into a number using Roman numerals
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

        int floored =  map.floorKey(number);
        return number == floored
                ? map.get(number)
                : map.get(floored) + arabicToRoman(number - floored);
    }

}
