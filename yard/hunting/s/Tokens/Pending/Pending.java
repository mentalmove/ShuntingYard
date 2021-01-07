package yard.hunting.s.Tokens.Pending;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Interfaces.CharacterValue;

abstract public class Pending extends Token implements CharacterValue {
    
    public char value () {
        return '?';
    }
    
    public Pending () {
        //System.out.println( "Pending()" );
    }
}
