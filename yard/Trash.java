package yard;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Invalid.Invalid;
import yard.hunting.s.Tokens.Pending.Pending;
import yard.hunting.s.Tokens.Valid.Brackets.*;
import yard.hunting.s.Tokens.Valid.Operators.Operator;
import yard.hunting.s.Tokens.Valid.Valid;

public class Trash {
    
    private Token remembered;
    
    private char recycle (Token token) {
        if (token instanceof Invalid) {
            return '0';
        }
        else {
            if (token instanceof Pending) {
                return token.value();
            }
            else {
                if (token instanceof Valid) {
                    if (token instanceof OpeningBracket || token instanceof ClosingBracket) {
                        return token.value();
                    }
                    else {
                        if (token instanceof Operator) {
                            return token.value();
                        }
                        else {
                            return '1';
                        }
                    }
                }
                return '?';
            }
        }
    }
    
    public void add (Token token) {
        if ( remembered == null ) {
            remembered = token;
        }
        else {
            char c = recycle(remembered);
            remembered = token;
        }
    }
    
    public Trash () {
        
    }
}
