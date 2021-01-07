package yard.hunting.s.Tokens.Valid.Operands.NumericTypes;

import yard.hunting.s.Interfaces.FloatingPointArithmetics;
import yard.hunting.s.Interfaces.IntegerArithmetics;
import yard.hunting.s.Interfaces.LongValue;


public class CalculatableIntegerValue extends Calculatable implements LongValue, IntegerArithmetics, FloatingPointArithmetics {
    
    private long _value;
    
    public long numericValue () {
        return _value;
    }
    
    protected void setValue (long value) {
        _value = value;
    }
    
    public Calculatable add (long summand) {
        _value += summand;
        return this;
    }
    public Calculatable subtract (long subtractor) {
        _value -= subtractor;
        return this;
    }
    public Calculatable multiplicateWith (long multiplicator) {
        _value *= multiplicator;
        return this;
    }
    
    private CalculatableIntegerValue () {}
    public CalculatableIntegerValue (IntegerValue original) {
        super(original);
        _value = original.numericValue();
        doubleValue = (double) _value;
    }
    public CalculatableIntegerValue (long originalValue) {
        _value = originalValue;
        doubleValue = (double) originalValue;
    }
}
