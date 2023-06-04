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
 *  @since 1.0
 *  @version 1.0
 */
@Getter
public class Assessors extends Node {
    private final ArrayList<Assessor> assessors = new ArrayList<>();

    @NonNull
    public void add(Assessor assessor) {
        this.assessors.add(assessor);
    }

    @Override
    public String toString() {
        return "\n\tAssessors{" +
                "assessors=" + assessors +
                '}';
    }

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

    @Override
    protected void checkForErrors() {
        for (var assessor : assessors) assessor.checkForErrors();
    }
}
