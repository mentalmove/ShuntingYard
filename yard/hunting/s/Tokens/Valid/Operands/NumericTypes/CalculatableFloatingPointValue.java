package yard.hunting.s.Tokens.Valid.Operands.NumericTypes;

import yard.hunting.s.Interfaces.DoubleValue;
import yard.hunting.s.Interfaces.FloatingPointArithmetics;


public class CalculatableFloatingPointValue extends Calculatable implements DoubleValue, FloatingPointArithmetics {
    
    private double _value;
    
    public double numericValue () {
        return _value;
    }
    
    protected void setValue (double value) {
        _value = value;
    }
    
    private CalculatableFloatingPointValue () {}
    public CalculatableFloatingPointValue (FloatingPointValue original) {
        super(original);
        _value = original.numericValue();
        doubleValue = _value;
    }
    public CalculatableFloatingPointValue (double originalValue) {
        _value = originalValue;
        doubleValue = originalValue;
    }
}
