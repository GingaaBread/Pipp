package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;

@Getter
public class Assessors extends Node {
    private final ArrayList<Assessor> assessors = new ArrayList<>();

    @NonNull
    public void add(Assessor assessor) {
        if (assessor == null) throw new IllegalStateException();
        this.assessors.add(assessor);
    }

    @Override
    public String toString() {
        return "\nAssessors{" +
                "assessors=" + assessors +
                '}';
    }
}
