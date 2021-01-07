package yard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import yard.hunting.s.Output.Output;
import yard.hunting.s.ShuntingYard.Calculation;

import yard.hunting.s.TokenHandling.TokenList;
import yard.hunting.s.Validators.Validator;

public class Yard {

    public Yard (String term) {
        
        // Output.verbosity = Output.VerbosityMode.VERBOSE;
        
        TokenList tokenList = TokenList.getInstance();
        
        tokenList.initialise(term);
        tokenList.show();
        
        tokenList.convert();
        tokenList.show();
        
        Validator validator = tokenList.runValidator(Validator.Mode.ALL);
        validator.tell();
        System.out.println( "Validation: " + validator.result() );
        if ( validator.result() != Validator.Evaluation.VALID ) {
            return;
        }
        
        tokenList.translate();
        
        Calculation result = tokenList.calculate();
        result.show();
    }
    
    public static void main(String[] args) throws IOException {
        
        String term = "";
        
        if ( term.equals("") ) {
            if ( args.length > 0 ) {
                for ( int i = 0; i < args.length; i++ ) {
                    if ( i == 0 ) {
                        term = args[i];
                    }
                    else {
                        term += " " + args[i];
                    }
                }
            }
            else {
                System.out.println( "\t Enter term:" );
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                term = in.readLine();
            }
        }
        
        new Yard(term);
    }
}
