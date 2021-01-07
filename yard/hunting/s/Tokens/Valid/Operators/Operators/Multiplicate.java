package yard.hunting.s.Tokens.Valid.Operators.Operators;

import yard.hunting.s.Tokens.Valid.Operators.Operator;


public class Multiplicate extends Operator {
    
    public char value () {
        return '*';
    }
    
    public Multiplicate () {
        _precedence = 3;
    }
}
