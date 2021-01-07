package yard.hunting.s.TokenHandling;

import yard.Trash;
import yard.hunting.s.Output.Output;
import yard.hunting.s.Tokens.Token;


abstract public class Helper {
    
    protected Token first;
    protected Trash trash;
    protected Output output;
    
    
    public Token getFirst () {
        return first;
    }
    
    
    public Helper (Token first, Trash trash, Output output) {
        this.first = first;
        this.trash = trash;
        this.output = output;
    }
    public Helper (Token first, Trash trash) {
        this.first = first;
        this.trash = trash;
    }
}
