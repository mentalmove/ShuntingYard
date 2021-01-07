package yard.hunting.s.Tokens.Valid.Operands;

import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.IntegerValue;
import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.FloatingPointValue;
import yard.hunting.s.Tokens.Valid.Valid;


abstract public class NumericValue extends Valid {
    
    public void stringValue (String s) {}
    
    public void signum () {}
    
    public NumericValue setCalculatable () {
        return this;
    }
    
    public NumericValue () {
        //System.out.println( "NumericValue()" );
    }
}
