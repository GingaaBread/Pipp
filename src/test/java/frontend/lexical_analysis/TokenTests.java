package frontend.lexical_analysis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests if the utility methods of the Token entity work as expected.
 * The equals method must be reflexive, symmetric, and transitive.
 */
class TokenTests {

    /**
     * Tests if the equals method of tokens is reflexive for all token types
     */
    @Test
    void equals_Is_Reflexive() {
        var indent = new Token(TokenType.INDENT, "1");
        Assertions.assertEquals(indent, indent);
        Assertions.assertEquals(indent.hashCode(), indent.hashCode());

        var keyword = new Token(TokenType.KEYWORD, "spacing");
        Assertions.assertEquals(keyword, keyword);
        Assertions.assertEquals(keyword.hashCode(), keyword.hashCode());

        var text = new Token(TokenType.TEXT, "\"Hello\"");
        Assertions.assertEquals(text, text);
        Assertions.assertEquals(text.hashCode(), text.hashCode());

        var newLine = new Token(TokenType.NEW_LINE, "\n");
        Assertions.assertEquals(newLine, newLine);
        Assertions.assertEquals(newLine.hashCode(), newLine.hashCode());

        var listSeparator = new Token(TokenType.LIST_SEPARATOR, ",");
        Assertions.assertEquals(listSeparator, listSeparator);
        Assertions.assertEquals(listSeparator.hashCode(), listSeparator.hashCode());
    }

    /**
     * Tests if the equals method of tokens is symmetric for all token types
     */
    @Test
    void equals_Is_Symmetric() {
        var indentA = new Token(TokenType.INDENT, "1");
        var indentB = new Token(TokenType.INDENT, "1");
        Assertions.assertEquals(indentA, indentB);
        Assertions.assertEquals(indentB, indentA);
        Assertions.assertEquals(indentA.hashCode(), indentB.hashCode());
        Assertions.assertEquals(indentB.hashCode(), indentA.hashCode());

        var textA = new Token(TokenType.TEXT, "\"Hello\"");
        var textB = new Token(TokenType.TEXT, "\"Hello\"");
        Assertions.assertEquals(textA, textB);
        Assertions.assertEquals(textB, textA);
        Assertions.assertEquals(textA.hashCode(), textB.hashCode());
        Assertions.assertEquals(textB.hashCode(), textA.hashCode());

        var newLineA = new Token(TokenType.NEW_LINE, "\n");
        var newLineB = new Token(TokenType.NEW_LINE, "\n");
        Assertions.assertEquals(newLineA, newLineB);
        Assertions.assertEquals(newLineB, newLineA);
        Assertions.assertEquals(newLineA.hashCode(), newLineB.hashCode());
        Assertions.assertEquals(newLineB.hashCode(), newLineA.hashCode());

        var listSeparatorA = new Token(TokenType.LIST_SEPARATOR, ",");
        var listSeparatorB = new Token(TokenType.LIST_SEPARATOR, ",");
        Assertions.assertEquals(listSeparatorA, listSeparatorB);
        Assertions.assertEquals(listSeparatorB, listSeparatorA);
        Assertions.assertEquals(listSeparatorA.hashCode(), listSeparatorB.hashCode());
        Assertions.assertEquals(listSeparatorB.hashCode(), listSeparatorA.hashCode());

        var keywordA = new Token(TokenType.KEYWORD, "spacing");
        var keywordB = new Token(TokenType.KEYWORD, "spacing");
        Assertions.assertEquals(keywordA, keywordB);
        Assertions.assertEquals(keywordB, keywordA);
        Assertions.assertEquals(keywordA.hashCode(), keywordB.hashCode());
        Assertions.assertEquals(keywordB.hashCode(), keywordA.hashCode());
    }


    /**
     * Tests if the equals method of tokens is transitive for all token types
     */
    @Test
    void equals_Is_Transitive() {
        var indentA = new Token(TokenType.INDENT, "1");
        var indentB = new Token(TokenType.INDENT, "1");
        var indentC = new Token(TokenType.INDENT, "1");

        if (indentA.equals(indentB) && indentB.equals(indentC)) {
            Assertions.assertEquals(indentA, indentC);
            Assertions.assertEquals(indentA.hashCode(), indentC.hashCode());
        }

        var textA = new Token(TokenType.TEXT, "\"Hello\"");
        var textB = new Token(TokenType.TEXT, "\"Hello\"");
        var textC = new Token(TokenType.TEXT, "\"Hello\"");

        if (textA.equals(textB) && textB.equals(textC)) {
            Assertions.assertEquals(textA, textC);
            Assertions.assertEquals(textA.hashCode(), textC.hashCode());
        }

        var newLineA = new Token(TokenType.NEW_LINE, "\n");
        var newLineB = new Token(TokenType.NEW_LINE, "\n");
        var newLineC = new Token(TokenType.NEW_LINE, "\n");

        if (newLineA.equals(newLineB) && newLineB.equals(newLineC)) {
            Assertions.assertEquals(newLineA, newLineC);
            Assertions.assertEquals(newLineA.hashCode(), newLineC.hashCode());
        }

        var listSeparatorA = new Token(TokenType.LIST_SEPARATOR, ",");
        var listSeparatorB = new Token(TokenType.LIST_SEPARATOR, ",");
        var listSeparatorC = new Token(TokenType.LIST_SEPARATOR, ",");

        if (listSeparatorA.equals(listSeparatorB) && listSeparatorB.equals(listSeparatorC)) {
            Assertions.assertEquals(listSeparatorA, listSeparatorC);
            Assertions.assertEquals(listSeparatorA.hashCode(), listSeparatorC.hashCode());
        }

        var keywordA = new Token(TokenType.KEYWORD, "spacing");
        var keywordB = new Token(TokenType.KEYWORD, "spacing");
        var keywordC = new Token(TokenType.KEYWORD, "spacing");

        if (keywordA.equals(keywordB) && keywordB.equals(keywordC)) {
            Assertions.assertEquals(keywordA, keywordC);
            Assertions.assertEquals(keywordA.hashCode(), keywordC.hashCode());
        }
    }

}
