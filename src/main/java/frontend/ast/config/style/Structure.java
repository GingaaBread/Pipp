package frontend.ast.config.style;

import frontend.ast.Node;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Structure extends Node {
    private final Paragraph paragraph = new Paragraph();
    private final Sentence sentence = new Sentence();
    private final Endnotes endnotes = new Endnotes();

    @Override
    public String toString() {
        return "\nStructure{" +
                "paragraph=" + paragraph +
                ", sentence=" + sentence +
                ", endnotes=" + endnotes +
                '}';
    }
}
