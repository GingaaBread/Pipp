package frontend.lexical_analysis;

import frontend.FrontEndBridge;

/**
 * Responsible for the lexical analysis of .pipp files
 * Uses DFAs to convert the input into a set of tokens
 */
public class Scanner {

    /**
     *  Defines the keywords that come with Pipp
     *  They are usable without having to be imported
     */
    private final String[] builtinKeywords = new String[] {
            "appendix",
            "article",
            "assessor",
            "author",
            "bibliography",
            "blank",
            "bold",
            "chair",
            "chapter",
            "citation",
            "config",
            "date",
            "firstname",
            "italic",
            "lastname",
            "name",
            "of",
            "publication",
            "strikethrough",
            "style",
            "tableofcontents",
            "title",
            "titlepage",
            "www"
    };

    private final FrontEndBridge frontEndBridge;

    public Scanner(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
    }

    private int tabulationLevel = 0;

    // Variables for debugging purposes
    private int lineNumber = 0, tokenInLineNumber = 0;

    // Token analysis
    private StringBuilder currentlyRead = new StringBuilder();
    private TokenType currentTokenType;

    // Keeps track of the current character
    // Determines if the scanner is currently in a comment and should ignore the characters
    private boolean inComment = false;

    /**
     * Uses the algorithm of the lecture
     *
     * @param current - the input of the file that should be analysed
     */
    public void scan(final char current) {
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
            else if (!inComment && current != '\r') {
                if (current == '\t') {
                    submitToken();
                    tabulationLevel++;
                } else if (current == ' ') {
                    submitToken();
                }
                else if (current == ',') {
                    submitToken();
                    currentlyRead.append(current);
                    currentTokenType = TokenType.LIST_SEPARATOR;
                    submitToken();
                } else if (current == '\n') {
                    submitToken();
                    currentlyRead.append(current);
                    currentTokenType = TokenType.NEW_LINE;
                    tabulationLevel = 0;
                    submitToken();
                } else if (current == '"') {
                    if (currentlyRead.isEmpty()) {
                        submitToken();
                        currentlyRead.append(current);
                        currentTokenType = TokenType.TEXT;
                    } else if (currentlyRead.length() > 1 && currentlyRead.charAt(0) == '"') {
                        currentlyRead.append(current);
                        submitToken();
                    } else {
                        currentlyRead.append(current);
                    }
                } else if (currentTokenType == TokenType.TEXT) {
                    currentlyRead.append(current);
                } else {
                    currentTokenType = TokenType.KEYWORD;
                    currentlyRead.append(current);
                }
        }
    }

    public void submitToken() {
        if (currentTokenType != null)
        {
            var token = new Token(currentTokenType, currentlyRead.toString());
            frontEndBridge.enqueue(token);

            currentTokenType = null;
            currentlyRead = new StringBuilder();
        }
    }

}
