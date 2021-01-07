package yard.hunting.s.Output;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.*;
import yard.hunting.s.Filters.NumericFilter;
import yard.hunting.s.Filters.OperatorFilter;

public class Output {
    
    public static enum VerbosityMode {SILENT, NORMAL, VERBOSE};
    public static VerbosityMode verbosity;
    
    public enum ShowMode {BASIC, INFIX, POLISH};
    
    private VerbosityMode debugMode;
    private ShowMode showMode; 
    
    private char separator;
    
    public void setDebugMode (VerbosityMode debugMode) {
        this.debugMode = debugMode;
    }
    public void setDebugMode () {
        this.setDebugMode(verbosity);
    }
    
    public void setShowMode (ShowMode showMode) {
        this.showMode = showMode;
    }
    public ShowMode getShowMode () {
        return showMode;
    }
    
    public void setSeparator (char c) {
        separator = c;
    }
    
    private void basic (Token first) {
        Token token = first;
        while ( token != null ) {
            System.out.print( token.value() );
            token = token.getNext();
        }
        System.out.println( "" );
        System.out.println( "" );
    }
    private void infix (Token first) {
        Token token = first;
        NumericFilter numericFilter = new NumericFilter();
        OperatorFilter operatorFilter = new OperatorFilter();
        while ( token != null ) {
            if ( numericFilter.applicable(token) ) {
                if ( token instanceof IntegerValue ) {
                    System.out.print( ((IntegerValue) token).numericValue() );
                }
                else {
                    System.out.print( ((FloatingPointValue) token).numericValue() );
                }
            }
            else {
                if ( operatorFilter.applicable(token) ) {
                    System.out.print( " " + token.value() + " " );
                }
                else {
                    System.out.print( token.value() );
                }
            }
            token = token.getNext();
        }
        System.out.println( "" );
        System.out.println( "" );
    }
    private void polish (Token first) {
        Token token = first;
        NumericFilter numericFilter = new NumericFilter();
        while ( token != null ) {
            if ( numericFilter.applicable(token) ) {
                if ( token instanceof IntegerValue ) {
                    System.out.print( ((IntegerValue) token).numericValue() );
                }
                else {
                    System.out.print( ((FloatingPointValue) token).numericValue() );
                }
            }
            else {
                System.out.print( token.value() );
            }
            token = token.getNext();
            System.out.print( " " );
        }
        System.out.println( "" );
        System.out.println( "" );
    }
    
    public String nonNumericSpacelessMessage (Token first) {
        String message = "";
        Token token = first;
        while ( token != null ) {
            message += token.value();
            token = token.getNext();
        }
        return message;
    }
    public String eventualNumericCommaSeparatedMessage (Token first) {
        String message = "";
        Token token = first;
        NumericFilter numericFilter = new NumericFilter();
        while ( token != null ) {
            if ( token != first ) {
                message += separator;
            }
            if ( numericFilter.applicable(token) ) {
                if ( token instanceof IntegerValue ) {
                    message += ((IntegerValue) token).numericValue();
                }
                else {
                    if ( token instanceof CalculatableIntegerValue ) {
                        message += ((CalculatableIntegerValue) token).numericValue();
                    }
                    else {
                        if ( token instanceof FloatingPointValue ) {
                            message += ((FloatingPointValue) token).numericValue();
                        }
                        else {
                            message += ((CalculatableFloatingPointValue) token).numericValue();
                        }
                    }
                }
            }
            else {
                message += token.value();
            }
            token = token.getNext();
        }
        return message;
    }
    public String simplePolishList (Token first) {
        return eventualNumericCommaSeparatedMessage(first);
    }
    
    public void show (Token first) {
        if ( debugMode == VerbosityMode.SILENT ) {
            System.out.println( "No output..." );
        }
        else {
            if ( showMode == ShowMode.BASIC ) {
                basic(first);
            }
            else {
                if ( showMode == ShowMode.INFIX ) {
                    infix(first);
                }
                else {
                    polish(first);
                }
            }
        }
    }
    public void message (String s) {
        System.out.println( s );
    }
    
    public Output () {
        
        if ( verbosity != null ) {
            this.debugMode = verbosity;
        }
        else {
            debugMode = VerbosityMode.NORMAL;
        }
        
        showMode = ShowMode.BASIC;
        
        separator = ',';
    }
}
