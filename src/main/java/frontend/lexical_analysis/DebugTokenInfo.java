package frontend.lexical_analysis;

import lombok.Data;
import lombok.NonNull;

/**
 * Used to include debug information in tokens that are used for
 * error messages, debugging, etc.
 *
 * @version 1.0
 * @since 1.0
 */
@Data
public class DebugTokenInfo {

    /**
     * Stores the line number of the start of the token in the input file.
     * Line numbers are indexed at one, not zero-indexed.
     */
    private int lineNumber;

    /**
     * Points to the input file.
     * Could be the document, the bibliography, etc.
     */
    private String filePath;

    /**
     * Provides a textual pointer to the token used for error messages.
     * Contains the entire line until the token and has a caret ^ pointing to it.
     */
    private String currentLine;

    public String errorMessage(@NonNull final String description) {
        String caretBuilder = " ".repeat(currentLine.length()) + "^";
        return "\n" + filePath + ":" + lineNumber + " " + description + "\n" + currentLine + "\n" + caretBuilder;
    }
}
