package processing.style;

import error.MissingMemberException;
import lombok.NonNull;

/**
 * The style table is responsible for resolving the name of a style guide
 * to its actual StyleGuide object. If the name cannot be resolved,
 * a {@link MissingMemberException} is thrown.
 *
 * @version 1.0
 * @since 1.0.
 */
public class StyleTable {

    /**
     * Prevents instantiation
     */
    private StyleTable() {
        throw new UnsupportedOperationException("Should not instantiate static helper class");
    }

    /**
     * Translates the specified style guide name into the respective style guide object.
     * Accepted names: MLA9
     *
     * @param name the non-null name of the style guide
     * @return a new instance of the respective style guide object
     * @throws MissingMemberException if the style guide name does not exist in the table
     */
    public static StyleGuide nameToStyleGuide(@NonNull final String name) {
        if (name.equals("MLA9")) return new MLA9();
        throw new MissingMemberException("3: The specified style guide (" + name + ")" +
                " is either missing or does not exist. Check if it has been imported correctly, " +
                "or if you misspelled the style guide name in the configuration");
    }
}
