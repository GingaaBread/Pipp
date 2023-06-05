package processing;

import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.AST;
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

public class Processor {

    /**
     *  Determines the Pipp versions this compiler supports.
     *  If the user tries to scan a document with a Pipp version not included in this array, an exception is thrown
     */
    public static final String[] SUPPORTED_VERSIONS = new String[] {
            "1.0"
    };

    /**
     *  Determines which stylesheet should be used during compilation
     */
    private StyleGuide usedStyleGuide;

    private PDRectangle dimensions;

    private double margin;

    private double spacing;

    private NumerationType numerationType;

    private NumerationPosition numerationPosition;

    private double numerationMargin;

    private List<Integer> skippedPages;

    private PDFont font;

    private int fontSize;

    private Color fontColour;

    private double paragraphIndentation;

    private String sentencePrefix;

    private AllowanceType allowBoldText;

    private AllowanceType allowItalicText;

    private WhitespaceAllowance allowWhitespace;

    private StructureType requiredStructureBeforeEndnotes;
    private DocumentType documentType;
    private Author[] authors;
    private Assessor[] assessors;

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

    @NonNull
    public void processAST(final AST ast) {
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
            this.authors[i] = new Author(author.getName() == null
                    ? author.getFirstname() + author.getLastname()
                    : author.getName(), author.getId());
            i++;
        }

        // Convert the given author nodes to actual author objects
        var assessors = ast.getConfiguration().getAssessors().getAssessors();
        this.assessors = new Assessor[assessors.size()];
        int j = 0;
        for (var assessor : assessors) {
            this.assessors[j] = new Assessor(assessor.getName() == null
                    ? assessor.getFirstname() + assessor.getLastname()
                    : assessor.getName(), assessor.getRole());
            j++;
        }

        System.out.println("Finished processing.");
        System.out.println(this);
    }

}
