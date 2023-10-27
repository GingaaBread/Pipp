package frontend.parsing;

import frontend.FrontEndBridge;
import frontend.ast.AST;
import frontend.ast.NoArgumentStructure;
import frontend.ast.Node;
import frontend.ast.config.*;
import frontend.lexical_analysis.Token;
import frontend.lexical_analysis.TokenType;
import lombok.NonNull;
import processing.StructureType;

/**
 *  The Parser class is responsible for creating a syntax tree for the Pipp document.
 *  It follows the top-down approach by making use of a lookahead in the FrontEndBridge.
 *
 * @version 1.0
 * @since 1.0
 */
public class Parser {

    /**
     *  The bridge is responsible for providing an interface between the Parser and the Scanner
     *  The Parser can access the tokens scanned by the Scanner, and dequeue them.
     *  Additionally, the bridge provides a lookahead method, which the Parser uses to look at a
     *  future token to choose the correct production
     */
    private final FrontEndBridge frontEndBridge;

    /**
     *  Tracks the current token, which needs to be parsed
     */
    private Token current;

    /**
     *  Tracks the last token, which has already been parsed.
     *  It ignores the following "helper" tokens: NewLine, Indentation
     */
    private Token last;

    /**
     *  Tracks the node, which was last added to the AST
     */
    private Node lastNode;

    /**
     *  Keeps track of the currently required indentation level
     */
    private int currentIndentationLevel = 0;

    /**
     *  Marks the currently parsed line for debugging messages
     */
    private int currentLine = 0;

    /**
     *  Determines the type of container that is currently being parsed.
     *  This is used to enable different parts to be parsed by different containers
     *  (for example, the title configuration)
     */
    private String currentlyParsedContainer = "Configuration";

    /**
     *  The abstract syntax tree of the document.
     *  The parser builds the AST depending on the text specified by the user.
     */
    private final AST ast;

    /**
     *  Creates the parser by supplying the bridge.
     * @param frontEndBridge - the bridge between the Scanner and Parser
     */
    public Parser(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
        this.ast = new AST();
    }

    /**
     *  Once done parsing, it throws an error if there are still tokens.
     *  If there are no more tokens, it starts the processing phase, instead.
     */
    private void afterParsing() {
        if (frontEndBridge.containsTokens()) error();

        frontEndBridge.startProcessor(ast);
    }

    /**
     *  Tries to parse an INDENT token and checks if its indentation value (amount of tabs) corresponds to the
     *  required amount. If not, an indentation error is thrown, but if it does, the parser skips to the next token
     */
    private void expectIndentation() {
        if (current.type == TokenType.INDENT) {
            if (Integer.parseInt(current.value) != currentIndentationLevel + 1) indendationError();
            else if (frontEndBridge.containsTokens()) {
                current = frontEndBridge.dequeueToken();

                if (current.type == TokenType.NEW_LINE) currentLine++;

                currentIndentationLevel++;
            }
        } else indendationError();
    }

    /**
     *  Decreases the expected indentation level if possible.
     *  Else, an IllegalStateException is thrown
     */
    private void forgoIndentation() {
        if (frontEndBridge.containsTokens()) {
            if (currentIndentationLevel <= 0) throw new IllegalStateException("(" + currentLine + ") Should not try" +
                    " to forgo indentation as there is no more indentation");

        }
            currentIndentationLevel--;
    }

    /**
     *  Tries to consume the current INDENT token which has the same indentation level as the currently required
     */
    private void remainIndentation() {
        if (frontEndBridge.containsTokens()) {
            if (current.type != TokenType.INDENT) throw new IllegalStateException("(" + currentLine + ") Should" +
                    " remain at the current indentation level (" + currentIndentationLevel + "). Instead" +
                    " found unexpected token: " + current);

            if (Integer.parseInt(current.value) != currentIndentationLevel) indendationError();
            else if (frontEndBridge.containsTokens()) {
                current = frontEndBridge.dequeueToken();

                if (current.type == TokenType.NEW_LINE) currentLine++;
            } else error();
        }
    }

    /**
     *  Tries to consume to current token by comparing it to the required token.
     *  If there is no current token, the program is in an illegal state.
     *  If the required token does not match with the current token, the program entered by the user
     *  is invalid, and an exception is thrown.
     *  If the tokens do match, the next token is dequeued in the bridge if it exists.
     * @param requiredToken - the required token compared to the current token
     */
    private void consume(final Token requiredToken) {
        var lastCurrent = current;
        // For debugging purposes
        if (current.type == TokenType.NEW_LINE) currentLine++;

        if (isKeyword() && requiredToken.type == TokenType.KEYWORD &&
                current.value.equals(requiredToken.value) || current.type != TokenType.KEYWORD &&
                current.type == requiredToken.type) {
            if (frontEndBridge.containsTokens()) current = frontEndBridge.dequeueToken();
        } else throw new IllegalArgumentException("(" + currentLine + ") Unexpected token: " + current +
                ". Expected: " + requiredToken);

        if (lastCurrent.type != TokenType.NEW_LINE && lastCurrent.type != TokenType.INDENT) last = lastCurrent;
    }

    /**
     *  This method is called whenever a token is specified in a production without having to
     *  consume a token directly. In that case, an IllegalArgumentException is thrown.
     */
    private void error() {
        throw new IllegalArgumentException("(" + currentLine + ") Unexpected token: " + current);
    }

    /**
     *  This method is called whenever an indentation is expected, but not given by the user
     */
    private void indendationError() {
        throw new IllegalArgumentException("(" + currentLine + ") Indentation error. Current token is: " +
                current);
    }

    /**
     *  S := Document | epsilon
     */
    public void s() {
        if (frontEndBridge.containsTokens()) {
            current = frontEndBridge.dequeueToken();
            document();

            afterParsing();
        }
    }

    /**
     *  Document := ConfigContainer InstructionList | InstructionList
     */
    private void document() {
        if (isKeyword() && current.value.equals("config")) configContainer();

        instructionList();
    }

    // InstructionList := OptionalNewLine Instruction InstructionList | OptionalNewLine Instruction
    private void instructionList() {
        do {
            if (current.type == TokenType.NEW_LINE) newline();
            else if (current.type == TokenType.KEYWORD) {
                switch (current.value) {
                    case "header" -> header();
                    case "title" -> title();
                    case "blank" -> blank();
                    default -> error();
                }
            } else error();
        } while (frontEndBridge.containsTokens() && (current.type == TokenType.NEW_LINE ||
                isKeyword() && (current.value.equals("header") || current.value.equals("title") ||
                        current.value.equals("blank")) ));
    }

    /**
     *  Title := "title" Newline
     */
    private void title() {
        consumeKeyword("title");
        newline();
        ast.pushDocumentNode(new NoArgumentStructure(StructureType.TITLE));
    }

    /**
     *  Header := "header" Newline
     */
    private void header() {
        consumeKeyword("header");
        newline();
        ast.pushDocumentNode(new NoArgumentStructure(StructureType.HEADER));
    }

    /**
     *  Blank := "blank" Newline
     */
    private void blank() {
        consumeKeyword("blank");
        newline();
        ast.pushDocumentNode(new NoArgumentStructure(StructureType.BLANKPAGE));
    }

    /**
     *  ConfigContainer := "config" Newline INDENT ConfigList DEDENT
     */
    private void configContainer() {
        consume(new Token(TokenType.KEYWORD, "config"));
        newline();
        expectIndentation();
        configList();
        forgoIndentation();
    }

    /**
     *  ConfigList := Configuration | Configuration ConfigList
     */
    private void configList() {
        do {
            configuration();

            if (frontEndBridge.containsTokens()) {
                var ahead = frontEndBridge.lookahead(0);
                if (ahead.value.equals("style") || ahead.value.equals("title") || ahead.value.equals("author") ||
                        ahead.value.equals("assessor") || ahead.value.equals("type") ||
                        ahead.value.equals("publication"))
                    remainIndentation();
            }
        } while (frontEndBridge.containsTokens() && isKeyword() && (
                current.value.equals("style") || current.value.equals("title") || current.value.equals("author") ||
                current.value.equals("assessor") || current.value.equals("type") ||
                        current.value.equals("publication")));
    }

    /**
     *  Configuration := StyleConfiguration | TitleConfiguration | AuthorConfiguration | AssessorConfiguration |
     *                   PublicationConfiguration | TypeConfiguration
     */
    private void configuration() {
        if (isKeyword()) {
            switch (current.value) {
                case "style" -> styleConfiguration();
                case "title" -> documentTitleConfiguration();
                case "author" -> authorConfiguration();
                case "assessor" -> assessorConfiguration();
                case "publication" -> publicationConfiguration();
                case "type" -> typeConfiguration();
                default -> error();
            }
        } else error();
    }

    private void documentTitleConfiguration() {
        currentlyParsedContainer = "Document Title";
        titleConfiguration();
    }

    /**
     *  PublicationConfiguration := "publication" NewLine INDENT TitleConfiguration DateConfiguration |
     *                              "publication" NewLine INDENT DateConfiguration TitleConfiguration |
     *                              "publication" NewLine INDENT TitleConfiguration DEDENT |
     *                              "publication" NewLine INDENT DateConfiguration DEDENT
     */
    private void publicationConfiguration() {
        currentlyParsedContainer = "publication";

        consume(new Token(TokenType.KEYWORD, "publication"));
        newline();
        expectIndentation();

        if (isKeyword()) {
            if (current.value.equals("title")) {
                titleConfiguration();

                if (frontEndBridge.lookahead(0).value.equals("date")) {
                    remainIndentation();
                    dateConfiguration();
                }
            } else if (current.value.equals("date")) {
                dateConfiguration();

                if (frontEndBridge.lookahead(0).value.equals("title")) {
                    remainIndentation();
                    titleConfiguration();
                }
            }
        } else error();

        forgoIndentation();
    }

    /**
     *  DateConfiguration := "date" Textual
     */
    private void dateConfiguration() {
        consume(new Token(TokenType.KEYWORD, "date"));
        textual();

        if (currentlyParsedContainer.equals("publication")) {
            ast.getConfiguration().getPublication().setDate(last.value);
            lastNode = ast.getConfiguration().getPublication();
        }
    }

    /**
     *  TypeConfiguration := "type" Textual
     */
    private void typeConfiguration() {
        consume(new Token(TokenType.KEYWORD, "type"));
        textual();

        ast.getConfiguration().setDocumentType(last.value);
        lastNode = ast.getConfiguration();
    }

    /**
     *  AssessorConfiguration := "assessor" NewLine INDENT AssessorSpecification DEDENT | "assessor" Textual
     */
    private void assessorConfiguration() {
        currentlyParsedContainer = "Assessor";

        consume(new Token(TokenType.KEYWORD, "assessor"));

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
     *  AssessorSpecification := NameSpecificationWithOptRole | AssessorList
     */
    private void assessorSpecification() {
        if (isKeyword()) {
            switch (current.value) {
                case "name", "firstname", "title" -> nameSpecificationWithOptRole();
                case "of" -> assessorList();
                default -> error();
            }
        } else error();
    }

    /**
     *  AssessorList := AssessorItem | AssessorItem AssessorList
     */
    private void assessorList() {
        do {
            consume(new Token(TokenType.KEYWORD, "of"));

            if (current.type == TokenType.NEW_LINE) {
                newline();
                expectIndentation();
                nameSpecificationWithOptRole();
                forgoIndentation();

                if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                        frontEndBridge.lookahead(0).value.equals("of")) {
                    remainIndentation();
                }
            } else if (current.type == TokenType.TEXT) {
                textual();

                if (currentlyParsedContainer.equals("Assessor")) {
                    var assessor = new Assessor();
                    assessor.setName(last.value);
                    ast.getConfiguration().getAssessors().add(assessor);
                }

                if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                        frontEndBridge.lookahead(0).value.equals("of")) {
                    remainIndentation();
                }
            }
        } while (frontEndBridge.containsTokens() && isKeyword() && current.value.equals("of"));
    }

    /**
     * NameSpecificationWithOptRole := NameSpecification | NameSpecification "role" Textual
     */
    private void nameSpecificationWithOptRole() {
        nameSpecification();
        if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                frontEndBridge.lookahead(0).value.equals("role")) {
            remainIndentation();
            consume(new Token(TokenType.KEYWORD, "role"));
            textual();

            if (currentlyParsedContainer.equals("Assessor")) {
                ((Assessor) lastNode).setRole(last.value);
            }
        }
    }

    /**
     *  AuthorConfiguration := "author" INDENT NewLine AuthorSpecification DEDENT | "author" Textual
     */
    private void authorConfiguration() {
        consume(new Token(TokenType.KEYWORD, "author"));

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
     *  AuthorSpecification := NameSpecificationWithOptID | AuthorList
     */
    private void authorSpecification() {
        if (isKeyword()) {
            currentlyParsedContainer = "Author";
            switch (current.value) {
                case "name", "firstname", "title" -> nameSpecificationWithOptId();
                case "of" -> authorList();
                default -> error();
            }
        } else error();
    }

    /**
     *  AuthorList := AuthorItem | AuthorItem AuthorList
     */
    private void authorList() {
        do {
            consume(new Token(TokenType.KEYWORD, "of"));

            if (current.type == TokenType.NEW_LINE) {
                newline();
                expectIndentation();
                nameSpecificationWithOptId();
                forgoIndentation();

                if (frontEndBridge.lookahead(0).value.equals("of")) remainIndentation();
            } else if (current.type == TokenType.TEXT) {
                textual();

                if (currentlyParsedContainer.equals("Author")) {
                    var author = new Author();
                    author.setName(last.value);
                    ast.getConfiguration().getAuthors().add(author);
                }

                if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                        frontEndBridge.lookahead(0).value.equals("of")) {
                    remainIndentation();
                }
            }
            else error();
        } while (frontEndBridge.containsTokens() && isKeyword() && current.value.equals("of"));
    }

    /**
     *  NameSpecificationWithOptID := NameSpecification | NameSpecification "id" Textual
     */
    private void nameSpecificationWithOptId() {
        nameSpecification();
        if (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
                frontEndBridge.lookahead(0).value.equals("id")) {
            remainIndentation();
            consume(new Token(TokenType.KEYWORD, "id"));
            textual();

            if (currentlyParsedContainer.equals("Author")) {
                ((Author) lastNode).setId(last.value);
            }
        }
    }

    /**
     *  NameSpecification := Name | FirstName LastName | Title Name | Title FirstName LastName
     */
    private void nameSpecification() {
        String title = null;
        if (isKeyword() && current.value.equals("title")) {
            consumeKeyword("title");
            textual();
            remainIndentation();

            title = last.value;
        }

        if (isKeyword()) {
            if (current.value.equals("name")) {
                name();

                if (currentlyParsedContainer.equals("Author")) {
                    var author = new Author();
                    author.setTitle(title);
                    author.setName(last.value);

                    ast.getConfiguration().getAuthors().add(author);
                    lastNode = author;
                } else if (currentlyParsedContainer.equals("Assessor")) {
                    var assessor = new Assessor();
                    assessor.setTitle(title);
                    assessor.setName(last.value);

                    ast.getConfiguration().getAssessors().add(assessor);
                    lastNode = assessor;
                }
            } else if (current.value.equals("firstname")) {
                firstname();

                var firstname = last.value;

                remainIndentation();
                lastname();

                var lastname = last.value;

                if (currentlyParsedContainer.equals("Author")) {
                    var author = new Author();
                    author.setTitle(title);
                    author.setFirstname(firstname);
                    author.setLastname(lastname);

                    ast.getConfiguration().getAuthors().add(author);
                    lastNode = author;
                } else if (currentlyParsedContainer.equals("Assessor")) {
                    var assessor = new Assessor();
                    assessor.setTitle(title);
                    assessor.setFirstname(firstname);
                    assessor.setLastname(lastname);

                    ast.getConfiguration().getAssessors().add(assessor);
                    lastNode = assessor;
                }
            } else error();
        } else error();
    }

    /**
     *  FirstName := "firstname" Textual
     */
    private void firstname() {
        consume(new Token(TokenType.KEYWORD, "firstname"));
        textual();
    }

    /**
     *  LastName := "lastname" Textual
     */
    private void lastname() {
        consume(new Token(TokenType.KEYWORD, "lastname"));
        textual();
    }

    /**
     *  Name := "name" Textual
     */
    private void name() {
        consume(new Token(TokenType.KEYWORD, "name"));
        textual();
    }

    /**
     *  TitleConfiguration := "title" Textual | "title" NewLine INDENT CitedTextual DEDENT
     */
    private void titleConfiguration() {
        consumeKeyword("title");

        if (current.type == TokenType.NEW_LINE) {
            newline();
            expectIndentation();
            titleTextual();
            forgoIndentation();
        } else if (current.type == TokenType.TEXT) {
            textual();

            if (currentlyParsedContainer.equals("Document Title")) {
                ast.getConfiguration().getTitle().add(new TitleText(last.value));
                lastNode = ast.getConfiguration().getTitle();
            } else if (currentlyParsedContainer.equals("publication")) {
                ast.getConfiguration().getPublication().getTitle().add(new TitleText(last.value));
                lastNode = ast.getConfiguration().getPublication().getTitle();
            }
        }
        else error();
    }

    /**
     *  StyleConfiguration := "style" Textual | "style" NewLine INDENT CustomStyle DEDENT
     */
    private void styleConfiguration() {
        consume(new Token(TokenType.KEYWORD, "style"));

        if (current.type == TokenType.TEXT) {
            textual();

            ast.getConfiguration().getStyle().setBaseStyle(last.value);
            lastNode = ast.getConfiguration().getStyle();
        }
        else {
            newline();
            expectIndentation();
            customStyle();
            forgoIndentation();
            remainIndentation();
        }
    }

    /**
     *  CustomStyle := "of" Textual OptionalCustomStyleList
     */
    private void customStyle() {
        consume(new Token(TokenType.KEYWORD, "of"));
        textual();

        ast.getConfiguration().getStyle().setBaseStyle(last.value);
        lastNode = ast.getConfiguration().getStyle();

        optionalCustomStyleList();
    }

    /**
     *  OptionalCustomStyleList := epsilon | GeneralLayoutStyle | PageNumerationStyle | FontStyle | StructuresStyle |
     *                     GeneralLayoutStyle CustomStyleList |
     *                     PageNumerationStyle CustomStyleList |
     *                     FontStyle CustomStyleList |
     *                     StructuresStyle | CustomStyleList
     */
    private void optionalCustomStyleList() {
        remainIndentation();
        while (current.value.equals("layout") || current.value.equals("numeration")
                || current.value.equals("font") || current.value.equals("structure")) {
            switch (current.value) {
                case "layout" -> generalLayoutStyle();
                case "numeration" -> pageNumerationStyle();
                case "font" -> fontStyle();
                case "structure" -> structureStyle();
                default -> error();
            }
        }
    }

    /**
     *  StructureStyle := "structure" NewLine INDENT ParagraphStructureStyle DEDENT |
     *                    "structure" NewLine INDENT SentenceStructureStyle DEDENT |
     *                    "structure" NewLine INDENT EndnotesStructureStyle DEDENT
     */
    private void structureStyle() {
        if (isKeyword()) {
            consume(new Token(TokenType.KEYWORD, "structure"));
            newline();
            expectIndentation();

            do {
                switch (current.value) {
                    case "paragraph" -> paragraphStructureStyle();
                    case "sentence" -> sentenceStructureStyle();
                    case "endnotes" -> endnotesStructureStyle();
                    default -> error();
                }
            } while (current.value.equals("paragraph") || current.value.equals("sentence") ||
                    current.value.equals("endnotes"));

            forgoIndentation();
            remainIndentation();
        } else error();
    }

    /**
     *  EndnotesStructureStyle := "endnotes" NewLine INDENT "allow" NewLine INDENT "before" Textual DEDENT DEDENT
     */
    private void endnotesStructureStyle() {
        consume(new Token(TokenType.KEYWORD, "endnotes"));
        newline();
        expectIndentation();

        consume(new Token(TokenType.KEYWORD, "allow"));
        newline();
        expectIndentation();

        consume(new Token(TokenType.KEYWORD, "before"));
        structuralInstruction();

        ast.getConfiguration().getStyle().getStructure().getEndnotes().setAllowBeforeStructure(last.value);
        lastNode = ast.getConfiguration().getStyle().getStructure().getEndnotes();

        forgoIndentation();
        forgoIndentation();
    }

    /**
     *  SentenceStructureStyle := "sentence" NewLine INDENT SentenceStructureStyleList DEDENT
     */
    private void sentenceStructureStyle() {
        consume(new Token(TokenType.KEYWORD, "sentence"));
        newline();
        expectIndentation();
        sentenceStructureStyleList();
        forgoIndentation();
        remainIndentation();
    }

    /**
     * SentenceStructureStyleList := "before" Textual SentenceStructureStyleList |
     *                               "allow" NewLine INDENT SentenceStructureStyleAllowConfiguration
     *                                  SentenceStructureStyleList DEDENT |
     *                               "before" Textual |
     *                               "allow" NewLine SentenceStructureStyleAllowConfiguration
     */
    private void sentenceStructureStyleList() {
        if (isKeyword()) {
            do {
                switch (current.value) {
                    case "before" -> {
                        consume(new Token(TokenType.KEYWORD, "before"));
                        textual();
                        ast.getConfiguration().getStyle().getStructure().getSentence().setPrefix(last.value);
                        lastNode = ast.getConfiguration().getStyle().getStructure().getSentence();
                    }
                    case "allow" -> {
                        consume(new Token(TokenType.KEYWORD, "allow"));
                        newline();
                        expectIndentation();
                        sentenceStructureStyleAllowConfiguration();
                        forgoIndentation();
                    }
                    default -> error();
                }

                var ahead = frontEndBridge.lookahead(0);
                if (ahead.value.equals("before") || ahead.value.equals("allow"))
                    remainIndentation();
            } while (current.value.equals("before") || current.value.equals("allow"));
        } else error();
    }

    /**
     *  SentenceStructureStyleAllowConfiguration := "whitespace" Textual SentenceStructureStyleAllowConfiguration |
     *                                              "bold" Textual SentenceStructureStyleAllowConfiguration |
     *                                              "italic" Textual SentenceStructureStyleAllowConfiguration |
     *                                              "whitespace" Textual |
     *                                              "bold" Textual |
     *                                              "italic" Textual
     */
    private void sentenceStructureStyleAllowConfiguration() {
        if (isKeyword()) {
            do {
                switch (current.value) {
                    case "whitespace" -> {
                        consume(new Token(TokenType.KEYWORD, "whitespace"));
                        textual();
                        ast.getConfiguration().getStyle().getStructure().getSentence().setAllowWhitespace(last.value);
                    }
                    case "bold" -> {
                        consume(new Token(TokenType.KEYWORD, "bold"));
                        textual();
                        ast.getConfiguration().getStyle().getStructure().getSentence().setAllowBoldText(last.value);
                    }
                    case "italic" -> {
                        consume(new Token(TokenType.KEYWORD, "italic"));
                        textual();
                        ast.getConfiguration().getStyle().getStructure().getSentence().setAllowItalicText(last.value);
                    }
                    default -> error();
                }

                lastNode = ast.getConfiguration().getStyle().getStructure().getSentence();

                var ahead = frontEndBridge.lookahead(0);
                if (ahead.value.equals("whitespace") || ahead.value.equals("bold") ||
                        ahead.value.equals("italic"))
                    remainIndentation();
            } while (current.value.equals("whitespace") || current.value.equals("bold") ||
                    current.value.equals("italic"));
        } else error();
    }

    /**
     *  ParagraphStructureStyle := "paragraph" NewLine INDENT "indentation" Textual DEDENT
     */
    private void paragraphStructureStyle() {
        consume(new Token(TokenType.KEYWORD, "paragraph"));
        newline();

        expectIndentation();
        consume(new Token(TokenType.KEYWORD, "indentation"));
        textual();

        ast.getConfiguration().getStyle().getStructure().getParagraph().setIndentation(last.value);

        lastNode = ast.getConfiguration().getStyle().getStructure().getParagraph();

        forgoIndentation();
        remainIndentation();
    }

    /**
     *  FontStyle := "font" NewLine INDENT FontStyleList DEDENT
     */
    private void fontStyle() {
        consume(new Token(TokenType.KEYWORD, "font"));
        newline();
        expectIndentation();
        fontStyleList();
        forgoIndentation();
        remainIndentation();
    }

    /**
     *  FontStyleList := "name" Textual FontStyleList |
     *                   "size" Textual FontStyleList |
     *                   "colour" Textual FontStyleList |
     *                   "name" Textual | "size" Textual | "colour" Textual
     */
    private void fontStyleList() {
        if (isKeyword()) {
            do {
                switch (current.value) {
                    case "name" -> {
                        consume(new Token(TokenType.KEYWORD, "name"));
                        textual();
                        ast.getConfiguration().getStyle().getFont().setName(last.value);
                    }
                    case "size" -> {
                        consume(new Token(TokenType.KEYWORD, "size"));
                        textual();
                        ast.getConfiguration().getStyle().getFont().setSize(last.value);
                    }
                    case "colour" -> {
                        consume(new Token(TokenType.KEYWORD, "colour"));
                        textual();
                        ast.getConfiguration().getStyle().getFont().setColour(last.value);
                    }
                    default -> error();
                }

                lastNode = ast.getConfiguration().getStyle().getFont();

                var ahead = frontEndBridge.lookahead(0);
                if (ahead.value.equals("name") || ahead.value.equals("size") || ahead.value.equals("colour"))
                    remainIndentation();
            } while (current.value.equals("name") || current.value.equals("size") ||
                    current.value.equals("colour"));
        } else error();
    }

    /**
     *  PageNumerationStyle := "numeration" NewLine INDENT PageNumerationStyleList DEDENT
     */
    private void pageNumerationStyle() {
        currentlyParsedContainer = "Numeration";

        consumeKeyword("numeration");
        newline();
        expectIndentation();
        pageNumerationStyleList();
        forgoIndentation();
    }

    /**
     *  PageNumerationStyleList := "in" Textual PageNumerationStyleList |
     *                             "display" Textual PageNumerationStyleList |
     *                             "margin" Textual PageNumerationStyleList |
     *                             "skip" TextualList PageNumerationStyleList |
     *                             "author" AuthorNameType PageNumerationStyleList |
     *                             "in" Textual | "display" Textual | "margin" Textual | "skip" TextualList |
     *                                  "author" AuthorNameType
     */
    private void pageNumerationStyleList() {
        if (isKeyword()) {
            do {
                switch (current.value) {
                    case "in" -> {
                        consumeKeyword("in");
                        textual();
                        ast.getConfiguration().getStyle().getNumeration().setNumerationType(last.value);
                    }
                    case "display" -> {
                        consumeKeyword("display");
                        textual();
                        ast.getConfiguration().getStyle().getNumeration().setPosition(last.value);
                    }
                    case "margin" -> {
                        consumeKeyword("margin");
                        textual();
                        ast.getConfiguration().getStyle().getNumeration().setMargin(last.value);
                    }
                    case "skip" -> {
                        consumeKeyword("skip");
                        textualList();
                    }
                    case "author" -> {
                        consumeKeyword("author");
                        authorNameType();
                    }
                    default -> error();
                }

                lastNode = ast.getConfiguration().getStyle().getNumeration();

                var ahead = frontEndBridge.lookahead(0);
                if (ahead.value.equals("in") || ahead.value.equals("display") ||
                        ahead.value.equals("margin") || ahead.value.equals("skip") || ahead.value.equals("author"))
                    remainIndentation();
            } while (current.value.equals("in") || current.value.equals("display") ||
                    current.value.equals("margin") || current.value.equals("skip") || current.value.equals("author"));
        } else error();
    }

    /**
     *  AuthorNameType := "firstname" | "lastname" | "name" | Textual
     */
    private void authorNameType() {
        if (isKeyword()) {
            switch (current.value) {
                case "firstname" -> consumeKeyword("firstname");
                case "lastname" -> consumeKeyword("lastname");
                case "name" -> consumeKeyword("name");
                default -> error();
            }
            newline();
        } else if (current.type == TokenType.TEXT) textual();
        else error();

        ast.getConfiguration().getStyle().getNumeration().setAuthorName(last.value);
    }

    /**
     *  GeneralLayoutStyle := "layout" NewLine INDENT LayoutStyleList DEDENT
     */
    private void generalLayoutStyle() {
        consumeKeyword("layout");
        newline();
        expectIndentation();
        layoutStyleList();
        forgoIndentation();
        remainIndentation();
    }

    /**
     *  LayoutStyleList := "width" Textual | "height" Textual | "margin" Textual | "spacing" Textual |
     *                     "width" Textual LayoutStyleList |
     *                     "height" Textual LayoutStyleList |
     *                     "margin" Textual LayoutStyleList |
     *                     "spacing" Textual LayoutStyleList
     */
    private void layoutStyleList() {
        if (isKeyword()) {
            do {
                switch (current.value) {
                    case "width" -> {
                        consumeKeyword("width");
                        textual();
                        ast.getConfiguration().getStyle().getLayout().setWidth(last.value);
                    }
                    case "height" -> {
                        consumeKeyword("height");
                        textual();
                        ast.getConfiguration().getStyle().getLayout().setHeight(last.value);
                    }
                    case "margin" -> {
                        consumeKeyword("margin");
                        textual();
                        ast.getConfiguration().getStyle().getLayout().setMargin(last.value);
                    }
                    case "spacing" -> {
                        consumeKeyword("spacing");
                        textual();
                        ast.getConfiguration().getStyle().getLayout().setSpacing(last.value);
                    }
                    default -> error();
                }

                lastNode = ast.getConfiguration().getStyle().getLayout();

                var ahead = frontEndBridge.lookahead(0);
                if (ahead.value.equals("width") || ahead.value.equals("height") ||
                        ahead.value.equals("margin") || ahead.value.equals("spacing"))
                    remainIndentation();
            } while (current.value.equals("width") || current.value.equals("height") ||
                    current.value.equals("margin") || current.value.equals("spacing"));
        } else error();
    }

    /**
     *  TitleTextual := Text TitleTextual | Emphasis TitleTextual | Work TitleTextual | Textual | Emphasis | Work
     */
    private void titleTextual() {
        var wasFirst = true;
        do {
            if (wasFirst) wasFirst = false;
            else remainIndentation();

            if (isKeyword("emphasise")) emphasis();
            else if (isKeyword("work")) work();
            else if (current.type == TokenType.TEXT) {
                textual();
                var titleText = new TitleText(last.value);

                if (currentlyParsedContainer.equals("Document Title"))
                    ast.getConfiguration().getTitle().add(titleText);
                else if (currentlyParsedContainer.equals("publication"))
                    ast.getConfiguration().getPublication().getTitle().add(titleText);

                lastNode = titleText;
            } else error();
        } while (frontEndBridge.containsTokens() && (frontEndBridge.lookahead(0).type == TokenType.KEYWORD &&
            (frontEndBridge.lookahead(0).value.equals("emphasise") ||
            frontEndBridge.lookahead(0).value.equals("work")) ||
            frontEndBridge.lookahead(0).type == TokenType.TEXT));
    }

    /**
     *  Emphasis := "emphasise" Textual NewLine
     */
    private void emphasis() {
        consumeKeyword("emphasise");

        if (current.type == TokenType.TEXT) {
            textual();

            var titleText = new TitleText(new Emphasis(last.value));

            if (currentlyParsedContainer.equals("Document Title"))
                ast.getConfiguration().getTitle().add(titleText);
            else if (currentlyParsedContainer.equals("publication"))
                ast.getConfiguration().getPublication().getTitle().add(titleText);

            lastNode = titleText;
        } else error();
    }

    /**
     *  Work := "work" Textual NewLine
     */
    private void work() {
        consumeKeyword("work");

        if (current.type == TokenType.TEXT) {
            textual();

            var titleText = new TitleText(new Work(last.value));

            if (currentlyParsedContainer.equals("Document Title"))
                ast.getConfiguration().getTitle().add(titleText);
            else if (currentlyParsedContainer.equals("publication"))
                ast.getConfiguration().getPublication().getTitle().add(titleText);

            lastNode = titleText;
        } else error();
    }

    /**
     *  StructuralInstruction := "bibliography" NewLine | "appendix" NewLine | "tableofcontents" NewLine |
     *                           "blank" NewLine
     */
    private void structuralInstruction() {
        if (isKeyword()) {
            switch (current.value) {
                case "bibliography" -> consumeKeyword("bibliography");
                case "appendix" -> consumeKeyword("appendix");
                case "tableofcontents" -> consumeKeyword("tableofcontents");
                case "blank" -> consumeKeyword("blank");
                default -> error();
            }
            newline();
        } else error();
    }

    /**
     *  Textual := Text Newline
     */
    private void textual() {
        consume(new Token(TokenType.TEXT, null));
        newline();
    }

    /**
     *  TextualList := Textual | Textual "," TextualList
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
     *  NewLine := "NewLine" | epsilon
     */
    private void newline() {
        if (frontEndBridge.containsTokens()) consume(new Token(TokenType.NEW_LINE, null));
    }

    /**
     *  Returns true if the current token is a KEYWORD token
     *
     * @return - true if the current type is KEYWORD, false if not
     */
    private boolean isKeyword() { return current.type == TokenType.KEYWORD; }

    /**
     *  Returns true if the current token is a keyword, which is equal to the specified keyword
     *
     * @param keyword - the keyword that should be matched against the current token value
     * @return - true if the keyword equals the specified keyword, false else
     */
    private boolean isKeyword(@NonNull final String keyword) {
        return isKeyword() && current.value.equals(keyword);
    }

    /**
     *  Simple helper method to consume the specified keyword.
     *  Note that this method does NOT throw state exceptions!
     *
     * @param keyword - the keyword that should be consumed
     */
    private void consumeKeyword(@NonNull final String keyword) {
        consume(new Token(TokenType.KEYWORD, keyword));
    }

}
