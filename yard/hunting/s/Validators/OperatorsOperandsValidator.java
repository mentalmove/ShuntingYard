package yard.hunting.s.Validators;

import yard.hunting.s.Filters.NumericFilter;
import yard.hunting.s.Filters.OperatorFilter;
import yard.hunting.s.Tokens.Valid.Operators.Operators.Plus;
import yard.hunting.s.Tokens.Token;


public class OperatorsOperandsValidator extends Validator {
    
    private int operandCounter;
    private int operatorCounter;
    private final OperatorFilter operatorFilter;
    private final NumericFilter numericFilter;
    
    public void run () {
        Token token = first;
        Boolean number = false;
        while ( token != null ) {
            if ( numericFilter.applicable(token) || operatorFilter.applicable(token) ) {
                if ( numericFilter.applicable(token) ) {
                    operandCounter++;
                    if ( number ) {
                        _result = Evaluation.INVALID;
                    }
                    number = true;
                }
                else {
                    operatorCounter++;
                    if ( !number ) {
                        _result = Evaluation.INVALID;
                    }
                    number = false;
                }
            }
            token = token.getNext();
        }
        if ( _result != Evaluation.INVALID && (operandCounter - operatorCounter) == 1 ) {
            _result = Evaluation.VALID;
            verboseResult = "" + operandCounter + " operands, " + operatorCounter + " operators";
        }
        else {
            verboseResult = "Operator / Operand mismatch";
        }
    }
    
    public OperatorsOperandsValidator (Token first) {
        this.first = first;
        operandCounter = 0;
        operatorCounter = 0;
        operatorFilter = new OperatorFilter();
        numericFilter = new NumericFilter();
    }
}
