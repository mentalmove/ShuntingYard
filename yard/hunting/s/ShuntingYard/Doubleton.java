package yard.hunting.s.ShuntingYard;

import yard.hunting.s.Tokens.Token;


public class Doubleton {
    
    private static Doubleton firstInstance;
    private static Doubleton secondInstance;
    
    private Boolean popable;
    private Token first, last;
    
    public static Doubleton getInstance() {
        if ( firstInstance == null ) {
            firstInstance = new Doubleton(true);
            return firstInstance;
        }
        else {
            if ( secondInstance == null ) {
                secondInstance = new Doubleton(false);
                return secondInstance;
            }
        }
        return null;
    }
    
    public Token finalise () {
        if ( first == null ) {
            return null;
        }
        first.setPrevious(null);
        last.setNext(null);
        return first;
    }
    
    public void push (Token token) {
        token.setNext(null);
        if ( first == null || last == null ) {
            first = token;
            last = token;
            token.setPrevious(null);
        }
        else {
            last.setNext(token);
            token.setPrevious(last);
            last = token;
        }
    }
    public Token pop () {
        if ( !popable || last == null ) {
            return null;
        }
        Token element = last;
        last = last.getPrevious();
        return element;
    }
    
    public Token getFirst () {
        return first;
    }
    public Token getLast () {
        return last;
    }
    
    
    private Doubleton (Boolean popable) {
        this.popable = popable;
    }
}
