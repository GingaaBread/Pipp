package processing.style;

import creation.ContentAlignment;
import creation.Text;
import lombok.NonNull;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import processing.*;
import processing.bibliography.BibliographySource;
import processing.bibliography.Book;

import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedList;

@Setter
public class MLA9 extends StyleGuide {

    private static Text getAuthorText(BibliographySource entry) {
        Text authorText = null;
        final var amountOfAuthors = entry.getAuthors().length;
        if (amountOfAuthors > 0) {
            final String authorTextContent = switch (amountOfAuthors) {
                case 1 -> entry.getAuthors()[0].getLastname() + ", " + entry.getAuthors()[0].getFirstname() + ". ";
                case 2 -> entry.getAuthors()[0].getLastname() + ", " + entry.getAuthors()[0].getFirstname() + ", and " +
                        entry.getAuthors()[1].getLastname() + ", " + entry.getAuthors()[1].getFirstname() + ". ";
                default ->
                        entry.getAuthors()[0].getLastname() + ", " + entry.getAuthors()[0].getFirstname() + ", et. al. ";
            };

            authorText = new Text(authorTextContent, Processor.getSentenceFont(), Processor.getSentenceFontSize(),
                    Processor.getSentenceFontColour());
        }
        return authorText;
    }

    private static String getPublisherTextContent(BibliographySource entry) {
        String publisherTextContent = null;
        if (entry instanceof Book bookEntry) {
            if (bookEntry.getPublicationName() != null && bookEntry.getPublicationYear() != null)
                publisherTextContent = bookEntry.getPublicationName() + ", " + bookEntry.getPublicationYear() + ".";
            else if (bookEntry.getPublicationName() != null) {
                // TODO print warning that year is missing
                publisherTextContent = bookEntry.getPublicationName() + ".";
            } else if (bookEntry.getPublicationYear() != null) {
                // TODO print warning that pub name is missing
                publisherTextContent = bookEntry.getPublicationYear() + ".";
            }
        }
        return publisherTextContent;
    }

    /**
     * MLA 9 uses the standardised U.S. Letter
     */
    @Override
    public PDRectangle pageFormat() {
        return PDRectangle.LETTER;
    }

    /**
     * MLA defines any easily readable font, which has a strongly contrasting italic version.
     * Pipp uses the Times Roman font for MLA9.
     */
    @Override
    public PDFont font() {
        return new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
    }

    /**
     * MLA defines any easily readable font, which has a strongly contrasting default version.
     * Pipp uses the italic Times font for MLA9.
     */
    @Override
    public PDFont emphasisFont() {
        return new PDType1Font(Standard14Fonts.FontName.TIMES_ITALIC);
    }

    /**
     * MLA defines any easily readable font, which has a strongly contrasting default version.
     * Pipp uses the italic Times font for MLA9.
     */
    @Override
    public PDFont workFont() {
        return new PDType1Font(Standard14Fonts.FontName.TIMES_ITALIC);
    }

    /**
     * MLA defines a font size of anywhere between 11 and 13 points.
     * Pipp uses a font size of 12.
     */
    @Override
    public int fontSize() {
        return 12;
    }

    /**
     * MLA defines a font size of anywhere between 11 and 13 points.
     * Pipp uses a font size of 12.
     */
    @Override
    public int emphasisFontSize() {
        return 12;
    }

    /**
     * MLA defines a font size of anywhere between 11 and 13 points.
     * Pipp uses a font size of 12.
     */
    @Override
    public int workFontSize() {
        return 12;
    }

    /**
     * MLA does not define a font colour.
     * Pipp uses a black font colour.
     */
    @Override
    public Color fontColour() {
        return Color.black;
    }

    /**
     * MLA does not define a font colour.
     * Pipp uses a black font colour.
     */
    @Override
    public Color emphasisFontColour() {
        return Color.black;
    }

    /**
     * MLA does not define a font colour.
     * Pipp uses a black font colour.
     */
    @Override
    public Color workFontColour() {
        return Color.black;
    }

    /**
     * MLA defines a margin of one inch to the top and bottom and both sides of the text
     */
    @Override
    public float margin() {
        return 1f;
    }

    /**
     * MLA defines double-spacing for the entire document and all its elements
     */
    @Override
    public float spacing() {
        return 2f;
    }

    @Override
    public ContentAlignment defaultImageAlignment() {
        return ContentAlignment.CENTER;
    }

    /**
     * MLA uses arabic numeration
     */
    @Override
    public NumerationType numerationType() {
        return NumerationType.ARABIC;
    }

    /**
     * MLA displays the page number in the top right corner
     */
    @Override
    public NumerationPosition numerationPosition() {
        return NumerationPosition.BOTTOM;
    }

    @Override
    public @NonNull NumerationAuthorName numerationAuthorName() {
        return NumerationAuthorName.FULL_NAME;
    }

    /**
     * MLA prefers to render all author names unless they do not fit in one line (then et al. is rendered)
     */
    @Override
    public Integer numerationLimit() {
        return null;
    }

    /**
     * MLA displays the page number half an inch away from the top of the document
     */
    @Override
    public float numerationMargin() {
        return 0.5f;
    }

    /**
     * MLA does only allow italic text if absolutely necessary
     */
    @Override
    public AllowanceType allowsEmphasis() {
        return AllowanceType.IF_NECESSARY;
    }

    /**
     * MLA defines that the first line of a paragraph should be indented half an inch from the left margin
     */
    @Override
    public float paragraphIndentation() {
        return 0.5f;
    }

    /**
     * MLA uses the following rules:
     * A full-stop is added if the last character is not a punctuation mark
     * Exactly one space is inserted before a new in-line sentence.
     */
    @Override
    public String formatText(final String textBlock) {
        return textBlock;
    }

    @Override
    public Text[] formatCitation(BibliographySource referenceSource, String content, String numeration) {
        if (referenceSource instanceof Book book) {
            final var citationBuilder = new StringBuilder();
            citationBuilder.append("\"");
            citationBuilder.append(content);
            citationBuilder.append("\"");
            citationBuilder.append(" ");

            citationBuilder.append("(");
            if (book.getAuthors().length > 0) {

                // If there is only one author, display the last name in parentheses
                if (book.getAuthors().length == 1) {
                    citationBuilder.append(book.getAuthors()[0].getLastname());
                    citationBuilder.append(" ");
                }
                // If there are two authors, display the last names of both in parentheses
                else if (book.getAuthors().length == 2) {
                    citationBuilder.append(book.getAuthors()[0].getLastname());
                    citationBuilder.append(", ");
                    citationBuilder.append(book.getAuthors()[1].getLastname());
                    citationBuilder.append(" ");
                }
                // If there are three or more authors only display the first author's last name and "et. al."
                else {
                    citationBuilder.append(book.getAuthors()[0].getLastname());
                    citationBuilder.append(" et. al. ");
                }
            }

            citationBuilder.append(numeration);
            citationBuilder.append(").");

            return new Text[]{new Text(
                    citationBuilder.toString(),
                    Processor.getSentenceFont(),
                    Processor.getSentenceFontSize(),
                    Processor.getSentenceFontColour())
            };
        }

        return new Text[0];
    }

    @Override
    public String formatBibliographyTitle(BibliographySource[] sources) {
        if (sources.length == 0)
            throw new IllegalStateException("Should not render bibliography title of an empty bibliography");
        return sources.length == 1 ? "Work Cited" : "Works Cited";
    }

    @Override
    public Text[] formatBibliographyEntry(BibliographySource entry) {
        final var textList = new LinkedList<Text>();

        Text authorText = getAuthorText(entry);
        if (authorText != null) textList.add(authorText);

        final var titleText = new Text(
                entry.getTitle() + ".",
                Processor.getWorkFont(),
                Processor.getWorkFontSize(),
                Processor.getWorkFontColour()
        );
        textList.add(titleText);

        String publisherTextContent = getPublisherTextContent(entry);

        if (publisherTextContent != null) textList.add(new Text(publisherTextContent,
                Processor.getSentenceFont(), Processor.getSentenceFontSize(), Processor.getSentenceFontColour()));

        return textList.toArray(Text[]::new);
    }

    /**
     * Dates are represented in the British date format
     *
     * @param date - the date as a LocalDate, which needs to be turned into a string
     * @return the date as required in MLA9
     */
    @Override
    @NonNull
    public String dateToString(final @NonNull LocalDate date) {
        final var monthAsLowerCase = date.getMonth().toString().toLowerCase();
        final var firstUppercase = String.valueOf(monthAsLowerCase.charAt(0)).toUpperCase();
        return firstUppercase + monthAsLowerCase.substring(1) + " " + date.getDayOfMonth()
                + " " + date.getYear();
    }

}
