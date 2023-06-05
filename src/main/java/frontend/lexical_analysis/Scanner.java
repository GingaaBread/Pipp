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
            "allow",
            "assessor",
            "author",
            "before",
            "bibliography",
            "blank",
            "bold",
            "chair",
            "chapter",
            "citation",
            "config",
            "colour",
            "date",
            "display",
            "endnotes",
            "firstname",
            "font",
            "height",
            "italic",
            "id",
            "in",
            "indentation",
            "lastname",
            "layout",
            "name",
            "numeration",
            "margin",
            "of",
            "paragraph",
            "publication",
            "role",
            "sentence",
            "size",
            "strikethrough",
            "structure",
            "style",
            "spacing",
            "skip",
            "tableofcontents",
            "title",
            "titlepage",
            "type",
            "width",
            "www",
            "whitespace"
    };

    private final FrontEndBridge frontEndBridge;

    public Scanner(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
    }

    private int indendationLevel = 0;

    // Token analysis
    private StringBuilder currentlyRead = new StringBuilder();
    private TokenType currentTokenType;

    // Keeps track of the current character
    // Determines if the scanner is currently in a comment and should ignore the characters
    private boolean inComment = false;

    public void scan(final char current) {
            // Starts ignoring the line if the character is a comment (#)
            if (currentTokenType != TokenType.TEXT && current == '#') inComment = true;

            // Marks the end of a comment
            else if (inComment && current == '\n') inComment = false;

            // Ignores spaces (but NOT new line or tabulator characters!)
            else if (!inComment && current != '\r') {
                if (current == '\t') {
                    if (currentTokenType != TokenType.INDENT) submitToken();
                    currentTokenType = TokenType.INDENT;
                    indendationLevel++;
                } else if (current == ' ' && currentTokenType != TokenType.TEXT) {
                    submitToken();
                    indendationLevel = 0;
                }
                else if (current == ',') {
                    submitToken();
                    indendationLevel = 0;
                    currentlyRead.append(current);
                    currentTokenType = TokenType.LIST_SEPARATOR;
                    submitToken();
                } else if (current == '\n') {
                    submitToken();
                    currentlyRead.append(current);
                    currentTokenType = TokenType.NEW_LINE;
                    indendationLevel = 0;
                    submitToken();
                } else if (current == '"') {
                    if (currentlyRead.isEmpty()) {
                        submitToken();
                        indendationLevel = 0;
                        currentlyRead.append(current);
                        currentTokenType = TokenType.TEXT;
                    } else if (currentlyRead.length() > 1 && currentlyRead.charAt(0) == '"') {
                        currentlyRead.append(current);
                        submitToken();
                        indendationLevel = 0;
                    } else {
                        indendationLevel = 0;
                        currentlyRead.append(current);
                    }
                } else if (currentTokenType == TokenType.TEXT) {
                    indendationLevel = 0;
                    currentlyRead.append(current);
                } else {
                    if (currentTokenType != null && currentTokenType != TokenType.KEYWORD) submitToken();

                    indendationLevel = 0;
                    currentTokenType = TokenType.KEYWORD;
                    currentlyRead.append(current);
                }
        }
    }

    public void submitToken() {
        if (currentTokenType != null)
        {
            var token = new Token(currentTokenType, currentlyRead.toString());

            System.out.println(token);
            if (token.type == TokenType.TEXT) token.value = token.value.substring(1, token.value.length() - 1);

            if (token.type == TokenType.KEYWORD) {
                var isLegalKeyword = false;
                for (var keyword : builtinKeywords) {
                    if (keyword.equals(token.value)) {
                        isLegalKeyword = true;
                        break;
                    }
                }

                if (!isLegalKeyword) throw new IllegalArgumentException("Unknown keyword:  '" + token.value + "'");
            } else if (token.type == TokenType.INDENT) token.value = Integer.toString(indendationLevel);

            frontEndBridge.enqueue(token);

            currentTokenType = null;
            currentlyRead = new StringBuilder();
        }
    }

}
