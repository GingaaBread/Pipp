package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Assessor extends Node {
    private String name;
    private String firstname;
    private String lastname;
    private String role;

    @Override
    public String toString() {
        return "\nAssessor{" +
                "name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
