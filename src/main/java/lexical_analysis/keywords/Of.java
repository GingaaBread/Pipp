package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Of extends DFA {

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'o') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'f') state = 2;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 2;
    }

}
