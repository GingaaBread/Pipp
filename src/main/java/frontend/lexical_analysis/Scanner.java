package frontend.lexical_analysis;

import error.MissingMemberException;
import frontend.FrontEndBridge;

/**
 * Responsible for the lexical analysis of .pipp files.
 * This class should only be instantiated by the FrontEndBridge class, which is used as an
 * interface between the Scanner and the Parser.
 * Note that the scanner ignores comments when a new line starts with the hash character #.
 */
public class Scanner {

    /**
     * Defines the keywords that come with Pipp.
     * Lowercase words without any special characters in them must match one of these keywords,
     * otherwise an exception is thrown.
     */
    public static final String[] builtinKeywords = new String[]{
            "appendix",
            "allow",
            "assessor",
            "author",
            "before",
            "bibliography",
            "blank",
            "bold",
            "chapter",
            "chair",
            "citation",
            "config",
            "colour",
            "date",
            "display",
            "endnotes",
            "emphasise",
            "firstname",
            "font",
            "header",
            "height",
            "italic",
            "id",
            "image",
            "in",
            "indentation",
            "institution",
            "lastname",
            "layout",
            "limit",
            "name",
            "numeration",
            "margin",
            "of",
            "paragraph",
            "publication",
            "role",
            "semester",
            "sentence",
            "size",
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
            "work",
            "whitespace"
    };
    /**
     * The reference to the front end bridge, which is used for communication purposes between
     * the scanner and the parser.
     */
    private final FrontEndBridge frontEndBridge;
    /**
     * Saves the type of the currently scanned token during the scanning process,
     * and resets it each time a token is submitted.
     */
    private TokenType currentTokenType;
    /**
     * Saves the value of the currently scanned token during the scanning process,
     * and resets it each time a token is submitted.
     */
    private StringBuilder currentlyReadValue = new StringBuilder();
    /**
     * Yields true if the scanner is currently in a comment and should therefore ignore all characters
     * until after the first NEW_LINE token.
     */
    private boolean inComment = false;
    /**
     * Yields true if the user uses the backslash in a text block and therefore wants to escape a character.
     * If the next character then is a quotation mark, the quotation mark is escaped.
     * If a character follows that cannot be escaped, an exception is thrown.
     */
    private boolean isEscapingACharacter = false;
    /**
     * Marks the current indentation level.
     * Each time the tab character \t is scanned this is incremented, and reset once
     * a different character is read.
     */
    private int currentAmountOfTabs = 0;
    /**
     * Tracks the current character of the current line.
     * Characters are not zero-indexed, but start at one.
     */
    private int charInLine;

    /**
     * Tracks if the token is a text that contains new line characters that have been converted into a new line.
     * This is used to turn multiple new lines into only a single space.
     */
    private boolean hasConvertedTextNewLine;

    /**
     * Creates a new instance of the scanner and passes the front end bridge as the
     * parent as an interface between the scanner and parser
     *
     * @param frontEndBridge - the instance of the FrontEndBridge used for communication
     */
    public Scanner(final FrontEndBridge frontEndBridge) {
        this.frontEndBridge = frontEndBridge;
    }

    /**
     * Scans the next character of the user input, and submits the token once found.
     *
     * @param current the next character that should be scanned
     */
    public void scan(final char current) {
        // We are scanning the next character, so the counter is incremented
        charInLine++;

        // Starts ignoring the line if the first character of a line is a comment (#)
        if (charInLine == 1 && current == '#') {
            inComment = true;
            charInLine = 0;
        }

        // Marks the end of a comment if the new line character is scanned
        else if (inComment && current == '\n') {
            inComment = false;
            charInLine = 0;
        }

        // Ignores all input if in a comment or if scanning a carriage return character
        else if (!inComment && current != '\r') {

            // Checks for indentation
            if (current == '\t') {
                // First submits the current token
                if (currentTokenType != TokenType.INDENT) submitToken();

                // Sets the INDENT type and increases the amount of tabs
                currentTokenType = TokenType.INDENT;
                currentAmountOfTabs++;
            }

            // Spaces submit the token and reset the amount of tabs for indent tokens
            else if (current == ' ' && currentTokenType != TokenType.TEXT) {
                submitToken();
                currentAmountOfTabs = 0;
            }

            // A comma submits the token and submits a new LIST_SEPARATOR token
            else if (current == ',' && currentTokenType != TokenType.TEXT) {
                submitToken();
                currentAmountOfTabs = 0;
                currentlyReadValue.append(current);
                currentTokenType = TokenType.LIST_SEPARATOR;
                submitToken();
            }

            // A new line character submits the current token and submits a NEW_LINE token
            else if (current == '\n' && currentTokenType != TokenType.TEXT) {
                submitToken();
                currentlyReadValue.append(current);
                currentTokenType = TokenType.NEW_LINE;
                currentAmountOfTabs = 0;
                charInLine = 0;
                submitToken();
            }

            // A quotation mark begins or ends a text token
            else if (current == '"') {

                // In this case, the current character is the beginning of a text
                if (currentlyReadValue.isEmpty()) {
                    submitToken();

                    // We still want to include the quotation mark in the text
                    currentlyReadValue.append(current);

                    currentAmountOfTabs = 0;
                    currentTokenType = TokenType.TEXT;
                }

                // In this case, the current character is the end of a text or an escaped quotation mark
                else if (currentlyReadValue.length() >= 1 && currentlyReadValue.charAt(0) == '"') {

                    // In this case, the current character is an escaped quotation mark
                    if (isEscapingACharacter) {
                        isEscapingACharacter = false;
                        currentlyReadValue.append("\"");
                    }

                    // In this case, the current character is the end of a text
                    else {
                        // Should not create empty strings
                        if (currentlyReadValue.substring(1, currentlyReadValue.length()).isBlank()) {
                            throw new MissingMemberException("1: A text component cannot be blank.");
                        } else {
                            // We still want to include the quotation mark in the text
                            currentlyReadValue.append(current);

                            submitToken();
                            currentAmountOfTabs = 0;
                        }
                    }
                }

                // A quotation mark ends a keyword
                else if (currentTokenType == TokenType.KEYWORD) {
                    submitToken();

                    // We still want to include the quotation mark in the text
                    currentlyReadValue.append(current);

                    currentAmountOfTabs = 0;
                    currentTokenType = TokenType.TEXT;
                }

                // In all other cases, append the quotation mark
                else {
                    currentAmountOfTabs = 0;
                    currentlyReadValue.append(current);
                }
            }

            // If the current token is a text, add the character to the text
            else if (currentTokenType == TokenType.TEXT) {
                currentAmountOfTabs = 0;

                // If the user wants to escape a character do not include the \
                if (current == '\\') {
                    hasConvertedTextNewLine = false;

                    // If the user is already escaping a character and the backslash is escaped, append it (\\)
                    if (isEscapingACharacter) {
                        isEscapingACharacter = false;
                        currentlyReadValue.append("\\");
                    } else isEscapingACharacter = true;
                } else if (current != '\n') {
                    hasConvertedTextNewLine = false;
                    currentlyReadValue.append(current);
                }
                // New line characters inside texts should be converted into spaces, but not repeatedly
                else if (!hasConvertedTextNewLine) {
                    hasConvertedTextNewLine = true;
                    currentlyReadValue.append(" ");
                }
            }

            // Keywords are the default tokens if no other tokens are matched
            else {
                if (currentTokenType != null && currentTokenType != TokenType.KEYWORD) submitToken();

                currentAmountOfTabs = 0;
                currentTokenType = TokenType.KEYWORD;
                currentlyReadValue.append(current);
            }
        }
    }

    /**
     * If there is a current token, this creates the type / value pair, notifies the FrontEndBridge
     * of the existence of the token, and then resets the token variables.
     */
    public void submitToken() {
        // Should only submit a token if it exists
        if (currentTokenType != null) {
            // Create the token object with the token type and value pair
            var token = new Token(currentTokenType, currentlyReadValue.toString());

            // In text tokens, the quotation marks do not have to be included ("Test" -> Test)
            if (token.type == TokenType.TEXT) token.value = token.value.substring(1, token.value.length() - 1);

            // Verify the integrity of KEYWORD tokens
            if (token.type == TokenType.KEYWORD) {
                var isLegalKeyword = false;
                for (var keyword : builtinKeywords) {
                    if (keyword.equals(token.value)) {
                        isLegalKeyword = true;
                        break;
                    }
                }

                if (!isLegalKeyword) throw new IllegalArgumentException("Unknown keyword:  '" + token.value + "'");
            }

            // INDENT tokens use the amount of tabs as their token values
            else if (token.type == TokenType.INDENT) token.value = Integer.toString(currentAmountOfTabs);

            // Tell the front end bridge that this token exists
            frontEndBridge.enqueueToken(token);

            // Reset token variables
            currentTokenType = null;
            currentlyReadValue = new StringBuilder();
        }
    }

}
