package yard.hunting.s.Tokens.Valid.Operands.NumericTypes;

import yard.hunting.s.Tokens.Valid.Operands.NumericValue;
import yard.hunting.s.Interfaces.DoubleValue;
import yard.hunting.s.Interfaces.StringValue;


public class FloatingPointValue extends NumericValue implements DoubleValue, StringValue {
    
    private double _value;
    
    public double numericValue () {
        return _value;
    }
    public void stringValue (String s) {
        _value = Double.parseDouble(s);
    }
    
    public void signum () {
        _value *= -1;
    }
    
    public Calculatable setCalculatable () {
        return new CalculatableFloatingPointValue(this);
    }
    
    public FloatingPointValue () {}
}
