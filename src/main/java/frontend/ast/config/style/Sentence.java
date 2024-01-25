package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Contains all configurations for the sentence structure
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class Sentence extends Node {

    /**
     * The font node contains information about a structure font used for that structure throughout the document.
     */
    private final Font font = new Font();

    /**
     * Does not produce warnings, but prompts the font configuration to check for warnings
     */
    @Override
    public void checkForWarnings() {
        font.checkForWarnings();
    }

}
