package yard.hunting.s.Tokens;


abstract public class Token {
    
    protected Token previous;
    protected Token next;
    
    public Token getPrevious () {
        return previous;
    }
    public Token getNext () {
        return next;
    }
    public void setPrevious (Token token) {
        previous = token;
    }
    public void setNext (Token token) {
        next = token;
    }
    
    public char value () {
        return '?';
    }
    
    public Token () {
        previous = null;
        next = null;
    }
}
