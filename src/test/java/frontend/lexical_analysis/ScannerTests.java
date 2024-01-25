package frontend.lexical_analysis;

import frontend.FrontEndBridge;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * This class is responsible for testing the scanner and its tokens.
 * Keywords, indentation, new line characters, commas, and texts should be recognised as tokens.
 * Keywords that are not included in Pipp should result in an exception.
 * The scanner should ignore comments and insignificant white space such as " " and "\r".
 */
class ScannerTests {

    /**
     * Tests if all built in keywords are recognised as tokens with the KEYWORD TokenType and
     * the keywords as their values.
     */
    @Test
    void built_In_Keywords_Are_Tokens() {
        // Get all built in keywords
        var keywords = Scanner.builtinKeywords;

        // Create a new bridge for each keyword.
        // The bridge simulates a text file each time.
        for (String keyword : keywords) {
            var bridge = new FrontEndBridge(keyword);

            // Each keyword should be a single token of type KEYWORD and its value
            // The bridge should have exactly one token
            Assertions.assertTrue(bridge.containsTokens());
            Assertions.assertEquals(new Token(TokenType.KEYWORD, keyword), bridge.dequeueToken());
            Assertions.assertFalse(bridge.containsTokens());
        }
    }

    /**
     * Tests if using any keywords not included in Pipp will throw an IllegalArgumentException.
     */
    @Test
    void other_Keywords_Throw_Exceptions() {
        var exampleKeywords = new String[]{"keyword", "12345", "!", "UPPER"};

        for (var keyword : exampleKeywords) {
            try {
                new FrontEndBridge(keyword);
            } catch (IllegalArgumentException e) {
                return;
            }

            Assertions.fail();
        }
    }

    /**
     * Tests if the amount of tabs used as an indentation is the value of the INDENT token.
     */
    @Test
    void indentation_Has_Correct_Indentation_Level() {
        final var builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append("\t");
            var bridge = new FrontEndBridge(builder.toString());

            Assertions.assertEquals(new Token(TokenType.INDENT, "" + (i + 1)), bridge.dequeueToken());
        }
    }

    /**
     * Tests if the new line character \n is recognised as the new line token.
     * It uses the \n character as its token value.
     */
    @Test
    void new_Line_Character_Is_New_Line_Token() {
        var bridge = new FrontEndBridge("\n");
        Assertions.assertEquals(new Token(TokenType.NEW_LINE, "\n"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the carriage return \r and the space " " are ignored and not turned into tokens.
     */
    @Test
    void insignificant_White_Space_Is_Ignored() {
        var bridge = new FrontEndBridge("\r  \r    \r    \r         ");
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the comma character "," is recognised as the list separator token.
     * It uses the comma as its token value.
     */
    @Test
    void comma_Character_Is_List_Separator_Token() {
        var bridge = new FrontEndBridge(",");
        Assertions.assertEquals(new Token(TokenType.LIST_SEPARATOR, ","), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if characters enclosed by quotation marks are recognised as text tokens.
     * They use the textual content without the quotation marks as their token values.
     */
    @Test
    void strings_Are_Text_Tokens() {
        var bridge = new FrontEndBridge("\"Hello World!\"\"X\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello World!"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.TEXT, "X"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the new line character can be used inside of text tokens, instead of being used for
     * the New Line token type.
     */
    @Test
    void text_Can_Span_Multiple_Lines() {
        var bridge = new FrontEndBridge("\"Hello \n \n\nWorld!\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello World!"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if lines beginning with a hash start a comment, ignoring the current line.
     */
    @Test
    void comments_Are_Ignored() {
        var bridge = new FrontEndBridge("#\"Hello!\"");
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the new line character \n is used to end a comment.
     */
    @Test
    void new_Line_Character_Ends_Comments() {
        var bridge = new FrontEndBridge("#\"Hello!\"\n\t");
        Assertions.assertEquals(new Token(TokenType.INDENT, "1"), bridge.dequeueToken());
    }

    /**
     * Tests if the hash character # is used, but not as the first character on a line,
     * the compiler will NOT interpret it as a comment.
     */
    @Test
    void hash_Is_Not_A_Comment_When_Not_In_First_Position() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FrontEndBridge(" #"));
    }

    /**
     * Tests if the space " " ends the prior keyword token.
     * Keywords are the only tokens that are ended by special characters or tokens.
     * Example: "type " <- the type keyword is ended.
     */
    @Test
    void spaces_Submit_Keyword_Tokens() {
        var bridge = new FrontEndBridge("spacing spacing");
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
    }

    /**
     * Tests if the space " " does not end the prior token if it is declared in a text token.
     * Example: ""type "<- the text should not be ended."
     */
    @Test
    void spaces_Do_Not_Submit_In_Text_Tokens() {
        var bridge = new FrontEndBridge("\"Hello World!\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello World!"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the comma character "," ends the prior token and adds a new list separator token.
     * Example: "type," <- the type keyword is ended and the list separator token is added.
     */
    @Test
    void commas_Submit_Token_And_Submit_List_Separator() {
        var bridge = new FrontEndBridge("spacing, spacing");
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.LIST_SEPARATOR, ","), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the comma character "," does not end the prior token if it is declared in a text token.
     * Example: ""type,"<- the text should not be ended."
     */
    @Test
    void commas_Do_Not_Submit_In_Text_Tokens() {
        var bridge = new FrontEndBridge("\"Hello, World!\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello, World!"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the new line character "\n" ends the prior token and adds a new line token.
     * Example: "type\n" <- the type keyword is ended and the new line token is added.
     */
    @Test
    void new_Line_Characters_Submit_Token_And_Submit_New_Line() {
        var bridge = new FrontEndBridge("spacing\n spacing");
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.NEW_LINE, "\n"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the escaped quotation mark characters does not end the prior token if it is declared in a
     * text token.
     * Example: "This \" should not end the text"
     */
    @Test
    void escaped_Quotation_Does_Not_Submit_In_Text_Tokens() {
        var bridge = new FrontEndBridge("\"Hello \\\" World!\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello \" World!"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     * Tests if the new quotation mark ends the prior token.
     * Example: "type"" <- the type keyword is ended.
     */
    @Test
    void quotation_Marks_Submit_Tokens() {
        var bridge = new FrontEndBridge("spacing\"Hello World!\"");
        Assertions.assertEquals(new Token(TokenType.KEYWORD, "spacing"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello World!"), bridge.dequeueToken());
        Assertions.assertFalse(bridge.containsTokens());
    }

}
