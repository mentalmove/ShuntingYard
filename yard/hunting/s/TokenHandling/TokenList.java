package yard.hunting.s.TokenHandling;

import yard.Trash;
import yard.hunting.s.Output.Output;
import yard.hunting.s.ShuntingYard.Calculation;
import yard.hunting.s.ShuntingYard.Doubleton;
import yard.hunting.s.ShuntingYard.StationMaster;

import yard.hunting.s.Tokens.Token;
import yard.hunting.s.Tokens.Invalid.Invalid;

import yard.hunting.s.Tokens.Pending.CharacterMinus;
import yard.hunting.s.Tokens.Pending.Point;
import yard.hunting.s.Tokens.Pending.Digits.*;

import yard.hunting.s.Tokens.Valid.Operators.Operators.*;
import yard.hunting.s.Tokens.Valid.Brackets.*;

import yard.hunting.s.TokenHandling.Helpers.*;
import yard.hunting.s.Validators.*;


public class TokenList {
    
    private static TokenList tokenList;
    
    private Token first;
    private Boolean initialised;
    
    private Output output;
    
    private Trash trash;
    
    public static TokenList getInstance() {
        if ( tokenList == null ) {
            tokenList = new TokenList();
        }
        return tokenList;
    }
    
    
    public void show () {
        if ( !initialised ) {
            output.message("\t* " + "Nothing to show!");
        }
        else {
            output.show(first);
        }
    }
    
    
    public void initialise (String term) {
        
        char[] singleCharacters = term.toCharArray();
        
        Token tmpToken = null;
        Token last = null;
        int charNumber;
        
        for ( int i = 0; i < singleCharacters.length; i++ ) {
            charNumber = singleCharacters[i];
            if ( "()*+/".indexOf(singleCharacters[i]) >= 0 ) {
                switch (charNumber) {
                    case 40:
                        tmpToken = new OpeningBracket();
                    break;
                    case 41:
                        tmpToken = new ClosingBracket();
                    break;
                    case 42:
                        tmpToken = new Multiplicate();
                    break;
                    case 43:
                        tmpToken = new Plus();
                    break;
                    case 47:
                        tmpToken = new Divide();
                }
            }
            else {
                if ( "-.0123456789".indexOf(singleCharacters[i]) >= 0 ) {
                    switch (charNumber) {
                        case 45:
                            tmpToken = new CharacterMinus();
                        break;
                        case 46:
                            tmpToken = new Point();
                        break;
                        case 48:
                            tmpToken = new Zero();
                        break;
                        case 49:
                            tmpToken = new One();
                        break;
                        case 50:
                            tmpToken = new Two();
                        break;
                        case 51:
                            tmpToken = new Three();
                        break;
                        case 52:
                            tmpToken = new Four();
                        break;
                        case 53:
                            tmpToken = new Five();
                        break;
                        case 54:
                            tmpToken = new Six();
                        break;
                        case 55:
                            tmpToken = new Seven();
                        break;
                        case 56:
                            tmpToken = new Eight();
                        break;
                        case 57:
                            tmpToken = new Nine();
                    }
                }
                else {
                    tmpToken = new Invalid();
                }
            }
            
            if ( first == null ) {
                first = tmpToken;
                last = tmpToken;
            }
            else {
                last.setNext(tmpToken);
                tmpToken.setPrevious(last);
                last = tmpToken;
            }
        }
        
        initialised = true;
        
        output.message("");
        if ( Output.verbosity != Output.VerbosityMode.SILENT ) {
            output.message("\t* " + "Term as uninterpreted characters" + " *");
        }
        
        output.setShowMode(Output.ShowMode.BASIC);
    }
    
    
    public void convert () {
        
        Output verboseOutput = (Output.verbosity == Output.VerbosityMode.VERBOSE) ? output : null;
        
        InvalidTokenDeletionHelper invalidTokenDeletionHelper = new InvalidTokenDeletionHelper(first, trash, verboseOutput);
        first = invalidTokenDeletionHelper.getFirst();
        
        output.setShowMode(Output.ShowMode.INFIX);
        
        DigitsToNumbersConversioHelper digitsToNumbersConversioHelper = new DigitsToNumbersConversioHelper(first, trash, verboseOutput);
        first = digitsToNumbersConversioHelper.getFirst();
        
        if ( first != null ) {
            CompletelyAutomatedPrivateTesttotellSignumsandHyphensApart CAPTSHA = new CompletelyAutomatedPrivateTesttotellSignumsandHyphensApart(first, trash);
            first = CAPTSHA.getFirst();
        }
        
        if ( Output.verbosity != Output.VerbosityMode.SILENT ) {
            output.message("\t* " + "Infix notation" + " *");
        }
    }
    
    public Validator runValidator (Validator.Mode mode) {
        Validator validator;
        if ( mode == Validator.Mode.BRACKETS ) {
            validator = new BracketValidator(first);
        }
        else {
            if ( mode == Validator.Mode.OPERATORS_OPERANDS ) {
                validator = new OperatorsOperandsValidator(first);
            }
            else {
                validator = new AllValidators(first);
            }
        }
        validator.run();
        return validator;
    }
    
    public void translate () {
        
        output.setShowMode(Output.ShowMode.POLISH);
        
        Output verboseOutput = (Output.verbosity == Output.VerbosityMode.VERBOSE) ? output : null;
        StationMaster stationMaster = new StationMaster(Doubleton.getInstance(), Doubleton.getInstance(), first, trash);
        
        System.out.println( "" );
        stationMaster.setOutput(verboseOutput);
        
        first = stationMaster.regulate();
        
        if ( verboseOutput != null ) {
            System.out.println( "" );
        }
        if ( Output.verbosity != Output.VerbosityMode.SILENT ) {
            output.message("\t* " + "Reverse polish notation" + " *");
        }
        output.show(first);
    }
    
    public Calculation calculate () {
        
        Output verboseOutput = (Output.verbosity == Output.VerbosityMode.VERBOSE) ? output : null;
        if ( verboseOutput != null ) {
            verboseOutput.setSeparator(' ');
        }
        
        Calculation calculation = new Calculation(first, trash, verboseOutput);
        calculation.run();
        //calculation.run();
        
        return calculation;
    }
    
    
    private TokenList () {
        trash = new Trash();
        
        initialised = false;
        
        output = new Output();
    }
}
