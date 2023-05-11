package frontend.parsing;

import frontend.FrontEndBridge;
import frontend.lexical_analysis.Token;
import frontend.lexical_analysis.TokenType;

public class Parser {

    private final FrontEndBridge frontEndBridge;
    private Token current;

    public Parser(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
    }

    private void consume(Token requiredToken) {
        if (current == null) {
            throw new IllegalArgumentException("Unexpected end: Still expecting: " + requiredToken);
        } else if (current.type == TokenType.KEYWORD && requiredToken.type == TokenType.KEYWORD &&
                current.value.equals(requiredToken.value) || current.type != TokenType.KEYWORD &&
                current.type == requiredToken.type) {
            if (frontEndBridge.isNotEmpty()) current = frontEndBridge.dequeue();
        }
        else throw new IllegalArgumentException("Unexpected token: " + current
                + ". Expected: " + requiredToken);
    }

    private void error() {
        throw new IllegalArgumentException("Unexpected token: " + current);
    }

    /**
     *  S or empty
     */
    public void s() {
        if (frontEndBridge.isNotEmpty()) {
            current = frontEndBridge.dequeue();
            document();
            if (frontEndBridge.isNotEmpty()) error();
        }
    }

    /**
     *  OptConfigContainer InstructionParagraphList
     */
    private void document() {
        optConfigContainer();
        instructionParagraphList();
    }

    /**
     *  ConfigContainer or empty
     */
    private void optConfigContainer() {
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
        } while (current.type == TokenType.KEYWORD && (
                current.value.equals("style") || current.value.equals("title") || current.value.equals("author")));
    }

    /**
     *  Configuration := StyleConfiguration | TitleConfiguration | AuthorConfiguration | AssessorConfiguration |
     *                   PublicationConfiguration | TypeConfiguration | DocumentConfiguration
     */
    private void configuration() {
        if (current.type == TokenType.KEYWORD) {
            switch (current.value) {
                case "style" -> styleConfiguration();
                case "title" -> titleConfiguration();
                case "author" -> authorConfiguration();
                default -> error();
            }
        } else error();
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
     *  AuthorSpecification := Textual | NameSpecificationWithOptID | AuthorList
     */
    private void authorSpecification() {
        if (current.type == TokenType.TEXT) textual();
        else if (current.type == TokenType.KEYWORD) {
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
        } while (current.type == TokenType.KEYWORD && current.value.equals("of"));
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
     *  TitleConfiguration := "title" Textual
     */
    private void titleConfiguration() {
        consume(new Token(TokenType.KEYWORD, "title"));
        textual();
    }

    /**
     *  StyleConfiguration := "style" Textual
     */
    private void styleConfiguration() {
        consume(new Token(TokenType.KEYWORD, "style"));
        textual();
    }

    /**
     *  (Instruction, Paragraph)*
     */
    private void instructionParagraphList() {
    }

    /**
     *  Textual+ (ParagraphInstruction, Textual)* NewLine
     *  Paragraph := TextualList
     */
    private void paragraph() {

    }

    /**
     *  Textual TextualList | Textual
     */
    private void textualList() {
    }

    /**
     *  author | citation | bold | italic | strikethrough
     */
    private void paragraphInstruction() {

    }

    /**
     *  (Text, Citation)* Textual
     *  CitedTextual := Text CitedTextual | Citation CitedTextual | Textual
     */
    private void citedTextual() {
        if (current.type == TokenType.TEXT) {
            consume(new Token(TokenType.TEXT, null));
            citedTextual();
        } else if (current.type == TokenType.KEYWORD && current.value.equals("citation")) {
            citation();
            citedTextual();
        } else textual();
    }

    /**
     *  Citation := "citation" Textual | "citation" ArgumentList
     */
    private void citation() {
        consume(new Token(TokenType.KEYWORD, "citation"));
        textual();
    }

    /**
     *  ArgumentList := Textual | AnyLineTextual "," ArgumentList
     */
    private void argumentList() {

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
