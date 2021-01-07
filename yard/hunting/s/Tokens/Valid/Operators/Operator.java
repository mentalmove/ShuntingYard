package yard.hunting.s.Tokens.Valid.Operators;

import yard.hunting.s.Tokens.Valid.Valid;
import yard.hunting.s.Interfaces.CharacterValue;


abstract public class Operator extends Valid implements CharacterValue {
    
    protected int _precedence;
    
    public char value () {
        return '?';
    }
    
    public int precedence () {
        return _precedence;
    }
    
    public Operator () {
        _precedence = 2;
    }
}
