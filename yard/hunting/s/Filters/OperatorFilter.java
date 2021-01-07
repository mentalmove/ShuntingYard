package yard.hunting.s.Filters;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Valid.Operators.Operator;
import yard.hunting.s.Interfaces.TokenType;

public final class OperatorFilter extends Filter implements TokenType {
    
    public Boolean applicable (Token token) {
        return (token instanceof Operator);
    }
}
