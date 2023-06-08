package processing;

import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.AST;
import lombok.Data;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.style.Pipp;
import processing.style.StyleGuide;
import processing.style.StyleTable;
import warning.InconsistencyWarning;
import warning.WarningQueue;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *  The processor class translates the AST given by the {@link frontend.parsing.Parser} to actual objects
 *  that can be used when creating the document
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Data
public class Processor {

    /**
     *  Determines the Pipp versions this compiler supports.
     *  If the user tries to scan a document with a Pipp version not included in this array, an error is thrown,
     *  and the user should be prompted to update this compiler or check if they misspelled the version.
     */
    public static final String[] SUPPORTED_VERSIONS = new String[] {
            "1.0"
    };


    //// GENERAL ////


    /**
     *  Determines the type of the document, which currently only affects the document's metadata
     */
    private DocumentType documentType;

    /**
     *  Determines the type of style guide which should be used during compilation
     */
    private StyleGuide usedStyleGuide;

    /**
     *  Determines the dimensions of the document (the width and height)
     */
    private PDRectangle dimensions;

    /**
     *  Determines the margin to all four sides of the document.
     *  All components need to have a minimum position of the margin to the left and top,
     *  and a maximum position of the margin to the right and bottom.
     */
    private float margin;

    /**
     *  Determines the paragraph spacing, which is the space between each line in a paragraph
     */
    private float spacing;


    //// PAGE NUMERATION ////


    /**
     *  Determines how page numbers should be represented
     */
    private NumerationType numerationType;

    /**
     *  Determines where in the document page numbers should be displayed
     */
    private NumerationPosition numerationPosition;

    /**
     *  Determines the page number margin to the respective sides of the document
     */
    private float numerationMargin;

    // TODO: Add configuration whether the author's name should be added before the page number

    /**
     *  Contains all page numbers that the user does not want to have page numbers.
     *  For each created page, the list will be checked if it contains the current page number,
     *  and if it does, it will not receive a numeration stamp.
     */
    private List<Integer> skippedPages;


    //// FONT ////


    /**
     *  Determines the main font family used throughout the document
     */
    private PDFont font;

    /**
     *  Determines the main font size used throughout the document
     */
    private int fontSize;

    /**
     *  Determines the main font size used throughout the document
     */
    private Color fontColour;


    //// PARAGRAPH ////


    /**
     *  Determines the paragraph indentation, which is the amount of space that a new paragraph will be
     *  indented to
     */
    private float paragraphIndentation;


    //// SENTENCES ////


    /**
     *  Determines the string that will be appended before each sentence.
     *  In most style guides, this is a single space.
     */
    private String sentencePrefix;

    /**
     *  Determines if the user is allowed to use bold text in a sentence
     */
    private AllowanceType allowBoldText;

    /**
     *  Determines if the user is allowed to use italic text in a sentence
     */
    private AllowanceType allowItalicText;

    /**
     *  Determines if the user is allowed to use white space in a sentence
     */
    private WhitespaceAllowanceType allowWhitespace;


    //// END NOTES ////


    /**
     *  Determines the type of structure that needs to appear before the user can declare an end notes section.
     *  This value can be null, if no structure is required to appear before the section.
     */
    private StructureType requiredStructureBeforeEndnotes;


    //// AUTHORS & ASSESSORS ////


    /**
     *  Determines the authors that have worked on the document.
     *  Note that this only includes the authors of the working document, it does not include authors that
     *  have been cited from in the document
     */
    private Author[] authors;

    /**
     *  Determines the assessors that may assess the document
     */
    private Assessor[] assessors;


    /**
     *  A simple automatically-generated method to represent the values of all properties of the processor
     * @return - the values of the processor
     */
    @Override
    public String toString() {
        return "Processor{" +
                "usedStyleGuide=" + usedStyleGuide +
                ", dimensions=" + dimensions.getWidth() + "/" + dimensions.getHeight() +
                ", margin=" + margin +
                ", spacing=" + spacing +
                ", numerationType=" + numerationType +
                ", numerationPosition=" + numerationPosition +
                ", numerationMargin=" + numerationMargin +
                ", skippedPages=" + skippedPages +
                ", font=" + font +
                ", fontSize=" + fontSize +
                ", fontColour=" + fontColour +
                ", paragraphIndentation=" + paragraphIndentation +
                ", sentencePrefix='" + sentencePrefix + '\'' +
                ", allowBoldText=" + allowBoldText +
                ", allowItalicText=" + allowItalicText +
                ", allowWhitespace=" + allowWhitespace +
                ", requiredStructureBeforeEndnotes=" + requiredStructureBeforeEndnotes +
                ", documentType=" + documentType +
                ", authors=" + Arrays.toString(authors) +
                ", assessors=" + Arrays.toString(assessors) +
                '}';
    }

    /**
     *  Starts the processing phase by trying to convert the specified AST into usable objects.
     *
     * @param ast - the abstract syntax tree produced by the {@link frontend.parsing.Parser}
     */
    public void processAST(@NonNull final AST ast) {
        ast.checkForErrors();
        ast.checkForWarnings();

        System.out.println(ast);

        boolean inchesUsed = false;
        boolean mmUsed = false;

        var styleConfiguration = ast.getConfiguration().getStyle();

        // Check if the user demands a different style than the default style
        if (styleConfiguration.getBaseStyle() != null) {
            // Try to apply that style
            usedStyleGuide = StyleTable.nameToStyleGuide(ast.getConfiguration().getStyle().getBaseStyle());
        } else {
            // Use the default style, instead
            usedStyleGuide = new Pipp();
        }

        var layout = styleConfiguration.getLayout();
        int pointsPerInch = 72;
        var pointsPerMM = 1 / (10 * 2.54f) * pointsPerInch;

        // Check if the user demands a custom document dimension

        float height;
        if (layout.getHeight() != null) {
            // Check if the user wants to use inches
            if (layout.getHeight().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getHeight().substring(0, layout.getHeight().length() - 2);
                height = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                height = pointsPerMM * Float.parseFloat(layout.getHeight());
            }
        } else height = usedStyleGuide.pageFormat().getHeight();

        float width;
        if (layout.getWidth() != null) {
            // Check if the user wants to use inches
            if (layout.getWidth().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getWidth().substring(0, layout.getWidth().length() - 2);
                width = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                width = pointsPerMM * Float.parseFloat(layout.getWidth());
            }
        } else width = usedStyleGuide.pageFormat().getWidth();

        // Set the document dimensions
        dimensions = new PDRectangle(width, height);

        // Check if the user demands a custom margin
        if (layout.getMargin() != null) {
            if (layout.getMargin().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getMargin().substring(0, layout.getMargin().length() - 2);
                margin = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                margin = pointsPerMM * Float.parseFloat(layout.getMargin());
            }
        } else margin = usedStyleGuide.margin();

        // Check if the user demands a custom spacing
        if (layout.getSpacing() != null) {
            if (layout.getSpacing().endsWith("in")) inchesUsed = true;
            else mmUsed = true;

            switch (layout.getSpacing()) {
                case "1" -> spacing = pointsPerMM;
                case "1.5" -> spacing = 1.5f * pointsPerMM;
                case "2" -> spacing = 2f * pointsPerMM;
                case "1in" -> spacing = pointsPerInch;
                case "1.5in" -> spacing = 1.5f * pointsPerInch;
                case "2in" -> spacing = 2f * pointsPerInch;
                default -> throw new IncorrectFormatException("10: Incorrect spacing constant.");
            }
        } else spacing = usedStyleGuide.spacing();

        var numeration = styleConfiguration.getNumeration();

        // Check if the user demands a custom numeration type
        if (numeration.getNumerationType() != null) {
            numerationType = switch (numeration.getNumerationType()) {
                case "Arabic" -> NumerationType.ARABIC;
                case "Roman" -> NumerationType.ROMAN;
                default -> throw new MissingMemberException("4: " + "The specified page numeration is either " +
                        "missing or does not exist. Check if it has been imported correctly, or if you " +
                        "have misspelled the numeration type in the configuration.");
            };
        } else numerationType = usedStyleGuide.numerationType();

        // Check if the user demands a custom numeration position
        if (numeration.getPosition() != null) {
            numerationPosition = switch (numeration.getPosition()) {
                case "Top Left" -> NumerationPosition.TOP_LEFT;
                case "Top" -> NumerationPosition.TOP;
                case "Top Right" -> NumerationPosition.TOP_RIGHT;
                case "Bottom Left" -> NumerationPosition.BOTTOM_LEFT;
                case "Bottom" -> NumerationPosition.BOTTOM;
                case "Bottom Right" -> NumerationPosition.BOTTOM_RIGHT;
                default -> throw new MissingMemberException("5: " + "The specified page position is either " +
                        "missing or does not exist. Check if it has been imported correctly, or if you " +
                        "have misspelled the numeration position in the configuration.");
            };
        } else numerationPosition = usedStyleGuide.numerationPosition();

        // Add all pages that the user desires to be skipped when adding the numeration stamp
        skippedPages = new LinkedList<>();
        for (var skippedPage : numeration.getSkippedPages()) {
            // Check if the user provided a span (for example, 5-12)
            if (skippedPage.contains("-")) {
                String[] pageSpan = skippedPage.split("-");

                if (pageSpan.length != 2) throw new IncorrectFormatException("11: A page span must include exactly" +
                        " two page-numbers.");

                try {
                    int first = Integer.parseInt(pageSpan[0]);
                    int second = Integer.parseInt(pageSpan[1]);

                    if (first < 1 || second < 1) throw new IncorrectFormatException("13: Page number expected.");
                    else if (first >= second) throw new IncorrectFormatException("12: The second page-number must " +
                            "be greater than the first page-number in a page span.");

                    for (int i = first; i < second; i++)
                        skippedPages.add(i);
                } catch (IllegalArgumentException e) {
                    throw new IncorrectFormatException("13: Page number expected.");
                }
            } else {
                try {
                    int pageNumber = Integer.parseInt(skippedPage);

                    if (pageNumber < 1) {
                        throw new IncorrectFormatException("13: Page number expected.");
                    } else skippedPages.add(pageNumber);
                } catch (IllegalArgumentException e) {
                    throw new IncorrectFormatException("13: Page number expected.");
                }
            }
        }

        var margin = numeration.getMargin();

        // Check if the user demands a custom numeration margin
        if (margin != null) {
            try {
                float unit;
                if (numeration.getMargin().endsWith("in")) {
                    inchesUsed = true;

                    margin = margin.substring(0, margin.length() - 2);
                    unit = pointsPerInch;
                } else {
                    unit = pointsPerMM;

                    mmUsed = true;
                }

                float asNumber = Float.parseFloat(margin);
                if (asNumber < 0) throw new IncorrectFormatException("2: Non-negative decimal expected.");
                else numerationMargin = asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        } else numerationMargin = usedStyleGuide.numerationMargin();

        var font = styleConfiguration.getFont();

        // Check if the user demands a custom font
        if (font.getName() != null) {
            this.font = switch (font.getName()) {
              case "Times Roman" -> PDType1Font.TIMES_ROMAN;
              case "Helvetica" -> PDType1Font.HELVETICA;
              case "Courier" -> PDType1Font.COURIER;
              case "Symbol" -> PDType1Font.SYMBOL;
              case "Zapf Dingbats" -> PDType1Font.ZAPF_DINGBATS;
              default -> throw new MissingMemberException("6: The specified font is missing or does" +
                      " not exist.");
            };
        } else this.font = usedStyleGuide.font();

        // Check if the user demands a custom font size
        if (font.getSize() != null) {
            try {
                var asNumber = Integer.parseInt(font.getSize());
                if (asNumber < 0) {
                    throw new IncorrectFormatException("3: Non-negative integer expected.");
                } else fontSize = asNumber;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("3: Non-negative integer expected.");
            }
        } else fontSize = usedStyleGuide.fontSize();

        // Check if the user demands a custom font colour
        if (font.getColour() != null) {
            try {
                fontColour = Color.decode(font.getColour());
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("4: Colour expected.");
            }
        } else fontColour = usedStyleGuide.fontColour();

        var structure = styleConfiguration.getStructure();

        var paragraph = structure.getParagraph();
        var indentation = paragraph.getIndentation();

        // Check if the user demands a custom paragraph indentation
        if (indentation != null) {
            try {
                float unit;
                if (indentation.endsWith("in")) {
                    inchesUsed = true;

                    indentation = indentation.substring(0, indentation.length() - 2);
                    unit = pointsPerInch;
                } else {
                    unit = pointsPerMM;

                    mmUsed = true;
                }

                float asNumber = Float.parseFloat(indentation);
                if (asNumber < 0) {
                    throw new IncorrectFormatException("2: Non-negative decimal expected.");
                } else paragraphIndentation = asNumber * unit;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        } else paragraphIndentation = usedStyleGuide.paragraphIndentation();

        var sentence = structure.getSentence();

        // Check if the user demands a custom sentence prefix
        if (sentence.getPrefix() != null) {
            sentencePrefix = sentence.getPrefix();
        } else sentencePrefix = usedStyleGuide.sentencePrefix();

        // Check if the user demands custom restrictions

        // Bold text
        if (sentence.getAllowBoldText() != null) {
            allowBoldText = switch (sentence.getAllowBoldText()) {
                case "Yes" -> AllowanceType.YES;
                case "No" -> AllowanceType.NO;
                case "If Necessary" -> AllowanceType.IF_NECESSARY;
                default -> throw new IncorrectFormatException("5: Allowance type expected.");
            };
        } else allowBoldText = usedStyleGuide.allowsBold();

        // Italic text
        if (sentence.getAllowItalicText() != null) {
            allowItalicText = switch (sentence.getAllowItalicText()) {
                case "Yes" -> AllowanceType.YES;
                case "No" -> AllowanceType.NO;
                case "If Necessary" -> AllowanceType.IF_NECESSARY;
                default -> throw new IncorrectFormatException("5: Allowance type expected.");
            };
        } else allowItalicText = usedStyleGuide.allowsItalic();

        // Whitespace
        if (sentence.getAllowWhitespace() != null) {
            allowWhitespace = switch (sentence.getAllowWhitespace()) {
              case "Yes" -> WhitespaceAllowanceType.YES;
              case "No" -> WhitespaceAllowanceType.NO;
              case "Escaped" -> WhitespaceAllowanceType.ESCAPED;
              default -> throw new IncorrectFormatException("6: Allowance type expected.");
            };
        } else allowWhitespace = usedStyleGuide.allowsWhitespace();

        var endnotes = structure.getEndnotes();

        // Check if the user demands a custom endnotes configuration
        if (endnotes.getAllowBeforeStructure() != null) {
            try {
                requiredStructureBeforeEndnotes =
                        StructureType.valueOf(endnotes.getAllowBeforeStructure().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("7: Structure type expected.");
            }
        } else requiredStructureBeforeEndnotes = usedStyleGuide.requiredStructureBeforeEndnotes();

        // Check if the user demands a custom document title
        if (ast.getConfiguration().getType() != null) {
            try {
                documentType = DocumentType.valueOf(ast.getConfiguration().getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("8: Document type expected.");
            }
        } else documentType = usedStyleGuide.documentType();

        // Convert the given author nodes to actual author objects
        var authors = ast.getConfiguration().getAuthors().getAuthors();
        this.authors = new Author[authors.size()];
        int i = 0;
        for (var author : authors) {
            if (author.getName() == null)
                this.authors[i] = new Author(author.getFirstname(), author.getLastname(), author.getId());
            else
                this.authors[i] = new Author(author.getName(), author.getId());

            i++;
        }

        // Convert the given author nodes to actual author objects
        var assessors = ast.getConfiguration().getAssessors().getAssessors();
        this.assessors = new Assessor[assessors.size()];
        int j = 0;
        for (var assessor : assessors) {
            if (assessor.getName() == null)
                this.assessors[j] = new Assessor(assessor.getFirstname(), assessor.getLastname(),
                        assessor.getRole());
            else
                this.assessors[j] = new Assessor(assessor.getName(), assessor.getRole());

            j++;
        }

        if (inchesUsed && mmUsed) WarningQueue.getInstance().enqueue(new InconsistencyWarning(
                "The style configuration uses both inches and millimeters."
        ));

        // For debugging purposes
        System.out.println("Finished processing.");
        System.out.println(this);
    }

}
