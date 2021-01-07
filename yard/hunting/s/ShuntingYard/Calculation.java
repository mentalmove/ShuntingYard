package yard.hunting.s.ShuntingYard;

import yard.Trash;
import yard.hunting.s.Filters.NumericFilter;
import yard.hunting.s.Filters.OperatorFilter;
import yard.hunting.s.Output.Output;
import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Valid.Operands.NumericValue;
import yard.hunting.s.Tokens.Valid.Operands.NumericTypes.*;
import yard.hunting.s.Tokens.Valid.Operators.Operator;
import yard.hunting.s.Tokens.Valid.Operators.Operators.*;


public class Calculation {
    
    private Token first;
    private Calculatable _result;
    private Output output;
    private Trash trash;
    
    
    private Calculatable calculate (Calculatable firstOperand, NumericValue secondOperand, Operator operator) {
        long longValue = 0;
        double doubleValue = 0.0;
        Boolean isDouble;
        
        if ( secondOperand instanceof IntegerValue || secondOperand instanceof CalculatableIntegerValue ) {
            if ( secondOperand instanceof IntegerValue ) {
                longValue = ((IntegerValue) secondOperand).numericValue();
            }
            else {
                longValue = ((CalculatableIntegerValue) secondOperand).numericValue();
            }
            isDouble = false;
        }
        else {
            if ( secondOperand instanceof FloatingPointValue ) {
                doubleValue = ((FloatingPointValue) secondOperand).numericValue();
            }
            else {
                doubleValue = ((CalculatableFloatingPointValue) secondOperand).numericValue();
            }
            isDouble = true;
        }
        
        Calculatable result = new CalculatableIntegerValue(0);
        
        if ( operator instanceof Plus ) {
            if ( isDouble ) {
                result = firstOperand.add(doubleValue);
            }
            else {
                if ( firstOperand instanceof CalculatableIntegerValue ) {
                    result = ((CalculatableIntegerValue) firstOperand).add(longValue);
                }
                else {
                    result = firstOperand.add(longValue);
                }
            }
        }
        else {
            if ( operator instanceof Minus ) {
                if ( isDouble ) {
                    result = firstOperand.subtract(doubleValue);
                }
                else {
                    if ( firstOperand instanceof CalculatableIntegerValue ) {
                        result = ((CalculatableIntegerValue) firstOperand).subtract(longValue);
                    }
                    else {
                        result = firstOperand.subtract(longValue);
                    }
                }
            }
            else {
                if ( operator instanceof Multiplicate ) {
                    if ( isDouble ) {
                        result = firstOperand.multiplicateWith(doubleValue);
                    }
                    else {
                        if ( firstOperand instanceof CalculatableIntegerValue ) {
                            result = ((CalculatableIntegerValue) firstOperand).multiplicateWith(longValue);
                        }
                        else {
                            result = firstOperand.multiplicateWith(longValue);
                        }
                    }
                }
                else {
                    if ( isDouble ) {
                        result = firstOperand.divideBy(doubleValue);
                    }
                    else {
                        result = firstOperand.divideBy(longValue);
                    }
                }
            }
        }
        
        return result;
    }
    private void reduce (Calculatable remaining, Calculatable start, Operator end) {
        remaining.setPrevious(start.getPrevious());
        remaining.setNext(end.getNext());
        if ( start.getPrevious() != null ) {
            start.getPrevious().setNext(remaining);
        }
        if ( end.getNext() != null ) {
            end.getNext().setPrevious(remaining);
        }
        if ( start != remaining ) {
            trash.add(start);
        }
        trash.add(end);
    }
    public void run () {
        Token token = first;
        NumericFilter numericFilter = new NumericFilter();
        OperatorFilter operatorFilter = new OperatorFilter();
        Token firstOperand;
        Token secondOperand;
        Calculatable resultingToken;
        while ( token != null ) {
            if ( operatorFilter.applicable(token) ) {
                secondOperand = token.getPrevious();
                if ( secondOperand == null || !numericFilter.applicable(secondOperand) ) {
                    break;
                }
                firstOperand = secondOperand.getPrevious();
                if ( firstOperand == null || !numericFilter.applicable(firstOperand) ) {
                    break;
                }
                firstOperand = ((NumericValue) firstOperand).setCalculatable();
                
                resultingToken = calculate((Calculatable) firstOperand, (NumericValue) secondOperand, (Operator) token);
                trash.add(secondOperand);
                reduce(resultingToken, (Calculatable) firstOperand, (Operator) token);
                
                token = resultingToken;
                if ( token.getPrevious() == null ) {
                    trash.add(first);
                    first = token;
                }
                
                if ( output != null ) {
                    System.out.println( "\t" + output.simplePolishList(first) );
                }
            }
            
            token = token.getNext();
        }
    }
    
    public void show () {
        
        if ( output != null ) {
            System.out.println( "" );
        }
        
        if ( first instanceof CalculatableIntegerValue ) {
            System.out.println( "RESULT: " + ((CalculatableIntegerValue) first).numericValue() );
        }
        else {
            if ( first instanceof CalculatableFloatingPointValue ) {
                System.out.println( "RESULT: " + ((CalculatableFloatingPointValue) first).numericValue() );
            }
            else {
                System.out.println( "Not calculatable" );
            }
        }
    }

    
    public Calculation (Token first, Trash trash, Output output) {
        this.first = first;
        this.trash = trash;
        this.output = output;
        
        Calculatable.trash = trash;
    }
}
