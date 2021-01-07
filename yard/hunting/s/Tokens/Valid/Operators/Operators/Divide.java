package yard.hunting.s.Tokens.Valid.Operators.Operators;

import yard.hunting.s.Tokens.Valid.Operators.Operator;


public class Divide extends Operator {
    
    public char value () {
        return '/';
    }
    
    public Divide () {
        _precedence = 3;
    }
}
