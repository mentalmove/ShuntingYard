package yard.hunting.s.Tokens.Valid.Brackets;

import yard.hunting.s.Tokens.Valid.Valid;
import yard.hunting.s.Interfaces.CharacterValue;


public class OpeningBracket extends Valid implements CharacterValue {
    
    public char value () {
        return '(';
    }
    
    public OpeningBracket () {
        //System.out.println( "OpeningBracket()" );
    }
}
