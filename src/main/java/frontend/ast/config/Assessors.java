package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;
import warning.InconsistencyWarning;
import warning.WarningQueue;

import java.util.ArrayList;

/**
 *  The assessors node groups together all assessors specified by the user.
 *
 * @author Gino Glink
 * @since 1.0
 * @version 1.0
 */
@Getter
public class Assessors extends Node {

    /**
     *  Contains all assessors specified by the user.
     *  To add an assessor to the list, use the add-method of this node.
     */
    private final ArrayList<Assessor> assessors = new ArrayList<>();

    /**
     *  Adds the specified assessor node to end of the assessors list
     *
     * @param assessor - the assessor AST node that should be added to the list
     */
    @NonNull
    public void add(final Assessor assessor) {
        this.assessors.add(assessor);
    }

    /**
     *  A textual representation of the Assessors node, which contains the assessors list as a string
     *
     * @return - the Assessors node as a String
     */
    @Override
    public String toString() {
        return "\n\tAssessors{" +
                "assessors=" + assessors +
                '}';
    }

    /**
     *  Yields a warning if there is at least one assessor with a role and at least one assessor without one
     */
    @Override
    protected void checkForWarnings() {
        boolean assessorWithRoleExists = false;
        boolean assessorWithoutRoleExists = false;

        for (var assessor : assessors) {
            if (!assessorWithoutRoleExists && assessor.getRole() == null) assessorWithoutRoleExists = true;
            else if (!assessorWithRoleExists && assessor.getRole() != null) assessorWithRoleExists = true;

            assessor.checkForWarnings();
        }

        if (assessorWithoutRoleExists && assessorWithRoleExists)
            WarningQueue.getInstance().enqueue(new InconsistencyWarning("1: At least one assessor has a role, but " +
                    "at least one assessor does not have a role. Make sure you really do not want all assessors" +
                    " to have a role."));
    }

}
