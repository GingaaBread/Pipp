package lexical_analysis;

public class Text implements DFA {

    private int state;

    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == '\"') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == '\"') state = 2;
                return true;

            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 2;
    }

    @Override
    public void reset() {
        state = 0;
    }
}
