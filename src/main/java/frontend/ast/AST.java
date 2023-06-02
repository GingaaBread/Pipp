package frontend.ast;

import frontend.ast.config.Configuration;
import lombok.Getter;

@Getter
public class AST {
    private final Configuration configuration = new Configuration();

    @Override
    public String toString() {
        return "AST:\n" + configuration.toString();
    }
}