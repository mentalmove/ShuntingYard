package yard.hunting.s.TokenHandling.Helpers;

import yard.Trash;
import yard.hunting.s.Filters.InvalidFilter;
import yard.hunting.s.Output.Output;
import yard.hunting.s.TokenHandling.Helper;
import yard.hunting.s.Tokens.Token;


public class InvalidTokenDeletionHelper extends Helper {
    
    private void run () {
        InvalidFilter filter = new InvalidFilter();
        Token token = first;
        while (token != null) {
            if ( filter.applicable(token) ) {
                trash.add(token);
                if ( token == first ) {
                    first = first.getNext();
                }
                else {
                    if ( token.getPrevious() != null ) {
                        token.getPrevious().setNext( token.getNext() );
                        if ( token.getNext() != null ) {
                            token.getNext().setPrevious( token.getPrevious() );
                        }
                    }
                }
            }
            token = token.getNext();
        }
        
        if ( output != null ) {
            output.message("\t* " + "Interpretabale characters only" + " *");
            output.show(first);
        }
    }
    
    public InvalidTokenDeletionHelper (Token first, Trash trash, Output output) {
        super(first, trash, output);
        run();
    }
}
