package processing;

import creation.DocumentCreator;
import creation.PageAssembler;
import creation.Text;
import error.ConfigurationException;
import error.ContentException;
import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.AST;
import frontend.ast.BodyNode;
import frontend.ast.config.Title;
import frontend.ast.config.style.Citation;
import frontend.ast.config.style.Font;
import frontend.ast.config.style.Structure;
import lombok.Getter;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import processing.bibliography.BibliographySource;
import processing.bibliography.Book;
import processing.numeration.NumerationAuthorName;
import processing.numeration.NumerationPosition;
import processing.numeration.NumerationType;
import processing.person.Assessor;
import processing.person.Author;
import processing.style.MLA9;
import processing.style.StyleGuide;
import processing.style.StyleTable;
import warning.*;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The processor class translates the AST given by the {@link frontend.parsing.Parser} to actual objects
 * that can be used when creating the document
 *
 * @version 1.0
 * @since 1.0
 */
public class Processor {

    /**
     * Determines the version of this compiler implementation.
     */
    public static final String COMPILER_VERSION = "1.0";
    /**
     * Determines the amount of points that make up one inch.
     * This is used to properly compute distances and sizes.
     */
    public static final int POINTS_PER_INCH = 72;
    /**
     * Determines the amount of points that make up one millimeter.
     * This is used to properly compute distances and sizes.
     */
    public static final float POINTS_PER_MM = 1 / (10 * 2.54f) * POINTS_PER_INCH;
    @Getter
    private static final HashMap<String, BibliographySource> bibliographyEntries = new HashMap<>();

    @Getter
    private static FontData sentenceFontData;
    @Getter
    private static FontData workFontData;
    @Getter
    private static FontData emphasisFontData;
    @Getter
    private static FontData[] chapterFontData;

    /**
     * Determines how the names of the authors should be inserted before the page number.
     * If there should not be a name before the page number, this value is null.
     */
    @Getter
    private static NumerationAuthorName numerationAuthorName;
    /**
     * Determines the page number margin to the top or bottom of the document.
     * Note that the default layout margin is used for the left or right side.
     */
    @Getter
    private static float numerationMargin;
    /**
     * Determines where in the document page numbers should be displayed
     */
    @Getter
    private static NumerationPosition numerationPosition;
    /**
     * Determines how page numbers should be represented
     */
    @Getter
    private static NumerationType numerationType;
    /**
     * Contains all page numbers that the user does not want to have page numbers.
     * For each created page, the list will be checked if it contains the current page number,
     * and if it does, it will not receive a numeration stamp.
     */
    @Getter
    private static List<Integer> skippedPages;
    /**
     * Contains the maximum amount of author names that should be rendered in the numeration.
     * If there is no limitation, this is null.
     */
    @Getter
    private static Integer numerationLimit;
    /**
     * Determines the paragraph spacing as a float, which is the space between each line in a paragraph.
     * The spacing is multiplied by the font size (and font size increment factor) to calculate line leading.
     * Note that the unit is a float! It is not represented in points, inches or any other measurement.
     */
    @Getter
    private static float spacing;
    /**
     * Determines the dimensions of the document (the width and height)
     */
    @Getter
    private static PDRectangle dimensions;
    /**
     * Determines the margin in points (pt.) to all four sides of the document.
     * All components need to have a minimum position of the margin to the left and top,
     * and a maximum position of the margin to the right and bottom.
     */
    @Getter
    private static float margin;
    /**
     * Determines the type of style guide which should be used during compilation
     */
    @Getter
    private static StyleGuide usedStyleGuide;
    /**
     * Determines the title of the document, which can be displayed using the title instruction.
     * Note that the Title class is taken from the AST package simply to not have to duplicate it.
     */
    @Getter
    private static Title documentTitle;


    //// SENTENCES ////
    /**
     * Determines the paragraph indentation, which is the amount of space that a new paragraph will be
     * indented to
     */
    @Getter
    private static float paragraphIndentation;
    /**
     * Determines if the user is allowed to use italic text in a sentence
     */
    @Getter
    private static AllowanceType allowEmphasis;

    /**
     * Yields true if the bibliography file is being processed and false if the document file is being processed
     */
    @Getter
    private static boolean isProcessingBibliography;


    //// AUTHORS & ASSESSORS ////
    /**
     * Determines the authors that have worked on the document.
     * Note that this only includes the authors of the working document, it does not include authors that
     * have been cited from in the document
     */
    @Getter
    private static Author[] authors;
    /**
     * Determines the assessors that may assess the document
     */
    @Getter
    private static Assessor[] assessors;
    /**
     * Determines the date of document publication, which is either given by the user or created by Pipp.
     * Note that if the user has explicitly expressed not to display the date, this is null.
     */
    @Getter
    private static LocalDate publicationDate;
    /**
     * Determines the chair of the publication institution.
     * Note that this should not be allowed to be set if the institution is not set.
     */
    @Getter
    private static String publicationChair;
    //// Publication ////
    @Getter
    private static LinkedList<BodyNode> documentBody = new LinkedList<>();
    /**
     * Determines the title of the publication, which in most style guides is put in the header or title page.
     * Note that the Title class is taken from the AST package simply to not have to duplicate it.
     */
    @Getter
    private static Title publicationTitle;
    /**
     * Determines the institution of the publication context.
     * An example could be "XYZ University".
     */
    @Getter
    private static String publicationInstitution;
    /**
     * Determines the semester of the publication context.
     * This is used academic papers.
     */
    @Getter
    private static String publicationSemester;

    /**
     * Prevents instantiation
     */
    private Processor() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    public static float getAvailableContentWidth() {
        return Processor.dimensions.getWidth() - 2 * Processor.margin;
    }

    /**
     * Starts the processing phase by trying to convert the specified AST into usable objects.
     *
     * @param ast - the abstract syntax tree produced by the {@link frontend.parsing.Parser}
     */
    public static void processAST(@NonNull final AST ast) {
        final var logger = Logger.getLogger(AST.class.getName());
        logger.info("AST has been generated generated.");

        isProcessingBibliography = true;
        final var entries = ast.getBibliographySources();
        entries.forEach(entry -> {
            entry.checkForWarnings();

            if (bibliographyEntries.containsKey(entry.getId()))
                throw new ContentException("3: Bibliography entry with ID '" + entry.getId() + "' already exists.");

            var newEntry = new BibliographySource();
            if (entry.getType() != null) {
                if (entry.getType().equals("Book")) {
                    newEntry = new Book();

                    if (entry.getPublication().getName() != null || entry.getPublication().getYear() != null) {
                        final var year = entry.getPublication().getYear();
                        if (year != null) ((Book) newEntry).setPublicationYear(year.trim());

                        final var name = entry.getPublication().getName();
                        if (name != null) ((Book) newEntry).setPublicationName(name.trim());
                    } else {
                        WarningQueue.enqueue(new SelfCheckWarning(
                                "3: The bibliography entry with the ID '" + entry.getId() +
                                        "' does not have a publication name or year.", WarningSeverity.CRITICAL));
                    }
                } else {
                    WarningQueue.enqueue(new MissingMemberWarning(
                            "2: The bibliography entry type '" + entry.getType() + "' is not supported.",
                            WarningSeverity.HIGH));
                }

            } else {
                WarningQueue.enqueue(new MissingMemberWarning("3: The bibliography entry with the ID '" +
                        entry.getId() + "' does not have a type.", WarningSeverity.CRITICAL));
            }

            newEntry.setId(entry.getId());
            newEntry.setTitle(entry.getTitle().trim());
            newEntry.setAuthors(entry
                    .getAuthors()
                    .getAuthorList()
                    .stream()
                    .map(author -> {
                        final Author newAuthor;
                        if (author.getFirstname() == null) newAuthor = new Author(author.getName().trim());
                        else newAuthor = new Author(author.getFirstname().trim(), author.getLastname().trim());

                        if (author.getTitle() != null) newAuthor.setTitle(author.getTitle().trim());

                        return newAuthor;
                    })
                    .toArray(Author[]::new)
            );

            bibliographyEntries.put(entry.getId(), newEntry);
        });
        isProcessingBibliography = false;

        logger.info("Now checking AST for possible warnings");
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

        // Check if the user demands a custom document dimension

        float height;
        if (layout.getHeight() != null) {
            // Check if the user wants to use inches
            if (layout.getHeight().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getHeight().substring(0, layout.getHeight().length() - 2);
                height = POINTS_PER_INCH * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                height = POINTS_PER_MM * Float.parseFloat(layout.getHeight());
            }
        } else height = usedStyleGuide.pageFormat().getHeight();

        if (height < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (height < 500)
            WarningQueue.enqueue(new UnlikelinessWarning("5: Odd layout dimensions specified.", WarningSeverity.HIGH));

        float width;
        if (layout.getWidth() != null) {
            // Check if the user wants to use inches
            if (layout.getWidth().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getWidth().substring(0, layout.getWidth().length() - 2);
                width = POINTS_PER_INCH * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                width = POINTS_PER_MM * Float.parseFloat(layout.getWidth());
            }
        } else width = usedStyleGuide.pageFormat().getWidth();

        if (width < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (width < 400)
            WarningQueue.enqueue(new UnlikelinessWarning("5: Odd layout dimensions specified.", WarningSeverity.HIGH));

        documentTitle = ast.getConfiguration().getTitle();

        // Set the document dimensions
        dimensions = new PDRectangle(width, height);

        // Check if the user demands a custom margin
        if (layout.getMargin() != null) {
            if (layout.getMargin().endsWith("in")) {
                inchesUsed = true;

                var asNumber = layout.getMargin().substring(0, layout.getMargin().length() - 2);
                margin = POINTS_PER_INCH * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                margin = POINTS_PER_MM * Float.parseFloat(layout.getMargin());
            }
        } else margin = POINTS_PER_INCH * usedStyleGuide.margin();

        if (margin < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (margin > 200)
            WarningQueue.enqueue(new UnlikelinessWarning("5: Odd layout dimensions specified.", WarningSeverity.HIGH));

        // Check if the user demands a custom spacing
        if (layout.getSpacing() != null) {
            try {
                Processor.spacing = Float.parseFloat(layout.getSpacing());
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("10: Incorrect spacing constant.");
            }
        } else Processor.spacing = usedStyleGuide.spacing();

        if (spacing < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (spacing >= 3)
            WarningQueue.enqueue(new UnlikelinessWarning("5: Odd layout dimensions specified.", WarningSeverity.HIGH));

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
                    unit = POINTS_PER_INCH;
                } else {
                    unit = POINTS_PER_MM;

                    mmUsed = true;
                }

                float asNumber = Float.parseFloat(pageNumerationMargin);
                if (asNumber < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
                else numerationMargin = asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
            }
        } else numerationMargin = POINTS_PER_INCH * usedStyleGuide.numerationMargin();

        var structure = styleConfiguration.getStructure();
        processFonts(structure);

        var emphasis = structure.getEmphasis();

        if (emphasis.getAllowEmphasis() != null) {
            allowEmphasis = switch (emphasis.getAllowEmphasis()) {
                case "Yes" -> AllowanceType.YES;
                case "No" -> AllowanceType.NO;
                case "If Necessary" -> AllowanceType.IF_NECESSARY;
                default -> throw new IncorrectFormatException("5: Allowance type expected.");
            };
        } else allowEmphasis = usedStyleGuide.allowsEmphasis();

        var paragraph = structure.getParagraph();
        var indentation = paragraph.getIndentation();

        // Check if the user demands a custom paragraph indentation
        if (indentation != null) {
            try {
                float unit;
                if (indentation.endsWith("in")) {
                    inchesUsed = true;

                    indentation = indentation.substring(0, indentation.length() - 2);
                    unit = POINTS_PER_INCH;
                } else {
                    unit = POINTS_PER_MM;

                    mmUsed = true;
                }

                float asNumber = Float.parseFloat(indentation);
                if (asNumber < 0) {
                    throw new IncorrectFormatException("2: Non-negative decimal expected.");
                } else paragraphIndentation = asNumber * unit;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("2: Non-negative decimal expected.");
            }
        } else paragraphIndentation = usedStyleGuide.paragraphIndentation() * POINTS_PER_INCH;

        if (paragraphIndentation > width)
            throw new ConfigurationException("10: The specified paragraph indentation exceeds the page width.");

        // Convert the given author nodes to actual author objects
        var authors = ast.getConfiguration().getAuthors().getAuthorList();
        Processor.authors = new Author[authors.size()];
        int i = 0;
        for (var author : authors) {
            if (author.getName() == null) {
                if (author.getFirstname().isBlank() || author.getLastname().isBlank() ||
                        author.getId() != null && author.getId().isBlank() ||
                        author.getTitle() != null && author.getTitle().isBlank())
                    throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

                var newAuthor = new Author(author.getFirstname(), author.getLastname());
                newAuthor.setTitle(author.getTitle());
                newAuthor.setId(author.getId());
                Processor.authors[i] = newAuthor;
            } else {
                if (author.getName().isBlank() || author.getId() != null && author.getId().isBlank() ||
                        author.getTitle() != null && author.getTitle().isBlank())
                    throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

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
        var assessors = ast.getConfiguration().getAssessors().getAssessorsList();
        Processor.assessors = new Assessor[assessors.size()];
        int j = 0;
        for (var assessor : assessors) {
            if (assessor.getName() == null && assessor.getFirstname() == null && assessor.getLastname() == null)
                throw new ConfigurationException("1: An assessor requires a name configuration, but neither " +
                        "name, firstname nor lastname has been configured.");
            else if (assessor.getName() != null && (assessor.getFirstname() != null || assessor.getLastname() != null))
                throw new ConfigurationException("2: An assessor can only be given a name configuration " +
                        "OR a firstname and lastname configuration.");
            else if (assessor.getName() == null && assessor.getFirstname() != null && assessor.getLastname() == null)
                throw new ConfigurationException("3: An assessor cannot only have a firstname configuration. " +
                        "Either also provide a lastname configuration or only use the name configuration.");
            else if (assessor.getName() == null && assessor.getFirstname() == null)
                throw new ConfigurationException("4: An assessor cannot only have a lastname configuration. " +
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
                if (Processor.assessors[k].nameToString().equals(Processor.assessors[j].nameToString())) {
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
        logger.info("Finished processing.");

        // Start the creation process
        DocumentCreator.create();
    }

    /**
     * Looks up the proper entry in the bibliography and returns an array of text components used to render the
     * citation. Throws an error exception if the entry does not exist in the bibliography.
     *
     * @param citation the non-null citation that should be processed
     * @return an array of text components to be rendered when the citation is handled
     */
    public static Text[] processCitation(@NonNull final Citation citation) {
        final var source = citation.getSource().trim();
        final var referenceSource = bibliographyEntries.get(source);

        if (referenceSource == null)
            throw new ContentException("4: Bibliography entry with ID '" + source + "' does not exist.");

        final var content = citation.getCitedContent().trim();
        final var numeration = citation.getNumeration().trim();

        // Mark the source as cited
        referenceSource.setHasBeenCited(true);

        return usedStyleGuide.formatCitation(referenceSource, content, numeration);
    }

    private static void processFonts(@NonNull Structure structure) {
        sentenceFontData = fontNodeToData(structure.getSentence().getFont(), usedStyleGuide.sentenceFontData());
        workFontData = fontNodeToData(structure.getWork().getFont(), usedStyleGuide.workFontData());
        emphasisFontData = fontNodeToData(structure.getEmphasis().getFont(), usedStyleGuide.emphasisFontData());
        // TODO Chapters here
    }

    private static @NonNull FontData fontNodeToData(@NonNull Font fontNode, @NonNull FontData alternativeDefaultData) {
        final PDFont fontFamily = fontNode.getName() == null
                ? alternativeDefaultData.font()
                : fontLookUp(fontNode.getName());

        final float fontSize;
        if (fontNode.getSize() != null) {
            try {
                var asNumber = Integer.parseInt(fontNode.getSize());
                if (asNumber < 1) {
                    throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
                } else fontSize = asNumber;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
            }
        } else fontSize = alternativeDefaultData.fontSize();

        final Color fontColour;
        if (fontNode.getColour() != null) {
            try {
                fontColour = Color.decode(fontNode.getColour());
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException("4: Colour expected.");
            }
        } else fontColour = alternativeDefaultData.fontColor();

        return new FontData(fontFamily, fontSize, fontColour);
    }

    /**
     * Tries to convert the given font name into a PDFont object to be used during the document creation phase.
     * If the name starts with the @ prefix, it tries to use a windows path to the fonts-folder.
     * Else, it must be one of the predefined PD fonts.
     *
     * @param name the non-null name of the font
     * @return the font as a PDFont object
     */
    private static @NonNull PDFont fontLookUp(@NonNull final String name) {
        // Check if the user is trying to import a font from their operating system
        if (name.startsWith("@")) {
            final String path = "C:\\Windows\\Fonts\\" + name.substring(1) + ".ttf";
            try {
                final var targetStream = new FileInputStream(Paths.get(path).toFile());
                return PDType0Font.load(PageAssembler.getDocument(), targetStream, false);
            } catch (IOException e) {
                throw new MissingMemberException("7: The specified windows font cannot be located.");
            }
        }

        return switch (name) {
            case "Times Roman" -> new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
            case "Helvetica" -> new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            case "Courier" -> new PDType1Font(Standard14Fonts.FontName.COURIER);
            case "Symbol" -> new PDType1Font(Standard14Fonts.FontName.SYMBOL);
            case "Zapf Dingbats" -> new PDType1Font(Standard14Fonts.FontName.ZAPF_DINGBATS);
            default -> throw new MissingMemberException("6: The specified font is missing or does" +
                    " not exist.");
        };
    }

}
