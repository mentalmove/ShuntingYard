package yard.hunting.s.Tokens.Invalid;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Interfaces.CharacterValue;


public class Invalid extends Token implements CharacterValue {
    
    public char value () {
        return ' ';
    }
    
    public Invalid () {
        //System.out.println( "Invalid()" );
    }
}
