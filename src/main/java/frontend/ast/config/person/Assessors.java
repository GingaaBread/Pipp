package frontend.ast.config.person;

import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import warning.InconsistencyWarning;
import warning.WarningQueue;
import warning.WarningSeverity;

import java.util.ArrayList;

/**
 * The assessors node groups together all assessors specified by the user.
 *
 * @version 1.0
 * @since 1.0
 */
@Getter
@ToString
public class Assessors extends Node {

    /**
     * Contains all assessors specified by the user.
     * To add an assessor to the list, use the add-method of this node.
     */
    private final ArrayList<Assessor> assessorsList = new ArrayList<>();

    /**
     * Adds the specified assessor node to end of the assessors list
     *
     * @param assessor - the assessor AST node that should be added to the list
     */
    @NonNull
    public void add(final Assessor assessor) {
        this.assessorsList.add(assessor);
    }

    /**
     * Yields a warning if there is at least one assessor with a role and at least one assessor without one
     */
    @Override
    public void checkForWarnings() {
        boolean assessorWithRoleExists = false;
        boolean assessorWithoutRoleExists = false;

        for (var assessor : assessorsList) {
            if (!assessorWithoutRoleExists && assessor.getRole() == null) assessorWithoutRoleExists = true;
            else if (!assessorWithRoleExists && assessor.getRole() != null) assessorWithRoleExists = true;

            assessor.checkForWarnings();
        }

        if (assessorWithoutRoleExists && assessorWithRoleExists)
            WarningQueue.enqueue(new InconsistencyWarning("1: At least one assessor has a role, but " +
                    "at least one assessor does not have a role. Make sure you really do not want all assessors" +
                    " to have a role.", WarningSeverity.HIGH));
    }

}
