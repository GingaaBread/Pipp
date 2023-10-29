package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.ToString;
import warning.*;

import java.util.ArrayList;

/**
 *  The authors node groups together all authors specified by the user
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
@ToString
public class Authors extends Node {

    /**
     *  Contains all authors specified by the user.
     *  To add an author to the list, use the add-method of this node.
     */
    private final ArrayList<Author> authors = new ArrayList<>();

    /**
     *  Adds the specified author node to end of the author list
     *
     * @param author - the author AST node that should be added to the list
     */
    public void add(Author author) {
        authors.add(author);
    }

    /**
     *  Produces a warning if there is at least one author with an ID, and one author without one
     *  Also produces a warning if there are two authors with the same ID, and a warning if there is no author
     */
    @Override
    protected void checkForWarnings() {
        boolean authorWithIDExists = false;
        boolean authorWithoutIDExists = false;

        for (var author : authors) {
            if (!authorWithoutIDExists && author.getId() == null) authorWithoutIDExists = true;
            else if (!authorWithIDExists && author.getId() != null) authorWithIDExists = true;

            for (var otherAuthor : authors) {
                if (author == otherAuthor) continue;

                if (author.getId() != null && otherAuthor.getId() != null &&
                        author.getId().equals(otherAuthor.getId())) {
                    WarningQueue.enqueue(new UnlikelinessWarning("1: Two authors have the same " +
                            "id, which seems unlikely. Check if that is correct. \n\tAuthor 1: " + author +
                            ". \n\tAuthor 2: " + otherAuthor,
                            WarningSeverity.CRITICAL));
                }
            }

            author.checkForWarnings();
        }

        if (authors.isEmpty())
            WarningQueue.enqueue(new MissingMemberWarning("1: There is no specified author. " +
                    "Check if you really want to omit an author specification.",
                    WarningSeverity.CRITICAL));

        if (authorWithoutIDExists && authorWithIDExists)
            WarningQueue.enqueue(new InconsistencyWarning("2: At least one author has an ID, " +
                    "but at least one author does not have an ID. Make sure you really do not want all authors" +
                    " to have an ID.", WarningSeverity.HIGH));
    }

}
