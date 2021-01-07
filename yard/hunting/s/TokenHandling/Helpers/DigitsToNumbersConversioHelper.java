package yard.hunting.s.TokenHandling.Helpers;

import yard.Trash;
import yard.hunting.s.Filters.PendingFilter;
import yard.hunting.s.Output.Output;
import yard.hunting.s.TokenHandling.Helper;
import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.*;


public class DigitsToNumbersConversioHelper extends Helper {
    
    private void run () {
        PendingFilter filter = new PendingFilter();
        Token token = first;
        Token start = first;
        Token end = first;
        Token recyclable;
        Boolean point = false;
        String number = "";
        while (token != null) {
            if ( filter.applicable(token) && token.value() != '-' ) {
                if ( number.equals("") ) {
                    start = token;
                }
                if ( point && token.value() == '.' ) {
                    trash.add(token);
                    if ( token.getPrevious() != null ) {
                        token.getPrevious().setNext( token.getNext() );
                    }
                    if ( token.getNext() != null ) {
                        token.getNext().setPrevious( token.getPrevious() );
                    }
                }
                else {
                    end = token;
                    if ( token.value() == '.' && number.equals("") ) {
                        number = "0.";
                    }
                    else {
                        number += token.value();
                    }
                    if ( token.value() == '.' ) {
                        point = true;
                    }
                }
            }
            else {
                if ( !number.equals("") ) {
                    if ( point ) {
                        FloatingPointValue trueNumber = new FloatingPointValue();
                        trueNumber.stringValue(number);
                        
                        if ( start == first ) {
                            first = trueNumber;
                        }
                        else {
                            start.getPrevious().setNext(trueNumber);
                            trueNumber.setPrevious(start.getPrevious());
                        }
                        trueNumber.setNext(end.getNext());
                        if ( end.getNext() != null ) {
                            end.getNext().setPrevious(trueNumber);
                        }
                    }
                    else {
                        IntegerValue trueNumber = new IntegerValue();
                        trueNumber.stringValue(number);
                        
                        if ( start == first ) {
                            first = trueNumber;
                        }
                        else {
                            start.getPrevious().setNext(trueNumber);
                            trueNumber.setPrevious(start.getPrevious());
                        }
                        trueNumber.setNext(end.getNext());
                        if ( end.getNext() != null ) {
                            end.getNext().setPrevious(trueNumber);
                        }
                    }
                    recyclable = start;
                    while (recyclable != null && recyclable != end) {
                        trash.add(recyclable);
                        recyclable = recyclable.getNext();
                    }
                    trash.add(recyclable);
                }
                number = "";
                point = false;
            }
            token = token.getNext();
        }
        
        if ( !number.equals("") ) {
            if ( point ) {
                FloatingPointValue trueNumber = new FloatingPointValue();
                trueNumber.stringValue(number);

                if ( start == first ) {
                    first = trueNumber;
                }
                else {
                    start.getPrevious().setNext(trueNumber);
                    trueNumber.setPrevious(start.getPrevious());
                }
                trueNumber.setNext(null);
            }
            else {
                IntegerValue trueNumber = new IntegerValue();
                trueNumber.stringValue(number);

                if ( start == first ) {
                    first = trueNumber;
                }
                else {
                    start.getPrevious().setNext(trueNumber);
                    trueNumber.setPrevious(start.getPrevious());
                }
                trueNumber.setNext(null);
            }
            if ( end == null || end == first ) {
                trash.add(start);
            }
            else {
                recyclable = start;
                while (recyclable != null && recyclable != end) {
                    trash.add(recyclable);
                    recyclable = recyclable.getNext();
                }
                trash.add(recyclable);
            }
        }
        
        if ( output != null ) {
            output.message("\t* " + "Digits as numbers" + " *");
            output.show(first);
        }
    }
    
    public DigitsToNumbersConversioHelper (Token first, Trash trash, Output output) {
        super(first, trash, output);
        run();
    }
}
