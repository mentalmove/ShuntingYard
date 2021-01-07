package yard.hunting.s.Tokens.Valid.Operands.NumericTypes;

import yard.hunting.s.Tokens.Valid.Operands.NumericValue;
import yard.hunting.s.Interfaces.LongValue;
import yard.hunting.s.Interfaces.StringValue;


public class IntegerValue extends NumericValue implements LongValue, StringValue {
    
    private long _value;
    
    public long numericValue () {
        return _value;
    }
    public void stringValue (String s) {
        _value = Long.parseLong(s);
    }
    
    public void signum () {
        _value *= -1;
    }
    
    public Calculatable setCalculatable () {
        return new CalculatableIntegerValue(this);
    }
    
    public IntegerValue () {}
}
