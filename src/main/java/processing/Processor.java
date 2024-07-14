package processing;

import creation.content.ContentAlignment;
import creation.content.text.Text;
import creation.document.DocumentCreator;
import creation.page.PageAssembler;
import error.ConfigurationException;
import error.ContentException;
import error.IncorrectFormatException;
import error.MissingMemberException;
import frontend.ast.AST;
import frontend.ast.config.Publication;
import frontend.ast.config.Title;
import frontend.ast.config.style.Font;
import frontend.ast.config.style.*;
import frontend.ast.structure.BodyNode;
import lombok.Getter;
import lombok.NonNull;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import processing.bibliography.BibliographySource;
import processing.bibliography.BibliographySourceTable;
import processing.constant.AllowanceType;
import processing.constant.ChapterSpacingType;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private static FontData[] chapterSentenceFontData;
    @Getter
    private static FontData[] chapterWorkFontData;
    @Getter
    private static FontData[] chapterEmphasisFontData;
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
    /**
     * Determines the paragraph indentation, which is the amount of space that a new paragraph will be
     * indented to
     */
    @Getter
    private static float paragraphIndentation;


    //// SENTENCES ////
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
    /**
     * Determines the authors that have worked on the document.
     * Note that this only includes the authors of the working document, it does not include authors that
     * have been cited from in the document
     */
    @Getter
    private static Author[] authors;


    //// AUTHORS & ASSESSORS ////
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
    private static boolean inchesUsed;
    private static boolean mmUsed;
    @Getter
    private static ContentAlignment chapterAlignment;
    @Getter
    private static ChapterSpacingType chapterSpacingType;

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
        final var logger = Logger.getLogger(Processor.class.getName());
        logger.info(ast.toString());

        ast.checkForWarnings();

        processBibliography(ast.getBibliographySources());
        logger.info("Successfully processed the bibliography");

        documentBody = ast.getDocumentBody();
        final var configuration = ast.getConfiguration();

        documentTitle = configuration.getTitle();
        processStyleConfiguration(configuration.getStyle());
        processDocumentAuthors(configuration.getAuthors().getAuthorList());
        processDocumentAssessors(configuration.getAssessors().getAssessorsList());
        processPublication(configuration.getPublication());

        if (inchesUsed && mmUsed)
            WarningQueue.enqueue(new InconsistencyWarning("3: The style configuration uses both inches and " +
                    "millimeters.", WarningSeverity.LOW));

        // After processing, start the creation process
        logger.info("Successfully finished processing.");
        DocumentCreator.create();
    }

    private static void processPublication(@NonNull final Publication publication) {
        processPublicationDate(publication.getDate());
        processPublicationSemester(publication.getSemester());

        publicationTitle = publication.getTitle();
        publicationInstitution = publication.getInstitution();

        if (publication.getChair() != null && publicationInstitution == null)
            throw new MissingMemberException("8: Cannot use a publication chair if no publication institution is defined.");
        else publicationChair = publication.getChair();
    }

    private static void processPublicationSemester(String semester) {
        if (semester == null) return;
        var nonStandardSemester = (!semester.startsWith("WS ") && !semester.startsWith("SS ")) ||
                publicationSemester.length() != 7 ||
                !Character.isDigit(semester.charAt(3)) ||
                !Character.isDigit(semester.charAt(4)) ||
                !Character.isDigit(semester.charAt(5)) ||
                !Character.isDigit(semester.charAt(6));

        if (nonStandardSemester)
            WarningQueue.enqueue(new SelfCheckWarning("2: The semester format used appears to deviate " +
                    "from the standard \"WS XXXX\" or \"SS XXXX\" where XXXX is the year. While this may not be " +
                    "an issue in your specific context or country, please self-check the format to ensure it" +
                    " aligns with your intended representation.", WarningSeverity.LOW));

        publicationSemester = semester;
    }

    private static void processPublicationDate(String date) {
        if (date == null) {
            publicationDate = LocalDate.now();
            return;
        } else if (date.equals("None")) {
            publicationDate = null;
            return;
        } else if (date.length() != 10)
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_1 + date);

        for (int k = 0; k < 10; k++) {
            if (k == 2 || k == 5) {
                if (date.charAt(k) != '/')
                    throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_1 + date);
            } else if (!Character.isDigit(date.charAt(k))) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_1 + date);
            }
        }

        publicationDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static void processDocumentAssessors(ArrayList<frontend.ast.config.person.Assessor> assessorsList) {
        Processor.assessors = new Assessor[assessorsList.size()];
        for (int i = 0; i < assessorsList.size(); i++) {
            processAssessor(assessorsList.get(i), i);

            for (int j = 0; j < i; j++) {
                if (Processor.assessors[j].nameToString().equals(Processor.assessors[i].nameToString())) {
                    WarningQueue.enqueue(new UnlikelinessWarning(
                            "4: Two assessors have the same name, which seems unlikely. " +
                                    "Check if that is correct. \n\tAssessor 1: " + Processor.assessors[j] +
                                    ". \n\tAssessor 2: " + Processor.assessors[j],
                            WarningSeverity.CRITICAL));
                }
            }
        }
    }

    private static void processAssessor(frontend.ast.config.person.Assessor assessor, int assessorIndex) {
        if (assessor.getName() == null) {
            if (assessor.getFirstname().isBlank() || assessor.getLastname().isBlank() ||
                    assessor.getRole() != null && assessor.getRole().isBlank() ||
                    assessor.getTitle() != null && assessor.getTitle().isBlank())
                throw new MissingMemberException("1: A text component cannot be blank.");

            var newAssessor = new Assessor(assessor.getFirstname(), assessor.getLastname());
            newAssessor.setTitle(assessor.getTitle());
            newAssessor.setRole(assessor.getRole());
            Processor.assessors[assessorIndex] = newAssessor;
        } else {
            if (assessor.getName().isBlank() || assessor.getRole() != null && assessor.getRole().isBlank() ||
                    assessor.getTitle() != null && assessor.getTitle().isBlank())
                throw new MissingMemberException("1: A text component cannot be blank.");

            var newAssessor = new Assessor(assessor.getName());
            newAssessor.setTitle(assessor.getTitle());
            newAssessor.setRole(assessor.getRole());
            Processor.assessors[assessorIndex] = newAssessor;
        }
    }

    private static void processDocumentAuthors(ArrayList<frontend.ast.config.person.Author> authors) {
        Processor.authors = new Author[authors.size()];
        for (int i = 0; i < authors.size(); i++) {
            processAuthor(authors.get(i), i);

            for (int j = 0; j < i; j++) {
                if (Processor.authors[j].nameToString().equals(Processor.authors[i].nameToString())) {
                    WarningQueue.enqueue(new UnlikelinessWarning(
                            "3: Two authors have the same name, which seems unlikely. " +
                                    "Check if that is correct. \n\tAuthor 1: " +
                                    Processor.authors[j] + ". \n\tAuthor 2: " + Processor.authors[i],
                            WarningSeverity.CRITICAL));
                }

                if (Processor.authors[j] != null && Processor.authors[i] != null &&
                        Processor.authors[j].getEmailAddress() != null &&
                        Processor.authors[i].getEmailAddress() != null &&
                        Processor.authors[j].getEmailAddress().equals(Processor.authors[i].getEmailAddress())) {
                    WarningQueue.enqueue(new UnlikelinessWarning(
                            "7: Two authors have the same email address, which seems unlikely. " +
                                    "Check if that is correct. \n\tAuthor 1: " +
                                    Processor.authors[j].getEmailAddress() + ". \n\tAuthor 2: " +
                                    Processor.authors[i].getEmailAddress(), WarningSeverity.CRITICAL));
                }
            }
        }
    }

    private static void processAuthor(frontend.ast.config.person.Author author, int authorIndex) {
        if (author.getName() == null) {
            if (author.getFirstname().isBlank() || author.getLastname().isBlank() ||
                    author.getId() != null && author.getId().isBlank() ||
                    author.getTitle() != null && author.getTitle().isBlank())
                throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

            var newAuthor = new Author(author.getFirstname(), author.getLastname());

            newAuthor.setTitle(author.getTitle());
            newAuthor.setId(author.getId());
            newAuthor.setEmailAddress(author.getEmailAddress());
            newAuthor.setArea(author.getArea());

            Processor.authors[authorIndex] = newAuthor;
        } else {
            if (author.getName().isBlank() || author.getId() != null && author.getId().isBlank() ||
                    author.getTitle() != null && author.getTitle().isBlank())
                throw new MissingMemberException(MissingMemberException.ERR_MSG_1);

            var newAuthor = new Author(author.getName());

            newAuthor.setTitle(author.getTitle());
            newAuthor.setId(author.getId());
            newAuthor.setEmailAddress(author.getEmailAddress());
            newAuthor.setArea(author.getArea());

            Processor.authors[authorIndex] = newAuthor;
        }
    }

    private static void processStyleConfiguration(@NonNull final Style styleConfiguration) {
        // Apply the default style or the style desired by the user
        if (styleConfiguration.getBaseStyle() == null) usedStyleGuide = new MLA9();
        else usedStyleGuide = StyleTable.nameToStyleGuide(styleConfiguration.getBaseStyle());

        // Note that the layout MUST be processed before the structures because some warnings depend on the
        // used dimensions
        processLayout(styleConfiguration.getLayout());
        processPageNumeration(styleConfiguration.getNumeration());
        processStructures(styleConfiguration.getStructure());
    }

    private static void processStructures(final Structure structure) {
        processFonts(structure);
        processEmphasisAllowance(structure.getEmphasis().getAllowEmphasis());
        processParagraphIndentation(structure.getParagraph().getIndentation());
        processChapters(structure.getChapters());
    }

    private static void processChapters(final Chapters chapters) {
        if (chapters.getChapterAlignment() != null) {
            try {
                final var specifiedAlignment = chapters.getChapterAlignment().trim().toUpperCase();
                chapterAlignment = ContentAlignment.valueOf(specifiedAlignment);
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_6);
            }
        } else chapterAlignment = usedStyleGuide.chapterAlignment();

        if (chapters.getLineSpacing() != null) {
            try {
                final var specifiedSpacingType = chapters.getLineSpacing().trim().toUpperCase();
                chapterSpacingType = ChapterSpacingType.valueOf(specifiedSpacingType);
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_6);
            }
        } else chapterSpacingType = usedStyleGuide.chapterSpacingType();
    }

    private static void processParagraphIndentation(String indentation) {
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
                    throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
                } else paragraphIndentation = asNumber * unit;
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
            }
        } else paragraphIndentation = usedStyleGuide.paragraphIndentation() * POINTS_PER_INCH;

        if (paragraphIndentation > dimensions.getWidth())
            throw new ConfigurationException("10: The specified paragraph indentation exceeds the page width.");
    }

    private static void processEmphasisAllowance(String allow) {
        if (allow != null) {
            allowEmphasis = switch (allow) {
                case "Yes" -> AllowanceType.YES;
                case "No" -> AllowanceType.NO;
                case "If Necessary" -> AllowanceType.IF_NECESSARY;
                default -> throw new IncorrectFormatException("5: Allowance type expected.");
            };
        } else allowEmphasis = usedStyleGuide.allowsEmphasis();
    }

    private static void processPageNumeration(@NonNull final Numeration numeration) {
        processNumerationType(numeration.getNumerationType());
        processNumerationPosition(numeration.getPosition());
        processNumerationMargin(numeration.getMargin());
        processNumerationAuthorName(numeration.getAuthorName());
        processNumerationAuthorLimit(numeration.getAuthorLimit());
        processNumerationSkippedPages(numeration.getSkippedPages());
    }

    private static void processNumerationSkippedPages(List<String> skippedPagesList) {
        skippedPages = new LinkedList<>();
        for (var skippedPage : skippedPagesList) {
            // Check if the user provided a span (for example, 5-12)
            if (skippedPage.contains("-")) {
                String[] pageSpan = skippedPage.split("-");
                processPageSpan(pageSpan);
            } else {
                try {
                    int pageNumber = Integer.parseInt(skippedPage);

                    if (pageNumber < 1) {
                        throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
                    } else skippedPages.add(pageNumber);
                } catch (IllegalArgumentException e) {
                    throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
                }
            }
        }
    }

    private static void processPageSpan(String[] pageSpan) {
        if (pageSpan.length != 2)
            throw new IncorrectFormatException("11: A page span must include exactly two page-numbers.");

        try {
            int first = Integer.parseInt(pageSpan[0]);
            int second = Integer.parseInt(pageSpan[1]);

            if (first < 1 || second < 1)
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
            else if (first >= second)
                throw new IncorrectFormatException("12: The second page-number must be greater than the " +
                        "first page-number in a page span.");

            for (int i = first; i < second; i++)
                skippedPages.add(i);
        } catch (IllegalArgumentException e) {
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
        }
    }

    private static void processNumerationMargin(String margin) {
        if (margin != null) {
            try {
                float unit;
                if (margin.endsWith("in")) {
                    inchesUsed = true;

                    margin = margin.substring(0, margin.length() - 2);
                    unit = POINTS_PER_INCH;
                } else {
                    mmUsed = true;
                    unit = POINTS_PER_MM;
                }

                float asNumber = Float.parseFloat(margin);
                if (asNumber < 0) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
                else numerationMargin = asNumber * unit;
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
            }
        } else numerationMargin = POINTS_PER_INCH * usedStyleGuide.numerationMargin();
    }

    private static void processNumerationAuthorName(final String authorName) {
        if (authorName != null) {
            numerationAuthorName = switch (authorName) {
                case "name", "Name" -> NumerationAuthorName.NAME;
                case "firstname", "Firstname" -> NumerationAuthorName.FIRST_NAME;
                case "lastname", "Lastname" -> NumerationAuthorName.LAST_NAME;
                case "Full Name" -> NumerationAuthorName.FULL_NAME;
                case "None" -> NumerationAuthorName.NONE;
                default -> throw new IncorrectFormatException("14: Author numeration name expected.");
            };
        } else numerationAuthorName = usedStyleGuide.numerationAuthorName();
    }

    private static void processNumerationAuthorLimit(final String authorLimit) {
        if (authorLimit != null) {
            if (authorLimit.equals("None")) {
                numerationLimit = null;
            } else {
                try {
                    var asInt = Integer.parseInt(authorLimit);
                    if (asInt < 1) throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
                    numerationLimit = asInt;
                } catch (NumberFormatException e) {
                    throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_13);
                }
            }
        } else numerationLimit = usedStyleGuide.numerationLimit();
    }

    private static void processNumerationPosition(final String position) {
        if (position != null) {
            numerationPosition = switch (position) {
                case "Top Left" -> NumerationPosition.TOP_LEFT;
                case "Top" -> NumerationPosition.TOP;
                case "Top Right" -> NumerationPosition.TOP_RIGHT;
                case "Bottom Left" -> NumerationPosition.BOTTOM_LEFT;
                case "Bottom" -> NumerationPosition.BOTTOM;
                case "Bottom Right" -> NumerationPosition.BOTTOM_RIGHT;
                default -> throw new MissingMemberException("5: The specified page position does not exist.");
            };
        } else numerationPosition = usedStyleGuide.numerationPosition();
    }

    private static void processNumerationType(final String pageNumerationType) {
        if (pageNumerationType != null) {
            numerationType = switch (pageNumerationType) {
                case "Arabic" -> NumerationType.ARABIC;
                case "Roman" -> NumerationType.ROMAN;
                default -> throw new MissingMemberException("4: The specified page numeration does not exist.");
            };
        } else numerationType = usedStyleGuide.numerationType();
    }

    private static void processLayout(@NonNull final Layout layout) {
        final float specifiedWidth = processDocumentWidth(layout);
        final float specifiedHeight = processDocumentHeight(layout);
        dimensions = new PDRectangle(specifiedWidth, specifiedHeight);

        processSideMargin(layout);
        processTextSpacing(layout);
    }

    private static void processTextSpacing(@NonNull final Layout layout) {
        if (layout.getSpacing() != null) {
            try {
                spacing = Float.parseFloat(layout.getSpacing());
            } catch (IllegalArgumentException e) {
                throw new IncorrectFormatException("10: Incorrect spacing constant.");
            }
        } else spacing = usedStyleGuide.spacing();

        if (spacing < 0)
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (spacing >= 3)
            WarningQueue.enqueue(new UnlikelinessWarning(UnlikelinessWarning.ERR_MSG_5, WarningSeverity.HIGH));

    }

    private static void processSideMargin(@NonNull final Layout layout) {
        if (layout.getMargin() != null) {
            if (layout.getMargin().endsWith("in")) {
                inchesUsed = true;

                final var asNumber = layout.getMargin().substring(0, layout.getMargin().length() - 2);
                margin = POINTS_PER_INCH * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                margin = POINTS_PER_MM * Float.parseFloat(layout.getMargin());
            }
        } else margin = POINTS_PER_INCH * usedStyleGuide.margin();

        if (margin < 0)
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (margin > 200)
            WarningQueue.enqueue(new UnlikelinessWarning(UnlikelinessWarning.ERR_MSG_5, WarningSeverity.HIGH));
    }

    private static float processDocumentWidth(@NonNull final Layout layout) {
        final float width;
        if (layout.getWidth() != null) {
            if (layout.getWidth().endsWith("in")) {
                inchesUsed = true;

                final var asNumber = layout.getWidth().substring(0, layout.getWidth().length() - 2);
                width = POINTS_PER_INCH * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                width = POINTS_PER_MM * Float.parseFloat(layout.getWidth());
            }
        } else width = usedStyleGuide.pageFormat().getWidth();

        // Checks for warnings regarding the used width
        if (width < 0)
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (width < 400)
            WarningQueue.enqueue(new UnlikelinessWarning(UnlikelinessWarning.ERR_MSG_5, WarningSeverity.HIGH));

        return width;
    }

    private static float processDocumentHeight(@NonNull final Layout layout) {
        final float height;
        if (layout.getHeight() != null) {
            if (layout.getHeight().endsWith("in")) {
                inchesUsed = true;

                final var asNumber = layout.getHeight().substring(0, layout.getHeight().length() - 2);
                height = POINTS_PER_INCH * Float.parseFloat(asNumber);
            } else {
                mmUsed = true;

                height = POINTS_PER_MM * Float.parseFloat(layout.getHeight());
            }
        } else height = usedStyleGuide.pageFormat().getHeight();

        // Checks for warnings regarding the used height
        if (height < 0)
            throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
        else if (height < 500)
            WarningQueue.enqueue(new UnlikelinessWarning(UnlikelinessWarning.ERR_MSG_5, WarningSeverity.HIGH));

        return height;
    }

    private static void processBibliography(final LinkedList<frontend.ast.bibliography.BibliographySource> entries) {
        isProcessingBibliography = true;

        entries.forEach(entry -> {
            if (bibliographyEntries.containsKey(entry.getId()))
                throw new ContentException("3: Bibliography entry with ID '" + entry.getId() + "' already exists.");
            else if (entry.getType() == null)
                WarningQueue.enqueue(new MissingMemberWarning("3: The bibliography entry with the ID '" +
                        entry.getId() + "' does not have a type.", WarningSeverity.CRITICAL));

            bibliographyEntries.put(entry.getId(), BibliographySourceTable.lookup(entry));
        });

        isProcessingBibliography = false;
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

        var content = citation.getCitedContent();
        if (content != null) content = content.trim();

        var numeration = citation.getNumeration();
        if (numeration != null) numeration = numeration.trim();

        // Mark the source as cited
        referenceSource.setHasBeenCited(true);

        return usedStyleGuide.formatCitation(referenceSource, content, numeration);
    }

    private static void processFonts(@NonNull Structure structure) {
        sentenceFontData = fontNodeToData(structure.getSentence().getFont(), usedStyleGuide.sentenceFontData());
        workFontData = fontNodeToData(structure.getWork().getFont(), usedStyleGuide.workFontData());
        emphasisFontData = fontNodeToData(structure.getEmphasis().getFont(), usedStyleGuide.emphasisFontData());

        // Initially use all default configurations and then replace the user defined ones
        chapterSentenceFontData = usedStyleGuide.chapterSentenceFontData();
        chapterEmphasisFontData = usedStyleGuide.chapterEmphasisFontData();
        chapterWorkFontData = usedStyleGuide.chapterWorkFontData();
        for (final Chapter chapter : structure.getSingleChapters()) {
            final var level = chapter.getAffectedLevel();
            try {
                final var asInt = Integer.parseInt(level);

                if (asInt < 0)
                    throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
                else if (asInt >= chapterSentenceFontData.length)
                    throw new ConfigurationException("11: The specified chapter depth exceeds the maximum allowed by" +
                            " the used style guide.");

                chapterSentenceFontData[asInt] = fontNodeToData(chapter.getSentenceFont(),
                        usedStyleGuide.chapterSentenceFontData()[asInt]);

                chapterEmphasisFontData[asInt] = fontNodeToData(chapter.getEmphasisFont(),
                        usedStyleGuide.chapterEmphasisFontData()[asInt]);

                chapterWorkFontData[asInt] = fontNodeToData(chapter.getWorkFont(),
                        usedStyleGuide.chapterWorkFontData()[asInt]);
            } catch (NumberFormatException e) {
                throw new IncorrectFormatException(IncorrectFormatException.ERR_MSG_2);
            }
        }
    }

    /**
     * Converts a font configuration AST node into processable font data, or uses its alternative default font data
     * from the style guide.
     *
     * @param fontNode               the AST node that contains the font configurations
     * @param alternativeDefaultData the default values that should be used if there is no configuration given
     * @return the font data to be used during further processing steps
     */
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
