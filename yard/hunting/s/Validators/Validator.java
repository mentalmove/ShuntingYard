package yard.hunting.s.Validators;

import yard.hunting.s.Tokens.Token;


abstract public class Validator {
    
    protected Token first;
    protected Evaluation _result;
    protected String verboseResult;
    
    public Evaluation result () {
        return _result;
    }
    public void tell () {
        if ( verboseResult == null ) {
            return;
        }
        System.out.println( "\t - " + verboseResult + " -" );
    }
    
    public static enum Mode {BRACKETS, OPERATORS_OPERANDS, ALL};
    public static enum Evaluation {VALID, INVALID, PENDING};
    
    public void run () {}
    
    public Validator () {
        this._result = Evaluation.PENDING;
    }
}
