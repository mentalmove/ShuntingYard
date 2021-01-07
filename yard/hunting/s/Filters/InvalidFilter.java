package yard.hunting.s.Filters;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Invalid.Invalid;
import yard.hunting.s.Interfaces.TokenType;

public final class InvalidFilter extends Filter implements TokenType {
    
    public Boolean applicable (Token token) {
        return (token instanceof Invalid);
    }
}
