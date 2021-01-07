package yard.hunting.s.Filters;

import yard.hunting.s.Interfaces.TokenType;
import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Valid.Brackets.*;

public final class BracketFilter extends Filter implements TokenType {
    
    public Boolean applicable (Token token) {
        return ((token instanceof OpeningBracket) || (token instanceof ClosingBracket));
    }
}
