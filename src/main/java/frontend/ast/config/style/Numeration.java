package frontend.ast.config.style;

import error.MissingMemberException;
import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.List;

/**
 * The numeration node contains information about how the page numbers should be styled
 *
 * @author Gino Glink
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Numeration extends Node {

    /**
     * Contains all pages that should be skipped during numeration.
     * Skipped pages do not receive a numeration in the first place, rather than not rendering the numbers.
     * To add a page or page span that should be skipped, use the addSkippedPage-method of this node.
     */
    private final List<String> skippedPages = new LinkedList<>();
    /**
     * The type of numeration that should be displayed.
     * This can be "ARABIC" for numbers like 1, 2 or 16,
     * or "ROMAN" for numbers like I, II, or XVI.
     */
    @Setter
    private String numerationType;
    /**
     * The limit of author names in the numeration. If that amount of authors or more are authors of the document,
     * it will only render the first author. If however, fewer than the limit are authors of the document, all
     * authors will be displayed.
     */
    @Setter
    private String authorLimit;
    /**
     * Determines where the page numbers should be placed
     */
    @Setter
    private String position;
    /**
     * Determines the margin to the respective sides of the document
     */
    @Setter
    private String margin;
    /**
     * Determines if, and if yes how, the names of the authors should be inserted before the page numbers
     */
    @Setter
    private String authorName;

    /**
     * Adds a page number to the pages that should be skipped during numeration
     *
     * @param skippedPage - the page number that should be skipped
     */
    @NonNull
    public void addSkippedPage(final String skippedPage) {
        skippedPages.add(skippedPage);
    }

    /**
     * The numeration produces warnings if any field is blank
     */
    @Override
    public void checkForWarnings() {
        if (numerationType != null && numerationType.isBlank() ||
                authorLimit != null && authorLimit.isBlank() ||
                position != null && position.isBlank() ||
                margin != null && margin.isBlank() ||
                authorName != null && authorName.isBlank())
            throw new MissingMemberException(MissingMemberException.ERR_MSG_1);
    }

}
