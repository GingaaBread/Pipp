package lexical_analysis;

import lexical_analysis.keywords.*;

import java.util.ArrayList;

/**
 *  Responsible for the lexical analysis of .pipp files
 *  Uses DFAs to convert the input into a set of tokens
 */
public class Scanner {

    /**
     *  Contains all DFAs representing the tokens.
     *  Tokens are keywords, Text (defined as any characters between double quotes), and separators (defined
     *  as either (,) , (\t), or (\n).
     */
    private final DFA[] dfas = new DFA[] {
            // keywords
            new Appendix(),
            new Article(),
            new Assessor(),
            new Author(),
            new Bibliography(),
            new Blank(),
            new Chair(),
            new Chapter(),
            new Citation(),
            new Config(),
            new Date(),
            new Name(),
            new Of(),
            new Publication(),
            new Style(),
            new TableOfContents(),
            new Title(),
            new TitlePage(),
            new Www(),
            // other
            new Text(),
            new Separator(),
    };

    /**
     * Creates the scanner and starts scanning the specified content
     *
     * @param content - the content that should be scanned
     */
    public Scanner(final String content) {
        scan(content);
    }

    /**
     * Uses the algorithm of the lecture
     *
     * @param input - the input of the file that should be analysed
     */
    private void scan(final String input) {
        // Keeps track of the current character
        char current;

        // Determines if the scanner is currently in a comment and should ignore the characters
        boolean inComment = false;

        // Variables for debugging purposes
        int lineNumber = 0, tokenInLineNumber = 0;

        for (int i = 0; i < input.length(); i++) {
            current = input.charAt(i);

            // Increases the debugging variables
            if (current == '\n') {
                lineNumber++;
                tokenInLineNumber = 0;
            } else tokenInLineNumber++;

            // Starts ignoring the line if the character is a comment (#)
            if (current == '#') inComment = true;

            // Marks the end of a comment
            else if (inComment && current == '\n') inComment = false;

            // Ignores spaces (but NOT new line or tabulator characters!)
            else if (!inComment && current != ' ') {
                var s = new ArrayList<String>();

                for (var dfa : dfas) s.add(longestPrefix(input, dfa));

                int k = 0;
                for (String prefix : s)
                    if (prefix.length() > k) k = prefix.length();

                if (k > 0) {
                    System.out.println("Token = " + input.substring(i, i + k - 1));
                    i += k;
                } else throw new IllegalStateException(lineNumber + ":" + tokenInLineNumber
                        + " Cannot scan: " + current);
            }
        }
    }

    /**
     * Finds the longest prefix using the second algorithm of the lecture
     *
     * @param input - the input of the file that should be analysed
     * @param dfa - the current DFA trying to match
     * @return - the longest accepted prefix as a String
     */
    private String longestPrefix(String input, DFA dfa) {
        var longestAcceptedPrefix = new StringBuilder();
        var scannedPrefix = new StringBuilder();

        for (char current : input.toCharArray()) {
            boolean transitionExists = dfa.transition(current);

            if (!transitionExists) break;

            scannedPrefix.append(current);

            if (dfa.inAcceptState()) {
                longestAcceptedPrefix = scannedPrefix;
            }
        }

        dfa.reset();

        return longestAcceptedPrefix.toString();
    }
}
