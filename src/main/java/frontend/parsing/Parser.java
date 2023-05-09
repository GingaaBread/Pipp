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
        }
        if (current.type == TokenType.TEXT && requiredToken.type == TokenType.TEXT ||
                current.type == requiredToken.type && current.value.equals(requiredToken.value)) {
            current = frontEndBridge.getTokens().poll();
        }
        else throw new IllegalArgumentException("Unexpected token: " + current.toString()
                + ". Expected: " + requiredToken);
    }

    /**
     *  S or empty
     */
    public void s() {
        current = frontEndBridge.getTokens().poll();

        document();
        System.out.println("Successfully matched s");
    }

    /**
     *  OptConfigContainer InstructionParagraphList
     */
    private void document() {
        optConfigContainer();
        instructionParagraphList();
        System.out.println("Successfully matched document");

    }

    /**
     *  ConfigContainer or empty
     */
    private void optConfigContainer() {
        configContainer();
        System.out.println("Successfully matched optConfigContainer");

    }

    /**
     *  "config" Newline Config+
     */
    private void configContainer() {
        consume(new Token(TokenType.KEYWORD, "config"));
        System.out.println("Successfully matched configContainer");

    }

    /**
     *  (Instruction, Paragraph)*
     */
    private void instructionParagraphList() {
        consume(new Token(TokenType.TEXT, null));
        System.out.println("Successfully matched instructionParagraphList");

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
     *  "citation" Textual
     */
    private void citation() {
        consume(new Token(TokenType.KEYWORD, "citation"));
        textual();
    }

    /**
     *  Text Newline
     */
    private void textual() {
        consume(new Token(TokenType.TEXT, null));
        consume(new Token(TokenType.NEW_LINE, null));
    }

}
