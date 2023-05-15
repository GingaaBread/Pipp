package frontend.parsing;

import frontend.FrontEndBridge;
import frontend.lexical_analysis.Token;
import frontend.lexical_analysis.TokenType;
import lombok.extern.slf4j.XSlf4j;

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
     *  Tracks the current token, which needs to be parsed.
     */
    private Token current;

    /**
     *  Creates the parser by supplying the bridge.
     * @param frontEndBridge - the bridge between the Scanner and Parser
     */
    public Parser(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
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
        if (current == null) {
            throw new IllegalStateException("Unexpected end: Still expecting: " + requiredToken);
        } else if (current.type == TokenType.KEYWORD && requiredToken.type == TokenType.KEYWORD &&
                current.value.equals(requiredToken.value) || current.type != TokenType.KEYWORD &&
                current.type == requiredToken.type) {
            if (frontEndBridge.isNotEmpty()) current = frontEndBridge.dequeue();
        }
        else throw new IllegalArgumentException("Unexpected token: " + current + ". Expected: " + requiredToken);
    }

    /**
     *  This method is called whenever a token is specified in a production without having to
     *  consume a token directly. In that case, an IllegalArgumentException is thrown.
     */
    private void error() {
        throw new IllegalArgumentException("Unexpected token: " + current);
    }

    /**
     *  S := Document | epsilon
     */
    public void s() {
        if (frontEndBridge.isNotEmpty()) {
            current = frontEndBridge.dequeue();
            document();
            if (frontEndBridge.isNotEmpty()) error();
        }
    }

    /**
     *  Document := ConfigContainer InstructionParagraphList | InstructionParagraphList
     */
    private void document() {
        if (current.type == TokenType.KEYWORD && current.value.equals("config")) configContainer();
    }

    /**
     *  ConfigContainer := "config" Newline ConfigList
     */
    private void configContainer() {
        consume(new Token(TokenType.KEYWORD, "config"));
        consume(new Token(TokenType.NEW_LINE, null));
        configList();
    }

    /**
     *  ConfigList := Configuration | Configuration ConfigList
     */
    private void configList() {
        do {
            configuration();
        } while (frontEndBridge.isNotEmpty() && current.type == TokenType.KEYWORD && (
                current.value.equals("style") || current.value.equals("title") || current.value.equals("author") ||
                current.value.equals("assessor") || current.value.equals("type") ||
                        current.value.equals("publication")));
    }

    /**
     *  Configuration := StyleConfiguration | TitleConfiguration | AuthorConfiguration | AssessorConfiguration |
     *                   PublicationConfiguration | TypeConfiguration
     */
    private void configuration() {
        if (current.type == TokenType.KEYWORD) {
            switch (current.value) {
                case "style" -> styleConfiguration();
                case "title" -> titleConfiguration();
                case "author" -> authorConfiguration();
                case "assessor" -> assessorConfiguration();
                case "publication" -> publicationConfiguration();
                case "type" -> typeConfiguration();
                default -> error();
            }
        } else error();
    }

    /**
     *  PublicationConfiguration := "publication" NewLine TitleConfiguration DateConfiguration |
     *                              "publication" NewLine DateConfiguration TitleConfiguration |
     *                              "publication" NewLine TitleConfiguration |
     *                              "publication" NewLine DateConfiguration
     */
    private void publicationConfiguration() {
        consume(new Token(TokenType.KEYWORD, "publication"));
        consume(new Token(TokenType.NEW_LINE, null));

        if (current.type == TokenType.KEYWORD) {
            if (current.value.equals("title")) {
                titleConfiguration();

                if (current.value.equals("date")) dateConfiguration();
            } else if (current.value.equals("date")) {
                dateConfiguration();

                if (current.value.equals("title")) titleConfiguration();
            }
        } else error();
    }

    /**
     *  DateConfiguration := "date" Textual
     */
    private void dateConfiguration() {
        consume(new Token(TokenType.KEYWORD, "date"));
        textual();
    }

    /**
     *  TypeConfiguration := "type" Textual
     */
    private void typeConfiguration() {
        consume(new Token(TokenType.KEYWORD, "type"));
        textual();
    }

    /**
     *  AssessorConfiguration := "assessor" NewLine AssessorSpecification | "assessor" Textual
     */
    private void assessorConfiguration() {
        consume(new Token(TokenType.KEYWORD, "assessor"));

        if (current.type == TokenType.NEW_LINE) {
            consume(new Token(TokenType.NEW_LINE, null));
            assessorSpecification();
        } else if (current.type == TokenType.TEXT) {
            textual();
        } else error();
    }

    /**
     *  AssessorSpecification := NameSpecificationWithOptRole | AssessorList
     */
    private void assessorSpecification() {
        if (current.type == TokenType.KEYWORD) {
            switch (current.value) {
                case "name", "firstname" -> nameSpecificationWithOptRole();
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
            assessorItem();
        } while (frontEndBridge.isNotEmpty() && current.type == TokenType.KEYWORD && current.value.equals("of"));
    }

    /**
     *  AssessorItem := "of" NewLine NameSpecificationWithOptRole | "of" Textual
     */
    private void assessorItem() {
        consume(new Token(TokenType.KEYWORD, "of"));

        if (current.type == TokenType.NEW_LINE) {
            consume(new Token(TokenType.NEW_LINE, null));
            nameSpecificationWithOptRole();
        } else if (current.type == TokenType.TEXT) {
            textual();
        }
    }

    /**
     * NameSpecificationWithOptRole := NameSpecification | NameSpecification "role" Textual
     */
    private void nameSpecificationWithOptRole() {
        nameSpecification();
        if (current.type == TokenType.KEYWORD && current.value.equals("role")) {
            consume(new Token(TokenType.KEYWORD, "role"));
            textual();
        }
    }

    /**
     *  AuthorConfiguration := "author" NewLine AuthorSpecification | "author" Textual
     */
    private void authorConfiguration() {
        consume(new Token(TokenType.KEYWORD, "author"));

        if (current.type == TokenType.NEW_LINE) {
            consume(new Token(TokenType.NEW_LINE, null));
            authorSpecification();
        } else if (current.type == TokenType.TEXT) {
            textual();
        } else error();
    }

    /**
     *  AuthorSpecification := NameSpecificationWithOptID | AuthorList
     */
    private void authorSpecification() {
        if (current.type == TokenType.KEYWORD) {
            switch (current.value) {
                case "name", "firstname" -> nameSpecificationWithOptId();
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
            authorItem();
        } while (frontEndBridge.isNotEmpty() && current.type == TokenType.KEYWORD && current.value.equals("of"));
    }

    /**
     *  AuthorItem := "of" NewLine NameSpecificationWithOptID | "of" Textual
     */
    private void authorItem() {
        consume(new Token(TokenType.KEYWORD, "of"));

        if (current.type == TokenType.NEW_LINE) {
            consume(new Token(TokenType.NEW_LINE, null));
            nameSpecificationWithOptId();
        } else if (current.type == TokenType.TEXT) {
            textual();
        }
    }

    /**
     *  NameSpecificationWithOptID := NameSpecification | NameSpecification "id" Textual
     */
    private void nameSpecificationWithOptId() {
        nameSpecification();
        if (current.type == TokenType.KEYWORD && current.value.equals("id")) {
            consume(new Token(TokenType.KEYWORD, "id"));
            textual();
        }
    }

    /**
     *  NameSpecification := Name | FirstName LastName
     */
    private void nameSpecification() {
        if (current.type == TokenType.KEYWORD) {
            if (current.value.equals("name")) name();
            else if (current.value.equals("firstname")) {
                firstname();
                lastname();
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
     *  TitleConfiguration := "title" CitedTextual | "title" NewLine CitedTextual
     */
    private void titleConfiguration() {
        consume(new Token(TokenType.KEYWORD, "title"));

        if (current.type == TokenType.NEW_LINE) consume(new Token(TokenType.NEW_LINE, null));

        citedTextual();
    }

    /**
     *  StyleConfiguration := "style" NewLine Textual | "style" NewLine CustomStyle
     */
    private void styleConfiguration() {
        consume(new Token(TokenType.KEYWORD, "style"));
        newline();

        if (current.type == TokenType.KEYWORD && current.value.equals("of")) customStyle();
        else textual();
    }

    /**
     *  CustomStyle := "of" Textual OptionalCustomStyleList
     */
    private void customStyle() {
        consume(new Token(TokenType.KEYWORD, "of"));
        textual();
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
     *  StructureStyle := "structure" NewLine ParagraphStructureStyle |
     *                    "sentence" NewLine SentenceStructureStyle |
     *                    "endnotes" NewLine EndnotesStructureStyle
     */
    private void structureStyle() {
        if (current.type == TokenType.KEYWORD) {
            consume(new Token(TokenType.KEYWORD, "structure"));
            newline();
            do {
                switch (current.value) {
                    case "paragraph" -> paragraphStructureStyle();
                    case "sentence" -> sentenceStructureStyle();
                    case "endnotes" -> endnotesStructureStyle();
                    default -> error();
                }
            } while (current.value.equals("paragraph") || current.value.equals("sentence") ||
                    current.value.equals("endnotes"));
        } else error();
    }

    /**
     *  EndnotesStructureStyle := "endnotes" NewLine "allow" NewLine "before" Textual
     */
    private void endnotesStructureStyle() {
        consume(new Token(TokenType.KEYWORD, "endnotes"));
        newline();
        consume(new Token(TokenType.KEYWORD, "allow"));
        newline();
        consume(new Token(TokenType.KEYWORD, "before"));
        structuralInstruction();
    }

    /**
     *  SentenceStructureStyle := "sentence" NewLine SentenceStructureStyleList
     */
    private void sentenceStructureStyle() {
        consume(new Token(TokenType.KEYWORD, "sentence"));
        newline();
        sentenceStructureStyleList();
    }

    /**
     * SentenceStructureStyleList := "before" Textual SentenceStructureStyleList |
     *                               "allow" NewLine SentenceStructureStyleAllowConfiguration SentenceStructureStyleList |
     *                               "before" Textual |
     *                               "allow" NewLine SentenceStructureStyleAllowConfiguration
     */
    private void sentenceStructureStyleList() {
        if (current.type == TokenType.KEYWORD) {
            do {
                switch (current.value) {
                    case "before" -> {
                        consume(new Token(TokenType.KEYWORD, "before"));
                        textual();
                    }
                    case "allow" -> {
                        consume(new Token(TokenType.KEYWORD, "allow"));
                        newline();
                        sentenceStructureStyleAllowConfiguration();
                    }
                    default -> error();
                }
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
        if (current.type == TokenType.KEYWORD) {
            do {
                switch (current.value) {
                    case "whitespace" -> consume(new Token(TokenType.KEYWORD, "whitespace"));
                    case "bold" -> consume(new Token(TokenType.KEYWORD, "bold"));
                    case "italic" -> consume(new Token(TokenType.KEYWORD, "italic"));
                    default -> error();
                }
                textual();
            } while (current.value.equals("whitespace") || current.value.equals("bold") ||
                    current.value.equals("italic"));
        } else error();
    }

    /**
     *  ParagraphStructureStyle := "paragraph" NewLine "indentation" Textual
     */
    private void paragraphStructureStyle() {
        consume(new Token(TokenType.KEYWORD, "paragraph"));
        newline();
        consume(new Token(TokenType.KEYWORD, "indentation"));
        textual();
    }

    /**
     *  FontStyle := "font" NewLine FontStyleList
     */
    private void fontStyle() {
        consume(new Token(TokenType.KEYWORD, "font"));
        newline();
        fontStyleList();
    }

    /**
     *  FontStyleList := "name" Textual FontStyleList |
     *                   "size" Textual FontStyleList |
     *                   "colour" Textual FontStyleList |
     *                   "name" Textual | "size" Textual | "colour" Textual
     */
    private void fontStyleList() {
        if (current.type == TokenType.KEYWORD) {
            do {
                switch (current.value) {
                    case "name" -> consume(new Token(TokenType.KEYWORD, "name"));
                    case "size" -> consume(new Token(TokenType.KEYWORD, "size"));
                    case "colour" -> consume(new Token(TokenType.KEYWORD, "colour"));
                    default -> error();
                }
                textual();
            } while (current.value.equals("name") || current.value.equals("size") ||
                    current.value.equals("colour"));
        } else error();
    }

    /**
     *  PageNumerationStyle := "numeration" NewLine PageNumerationStyleList
     */
    private void pageNumerationStyle() {
        consume(new Token(TokenType.KEYWORD, "numeration"));
        newline();
        pageNumerationStyleList();
    }

    /**
     *  PageNumerationStyleList := "in" Textual PageNumerationStyleList |
     *                             "display" Textual PageNumerationStyleList |
     *                             "margin" Textual PageNumerationStyleList |
     *                             "skip" TextualList PageNumerationStyleList |
     *                             "in" Textual | "display" Textual | "margin" Textual | "skip" TextualList
     */
    private void pageNumerationStyleList() {
        if (current.type == TokenType.KEYWORD) {
            do {
                switch (current.value) {
                    case "in" -> {
                        consume(new Token(TokenType.KEYWORD, "in"));
                        textual();
                    }
                    case "display" -> {
                        consume(new Token(TokenType.KEYWORD, "display"));
                        textual();
                    }
                    case "margin" -> {
                        consume(new Token(TokenType.KEYWORD, "margin"));
                        textual();
                    }
                    case "skip" -> {
                        consume(new Token(TokenType.KEYWORD, "skip"));
                        textualList();
                    }
                    default -> error();
                }
            } while (current.value.equals("in") || current.value.equals("display") ||
                    current.value.equals("margin") || current.value.equals("skip"));
        } else error();
    }

    /**
     *  GeneralLayoutStyle := "layout" NewLine LayoutStyleList
     */
    private void generalLayoutStyle() {
        consume(new Token(TokenType.KEYWORD, "layout"));
        newline();
        layoutStyleList();
    }

    /**
     *  LayoutStyleList := "width" Textual | "height" Textual | "margin" Textual | "spacing" Textual |
     *                     "width" Textual LayoutStyleList |
     *                     "height" Textual LayoutStyleList |
     *                     "margin" Textual LayoutStyleList |
     *                     "spacing" Textual LayoutStyleList
     */
    private void layoutStyleList() {
        if (current.type == TokenType.KEYWORD) {
            do {
                switch (current.value) {
                    case "width" -> consume(new Token(TokenType.KEYWORD, "width"));
                    case "height" -> consume(new Token(TokenType.KEYWORD, "height"));
                    case "margin" -> consume(new Token(TokenType.KEYWORD, "margin"));
                    case "spacing" -> consume(new Token(TokenType.KEYWORD, "spacing"));
                    default -> error();
                }
                textual();
            } while (current.value.equals("width") || current.value.equals("height") ||
                    current.value.equals("margin") || current.value.equals("spacing"));
        } else error();
    }

    /**
     *  CitedTextual := Text CitedTextual | Citation CitedTextual | Textual | Citation
     */
    private void citedTextual() {
        if (current.type == TokenType.KEYWORD && current.value.equals("citation") ||
                current.type == TokenType.TEXT) {
            do {
                if (current.type == TokenType.KEYWORD) citation();
                else textual();
            } while (frontEndBridge.isNotEmpty() && (current.type == TokenType.KEYWORD && current.value.equals("citation") ||
                    current.type == TokenType.TEXT));
        } else error();
    }

    /**
     *  StructuralInstruction := "bibliography" NewLine | "appendix" NewLine | "tableofcontents" NewLine
     */
    private void structuralInstruction() {
        if (current.type == TokenType.KEYWORD) {
            switch (current.value) {
                case "bibliography" -> consume(new Token(TokenType.KEYWORD, "bibliography"));
                case "appendix" -> consume(new Token(TokenType.KEYWORD, "appendix"));
                case "tableofcontents" -> consume(new Token(TokenType.KEYWORD, "tableofcontents"));
                default -> error();
            }
            newline();
        } else error();
    }

    /**
     *  Citation := "citation" NewLine "of" Textual "in" Textual "page" Textual
     */
    private void citation() {
        consume(new Token(TokenType.KEYWORD, "citation"));
        consume(new Token(TokenType.NEW_LINE, null));

        consume(new Token(TokenType.KEYWORD, "of"));
        textual();

        consume(new Token(TokenType.KEYWORD, "in"));
        textual();

        consume(new Token(TokenType.KEYWORD, "page"));
        textual();
    }

    /**
     *  AnyLineTextual := Textual | Text
     */
    private void anyLineTextual() {
        if (frontEndBridge.lookahead(1).type == TokenType.NEW_LINE) {
            textual();
        } else consume(new Token(TokenType.TEXT, null));
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
        do {
            textual();
            consume(new Token(TokenType.LIST_SEPARATOR, null));
        } while (current.type == TokenType.TEXT);
    }

    /**
     *  NewLine := "NewLine" | epsilon
     */
    private void newline() {
        if (frontEndBridge.isNotEmpty()) consume(new Token(TokenType.NEW_LINE, null));
    }

}
