package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Www implements DFA {

    private int state;

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'w') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'w') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 'w') state = 3;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 3;
    }

    @Override
    public void reset() {
        state = 0;
    }
}