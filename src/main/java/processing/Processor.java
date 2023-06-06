package processing;

import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.AST;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.style.Pipp;
import processing.style.StyleGuide;
import processing.style.StyleTable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
     *  and a maximum position of the margin to the right and bottom
     */
    private double margin;

    /**
     *  Determines the paragraph spacing, which is the space between each line in a paragraph
     */
    private double spacing;


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
    private double numerationMargin;

    // TODO
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
    private double paragraphIndentation;


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
    private WhitespaceAllowance allowWhitespace;


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
        var pointsPerInch = 72;
        var pointsPerMM = 1 / (10 * 2.54f) * pointsPerInch;;

        // Check if the user demands a custom document dimension

        float height;
        if (layout.getHeight() != null) {
            // Check if the user wants to use inches
            if (layout.getHeight().endsWith("\"")) {
                var asNumber = layout.getHeight().substring(0, layout.getHeight().length() - 1);
                height = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                height = pointsPerMM * Float.parseFloat(layout.getHeight());
            }
        } else height = usedStyleGuide.pageFormat().getHeight();

        float width;
        if (layout.getWidth() != null) {
            // Check if the user wants to use inches
            if (layout.getWidth().endsWith("\"")) {
                var asNumber = layout.getWidth().substring(0, layout.getWidth().length() - 1);
                width = pointsPerInch *  Float.parseFloat(asNumber);
            } else {
                width = pointsPerMM * Float.parseFloat(layout.getWidth());
            }
        } else width = usedStyleGuide.pageFormat().getWidth();

        // Set the document dimensions
        dimensions = new PDRectangle(width, height);

        // Check if the user demands a custom margin
        if (layout.getMargin() != null) {
            margin = Double.parseDouble(layout.getMargin());
        } else margin = usedStyleGuide.margin();

        // Check if the user demands a custom spacing
        if (layout.getSpacing() != null) {
            switch (layout.getSpacing()) {
                case "Single" -> spacing = 1d;
                case "Increased" -> spacing = 1.5d;
                case "Double" -> spacing = 2d;
                default -> spacing = Double.parseDouble(layout.getSpacing());
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

        // Check if the user demands a custom numeration margin
        if (numeration.getMargin() != null) {
            try {
                double asNumber = Double.parseDouble(numeration.getMargin());
                if (asNumber < 0) throw new IncorrectFormatException("2: Non-negative decimal expected.");
                else numerationMargin = asNumber;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        } else numerationMargin = usedStyleGuide.numerationMargin();

        // TODO
        skippedPages = new ArrayList<>();

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

        // Check if the user demands a custom paragraph indentation
        if (paragraph.getIndentation() != null) {
            try {
                var asNumber = Double.parseDouble(paragraph.getIndentation());
                if (asNumber < 0) {
                    throw new IncorrectFormatException("2: Non-negative decimal expected.");
                } else paragraphIndentation = asNumber;
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
              case "Yes" -> WhitespaceAllowance.YES;
              case "Remove" -> WhitespaceAllowance.REMOVE;
              case "Escape" -> WhitespaceAllowance.ESCAPE;
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

        System.out.println("Finished processing.");
        System.out.println(this);
    }

}
