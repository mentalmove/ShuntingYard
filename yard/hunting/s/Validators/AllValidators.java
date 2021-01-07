package yard.hunting.s.Validators;

import yard.hunting.s.Tokens.Token;


public class AllValidators extends Validator {
    
    private final OperatorsOperandsValidator operatorsOperandsValidator;
    private final BracketValidator bracketValidator;
    
    public void run () {
        operatorsOperandsValidator.run();
        bracketValidator.run();
        if ( operatorsOperandsValidator.result() == Evaluation.INVALID
                || bracketValidator.result() == Evaluation.INVALID ) {
            _result = Evaluation.INVALID;
        }
        else {
            if ( operatorsOperandsValidator.result() == Evaluation.PENDING 
                    || bracketValidator.result() == Evaluation.PENDING ) {
                _result = Evaluation.PENDING;
            }
            else {
                _result = Evaluation.VALID;
            }
        }
    }
    
    public void tell () {
        operatorsOperandsValidator.tell();
        bracketValidator.tell();
    }
    
    public AllValidators (Token first) {
        operatorsOperandsValidator = new OperatorsOperandsValidator(first);
        bracketValidator = new BracketValidator(first);
    }
}
