package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author extends Node {
    private String name;
    private String firstname;
    private String lastname;
    private String id;

    @Override
    public String toString() {
        return "\nAuthor{" +
                "name='" + name + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
