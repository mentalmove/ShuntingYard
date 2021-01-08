package yard.hunting.s.Tokens.Valid.Operands.NumericTypes;

import yard.Trash;
import yard.hunting.s.Tokens.Valid.Operands.NumericValue;


abstract public class Calculatable extends NumericValue {
    
    public static Trash trash;
    protected double doubleValue;
    
    protected void setValue (long value) {}
    protected void setValue (double value) {}
    
    private Calculatable executeCalculation (double result) {
        long truncated = (long) result;
        if ( result == truncated ) {
            if ( this instanceof CalculatableFloatingPointValue ) {
                return new CalculatableIntegerValue((long) result);
            }
            else {
                this.setValue((long) result);
            }
        }
        else {
            if ( this instanceof CalculatableFloatingPointValue ) {
                this.setValue(result);
            }
            else {
                return new CalculatableFloatingPointValue(result);
            }
        }
        return this;
    }
    
    public Calculatable add (double summand) {
        double result = doubleValue + summand;
        return executeCalculation(result);
    }
    public Calculatable subtract (double subtractor) {
        double result = doubleValue - subtractor;
        return executeCalculation(result);
    }
    public Calculatable multiplicateWith (double multiplicator) {
        double result = doubleValue * multiplicator;
        return executeCalculation(result);
    }
    public Calculatable divideBy (double divisor) {
        if ( divisor == 0 ) {
            return new CalculatableIntegerValue(0);
        }
        double result = doubleValue / divisor;
        return executeCalculation(result);
    }
    
    public Calculatable () {}
    protected Calculatable (NumericValue original) {
        this.setPrevious(original.getPrevious());
        this.setNext(original.getNext());
        if ( this.getPrevious() != null ) {
            original.getPrevious().setNext(this);
        }
        if ( this.getNext() != null ) {
            original.getNext().setPrevious(this);
        }
        
        if ( trash != null ) {
            trash.add(original);
        }
    }
}
