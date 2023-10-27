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
 *  The scanner should ignore comments.
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
     *  Tests if the amount of tabs used as an indentation is the value of the INDENT token
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

    public void new_Line_Character_Resets_Indentation_Level() {

    }

    public void new_Line_Character_Is_New_Line_Token() {

    }

    public void comma_Character_Is_List_Separator_Token() {

    }

    public void strings_Are_Text_Tokens() {

    }

    public void text_Can_Span_Multiple_Lines() {

    }

    public void multi_Line_Texts_Disregard_Special_Characters() {

    }

    public void comments_Are_Ignored() {

    }

    public void new_Line_Character_Ends_Comments() {

    }

}
