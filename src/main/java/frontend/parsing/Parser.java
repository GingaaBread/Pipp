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

    public void provideToken(final Token token) {
        current = token;
    }

    private void consume(Token requiredToken) {
        if (current.type == TokenType.TEXT && requiredToken.type == TokenType.TEXT ||
                current.type == requiredToken.type && current.value.equals(requiredToken.value)) {
            current = null;
            frontEndBridge.requestNextToken();
        }
        else throw new IllegalArgumentException("Unexpected token: " + current.toString()
                + ". Expected: " + requiredToken.toString());
    }

    /**
     *  S or empty
     */
    public void s() {
        document();
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
        configContainer();
    }

    /**
     *  "config" Newline Config+
     */
    private void configContainer() {
        consume(new Token(TokenType.KEYWORD, "config"));
    }

    /**
     *  (Instruction, Paragraph)*
     */
    private void instructionParagraphList() {
        consume(new Token(TokenType.TEXT, null));
    }

    /**
     *  (Text, ParagraphInstruction)* NewLine
     */
    private void paragraph() {

    }

    /**
     *  author | citation | bold | italic | strikethrough
     */
    private void paragraphInstruction() {

    }

}
