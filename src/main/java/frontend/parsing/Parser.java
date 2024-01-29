package frontend.parsing;

import frontend.FrontEndBridge;
import frontend.ast.*;
import frontend.ast.config.*;
import frontend.ast.config.style.Citation;
import frontend.ast.paragraph.Emphasise;
import frontend.ast.paragraph.Paragraph;
import frontend.ast.paragraph.Text;
import frontend.lexical_analysis.Token;
import frontend.lexical_analysis.TokenType;
import lombok.NonNull;
import processing.Processor;
import processing.StructureType;

import java.util.function.Consumer;

/**
 * The Parser class is responsible for creating a syntax tree for the Pipp document.
 * It follows the top-down approach by making use of a lookahead in the FrontEndBridge.
 *
 * @version 1.0
 * @since 1.0
 */
public class Parser {

    /// CONSTANTS

    private static final String AUTHOR_KEYWORD = "author";
    private static final String YEAR_KEYWORD = "year";
    private static final String BIBLIOGRAPHY_KEYWORD = "bibliography";
    private static final String SEMESTER_KEYWORD = "semester";
    private static final String INSTITUTION_KEYWORD = "institution";
    private static final String CHAIR_KEYWORD = "chair";
    private static final String DATE_KEYWORD = "date";
    private static final String ASSESSOR_KEYWORD = "assessor";
    private static final String FIRST_NAME_KEYWORD = "firstname";
    private static final String LAST_NAME_KEYWORD = "lastname";
    private static final String NAME_KEYWORD = "name";
    private static final String IN_KEYWORD = "in";
    private static final String SKIP_KEYWORD = "skip";
    private static final String EMPHASISE_KEYWORD = "emphasise";
    private static final String WORK_KEYWORD = "work";
    private static final String COLOUR_KEYWORD = "colour";
    private static final String LAYOUT_KEYWORD = "layout";
    private static final String CONFIGURATION_KEYWORD = "config";
    private static final String HEADER_KEYWORD = "header";
    private static final String TITLE_KEYWORD = "title";
    private static final String ID_KEYWORD = "id";
    private static final String SIZE_KEYWORD = "size";
    private static final String NUMERATION_KEYWORD = "numeration";
    private static final String CITATION_KEYWORD = "citation";
    private static final String IMAGE_KEYWORD = "image";
    private static final String BLANK_KEYWORD = "blank";
    private static final String LIMIT_KEYWORD = "limit";
    private static final String ALLOW_KEYWORD = "allow";
    private static final String PARAGRAPH_KEYWORD = "paragraph";
    private static final String SENTENCE_KEYWORD = "sentence";
    private static final String FONT_KEYWORD = "font";
    private static final String WIDTH_KEYWORD = "width";
    private static final String DISPLAY_KEYWORD = "display";
    private static final String OF_KEYWORD = "of";
    private static final String MARGIN_KEYWORD = "margin";
    private static final String SPACING_KEYWORD = "spacing";
    private static final String INDENTATION_KEYWORD = "indentation";
    private static final String HEIGHT_KEYWORD = "height";
    private static final String STRUCTURE_KEYWORD = "structure";
    private static final String PUBLICATION_KEYWORD = "publication";
    private static final String TYPE_KEYWORD = "type";
    private static final String PAGE_KEYWORD = "page";
    private static final String STYLE_KEYWORD = "style";

    private static final String DOCUMENT_TITLE_CONTAINER_NAME = "Document Title";
    private static final String PUBLICATION_CONFIGURATION_CONTAINER_NAME = "Publication Configuration";

    /**
     * The bridge is responsible for providing an interface between the Parser and the Scanner
     * The Parser can access the tokens scanned by the Scanner, and dequeue them.
     * Additionally, the bridge provides a lookahead method, which the Parser uses to look at a
     * future token to choose the correct production
     */
    private final FrontEndBridge frontEndBridge;
    /**
     * The abstract syntax tree of the document.
     * The parser builds the AST depending on the text specified by the user.
     */
    private final AST ast;
    /**
     * Tracks the current token, which needs to be parsed
     */
    private Token current;


    /**
     * Tracks the current paragraph in order to be able to add paragraph instructions to it when parsing
     * its children, and then add the paragraph with the child nodes to the document body.
     * Note that this should be null when there is no paragraph being parsed.
     */
    private Paragraph currentParagraph;
    /**
     * Tracks the last token, which has already been parsed.
     * It ignores the following "helper" tokens: NewLine, Indentation
     */
    private Token last;
    /**
     * Tracks the node, which was last added to the AST
     */
    private Node lastNode;
    /**
     * Keeps track of the currently required indentation level
     */
    private int currentIndentationLevel = 0;
    /**
     * Determines the type of container that is currently being parsed.
     * This is used to enable different parts to be parsed by different containers
     * (for example, the title configuration)
     */
    private String currentlyParsedContainer = "Configuration";

    /**
     * Creates the parser by supplying the bridge.
     *
     * @param frontEndBridge - the bridge between the Scanner and Parser
     */
    public Parser(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
        this.ast = new AST();
    }

    /**
     * Once done parsing, it throws an error if there are still tokens.
     * If there are no more tokens, it starts the processing phase, instead.
     */
    private void finishParsing() {
        if (frontEndBridge.containsTokens()) error();

        Processor.processAST(ast);
    }

    /**
     * Tries to parse an INDENT token and checks if its indentation value (amount of tabs) corresponds to the
     * required amount. If not, an indentation error is thrown, but if it does, the parser skips to the next token
     */
    private void expectIndentation() {
        if (current.type == TokenType.INDENT) {
            if (indentTokenToInt() != currentIndentationLevel + 1) indendationError();
            else if (frontEndBridge.containsTokens()) {
                current = frontEndBridge.dequeueToken();
                currentIndentationLevel++;
            }
        } else indendationError();
    }

    private int indentTokenToInt() {
        if (current.type != TokenType.INDENT)
            throw new IllegalStateException("Should not check for indentation level if the current token is" +
                    " not an indentation token");

        return Integer.parseInt(current.value);
    }

    /**
     * Decreases the expected indentation level if possible.
     * Else, an IllegalStateException is thrown
     */
    private void forgoIndentation() {
        if (frontEndBridge.containsTokens() && (currentIndentationLevel <= 0))
            throw new IllegalStateException("Should not try to forgo indentation as there is no more indentation");

        currentIndentationLevel--;
    }

    /**
     * Tries to consume the current INDENT token which has the same indentation level as the currently required
     */
    private void remainIndentation() {
        if (frontEndBridge.containsTokens() && currentIndentationLevel > 0) {
            if (current.type != TokenType.INDENT)
                throw new IllegalStateException(current.getDebugInfo().errorMessage("Should remain at the current " +
                        "indentation level (" + currentIndentationLevel + "). Instead" +
                        " found unexpected token: " + current));

            if (Integer.parseInt(current.value) != currentIndentationLevel) indendationError();
            else if (frontEndBridge.containsTokens()) current = frontEndBridge.dequeueToken();
            else error();
        }
    }

    /**
     * Tries to consume to current token by comparing it to the required token.
     * If there is no current token, the program is in an illegal state.
     * If the required token does not match with the current token, the program entered by the user
     * is invalid, and an exception is thrown.
     * If the tokens do match, the next token is dequeued in the bridge if it exists.
     *
     * @param requiredToken - the required token compared to the current token
     */
    private void consume(final Token requiredToken) {
        var lastCurrent = current;

        if (isKeyword() && requiredToken.type == TokenType.KEYWORD &&
                current.value.equals(requiredToken.value) || current.type != TokenType.KEYWORD &&
                current.type == requiredToken.type) {
            if (frontEndBridge.containsTokens()) current = frontEndBridge.dequeueToken();
        } else throw new IllegalArgumentException("Unexpected token: " + current + ". Expected: " + requiredToken);

        if (lastCurrent.type != TokenType.NEW_LINE && lastCurrent.type != TokenType.INDENT) last = lastCurrent;
    }

    /**
     * This method is called whenever a token is specified in a production without having to
     * consume a token directly. In that case, an IllegalArgumentException is thrown.
     */
    private void error() {
        throw new IllegalArgumentException(current.getDebugInfo().errorMessage("Unexpected token: " + current.type));
    }

    private void expectKeyword(Consumer<String> handler, String... keywords) {
        var currentIsAcceptableKeyword = false;
        if (isKeyword()) {
            do {
                handler.accept(current.value);

                boolean insufficientIndentation = handleLookaheadIndentation(keywords);
                if (insufficientIndentation) break;

                currentIsAcceptableKeyword = false;
                for (var keyword : keywords) {
                    if (current.value.equals(keyword)) {
                        currentIsAcceptableKeyword = true;
                        break;
                    }
                }
            } while (currentIsAcceptableKeyword);
        } else error();
    }

    private boolean handleLookaheadIndentation(@NonNull String... keywords) {
        if (current.type != TokenType.INDENT || indentTokenToInt() < currentIndentationLevel) return true;

        var ahead = frontEndBridge.lookahead(0);
        for (var keyword : keywords) {
            if (frontEndBridge.containsTokens() && ahead.value.equals(keyword)) {
                remainIndentation();
                return false;
            }
        }

        return false;
    }

    /**
     * This method is called whenever an indentation is expected, but not given by the user
     */
    private void indendationError() {
        throw new IllegalArgumentException(current.getDebugInfo().errorMessage("Indentation error." +
                " The required level is: " + (currentIndentationLevel + 1)));
    }

    /**
     * Bibliography := "bibliography" INDENT BibliographyItemList
     */
    public void bibliography() {
        if (frontEndBridge.containsTokens()) {
            current = frontEndBridge.dequeueToken();
            consumeKeyword(BIBLIOGRAPHY_KEYWORD);
            newline();
            expectIndentation();
            bibliographyItemList();

            currentIndentationLevel = 0;
            if (frontEndBridge.containsTokens()) error();
        }
    }

    /**
     * BibliographyItemList := "id" INDENT BibliographyItem DEDENT
     */
    private void bibliographyItemList() {
        expectKeyword(it -> {
            lastNode = new BibliographySource();
            consumeKeyword(ID_KEYWORD);
            textual();
            ((BibliographySource) lastNode).setId(last.value);
            expectIndentation();
            bibliographyItem();

            ast.includeBibliographySource((BibliographySource) lastNode);
            forgoIndentation();
        }, ID_KEYWORD);
    }

    /**
     * BibliographyItem := Type | Author | Title | PublicationInfo
     */
    private void bibliographyItem() {
        expectKeyword(it -> {
            switch (it) {
                case TYPE_KEYWORD -> {
                    consumeKeyword(TYPE_KEYWORD);
                    textual();
                    ((BibliographySource) lastNode).setType(last.value);
                }
                case TITLE_KEYWORD -> {
                    consumeKeyword(TITLE_KEYWORD);
                    textual();
                    ((BibliographySource) lastNode).setTitle(last.value);
                }
                case AUTHOR_KEYWORD -> {
                    consumeKeyword(AUTHOR_KEYWORD);
                    bibliographyItemAuthor();
                }
                case PUBLICATION_KEYWORD -> {
                    consumeKeyword(PUBLICATION_KEYWORD);
                    newline();
                    expectIndentation();
                    bibliographyItemPublication();
                    forgoIndentation();
                }
                default -> error();
            }
        }, TYPE_KEYWORD, AUTHOR_KEYWORD, TITLE_KEYWORD, PUBLICATION_KEYWORD);
    }

    private void bibliographyItemPublication() {
        expectKeyword(it -> {
            if (it.equals(NAME_KEYWORD)) {
                consumeKeyword(NAME_KEYWORD);
                textual();
                ((BibliographySource) lastNode).getPublication().setName(last.value);
            } else if (it.equals(YEAR_KEYWORD)) {
                consumeKeyword(YEAR_KEYWORD);
                textual();
                ((BibliographySource) lastNode).getPublication().setYear(last.value);
            } else error();
        }, NAME_KEYWORD, YEAR_KEYWORD);
    }

    private void bibliographyItemAuthor() {
        currentlyParsedContainer = "Bibliography Author";
        if (current.type == TokenType.NEW_LINE) {
            newline();
            expectIndentation();

            if (isKeyword()) {
                switch (current.value) {
                    case NAME_KEYWORD, FIRST_NAME_KEYWORD, TITLE_KEYWORD -> nameSpecification();
                    case OF_KEYWORD -> expectKeyword(it -> {
                        consumeKeyword(OF_KEYWORD);

                        if (current.type == TokenType.NEW_LINE) {
                            newline();
                            expectIndentation();
                            nameSpecification();
                            forgoIndentation();
                        } else if (current.type == TokenType.TEXT) bibliographyInlineAuthor();
                        else error();
                    }, OF_KEYWORD);
                    default -> error();
                }
            } else error();

            forgoIndentation();
        } else if (current.type == TokenType.TEXT) bibliographyInlineAuthor();
        else error();
    }

    private void bibliographyInlineAuthor() {
        textual();

        var author = new Author();
        author.setName(last.value);

        ((BibliographySource) lastNode).getAuthors().add(author);
    }

    /**
     * S := Document | epsilon
     */
    public void s() {
        if (frontEndBridge.containsTokens()) {
            current = frontEndBridge.dequeueToken();
            document();
            finishParsing();
        }
    }

    /**
     * Document := ConfigContainer InstructionList | InstructionList
     */
    private void document() {
        if (isKeyword(CONFIGURATION_KEYWORD)) configContainer();
        instructionList();
    }

    // InstructionList := OptionalNewLine Instruction InstructionList | OptionalNewLine Instruction
    private void instructionList() {
        do {
            if (current.type == TokenType.NEW_LINE) newline();
            else if (current.type == TokenType.KEYWORD) {
                switch (current.value) {
                    case HEADER_KEYWORD -> header();
                    case TITLE_KEYWORD -> title();
                    case BLANK_KEYWORD -> blank();
                    case IMAGE_KEYWORD -> image();
                    case EMPHASISE_KEYWORD, WORK_KEYWORD, CITATION_KEYWORD -> paragraph();
                    default -> error();
                }
            } else paragraph();
        } while (frontEndBridge.containsTokens() && (current.type == TokenType.TEXT ||
                current.type == TokenType.NEW_LINE || isKeyword()
                && (isKeyword(HEADER_KEYWORD) || isKeyword(TITLE_KEYWORD) || isKeyword(CITATION_KEYWORD) ||
                isKeyword(EMPHASISE_KEYWORD) || isKeyword(WORK_KEYWORD) || isKeyword(BLANK_KEYWORD) ||
                isKeyword(IMAGE_KEYWORD))));
    }

    /**
     * Title := "title" Newline
     */
    private void title() {
        consumeKeyword(TITLE_KEYWORD);
        newline();
        ast.pushDocumentNode(new NoArgumentStructure(StructureType.TITLE));
    }

    /**
     * Header := "header" Newline
     */
    private void header() {
        consumeKeyword(HEADER_KEYWORD);
        newline();
        ast.pushDocumentNode(new NoArgumentStructure(StructureType.HEADER));
    }

    /**
     * Blank := "blank" Newline
     */
    private void blank() {
        consumeKeyword(BLANK_KEYWORD);
        newline();
        ast.pushDocumentNode(new NoArgumentStructure(StructureType.BLANKPAGE));
    }

    /**
     * Image := "image" NewLine ImageDeclaration | "image" ImageInlineDeclaration
     */
    private void image() {
        consumeKeyword(IMAGE_KEYWORD);
        lastNode = new Image();

        if (current.type == TokenType.NEW_LINE) {
            newline();
            imageDeclaration();
        } else if (current.type == TokenType.TEXT) {
            imageInlineDeclaration();
        } else error();
    }

    /**
     * ImageDeclaration := INDENT ImageID OptionalImageDimensions DEDENT
     */
    private void imageDeclaration() {
        expectIndentation();

        expectKeyword(it -> {
            switch (it) {
                case ID_KEYWORD -> {
                    consumeKeyword(ID_KEYWORD);
                    textual();
                    ((Image) lastNode).setId(last.value);
                }
                case WIDTH_KEYWORD -> {
                    consumeKeyword(WIDTH_KEYWORD);
                    textual();
                    ((Image) lastNode).setWidth(last.value);
                }
                case HEIGHT_KEYWORD -> {
                    consumeKeyword(HEIGHT_KEYWORD);
                    textual();
                    ((Image) lastNode).setHeight(last.value);
                }
                case SIZE_KEYWORD -> {
                    consumeKeyword(SIZE_KEYWORD);
                    textual();
                    ((Image) lastNode).setSize(last.value);
                }
                case DISPLAY_KEYWORD -> {
                    consumeKeyword(DISPLAY_KEYWORD);
                    textual();
                    ((Image) lastNode).setAlignment(last.value);
                }
                default -> error();
            }
        }, ID_KEYWORD, WIDTH_KEYWORD, HEIGHT_KEYWORD, SIZE_KEYWORD, DISPLAY_KEYWORD);

        ast.pushDocumentNode((Image) lastNode);
        forgoIndentation();
    }

    /**
     * ImageInlineDeclaration := Text (Id) |
     * Text (Id) ListSeparator Text (Size) |
     * Text (Id) ListSeparator Text (Width) ListSeparator Text (Height)
     */
    private void imageInlineDeclaration() {
        if (current.type == TokenType.TEXT) {
            consume(new Token(TokenType.TEXT, null));
            ((Image) lastNode).setId(last.value);

            if (current.type == TokenType.LIST_SEPARATOR) sizedImageInlineDeclaration();
        } else error();

        ast.pushDocumentNode((Image) lastNode);
    }

    private void sizedImageInlineDeclaration() {
        listSeparator();

        if (current.type == TokenType.TEXT) {
            consume(new Token(TokenType.TEXT, null));
            var secondArgument = last.value;

            if (current.type == TokenType.LIST_SEPARATOR) {
                listSeparator();
                ((Image) lastNode).setWidth(secondArgument);

                if (current.type == TokenType.TEXT) {
                    consume(new Token(TokenType.TEXT, null));
                    ((Image) lastNode).setHeight(last.value);
                } else error();
            } else {
                ((Image) lastNode).setSize(secondArgument);
            }
        } else error();
    }

    /**
     * ConfigContainer := "config" Newline INDENT ConfigList DEDENT
     */
    private void configContainer() {
        consumeKeyword(CONFIGURATION_KEYWORD);
        newline();
        expectIndentation();
        configList();
        forgoIndentation();
    }

    /**
     * ConfigList := Configuration | Configuration ConfigList
     */
    private void configList() {
        expectKeyword(it -> {
            switch (it) {
                case TITLE_KEYWORD -> {
                    currentlyParsedContainer = DOCUMENT_TITLE_CONTAINER_NAME;
                    titleConfiguration();
                }
                case STYLE_KEYWORD -> styleConfiguration();
                case AUTHOR_KEYWORD -> authorConfiguration();
                case ASSESSOR_KEYWORD -> assessorConfiguration();
                case PUBLICATION_KEYWORD -> publicationConfiguration();
                case TYPE_KEYWORD -> typeConfiguration();
                default -> error();
            }
        }, STYLE_KEYWORD, TITLE_KEYWORD, AUTHOR_KEYWORD, ASSESSOR_KEYWORD, PUBLICATION_KEYWORD, TYPE_KEYWORD);
    }

    /**
     * PublicationConfiguration := "publication" NewLine INDENT TitleConfiguration DateConfiguration |
     * "publication" NewLine INDENT DateConfiguration TitleConfiguration |
     * "publication" NewLine INDENT TitleConfiguration DEDENT |
     * "publication" NewLine INDENT DateConfiguration DEDENT
     */
    private void publicationConfiguration() {
        currentlyParsedContainer = PUBLICATION_CONFIGURATION_CONTAINER_NAME;

        consumeKeyword(PUBLICATION_KEYWORD);
        newline();
        expectIndentation();

        expectKeyword(it -> {
            switch (it) {
                case TITLE_KEYWORD -> titleConfiguration();
                case DATE_KEYWORD -> dateConfiguration();
                case INSTITUTION_KEYWORD -> institutionConfiguration();
                case SEMESTER_KEYWORD -> semesterConfiguration();
                case CHAIR_KEYWORD -> chairConfiguration();
                default -> error();
            }
        }, TITLE_KEYWORD, DATE_KEYWORD, INSTITUTION_KEYWORD, SEMESTER_KEYWORD, CHAIR_KEYWORD);

        forgoIndentation();
    }

    /**
     * DateConfiguration := "date" Textual
     */
    private void dateConfiguration() {
        consumeKeyword("date");
        textual();

        if (currentlyParsedContainer.equals(PUBLICATION_CONFIGURATION_CONTAINER_NAME)) {
            ast.getConfiguration().getPublication().setDate(last.value);
            lastNode = ast.getConfiguration().getPublication();
        } else error();
    }

    /**
     * InstitutionConfiguration := "institution" Textual
     */
    private void institutionConfiguration() {
        consumeKeyword(INSTITUTION_KEYWORD);
        textual();

        if (currentlyParsedContainer.equals(PUBLICATION_CONFIGURATION_CONTAINER_NAME)) {
            ast.getConfiguration().getPublication().setInstitution(last.value);
            lastNode = ast.getConfiguration().getPublication();
        } else error();
    }

    /**
     * ChairConfiguration := "chair" Textual
     */
    private void chairConfiguration() {
        consumeKeyword(CHAIR_KEYWORD);
        textual();

        if (currentlyParsedContainer.equals(PUBLICATION_CONFIGURATION_CONTAINER_NAME)) {
            ast.getConfiguration().getPublication().setChair(last.value);
            lastNode = ast.getConfiguration().getPublication();
        } else error();
    }

    /**
     * SemesterConfiguration := "semester" Textual
     */
    private void semesterConfiguration() {
        consumeKeyword(SEMESTER_KEYWORD);
        textual();

        if (currentlyParsedContainer.equals(PUBLICATION_CONFIGURATION_CONTAINER_NAME)) {
            ast.getConfiguration().getPublication().setSemester(last.value);
            lastNode = ast.getConfiguration().getPublication();
        } else error();
    }

    /**
     * TypeConfiguration := "type" Textual
     */
    private void typeConfiguration() {
        consumeKeyword(TYPE_KEYWORD);
        textual();

        ast.getConfiguration().setDocumentType(last.value);
        lastNode = ast.getConfiguration();
    }

    /**
     * AssessorConfiguration := "assessor" NewLine INDENT AssessorSpecification DEDENT | "assessor" Textual
     */
    private void assessorConfiguration() {
        currentlyParsedContainer = "Assessor";

        consumeKeyword(ASSESSOR_KEYWORD);

        if (current.type == TokenType.NEW_LINE) {
            newline();
            expectIndentation();
            assessorSpecification();
            forgoIndentation();
        } else if (current.type == TokenType.TEXT) {
            textual();

            var assessor = new Assessor();
            assessor.setName(last.value);
            ast.getConfiguration().getAssessors().add(assessor);
            lastNode = assessor;
        } else error();
    }

    /**
     * AssessorSpecification := NameSpecificationWithOptRole | AssessorList
     */
    private void assessorSpecification() {
        if (isKeyword()) {
            switch (current.value) {
                case NAME_KEYWORD, LAST_NAME_KEYWORD, TITLE_KEYWORD -> nameSpecificationWithOptRole();
                case OF_KEYWORD -> assessorList();
                default -> error();
            }
        } else error();
    }

    /**
     * AssessorList := AssessorItem | AssessorItem AssessorList
     */
    private void assessorList() {
        expectKeyword(it -> {
            consumeKeyword(OF_KEYWORD);

            if (current.type == TokenType.NEW_LINE) {
                newline();
                expectIndentation();
                nameSpecificationWithOptRole();
                forgoIndentation();
            } else if (current.type == TokenType.TEXT) {
                textual();

                if (currentlyParsedContainer.equals("Assessor")) {
                    var assessor = new Assessor();
                    assessor.setName(last.value);
                    ast.getConfiguration().getAssessors().add(assessor);
                }
            }
        }, OF_KEYWORD);
    }

    /**
     * NameSpecificationWithOptRole := NameSpecification | NameSpecification "role" Textual
     */
    private void nameSpecificationWithOptRole() {
        nameSpecification();
        if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                frontEndBridge.lookahead(0).value.equals("role")) {
            remainIndentation();
            consumeKeyword("role");
            textual();

            if (currentlyParsedContainer.equals("Assessor")) {
                ((Assessor) lastNode).setRole(last.value);
            } else error();
        }
    }

    /**
     * AuthorConfiguration := "author" INDENT NewLine AuthorSpecification DEDENT | "author" Textual
     */
    private void authorConfiguration() {
        consumeKeyword(AUTHOR_KEYWORD);

        if (current.type == TokenType.NEW_LINE) {
            newline();
            expectIndentation();
            authorSpecification();
            forgoIndentation();
        } else if (current.type == TokenType.TEXT) {
            textual();

            var author = new Author();
            author.setName(last.value);

            ast.getConfiguration().getAuthors().add(author);
        } else error();
    }

    /**
     * AuthorSpecification := NameSpecificationWithOptID | AuthorList
     */
    private void authorSpecification() {
        if (isKeyword()) {
            currentlyParsedContainer = "Author";
            switch (current.value) {
                case NAME_KEYWORD, FIRST_NAME_KEYWORD, TITLE_KEYWORD -> nameSpecificationWithOptId();
                case OF_KEYWORD -> authorList();
                default -> error();
            }
        } else error();
    }

    /**
     * AuthorList := AuthorItem | AuthorItem AuthorList
     */
    private void authorList() {
        expectKeyword(it -> {
            consumeKeyword(OF_KEYWORD);

            if (current.type == TokenType.NEW_LINE) {
                newline();
                expectIndentation();
                nameSpecificationWithOptId();
                forgoIndentation();
            } else if (current.type == TokenType.TEXT) {
                textual();

                if (currentlyParsedContainer.equals("Author")) {
                    var author = new Author();
                    author.setName(last.value);
                    ast.getConfiguration().getAuthors().add(author);
                }
            } else error();
        }, OF_KEYWORD);
    }

    /**
     * NameSpecificationWithOptID := NameSpecification | NameSpecification "id" Textual
     */
    private void nameSpecificationWithOptId() {
        nameSpecification();
        if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                frontEndBridge.lookahead(0).value.equals(ID_KEYWORD)) {
            remainIndentation();
            consume(new Token(TokenType.KEYWORD, ID_KEYWORD));
            textual();

            if (currentlyParsedContainer.equals("Author")) {
                ((Author) lastNode).setId(last.value);
            }
        }
    }

    /**
     * NameSpecification := Name | FirstName LastName | Title Name | Title FirstName LastName
     */
    private void nameSpecification() {
        String title = null;
        if (isKeyword(TITLE_KEYWORD)) {
            consumeKeyword(TITLE_KEYWORD);
            textual();
            remainIndentation();

            title = last.value;
        }

        if (isKeyword()) {
            if (current.value.equals(NAME_KEYWORD)) {
                name();

                switch (currentlyParsedContainer) {
                    case "Author" -> {
                        var author = new Author();
                        author.setTitle(title);
                        author.setName(last.value);

                        ast.getConfiguration().getAuthors().add(author);
                        lastNode = author;
                    }
                    case "Assessor" -> {
                        var assessor = new Assessor();
                        assessor.setTitle(title);
                        assessor.setName(last.value);
                        ast.getConfiguration().getAssessors().add(assessor);
                        lastNode = assessor;
                    }
                    case "Bibliography Author" -> {
                        var author = new Author();
                        author.setTitle(title);
                        author.setName(last.value);

                        ((BibliographySource) lastNode).getAuthors().add(author);
                    }
                    default -> error();
                }
            } else if (current.value.equals(FIRST_NAME_KEYWORD)) {
                firstname();

                var firstname = last.value;

                remainIndentation();
                lastname();

                var lastname = last.value;

                switch (currentlyParsedContainer) {
                    case "Author" -> {
                        var author = new Author();
                        author.setTitle(title);
                        author.setFirstname(firstname);
                        author.setLastname(lastname);

                        ast.getConfiguration().getAuthors().add(author);
                        lastNode = author;
                    }
                    case "Assessor" -> {
                        var assessor = new Assessor();
                        assessor.setTitle(title);
                        assessor.setFirstname(firstname);
                        assessor.setLastname(lastname);
                        ast.getConfiguration().getAssessors().add(assessor);
                        lastNode = assessor;
                    }
                    case "Bibliography Author" -> {
                        var author = new Author();
                        author.setTitle(title);
                        author.setFirstname(firstname);
                        author.setLastname(lastname);

                        ((BibliographySource) lastNode).getAuthors().add(author);
                    }
                    default -> error();
                }
            } else error();
        } else error();
    }

    /**
     * FirstName := "firstname" Textual
     */
    private void firstname() {
        consumeKeyword(FIRST_NAME_KEYWORD);
        textual();
    }

    /**
     * LastName := "lastname" Textual
     */
    private void lastname() {
        consumeKeyword(LAST_NAME_KEYWORD);
        textual();
    }

    /**
     * Name := "name" Textual
     */
    private void name() {
        consumeKeyword(NAME_KEYWORD);
        textual();
    }

    /**
     * TitleConfiguration := "title" Textual | "title" NewLine INDENT CitedTextual DEDENT
     */
    private void titleConfiguration() {
        consumeKeyword(TITLE_KEYWORD);

        if (current.type == TokenType.NEW_LINE) {
            newline();
            expectIndentation();
            titleTextual();
            forgoIndentation();
        } else if (current.type == TokenType.TEXT) {
            textual();

            if (currentlyParsedContainer.equals(DOCUMENT_TITLE_CONTAINER_NAME)) {
                ast.getConfiguration().getTitle().add(new TitleText(last.value));
                lastNode = ast.getConfiguration().getTitle();
            } else if (currentlyParsedContainer.equals(PUBLICATION_CONFIGURATION_CONTAINER_NAME)) {
                ast.getConfiguration().getPublication().getTitle().add(new TitleText(last.value));
                lastNode = ast.getConfiguration().getPublication().getTitle();
            }
        } else error();
    }

    /**
     * StyleConfiguration := "style" Textual | "style" NewLine INDENT CustomStyle DEDENT
     */
    private void styleConfiguration() {
        consumeKeyword(STYLE_KEYWORD);

        if (current.type == TokenType.TEXT) {
            textual();
            ast.getConfiguration().getStyle().setBaseStyle(last.value);
            lastNode = ast.getConfiguration().getStyle();
        } else {
            newline();
            expectIndentation();
            customStyle();
            forgoIndentation();
        }
    }

    /**
     * CustomStyle := "of" Textual OptionalCustomStyleList
     */
    private void customStyle() {
        consumeKeyword(OF_KEYWORD);
        textual();

        ast.getConfiguration().getStyle().setBaseStyle(last.value);
        lastNode = ast.getConfiguration().getStyle();

        remainIndentation();
        optionalCustomStyleList();
    }

    /**
     * OptionalCustomStyleList := epsilon | GeneralLayoutStyle | PageNumerationStyle |  StructuresStyle |
     * GeneralLayoutStyle CustomStyleList |
     * PageNumerationStyle CustomStyleList |
     * StructuresStyle | CustomStyleList
     */
    private void optionalCustomStyleList() {
        expectKeyword(it -> {
            switch (it) {
                case LAYOUT_KEYWORD -> generalLayoutStyle();
                case NUMERATION_KEYWORD -> pageNumerationStyle();
                case STRUCTURE_KEYWORD -> structureStyle();
                default -> error();
            }
        }, LAYOUT_KEYWORD, NUMERATION_KEYWORD, STRUCTURE_KEYWORD);
    }

    /**
     * StructureStyle := "structure" NewLine INDENT ParagraphStructureStyle DEDENT |
     * "structure" NewLine INDENT SentenceStructureStyle DEDENT |
     * "structure" NewLine INDENT EndnotesStructureStyle DEDENT
     */
    private void structureStyle() {
        if (isKeyword()) {
            consumeKeyword(STRUCTURE_KEYWORD);
            newline();
            expectIndentation();

            expectKeyword(it -> {
                switch (it) {
                    case PARAGRAPH_KEYWORD -> paragraphStructureStyle();
                    case SENTENCE_KEYWORD -> sentenceStructureStyle();
                    case WORK_KEYWORD -> workStructureStyle();
                    case EMPHASISE_KEYWORD -> emphasisStructureStyle();
                    default -> error();
                }
            }, PARAGRAPH_KEYWORD, SENTENCE_KEYWORD, WORK_KEYWORD, EMPHASISE_KEYWORD);

            forgoIndentation();
        } else error();
    }

    /**
     * SentenceStructureStyle := "sentence" NewLine INDENT SentenceStructureStyleList DEDENT
     */
    private void sentenceStructureStyle() {
        currentlyParsedContainer = "Sentence Structure";
        consumeKeyword(SENTENCE_KEYWORD);
        newline();
        expectIndentation();
        sentenceStructureStyleList();
        forgoIndentation();
    }

    /**
     * WorkStructureStyle := "work" NewLine INDENT WorkStructureStyleList DEDENT
     */
    private void workStructureStyle() {
        currentlyParsedContainer = "Work Structure";
        consumeKeyword(WORK_KEYWORD);
        newline();
        expectIndentation();
        workStructureStyleList();
        forgoIndentation();
    }

    /**
     * WorkStructureStyle := "emphasise" NewLine INDENT EmphasisStructureStyleList DEDENT
     */
    private void emphasisStructureStyle() {
        currentlyParsedContainer = "Work Structure";
        consumeKeyword(EMPHASISE_KEYWORD);
        newline();
        expectIndentation();
        emphasisStructureStyleList();
        forgoIndentation();
    }

    /**
     * SentenceStructureStyleList := "font" Font
     */
    private void sentenceStructureStyleList() {
        expectKeyword(it -> {
            if (it.equals(FONT_KEYWORD)) {
                font();
            } else {
                error();
            }
        }, FONT_KEYWORD);
    }

    /**
     * Font := "font" NewLine INDENT FontStyleList DEDENT
     */
    private void font() {
        consumeKeyword(FONT_KEYWORD);
        newline();
        expectIndentation();
        fontStyleList();
        forgoIndentation();
    }

    private void workStructureStyleList() {
        expectKeyword(it -> {
            if (it.equals(FONT_KEYWORD)) font();
            else error();
        }, FONT_KEYWORD);
    }

    private void emphasisStructureStyleList() {
        expectKeyword(it -> {
            switch (it) {
                case FONT_KEYWORD -> font();
                case ALLOW_KEYWORD -> {
                    consumeKeyword(ALLOW_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getStructure().getEmphasis().setAllowEmphasis(last.value);
                }
                default -> error();
            }
        }, FONT_KEYWORD);
    }

    /**
     * ParagraphStructureStyle := "paragraph" NewLine INDENT "indentation" Textual DEDENT
     */
    private void paragraphStructureStyle() {
        consumeKeyword(PARAGRAPH_KEYWORD);
        newline();

        expectIndentation();
        consume(new Token(TokenType.KEYWORD, INDENTATION_KEYWORD));
        textual();

        ast.getConfiguration().getStyle().getStructure().getParagraph().setIndentation(last.value);

        lastNode = ast.getConfiguration().getStyle().getStructure().getParagraph();

        forgoIndentation();
    }

    /**
     * FontStyleList := "name" Textual FontStyleList |
     * "size" Textual FontStyleList |
     * "colour" Textual FontStyleList |
     * "name" Textual | "size" Textual | "colour" Textual
     */
    private void fontStyleList() {
        expectKeyword(it -> {
            switch (it) {
                case NAME_KEYWORD -> {
                    consumeKeyword(NAME_KEYWORD);
                    textual();

                    var structure = ast.getConfiguration().getStyle().getStructure();
                    switch (currentlyParsedContainer) {
                        case "Sentence Structure" -> structure.getSentence().getFont().setName(last.value);
                        case "Work Structure" -> structure.getWork().getFont().setName(last.value);
                        case "Emphasis Structure" -> structure.getEmphasis().getFont().setName(last.value);
                        default -> error();
                    }
                }
                case SIZE_KEYWORD -> {
                    consumeKeyword(SIZE_KEYWORD);
                    textual();

                    var structure = ast.getConfiguration().getStyle().getStructure();
                    switch (currentlyParsedContainer) {
                        case "Sentence Structure" -> structure.getSentence().getFont().setSize(last.value);
                        case "Work Structure" -> structure.getWork().getFont().setSize(last.value);
                        case "Emphasis Structure" -> structure.getEmphasis().getFont().setSize(last.value);
                        default -> error();
                    }
                }
                case COLOUR_KEYWORD -> {
                    consumeKeyword(COLOUR_KEYWORD);
                    textual();

                    var structure = ast.getConfiguration().getStyle().getStructure();
                    switch (currentlyParsedContainer) {
                        case "Sentence Structure" -> structure.getSentence().getFont().setColour(last.value);
                        case "Work Structure" -> structure.getWork().getFont().setColour(last.value);
                        case "Emphasis Structure" -> structure.getEmphasis().getFont().setColour(last.value);
                        default -> error();
                    }
                }
                default -> error();
            }

            var structure = ast.getConfiguration().getStyle().getStructure();
            lastNode = switch (currentlyParsedContainer) {
                case "Sentence Structure" -> structure.getSentence().getFont();
                case "Work Structure" -> structure.getWork().getFont();
                case "Emphasis Structure" -> structure.getEmphasis().getFont();
                default -> throw new IllegalStateException("Should be in either parsed container");
            };
        }, NAME_KEYWORD, SIZE_KEYWORD, COLOUR_KEYWORD);
    }

    /**
     * PageNumerationStyle := "numeration" NewLine INDENT PageNumerationStyleList DEDENT
     */
    private void pageNumerationStyle() {
        currentlyParsedContainer = "Numeration";

        consumeKeyword(NUMERATION_KEYWORD);
        newline();
        expectIndentation();
        pageNumerationStyleList();
        forgoIndentation();
    }

    /**
     * PageNumerationStyleList := "in" Textual PageNumerationStyleList |
     * "display" Textual PageNumerationStyleList |
     * "margin" Textual PageNumerationStyleList |
     * "skip" TextualList PageNumerationStyleList |
     * "author" AuthorNameType PageNumerationStyleList |
     * "in" Textual | "display" Textual | "margin" Textual | "skip" TextualList |
     * "author" AuthorNameType
     */
    private void pageNumerationStyleList() {
        expectKeyword(it -> {
            switch (current.value) {
                case IN_KEYWORD -> {
                    consumeKeyword(IN_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getNumeration().setNumerationType(last.value);
                }
                case DISPLAY_KEYWORD -> {
                    consumeKeyword(DISPLAY_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getNumeration().setPosition(last.value);
                }
                case LIMIT_KEYWORD -> {
                    consumeKeyword(LIMIT_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getNumeration().setAuthorLimit(last.value);
                }
                case MARGIN_KEYWORD -> {
                    consumeKeyword(MARGIN_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getNumeration().setMargin(last.value);
                }
                case SKIP_KEYWORD -> {
                    consumeKeyword(SKIP_KEYWORD);
                    textualList();
                }
                case AUTHOR_KEYWORD -> {
                    consumeKeyword(AUTHOR_KEYWORD);
                    authorNameType();
                }
                default -> error();
            }

            lastNode = ast.getConfiguration().getStyle().getNumeration();
        }, IN_KEYWORD, DISPLAY_KEYWORD, LIMIT_KEYWORD, MARGIN_KEYWORD, SKIP_KEYWORD, AUTHOR_KEYWORD);
    }

    /**
     * AuthorNameType := "firstname" | "lastname" | "name" | Textual
     */
    private void authorNameType() {
        if (isKeyword()) {
            switch (current.value) {
                case FIRST_NAME_KEYWORD -> consumeKeyword(FIRST_NAME_KEYWORD);
                case LAST_NAME_KEYWORD -> consumeKeyword(LAST_NAME_KEYWORD);
                case NAME_KEYWORD -> consumeKeyword(NAME_KEYWORD);
                default -> error();
            }
            newline();
        } else if (current.type == TokenType.TEXT) textual();
        else error();

        ast.getConfiguration().getStyle().getNumeration().setAuthorName(last.value);
    }

    /**
     * GeneralLayoutStyle := "layout" NewLine INDENT LayoutStyleList DEDENT
     */
    private void generalLayoutStyle() {
        consumeKeyword(LAYOUT_KEYWORD);
        newline();
        expectIndentation();
        layoutStyleList();
        forgoIndentation();
    }

    /**
     * LayoutStyleList := "width" Textual | "height" Textual | "margin" Textual | "spacing" Textual |
     * "width" Textual LayoutStyleList |
     * "height" Textual LayoutStyleList |
     * "margin" Textual LayoutStyleList |
     * "spacing" Textual LayoutStyleList
     */
    private void layoutStyleList() {
        expectKeyword(it -> {
            switch (it) {
                case WIDTH_KEYWORD -> {
                    consumeKeyword(WIDTH_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getLayout().setWidth(last.value);
                }
                case HEIGHT_KEYWORD -> {
                    consumeKeyword(HEIGHT_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getLayout().setHeight(last.value);
                }
                case MARGIN_KEYWORD -> {
                    consumeKeyword(MARGIN_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getLayout().setMargin(last.value);
                }
                case SPACING_KEYWORD -> {
                    consumeKeyword(SPACING_KEYWORD);
                    textual();
                    ast.getConfiguration().getStyle().getLayout().setSpacing(last.value);
                }
                default -> error();
            }

            lastNode = ast.getConfiguration().getStyle().getLayout();
        }, WIDTH_KEYWORD, HEIGHT_KEYWORD, MARGIN_KEYWORD, SPACING_KEYWORD);
    }


    /**
     * TitleTextual := Text TitleTextual | Emphasis TitleTextual | Work TitleTextual | Textual | Emphasis | Work
     */
    private void titleTextual() {
        var wasFirst = true;
        do {
            if (wasFirst) wasFirst = false;
            else remainIndentation();

            if (isKeyword(EMPHASISE_KEYWORD)) emphasis();
            else if (isKeyword(WORK_KEYWORD)) work();
            else if (current.type == TokenType.TEXT) {
                textual();
                var titleText = new TitleText(last.value);

                if (currentlyParsedContainer.equals(DOCUMENT_TITLE_CONTAINER_NAME))
                    ast.getConfiguration().getTitle().add(titleText);
                else if (currentlyParsedContainer.equals(PUBLICATION_CONFIGURATION_CONTAINER_NAME))
                    ast.getConfiguration().getPublication().getTitle().add(titleText);

                lastNode = titleText;
            } else error();
        } while (frontEndBridge.containsTokens() && (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                (frontEndBridge.lookahead(0).value.equals(EMPHASISE_KEYWORD) ||
                        frontEndBridge.lookahead(0).value.equals(WORK_KEYWORD)) ||
                frontEndBridge.lookahead(0).type == TokenType.TEXT));
    }

    /**
     * Emphasis := "emphasise" Textual
     */
    private void emphasis() {
        consumeKeyword(EMPHASISE_KEYWORD);

        if (current.type == TokenType.TEXT) {
            textual();

            var titleText = new TitleText(new Emphasis(last.value));

            switch (currentlyParsedContainer) {
                case DOCUMENT_TITLE_CONTAINER_NAME -> ast.getConfiguration().getTitle().add(titleText);
                case PUBLICATION_CONFIGURATION_CONTAINER_NAME ->
                        ast.getConfiguration().getPublication().getTitle().add(titleText);
                case "paragraph" -> currentParagraph.enqueueParagraphInstruction(new Emphasise(last.value));
                default -> throw new UnsupportedOperationException("Container type not yet implemented");
            }

            lastNode = titleText;
        } else error();
    }

    /**
     * Work := "work" Textual
     */
    private void work() {
        consumeKeyword(WORK_KEYWORD);

        if (current.type == TokenType.TEXT) {
            textual();

            var titleText = new TitleText(new Work(last.value));

            switch (currentlyParsedContainer) {
                case DOCUMENT_TITLE_CONTAINER_NAME -> ast.getConfiguration().getTitle().add(titleText);
                case PUBLICATION_CONFIGURATION_CONTAINER_NAME ->
                        ast.getConfiguration().getPublication().getTitle().add(titleText);
                case "paragraph" ->
                        currentParagraph.enqueueParagraphInstruction(new frontend.ast.paragraph.Work(last.value));
                default -> throw new UnsupportedOperationException("Container type not yet implemented");
            }

            lastNode = titleText;
        } else error();
    }

    /**
     * Paragraph := ParagraphInstruction+ NewLine
     */
    private void paragraph() {
        currentParagraph = new Paragraph();
        currentlyParsedContainer = "paragraph";

        do paragraphInstruction();
        while (frontEndBridge.containsTokens() && current.type == TokenType.TEXT ||
                isKeyword(CITATION_KEYWORD) || isKeyword(WORK_KEYWORD) || isKeyword(EMPHASISE_KEYWORD));

        newline();

        ast.pushDocumentNode(currentParagraph);
        currentParagraph = null;
    }

    /**
     * ParagraphInstruction := Textual | Emphasis | Work | Citation
     */
    private void paragraphInstruction() {
        if (current.type == TokenType.TEXT) {
            remainIndentation();
            textual();

            currentParagraph.enqueueParagraphInstruction(new Text(last.value));
        } else if (isKeyword()) {
            remainIndentation();
            switch (current.value) {
                case EMPHASISE_KEYWORD -> emphasis();
                case WORK_KEYWORD -> work();
                case CITATION_KEYWORD -> citation();
                default -> error();
            }
        } else error();
    }

    /**
     * Citation := "citation" KeywordCitation | "citation" CitationInlineDeclaration
     */
    private void citation() {
        consumeKeyword(CITATION_KEYWORD);

        if (current.type == TokenType.NEW_LINE) {
            newline();
            expectIndentation();
            citationDetails();
            forgoIndentation();
        } else if (current.type == TokenType.TEXT) {
            citationInlineDeclaration();
        } else error();
    }

    /**
     * CitationInlineDeclaration := Textual ListSeparator Textual | Textual ListSeparator Textual ListSeparator Textual
     */
    private void citationInlineDeclaration() {
        final var citation = new Citation();

        consume(new Token(TokenType.TEXT, null));
        citation.setSource(last.value);

        listSeparator();

        consume(new Token(TokenType.TEXT, null));
        citation.setCitedContent(last.value);

        if (current.type == TokenType.LIST_SEPARATOR) {
            listSeparator();
            textual();
            citation.setNumeration(last.value);
        } else if (current.type == TokenType.NEW_LINE) {
            newline();
        }

        currentParagraph.enqueueParagraphInstruction(citation);
    }

    /**
     * CitationDetails := "id" Textual | "of" Textual | "page" Textual | _any_ CitationDetails
     */
    private void citationDetails() {
        final var citation = new Citation();
        expectKeyword(it -> {
            switch (it) {
                case ID_KEYWORD -> {
                    consumeKeyword(ID_KEYWORD);
                    textual();
                    citation.setSource(last.value);
                }
                case OF_KEYWORD -> {
                    consumeKeyword(OF_KEYWORD);
                    textual();
                    citation.setCitedContent(last.value);
                }
                case PAGE_KEYWORD -> {
                    consumeKeyword(PAGE_KEYWORD);
                    textual();
                    citation.setNumeration(last.value);
                }
                default -> error();
            }
        }, ID_KEYWORD, OF_KEYWORD, PAGE_KEYWORD);

        currentParagraph.enqueueParagraphInstruction(citation);
    }

    /**
     * ListSeparator := ","
     */
    private void listSeparator() {
        consume(new Token(TokenType.LIST_SEPARATOR, ","));
    }

    /**
     * Textual := Text Newline
     */
    private void textual() {
        consume(new Token(TokenType.TEXT, null));
        newline();
    }

    /**
     * TextualList := Textual | Textual "," TextualList
     */
    private void textualList() {
        textual();

        if (currentlyParsedContainer.equals("Numeration")) {
            ast.getConfiguration().getStyle().getNumeration().addSkippedPage(last.value);
        }

        if (current.type == TokenType.LIST_SEPARATOR) {
            consume(new Token(TokenType.LIST_SEPARATOR, null));
            textualList();
        }
    }

    /**
     * NewLine := "NewLine" | epsilon
     */
    private void newline() {
        if (frontEndBridge.containsTokens()) consume(new Token(TokenType.NEW_LINE, null));
    }

    /**
     * Returns true if the current token is a KEYWORD token
     *
     * @return - true if the current type is KEYWORD, false if not
     */
    private boolean isKeyword() {
        return current.type == TokenType.KEYWORD;
    }

    /**
     * Returns true if the current token is a keyword, which is equal to the specified keyword
     *
     * @param keyword - the keyword that should be matched against the current token value
     * @return - true if the keyword equals the specified keyword, false else
     */
    private boolean isKeyword(@NonNull final String keyword) {
        return isKeyword() && current.value.equals(keyword);
    }

    /**
     * Simple helper method to consume the specified keyword.
     * Note that this method does NOT throw state exceptions!
     *
     * @param keyword - the keyword that should be consumed
     */
    private void consumeKeyword(@NonNull final String keyword) {
        consume(new Token(TokenType.KEYWORD, keyword));
    }

}
