package lexical_analysis.keywords;

import lexical_analysis.DFA;

public class Article extends DFA {

    @Override
    public boolean transition(char character) {
        switch (state) {
            case 0:
                if (character == 'a') state = 1;
                else return reject();
                return true;
            case 1:
                if (character == 'r') state = 2;
                else return reject();
                return true;
            case 2:
                if (character == 't') state = 3;
                else return reject();
                return true;
            case 3:
                if (character == 'i') state = 4;
                else return reject();
                return true;
            case 4:
                if (character == 'c') state = 5;
                else return reject();
                return true;
            case 5:
                if (character == 'l') state = 6;
                else return reject();
                return true;
            case 6:
                if (character == 'e') state = 7;
                else return reject();
                return true;
            default: return reject();
        }
    }

    private boolean reject() { state = -1; return false; }

    @Override
    public boolean inAcceptState() {
        return state == 7;
    }

}
