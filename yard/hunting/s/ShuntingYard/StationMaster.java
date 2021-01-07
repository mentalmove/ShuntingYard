package yard.hunting.s.ShuntingYard;

import yard.Trash;
import yard.hunting.s.Filters.NumericFilter;
import yard.hunting.s.Filters.OperatorFilter;
import yard.hunting.s.Output.Output;
import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Valid.Brackets.ClosingBracket;
import yard.hunting.s.Tokens.Valid.Brackets.OpeningBracket;
import yard.hunting.s.Tokens.Valid.Operands.NumericValue;
import yard.hunting.s.Tokens.Valid.Operators.Operator;


public class StationMaster {
    
    private Doubleton sidetrack;
    private Doubleton maintrack;
    private Token first;
    private Output output;
    private Trash trash;
    
    public void setOutput (Output output) {
        this.output = output;
    }
    
    public Token regulate () {
        Token token = first;
        Token popped;
        Token next;
        NumericFilter numericFilter = new NumericFilter();
        OperatorFilter operatorFilter = new OperatorFilter();
        String debug;
        while ( token != null ) {
            next = token.getNext();
            if ( operatorFilter.applicable(token) ) {
                while ( sidetrack.getLast() != null && operatorFilter.applicable(sidetrack.getLast()) && ((Operator) sidetrack.getLast()).precedence() >= ((Operator) token).precedence() ) {
                    popped = sidetrack.pop();
                    maintrack.push(popped);
                }
                sidetrack.push(token);
            }
            else {
                if ( numericFilter.applicable(token) ) {
                    maintrack.push(token);
                }
                else {
                    if ( token instanceof OpeningBracket ) {
                        sidetrack.push(token);
                    }
                    else {
                        while ( sidetrack.getLast() != null && !(sidetrack.getLast() instanceof OpeningBracket) ) {
                            popped = sidetrack.pop();
                            maintrack.push(popped);
                        }
                        popped = sidetrack.pop();
                        if ( popped != null && popped instanceof OpeningBracket ) {
                            trash.add(popped);
                        }
                        
                        token.setPrevious(null);
                        token.setNext(null);
                        trash.add(token);
                    }
                }
            }
            
            if ( output != null && sidetrack.getFirst() != null && maintrack.getFirst() != null ) {
                debug = "`" + output.nonNumericSpacelessMessage(sidetrack.getFirst()) + "` => `" + output.eventualNumericCommaSeparatedMessage(maintrack.getFirst()) + "`";
                System.out.println( "\t" + debug );
            }
            
            token = next;
        }
        
        while ( sidetrack.getLast() != null ) {
            popped = sidetrack.pop();
            if ( operatorFilter.applicable(popped) ) {
                maintrack.push(popped);
            }
        }
        
        return maintrack.finalise();
    }
    
    public StationMaster (Doubleton sidetrack, Doubleton maintrack, Token first, Trash trash) {
        this.sidetrack = sidetrack;
        this.maintrack = maintrack;
        this.first = first;
        this.trash = trash;
    }
}
