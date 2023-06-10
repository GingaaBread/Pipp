package creation;

import error.PippException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDType3Font;
import processing.*;

import java.util.TreeMap;

import java.io.IOException;
import java.util.Stack;

public class PageNumberStamp {

    private final Stack<PDPage> stampedPages = new Stack<>();
    private int nextNumber = 1;
    private int numberIndex = 1;

    @NonNull
    public PDPage stampPage(final PDPage page) {
        // If a page has been stamped, it should not be stamped again.
        if (stampedPages.contains(page)) throw new IllegalStateException("Trying to re-stamp an already stamped page");

        // Do not stamp the page if the "actual" page is included in the skipped pages list
        if (!Processor.skippedPages.contains(numberIndex)) {
            try {
                var contentStream = new PDPageContentStream(PDFCreator.document, page);
                contentStream.beginText();
                contentStream.setFont(Processor.font, Processor.fontSize);
                contentStream.setStrokingColor(Processor.fontColour);

                final var authorNamePrefixBuilder = new StringBuilder();
                if (Processor.authors.length > 0) {
                    final String authorPrefix = switch (Processor.numerationAuthorName) {
                        case FIRST_NAME -> Processor.authors[0].getFirstname();
                        case LAST_NAME -> Processor.authors[0].getLastname();
                        case NAME -> Processor.authors[0].getFirstname() + " " + Processor.authors[0].getLastname();
                        case NONE -> "";
                    };
                    authorNamePrefixBuilder.append(authorPrefix);

                    if (Processor.authors.length > 1 && Processor.numerationAuthorName != NumerationAuthorName.NONE)
                        authorNamePrefixBuilder.append(" et al. ");
                }

                final String pageString = switch (Processor.numerationType) {
                    case ARABIC -> nextNumber + "";
                    case ROMAN -> arabicToRoman(nextNumber);
                };

                final String content = authorNamePrefixBuilder + pageString;
                final float textWidth = Processor.font.getStringWidth(content) / 1000 * Processor.fontSize;
                final float textHeight = Processor.font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000
                        * Processor.fontSize;

                float x = switch (Processor.numerationPosition) {
                    case TOP_LEFT, BOTTOM_LEFT -> Processor.margin + textHeight;
                    case TOP_RIGHT, BOTTOM_RIGHT -> page.getMediaBox().getWidth() -
                            Processor.margin - textWidth - textHeight;
                    case TOP, BOTTOM -> (page.getMediaBox().getWidth() - textWidth) / 2;
                };

                float y = switch (Processor.numerationPosition) {
                    case TOP, TOP_LEFT, TOP_RIGHT -> page.getMediaBox().getHeight() - textHeight - Processor.margin;
                    case BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT -> Processor.margin + Processor.fontSize;
                };

                contentStream.newLineAtOffset(x, y);

                contentStream.showText(content);
                contentStream.endText();
                contentStream.close();

                PDFCreator.currentYPosition -= textHeight + Processor.margin;

                nextNumber++;

            } catch (IOException e) {
                throw new PippException("Could not add the page stamp to the page.");
            }
        }

        numberIndex++;
        stampedPages.add(page);

        return page;
    }

    private String arabicToRoman(int number) {
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
