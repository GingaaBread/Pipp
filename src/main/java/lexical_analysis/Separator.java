package lexical_analysis;

public class Separator extends DFA {

    public boolean transition(char character) {
        if (state == 0) {
            if (character == '\t') state = 1;
            else if (character == '\n') state = 2;
            else if (character == '\r') state = 2;
            else if (character == ',') state = 3;
            else return reject();
            return true;
        } else return reject();
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 1 || state == 2 || state == 3;
    }

}
