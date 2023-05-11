package frontend.parsing;

import frontend.FrontEndBridge;
import frontend.lexical_analysis.Token;
import frontend.lexical_analysis.TokenType;

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
     *  StyleConfiguration := "style" Textual
     *
     *  TODO: Add style overrides
     */
    private void styleConfiguration() {
        consume(new Token(TokenType.KEYWORD, "style"));
        textual();
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

    private void newline() {
        if (frontEndBridge.isNotEmpty()) consume(new Token(TokenType.NEW_LINE, null));
    }

}
