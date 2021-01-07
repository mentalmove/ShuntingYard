package yard.hunting.s.Validators;

import yard.hunting.s.Filters.BracketFilter;
import yard.hunting.s.Tokens.Token;


public class BracketValidator extends Validator {
    
    private int bracketPairCounter;
    private int pendingBracketCounter;
    private final BracketFilter filter;
    
    public void run () {
        Token token = first;
        int innerTokenCounter = 0;
        while ( token != null ) {
            if ( filter.applicable(token) ) {
                if ( token.value() == '(' ) {
                    bracketPairCounter++;
                    pendingBracketCounter++;
                    innerTokenCounter = 0;
                }
                else {
                    pendingBracketCounter--;
                    if ( (innerTokenCounter % 2) == 0 ) {
                        _result = Evaluation.INVALID;
                    }
                }
                if ( pendingBracketCounter < 0 ) {
                    _result = Evaluation.INVALID;
                }
            }
            else {
                innerTokenCounter++;
            }
            token = token.getNext();
        }
        if ( pendingBracketCounter != 0 ) {
            _result = Evaluation.INVALID;
        }
        if ( _result != Evaluation.INVALID ) {
            _result = Evaluation.VALID;
            verboseResult = "" + bracketPairCounter + " bracket pairs";
        }
        else {
            if ( pendingBracketCounter == 0 ) {
                verboseResult = "Wrong bracket order";
            }
            else {
                verboseResult = "Too many";
                if ( pendingBracketCounter > 0 ) {
                    verboseResult += " opening ";
                }
                else {
                    verboseResult += " closing ";
                }
                verboseResult += "brackets";
            }
        }
    }
    
    public BracketValidator (Token first) {
        this.first = first;
        bracketPairCounter = 0;
        pendingBracketCounter = 0;
        filter = new BracketFilter();
    }
}
