package frontend.ast.config;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Publication extends Node {
    private final Title title = new Title();

    @Setter
    private String date;

    @Override
    public String toString() {
        return "\nPublication{" +
                "title=" + title +
                ", date='" + date + '\'' +
                '}';
    }
}
