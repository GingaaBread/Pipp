package processing;

import creation.DocumentCreator;
import creation.PageAssembler;
import creation.Text;
import error.*;
import frontend.ast.AST;
import frontend.ast.BodyNode;
import frontend.ast.config.Title;
import frontend.ast.paragraph.Emphasise;
import frontend.ast.paragraph.ParagraphInstruction;
import frontend.ast.paragraph.Work;
import lombok.NonNull;
import lombok.ToString;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import processing.style.MLA9;
import processing.style.StyleGuide;
import processing.style.StyleTable;
import warning.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * The processor class translates the AST given by the {@link frontend.parsing.Parser} to actual objects
 * that can be used when creating the document
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@ToString
public class Processor {

    /**
     * Determines the version of this compiler implementation.
     * Note that the user can choose to opt for a different version as long as it is supported by the compiler.
     */
    public static final String COMPILER_VERSION = "1.0";


    //// GENERAL ////


    /**
     * Determines the type of the document, which currently only affects the document's metadata
     */
    public static DocumentType documentType;

    /**
     * Determines the title of the document, which can be displayed using the title instruction.
     * Note that the Title class is taken from the AST package simply to not have to duplicate it.
     */
    public static Title documentTitle;

    /**
     * Determines the type of style guide which should be used during compilation
     */
    public static StyleGuide usedStyleGuide;

    /**
     * Determines the dimensions of the document (the width and height)
     */
    public static PDRectangle dimensions;

    /**
     * Determines the margin in points (pt.) to all four sides of the document.
     * All components need to have a minimum position of the margin to the left and top,
     * and a maximum position of the margin to the right and bottom.
     */
    public static float margin;

    /**
     * Determines the paragraph spacing as a float, which is the space between each line in a paragraph.
     * The spacing is multiplied by the font size (and font size increment factor) to calculate line leading.
     * Note that the unit is a float! It is not represented in points, inches or any other measurement.
     */
    public static float spacing;


    //// PAGE NUMERATION ////


    /**
     * Determines how page numbers should be represented
     */
    public static NumerationType numerationType;

    /**
     * Determines where in the document page numbers should be displayed
     */
    public static NumerationPosition numerationPosition;

    /**
     * Determines the page number margin to the top or bottom of the document.
     * Note that the default layout margin is used for the left or right side.
     */
    public static float numerationMargin;

    /**
     * Determines how the names of the authors should be inserted before the page number.
     * If there should not be a name before the page number, this value is null.
     */
    public static NumerationAuthorName numerationAuthorName;

    /**
     * Contains all page numbers that the user does not want to have page numbers.
     * For each created page, the list will be checked if it contains the current page number,
     * and if it does, it will not receive a numeration stamp.
     */
    public static List<Integer> skippedPages;

    /**
     * Contains the maximum amount of author names that should be rendered in the numeration.
     * If there is no limitation, this is null.
     */
    public static Integer numerationLimit;


    //// FONT ////


    /**
     * Determines the main font family used throughout the document
     */
    public static PDFont sentenceFont;

    /**
     * Determines the main font size in points (pt.) used throughout the document
     */
    public static float sentenceFontSize;

    /**
     * Determines the main font size used throughout the document
     */
    public static Color sentenceFontColour;

    /**
     * Determines the font family used for emphasis
     */
    public static PDFont emphasisFont;

    /**
     * Determines the font size in points (pt.) used throughout for emphasis
     */
    public static float emphasisFontSize;

    /**
     * Determines the main font size used for emphasis
     */
    public static Color emphasisFontColour;

    /**
     * Determines the font family used for work references
     */
    public static PDFont workFont;

    /**
     * Determines the font size in points (pt.) used throughout for work references
     */
    public static float workFontSize;

    /**
     * Determines the main font size used for work references
     */
    public static Color workFontColour;


    //// PARAGRAPH ////


    /**
     * Determines the paragraph indentation, which is the amount of space that a new paragraph will be
     * indented to
     */
    public static float paragraphIndentation;


    //// SENTENCES ////


    /**
     * Determines the string that will be appended before each sentence.
     * In most style guides, this is a single space.
     */
    public static String sentencePrefix;

    /**
     * Determines if the user is allowed to use italic text in a sentence
     */
    public static AllowanceType allowEmphasis;


    //// AUTHORS & ASSESSORS ////


    /**
     * Determines the authors that have worked on the document.
     * Note that this only includes the authors of the working document, it does not include authors that
     * have been cited from in the document
     */
    public static Author[] authors;

    /**
     * Determines the assessors that may assess the document
     */
    public static Assessor[] assessors;


    //// Publication ////


    /**
     * Determines the date of document publication, which is either given by the user or created by Pipp.
     * Note that if the user has explicitly expressed not to display the date, this is null.
     */
    public static LocalDate publicationDate;

    /**
     * Determines the title of the publication, which in most style guides is put in the header or title page.
     * Note that the Title class is taken from the AST package simply to not have to duplicate it.
     */
    public static Title publicationTitle;

    /**
     * Determines the institution of the publication context.
     * An example could be "XYZ University".
     */
    public static String publicationInstitution;

    /**
     * Determines the chair of the publication institution.
     * Note that this should not be allowed to be set if the institution is not set.
     */
    public static String publicationChair;

    /**
     * Determines the semester of the publication context.
     * This is used academic papers.
     */
    public static String publicationSemester;


    //// Document Body ////


    public static LinkedList<BodyNode> documentBody = new LinkedList<>();

    // TODO: Change to adapt configurations
    public static Text paragraphInstructionToText(@NonNull final ParagraphInstruction paragraphInstruction) {
        if (paragraphInstruction instanceof frontend.ast.paragraph.Text text) {
            return new Text(text.getContent(), sentenceFont, sentenceFontSize, sentenceFontColour);
        } else if (paragraphInstruction instanceof Emphasise emphasis) {
            if (Processor.allowEmphasis == AllowanceType.NO)
                throw new ConfigurationException("9: The style guide does not allow the use of emphasis, but you are " +
                        "trying to emphasise text nonetheless.");
            else if (Processor.allowEmphasis == AllowanceType.IF_NECESSARY)
                WarningQueue.enqueue(new SelfCheckWarning("1: You are using a style guide that recommends" +
                        " only using emphasis if absolutely necessary. Make sure that is the case.", WarningSeverity.LOW));

            return new Text(emphasis.getContent(), emphasisFont, emphasisFontSize, emphasisFontColour);
        } else if (paragraphInstruction instanceof Work work) {
            return new Text(work.getWorkContent(), workFont, workFontSize, workFontColour);
        } else throw new MissingFormatArgumentException("Not implemented");
    }

    /**
     * Starts the processing phase by trying to convert the specified AST into usable objects.
     *
     * @param ast - the abstract syntax tree produced by the {@link frontend.parsing.Parser}
     */
    public void processAST(@NonNull final AST ast) {
        System.out.println("AST has been generated generated.");

        System.out.println("Now checking AST for possible warnings");
        ast.checkForWarnings();

        documentBody = ast.getDocumentBody();

        // Used to track a warning if both inches and mm are used, which may be considered inconsistent
        boolean inchesUsed = false;
        boolean mmUsed = false;

        var styleConfiguration = ast.getConfiguration().getStyle();

        // Check if the user demands a different style than the default style
        if (styleConfiguration.getBaseStyle() != null) {
            // Try to apply that style
            usedStyleGuide = StyleTable.nameToStyleGuide(ast.getConfiguration().getStyle().getBaseStyle());
        } else {
            // Use the default style, instead
            usedStyleGuide = new MLA9();
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
                height = pointsPerInch * Float.parseFloat(asNumber);
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
                width = pointsPerInch * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                width = pointsPerMM * Float.parseFloat(layout.getWidth());
            }
        } else width = usedStyleGuide.pageFormat().getWidth();

        documentTitle = ast.getConfiguration().getTitle();

        // Set the document dimensions
        dimensions = new PDRectangle(width, height);

        // Check if the user demands a custom margin
        if (layout.getMargin() != null) {
            if (layout.getMargin().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getMargin().substring(0, layout.getMargin().length() - 2);
                margin = pointsPerInch * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                margin = pointsPerMM * Float.parseFloat(layout.getMargin());
            }
        } else margin = pointsPerInch * usedStyleGuide.margin();

        // Check if the user demands a custom spacing
        if (layout.getSpacing() != null) {
            try {
                Processor.spacing = Float.parseFloat(layout.getSpacing());
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("10: Incorrect spacing constant.");
            }
        } else Processor.spacing = usedStyleGuide.spacing();

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

        if (numeration.getAuthorLimit() != null) {
            if (numeration.getAuthorLimit().equals("None")) {
                numerationLimit = null;
            } else {
                try {
                    var asInt = Integer.parseInt(numeration.getAuthorLimit());
                    if (asInt < 1) throw new IncorrectFormatException("13: Integer larger than zero expected.");
                    numerationLimit = asInt;
                } catch (NumberFormatException e) {
                    throw new IncorrectFormatException("13: Integer larger than zero expected.");
                }
            }
        } else numerationLimit = usedStyleGuide.numerationLimit();

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

        // Check if the user demands a custom author name before the numeration
        if (numeration.getAuthorName() != null) {
            numerationAuthorName = switch (numeration.getAuthorName()) {
                case "name" -> NumerationAuthorName.NAME;
                case "firstname" -> NumerationAuthorName.FIRST_NAME;
                case "lastname" -> NumerationAuthorName.LAST_NAME;
                case "Full Name" -> NumerationAuthorName.FULL_NAME;
                case "None" -> NumerationAuthorName.NONE;
                default -> throw new IncorrectFormatException("14: Author numeration name expected.");
            };
        } else numerationAuthorName = usedStyleGuide.numerationAuthorName();

        var pageNumerationMargin = numeration.getMargin();

        // Check if the user demands a custom numeration margin
        if (pageNumerationMargin != null) {
            try {
                float unit;
                if (numeration.getMargin().endsWith("in")) {
                    inchesUsed = true;

                    pageNumerationMargin = pageNumerationMargin.substring(0, pageNumerationMargin.length() - 2);
                    unit = pointsPerInch;
                } else {
                    unit = pointsPerMM;

                    mmUsed = true;
                }

                float asNumber = Float.parseFloat(pageNumerationMargin);
                if (asNumber < 0) throw new IncorrectFormatException("2: Non-negative decimal expected.");
                else numerationMargin = asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        } else numerationMargin = pointsPerInch * usedStyleGuide.numerationMargin();

        var structure = styleConfiguration.getStructure();

        // Check if the user demands a custom font
        if (structure.getSentence().getFont().getName() != null) {
            Processor.sentenceFont = fontLookUp(structure.getSentence().getFont().getName());
        } else Processor.sentenceFont = usedStyleGuide.font();

        // Check if the user demands a custom font size
        if (structure.getSentence().getFont().getSize() != null) {
            try {
                var asNumber = Integer.parseInt(structure.getSentence().getFont().getSize());
                if (asNumber < 1) {
                    throw new IncorrectFormatException("13: Integer larger than zero expected.");
                } else Processor.sentenceFontSize = asNumber;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("13: Integer larger than zero expected.");
            }
        } else Processor.sentenceFontSize = usedStyleGuide.fontSize();

        // Check if the user demands a custom font colour
        if (structure.getSentence().getFont().getColour() != null) {
            try {
                sentenceFontColour = Color.decode(structure.getSentence().getFont().getColour());
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("4: Colour expected.");
            }
        } else sentenceFontColour = usedStyleGuide.fontColour();

        var emphasis = structure.getEmphasis();

        if (emphasis.getAllowEmphasis() != null) {
            allowEmphasis = switch (emphasis.getAllowEmphasis()) {
                case "Yes" -> AllowanceType.YES;
                case "No" -> AllowanceType.NO;
                case "If Necessary" -> AllowanceType.IF_NECESSARY;
                default -> throw new IncorrectFormatException("5: Allowance type expected.");
            };
        } else allowEmphasis = usedStyleGuide.allowsEmphasis();

        var emphasisFont = structure.getEmphasis().getFont();

        if (emphasisFont.getName() != null) Processor.emphasisFont = fontLookUp(emphasisFont.getName());
        else Processor.emphasisFont = usedStyleGuide.emphasisFont();

        // Check if the user demands a custom font size
        if (emphasisFont.getSize() != null) {
            try {
                var asNumber = Integer.parseInt(emphasisFont.getSize());
                if (asNumber < 1) {
                    throw new IncorrectFormatException("13: Integer larger than zero expected.");
                } else Processor.emphasisFontSize = asNumber;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("13: Integer larger than zero expected.");
            }
        } else Processor.emphasisFontSize = usedStyleGuide.emphasisFontSize();

        // Check if the user demands a custom font colour
        if (emphasisFont.getColour() != null) {
            try {
                emphasisFontColour = Color.decode(emphasisFont.getColour());
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("4: Colour expected.");
            }
        } else emphasisFontColour = usedStyleGuide.emphasisFontColour();

        var workFont = structure.getWork().getFont();

        if (workFont.getName() != null) Processor.workFont = fontLookUp(workFont.getName());
        else Processor.workFont = usedStyleGuide.workFont();

        // Check if the user demands a custom font size
        if (workFont.getSize() != null) {
            try {
                var asNumber = Integer.parseInt(workFont.getSize());
                if (asNumber < 1) {
                    throw new IncorrectFormatException("13: Integer larger than zero expected.");
                } else Processor.workFontSize = asNumber;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("13: Integer larger than zero expected.");
            }
        } else Processor.workFontSize = usedStyleGuide.workFontSize();

        // Check if the user demands a custom font colour
        if (workFont.getColour() != null) {
            try {
                workFontColour = Color.decode(workFont.getColour());
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("4: Colour expected.");
            }
        } else workFontColour = usedStyleGuide.workFontColour();

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

        // Check if the user demands a custom document type
        if (ast.getConfiguration().getDocumentType() != null) {
            try {
                documentType = DocumentType.valueOf(ast.getConfiguration().getDocumentType().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("8: Document type expected.");
            }
        } else documentType = usedStyleGuide.documentType();

        // Convert the given author nodes to actual author objects
        var authors = ast.getConfiguration().getAuthors().getAuthors();
        Processor.authors = new Author[authors.size()];
        int i = 0;
        for (var author : authors) {
            if (author.getName() == null && author.getFirstname() == null && author.getLastname() == null)
                throw new MissingConfigurationException("6: An author requires a name configuration, but neither " +
                        "name, firstname nor lastname has been configured.");
            else if (author.getName() != null && (author.getFirstname() != null || author.getLastname() != null))
                throw new IllegalConfigurationException("7: An author can only be given a name configuration " +
                        "OR a firstname and lastname configuration.");
            else if (author.getName() == null && author.getFirstname() != null && author.getLastname() == null)
                throw new IllegalConfigurationException("8: An author cannot only have a firstname configuration. " +
                        "Either also provide a lastname configuration or only use the name configuration.");
            else if (author.getName() == null && author.getFirstname() == null) {
                throw new IllegalConfigurationException("9: An author cannot only have a lastname configuration. " +
                        "Either also provide a firstname configuration or only use the name configuration.");
            }

            if (author.getName() == null) {
                if (author.getFirstname().isBlank() || author.getLastname().isBlank() ||
                        author.getId() != null && author.getId().isBlank() ||
                        author.getTitle() != null && author.getTitle().isBlank())
                    throw new MissingMemberException("1: A text component cannot be blank.");

                var newAuthor = new Author(author.getFirstname(), author.getLastname());
                newAuthor.setTitle(author.getTitle());
                newAuthor.setId(author.getId());
                Processor.authors[i] = newAuthor;
            } else {
                if (author.getName().isBlank() || author.getId() != null && author.getId().isBlank() ||
                        author.getTitle() != null && author.getTitle().isBlank())
                    throw new MissingMemberException("1: A text component cannot be blank.");

                var newAuthor = new Author(author.getName());
                newAuthor.setTitle(author.getTitle());
                newAuthor.setId(author.getId());
                Processor.authors[i] = newAuthor;
            }

            for (int j = 0; j < i; j++) {
                if (Processor.authors[j].nameToString().equals(Processor.authors[i].nameToString())) {
                    WarningQueue.enqueue(new UnlikelinessWarning(
                            "3: Two authors have the same name, which seems unlikely. " +
                                    "Check if that is correct. \n\t 1: " +
                                    Processor.authors[j] + ". \n\tAuthor 2: " + Processor.authors[i],
                            WarningSeverity.CRITICAL));
                }
            }

            i++;
        }

        // Convert the given author nodes to actual author objects
        var assessors = ast.getConfiguration().getAssessors().getAssessors();
        Processor.assessors = new Assessor[assessors.size()];
        int j = 0;
        for (var assessor : assessors) {
            if (assessor.getName() == null && assessor.getFirstname() == null && assessor.getLastname() == null)
                throw new MissingConfigurationException("1: An assessor requires a name configuration, but neither " +
                        "name, firstname nor lastname has been configured.");
            else if (assessor.getName() != null && (assessor.getFirstname() != null || assessor.getLastname() != null))
                throw new IllegalConfigurationException("2: An assessor can only be given a name configuration " +
                        "OR a firstname and lastname configuration.");
            else if (assessor.getName() == null && assessor.getFirstname() != null && assessor.getLastname() == null)
                throw new IllegalConfigurationException("3: An assessor cannot only have a firstname configuration. " +
                        "Either also provide a lastname configuration or only use the name configuration.");
            else if (assessor.getName() == null && assessor.getFirstname() == null)
                throw new IllegalConfigurationException("4: An assessor cannot only have a lastname configuration. " +
                        "Either also provide a firstname configuration or only use the name configuration.");

            if (assessor.getName() == null) {
                if (assessor.getFirstname().isBlank() || assessor.getLastname().isBlank() ||
                        assessor.getRole() != null && assessor.getRole().isBlank() ||
                        assessor.getTitle() != null && assessor.getTitle().isBlank())
                    throw new MissingMemberException("1: A text component cannot be blank.");

                var newAssessor = new Assessor(assessor.getFirstname(), assessor.getLastname());
                newAssessor.setTitle(assessor.getTitle());
                newAssessor.setRole(assessor.getRole());
                Processor.assessors[j] = newAssessor;
            } else {
                if (assessor.getName().isBlank() || assessor.getRole() != null && assessor.getRole().isBlank() ||
                        assessor.getTitle() != null && assessor.getTitle().isBlank())
                    throw new MissingMemberException("1: A text component cannot be blank.");

                var newAssessor = new Assessor(assessor.getName());
                newAssessor.setTitle(assessor.getTitle());
                newAssessor.setRole(assessor.getRole());
                Processor.assessors[j] = newAssessor;
            }

            for (int k = 0; k < j; k++) {
                if (Processor.assessors[k].nameToString().equals(Processor.assessors[k].nameToString())) {
                    WarningQueue.enqueue(new UnlikelinessWarning(
                            "4: Two assessors have the same name, which seems unlikely. " +
                                    "Check if that is correct. \n\tAssessor 1: " + Processor.assessors[k] +
                                    ". \n\tAssessor 2: " + Processor.assessors[k],
                            WarningSeverity.CRITICAL));
                }
            }

            j++;
        }

        // Publication
        var publication = ast.getConfiguration().getPublication();

        // Check if the user demands to render the date
        if (publication.getDate() != null) {
            if (!publication.getDate().equals("None")) {
                if (publication.getDate().length() != 10)
                    throw new IncorrectFormatException("1: The specified date is not 'None' and does not adhere to " +
                            "the British date format: 'dd/MM/yyyy' For example, June 3, 2023, is 03/06/2023. Date: " +
                            publication.getDate());

                for (int k = 0; k < 10; k++) {
                    if (k == 2 || k == 5) {
                        if (publication.getDate().charAt(k) != '/') {
                            throw new IncorrectFormatException("1: The specified date is not 'None' and does not" +
                                    " adhere to the British date format: 'dd/MM/yyyy'" +
                                    " For example, June 3, 2023, is 03/06/2023. Date: " + publication.getDate());
                        }
                    } else if (!Character.isDigit(publication.getDate().charAt(k))) {
                        throw new IncorrectFormatException("1: The specified date is not 'None' and does" +
                                " not adhere to the British date format: 'dd/MM/yyyy'" +
                                " For example, June 3, 2023, is 03/06/2023. Date: " + publication.getDate());
                    }
                }

                publicationDate = LocalDate.parse(publication.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else publicationDate = null;
        } else publicationDate = LocalDate.now();

        publicationTitle = publication.getTitle();
        publicationInstitution = publication.getInstitution();

        if (publication.getChair() != null && publicationInstitution == null)
            throw new MissingMemberException("8: Cannot use a publication chair if no publication institution is defined.");
        else publicationChair = publication.getChair();

        if (publication.getSemester() != null) {
            publicationSemester = publication.getSemester();

            var nonStandardSemester = (!publicationSemester.startsWith("WS ") && !publicationSemester.startsWith("SS ")) ||
                    publicationSemester.length() != 7 ||
                    !Character.isDigit(publicationSemester.charAt(3)) ||
                    !Character.isDigit(publicationSemester.charAt(4)) ||
                    !Character.isDigit(publicationSemester.charAt(5)) ||
                    !Character.isDigit(publicationSemester.charAt(6));

            if (nonStandardSemester)
                WarningQueue.enqueue(new SelfCheckWarning("2: The semester format used appears to deviate " +
                        "from the standard \"WS XXXX\" or \"SS XXXX\" where XXXX is the year. While this may not be " +
                        "an issue in your specific context or country, please self-check the format to ensure it" +
                        " aligns with your intended representation.", WarningSeverity.LOW));
        }

        if (inchesUsed && mmUsed) WarningQueue.enqueue(new InconsistencyWarning(
                "3: The style configuration uses both inches and millimeters.",
                WarningSeverity.LOW));

        // For debugging purposes
        System.out.println("Finished processing.");
        System.out.println(this);

        // Start the creation process
        DocumentCreator.create();
    }

    private PDFont fontLookUp(String name) {
        // Check if the user is trying to import a font from their operating system
        if (name.startsWith("@")) {
            final String path = "C:\\Windows\\Fonts\\" + name.substring(1) + ".ttf";
            try {
                return PDType0Font.load(PageAssembler.getDocument(), new File(path));
            } catch (IOException e) {
                throw new MissingMemberException("7: The specified windows font cannot be located.");
            }
        }

        return switch (name) {
            case "Times Roman" -> PDType1Font.TIMES_ROMAN;
            case "Helvetica" -> PDType1Font.HELVETICA;
            case "Courier" -> PDType1Font.COURIER;
            case "Symbol" -> PDType1Font.SYMBOL;
            case "Zapf Dingbats" -> PDType1Font.ZAPF_DINGBATS;
            default -> throw new MissingMemberException("6: The specified font is missing or does" +
                    " not exist.");
        };
    }

}
