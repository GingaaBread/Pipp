package lexical_analysis;

public interface DFA {

    boolean transition(char character);

    boolean inAcceptState();

    void reset();
}
