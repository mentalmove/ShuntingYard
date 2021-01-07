package yard.hunting.s.TokenHandling.Helpers;

import yard.Trash;
import yard.hunting.s.Filters.*;
import yard.hunting.s.Tokens.Token;
import yard.hunting.s.TokenHandling.Helper;
import yard.hunting.s.Tokens.Valid.Operands.NumericValue;
import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.IntegerValue;
import yard.hunting.s.Tokens.Valid.Operators.Operators.Minus;
import yard.hunting.s.Tokens.Valid.Operators.Operators.Multiplicate;


public class CompletelyAutomatedPrivateTesttotellSignumsandHyphensApart extends Helper {
    
    private void run () {
        
        InvalidFilter invalidFilter = new InvalidFilter();
        PendingFilter pendingFilter = new PendingFilter();
        NumericFilter numericFilter = new NumericFilter();
        OperatorFilter operatorFilter = new OperatorFilter();
        IntegerValue minusOne;
        Multiplicate multiplicate;
        Minus minus;
        
        Token token = first;
        
        if ( pendingFilter.applicable(token) ) {
            if ( token.value() != '-' || token.getNext() == null || (token.getNext().value() != '(' && !numericFilter.applicable(token.getNext())) ) {
                first = token.getNext();
                trash.add(token);
            }
            else {
                if ( token.getNext().value() == '(' ) {
                    minusOne = new IntegerValue();
                    minusOne.stringValue("1");
                    minusOne.signum();
                    multiplicate = new Multiplicate();
                    minusOne.setNext(multiplicate);
                    multiplicate.setPrevious(minusOne);
                    multiplicate.setNext(token.getNext());
                    token.getNext().setPrevious(multiplicate);
                    first = minusOne;
                    trash.add(token);
                }
                else {
                    ((NumericValue) token.getNext()).signum();
                    first = token.getNext();
                    first.setPrevious(null);
                    trash.add(token);
                }
            }
        }
        
        token = first.getNext();
        while ( token != null ) {
            
            if ( !invalidFilter.applicable(token) && !pendingFilter.applicable(token) ) {
                token = token.getNext();
                continue;
            }
            
            if ( token.getNext() != null && token.value() == '-' && (numericFilter.applicable(token.getPrevious()) || token.getPrevious().value() == ')') && (numericFilter.applicable(token.getNext()) || token.getNext().value() == '(' || token.getNext().value() == '-') ) {
                minus = new Minus();
                token.getPrevious().setNext(minus);
                minus.setPrevious(token.getPrevious());
                minus.setNext(token.getNext());
                token.getNext().setPrevious(minus);
                trash.add(token);
            }
            else {
                if ( token.getNext() != null && token.value() == '-' && (operatorFilter.applicable(token.getPrevious()) || token.getPrevious().value() == '(') && (numericFilter.applicable(token.getNext()) || token.getNext().value() == '(') ) {
                    if ( token.getNext().value() == '(' ) {
                        minusOne = new IntegerValue();
                        minusOne.stringValue("1");
                        minusOne.signum();
                        multiplicate = new Multiplicate();
                        minusOne.setNext(multiplicate);
                        multiplicate.setPrevious(minusOne);
                        multiplicate.setNext(token.getNext());
                        token.getNext().setPrevious(multiplicate);
                        minusOne.setPrevious(token.getPrevious());
                        token.getPrevious().setNext(minusOne);
                        trash.add(token);
                    }
                    else {
                        ((NumericValue) token.getNext()).signum();
                        token.getPrevious().setNext(token.getNext());
                        token.getNext().setPrevious(token.getPrevious());
                        trash.add(token);
                    }
                }
                else {
                    token.getPrevious().setNext(token.getNext());
                    if ( token.getNext() != null ) {
                        token.getNext().setPrevious(token.getPrevious());
                    }
                    trash.add(token);
                }
            }
            
            token = token.getNext();
        }
    }

    public CompletelyAutomatedPrivateTesttotellSignumsandHyphensApart(Token first, Trash trash) {
        super(first, trash);
        run();
    }
}
