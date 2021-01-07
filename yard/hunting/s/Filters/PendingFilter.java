package yard.hunting.s.Filters;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Pending.Pending;
import yard.hunting.s.Interfaces.TokenType;

public final class PendingFilter extends Filter implements TokenType {
    
    public Boolean applicable (Token token) {
        return (token instanceof Pending);
    }
}
