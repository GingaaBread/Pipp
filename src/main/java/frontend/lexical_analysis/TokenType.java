package frontend.lexical_analysis;

/**
 *  Defines the types of tokens in Pipp.
 *  The scanner uses this enumeration to create tokens or throw exceptions if the input is invalid.
 *
 * @version 1.0
 * @author Gino Glink
 */
public enum TokenType {

    /**
     *  A Pipp keyword is a lowercase word using no special characters.
     *  There is a set amount of builtin keywords in Pipp. All other lowercase words
     *  will not be classified as a KEYWORD TokenType, and an exception will be thrown.
     *
     * @see Scanner#builtinKeywords
     */
    KEYWORD,

    /**
     *  The NEW_LINE TokenType marks the use of a new line character on the user's operating
     *  system, such as {@code  \n} and {@code  \r}, which is usually inserted using the ENTER key.
     */
    NEW_LINE,

    /**
     *  The LIST_SEPARATOR TokenType is currently the comma {@code ,}, which is used
     *  to list multiple items in Pipp.
     */
    LIST_SEPARATOR,

    /**
     *  The TEXT TokenType marks textual content specified by the user.
     *  This includes any string within quotation marks, even special characters
     *  like the LIST_SEPARATOR and NEW_LINE characters.
     *  Example: {@code "Hello, World!\n"}
     */
    TEXT,

    /**
     *  The INDENT TokenType marks the use of indentation on the user's operating system, such as
     *  {@code \t}, which is usually inserted using the TAB key.
     */
    INDENT

}
