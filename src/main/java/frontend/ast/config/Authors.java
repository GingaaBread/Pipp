package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Authors extends Node {
    private final ArrayList<Author> authors = new ArrayList<>();

    public void add(Author author) {
        authors.add(author);
    }

    @Override
    public String toString() {
        return "\nAuthors{" +
                "authors=" + authors +
                '}';
    }
}
