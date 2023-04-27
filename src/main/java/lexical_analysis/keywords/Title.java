package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Title extends DFA {

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 't') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'i') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 't') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'l') state = 4;
                else return reject();
                return true;
            case 4:
                if (character == 'e') state = 5;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 5;
    }

}
