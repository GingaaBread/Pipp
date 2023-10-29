import frontend.FrontEndBridge;
import frontend.lexical_analysis.Scanner;
import frontend.lexical_analysis.Token;
import frontend.lexical_analysis.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *  This class is responsible for testing the scanner and its tokens.
 *  Keywords, indentation, new line characters, commas, and texts should be recognised as tokens.
 *  Keywords that are not included in Pipp should result in an exception.
 *  The scanner should ignore comments and insignificant white space such as " " and "\r".
 */
public class ScannerTests {

    /**
     *  Tests if all built in keywords are recognised as tokens with the KEYWORD TokenType and
     *  the keywords as their values.
     */
    @Test
    public void built_In_Keywords_Are_Tokens() {
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
     *  Tests if using any keywords not included in Pipp will throw an IllegalArgumentException.
     */
    @Test
    public void other_Keywords_Throw_Exceptions() {
        var exampleKeywords = new String[] { "keyword", "12345", "!", "UPPER" };

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
     *  Tests if the amount of tabs used as an indentation is the value of the INDENT token.
     */
    @Test
    public void indentation_Has_Correct_Indentation_Level() {
        final var builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append("\t");
            var bridge = new FrontEndBridge(builder.toString());

            Assertions.assertEquals(new Token(TokenType.INDENT, "" + (i + 1)), bridge.dequeueToken());
        }
    }

    /**
     *  Tests if the new line character \n is recognised as the new line token.
     *  It uses the \n character as its token value.
     */
    @Test
    public void new_Line_Character_Is_New_Line_Token() {
        var bridge = new FrontEndBridge("\n");
        Assertions.assertEquals(new Token(TokenType.NEW_LINE, "\n"), bridge.dequeueToken());
    }

    /**
     *  Tests if the carriage return \r and the space " " are ignored and not turned into tokens.
     */
    @Test
    public void insignificant_White_Space_Is_Ignored() {
        var bridge = new FrontEndBridge("\r  \r    \r    \r         ");
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     *  Tests if the comma character "," is recognised as the list separator token.
     *  It uses the comma as its token value.
     */
    @Test
    public void comma_Character_Is_List_Separator_Token() {
        var bridge = new FrontEndBridge(",");
        Assertions.assertEquals(new Token(TokenType.LIST_SEPARATOR, ","), bridge.dequeueToken());
    }

    /**
     *  Tests if characters enclosed by quotation marks are recognised as text tokens.
     *  They use the textual content without the quotation marks as their token values.
     */
    @Test
    public void strings_Are_Text_Tokens() {
        var bridge = new FrontEndBridge("\"Hello World!\"\"\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello World!"), bridge.dequeueToken());
        Assertions.assertEquals(new Token(TokenType.TEXT, ""), bridge.dequeueToken());
    }

    /**
     *  Tests if the new line character can be used inside of text tokens, instead of being used for
     *  the New Line token type.
     */
    @Test
    public void text_Can_Span_Multiple_Lines() {
        var bridge = new FrontEndBridge("\"Hello\nWorld\n!\"");
        Assertions.assertEquals(new Token(TokenType.TEXT, "Hello World!"), bridge.dequeueToken());
    }

    /**
     *  Tests if lines beginning with a hash start a comment, ignoring the current line.
     */
    @Test
    public void comments_Are_Ignored() {
        var bridge = new FrontEndBridge("#\"Hello!\"");
        Assertions.assertFalse(bridge.containsTokens());
    }

    /**
     *  Tests if the new line character \n is used to end a comment.
     */
    @Test
    public void new_Line_Character_Ends_Comments() {
        var bridge = new FrontEndBridge("#\"Hello!\"\n\t");
        Assertions.assertEquals(new Token(TokenType.INDENT, "1"), bridge.dequeueToken());
    }

    /**
     *  Tests if the hash character # is used, but not as the first character on a line,
     *  the compiler will NOT interpret it as a comment.
     */
    @Test
    public void hash_Is_Not_A_Comment_When_Not_In_First_Position() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FrontEndBridge(" #"));
    }

    /**
     *  Tests if the space " " ends the prior token if it was not a text token.
     *  Example: "type " <- the type keyword is ended.
     */
    public void spaces_Submit_Non_Text_Tokens() {

    }

    /**
     *  Tests if the comma character "," ends the prior token and adds a new list separator token.
     *  Example: "type," <- the type keyword is ended and the list separator token is added.
     */
    public void commas_Submit_Token_And_Submit_List_Separator() {

    }

    /**
     *  Tests if the new line character "\n" ends the prior token and adds a new line token.
     *  Example: "type\n" <- the type keyword is ended and the new line token is added.
     */
    public void new_Line_Characters_Submit_Token_And_Submit_New_Line() {

    }

    /**
     *  Tests if the new quotation mark ends the prior token.
     *  Example: "type"" <- the type keyword is ended.
     */
    public void quotation_Marks_Submit_Tokens() {

    }

    /**
     *  Tests if keywords are the default token types used for input whenever no other token
     *  can be matched.
     */
    public void keywords_Are_Default_Tokens() {

    }

}
