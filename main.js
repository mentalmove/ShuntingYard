var VERBOSE = 1;                                                                // 0 - None; 1 - Reverse polish Notation; 2 - Function Content

function ShuntingYardHelper (output) {
    
    var property;
    
    function plus (index) {
        output[index] = output[index - 2] + output[index - 1];
    }
    function minus (index) {
        output[index] = output[index - 2] - output[index - 1];
    }
    function multiplicate (index) {
        output[index] = output[index - 2] * output[index - 1];
    }
    function divide (index) {
        if ( output[index - 1] )
            output[index] = output[index - 2] / output[index - 1];
        else {
            if ( !output[index - 2] )
                output[index] = 0;
            else
                output[index] = (output[index - 2] > 0) ? Number.MAX_VALUE : (-1 * Number.MAX_VALUE);
        }
    }
    var arithmetics = {
        "+": plus,
        "-": minus,
        "*": multiplicate,
        "/": divide
    };
    for ( property in arithmetics ) {
        arithmetics[property] = (function (former_fnc) {
            return (function (index) {
                former_fnc(index);
                output.splice(index - 2, 2);
            });
        })(arithmetics[property]);
    }
    
    var operators = [];
    operators.fnc_pop = function () {
        return arithmetics[operators.pop()];
    };
    
    var operator_precedence = {
        "+": 2,
        "-": 2,
        "*": 3,
        "/": 3,
        "redundant": 0
    };
    
    function real_operator (token) {
        while ( operators.length && operator_precedence[operators[operators.length - 1]] >= operator_precedence[token] )
            output.push(operators.fnc_pop());
        operators.push(token);
    }
    
    function open_bracket () {
        operators.push("(");
    }
    function closing_bracket () {
        while ( operators[operators.length - 1] != "(" )
            output.push(operators.fnc_pop());
        operators.pop();
    }
    
    var token_functions = {
        "(": open_bracket,
        ")": closing_bracket,
        "+": function(){real_operator("+")},
        "-": function(){real_operator("-")},
        "*": function(){real_operator("*")},
        "/": function(){real_operator("/")}
    };
    for ( property in token_functions )
        this[property] = token_functions[property];
    
    this.push_remaining_operators = function () {
        real_operator("redundant");
    };
    
    /**
     * No functionality - debugging purpose
     */
    this.recompile = function (very_verbose) {
        
        var s = "";
        var replacements = {
            "+": plus,
            "-": minus,
            "*": multiplicate,
            "/": divide
        };
        for ( var property in replacements )
            replacements[property] = "function (index) {"
                + replacements[property].toString().replace(/\s+/g, " ").replace(/[^\{]+\{/, "").replace(/\}[^\{]?$/, "")
                + "output.splice(index - 2, 2); }\n";
        for ( var i = 0; i < output.length; i++ ) {
            if ( i )
                s += " ";
            if ( !isNaN(output[i]) ) {
                s += output[i];
                continue;
            }
            for ( property in arithmetics ) {
                if ( output[i] == arithmetics[property] ) {
                    if ( very_verbose )
                        s += replacements[property];
                    else
                        s += property;
                    break;
                }
            }
        }

        return s;
    };
}

function calculate_polish (tokens) {
    
    var index = 2;
    while ( tokens.length > 2 ) {
        while ( !isNaN(tokens[index]) )
            index++;
        tokens[index](index);
        index--;
    }
    
    return tokens[0];
}
function shunting_yard (infix_tokens) {
    
    var output = [];
    
    var helper = new ShuntingYardHelper(output);
    
    var token;
    for ( var i = 0; i < infix_tokens.length; i++ ) {
        token = infix_tokens[i];
        if ( !isNaN(token) ) {
            output.push(token);
            continue;
        }
        helper[token]();
    }
    
    helper.push_remaining_operators();
    
    /**
     * No functionality - debugging purpose
     */
    if ( VERBOSE ) {
        console.log( helper.recompile(false) );
        if ( VERBOSE == 2 )
            console.log( helper.recompile(true) );
    }
    
    return output;
}

function validate (tokens) {
    
    var number_counter = 0;
    var bracket_counter = 0;
    var first_number = 0;
    
    var token;
    for ( var i = 0; i < tokens.length; i++ ) {
        token = tokens[i];
        if ( token == "(" ) {
            bracket_counter++;
            continue;
        }
        if ( token == ")" ) {
            bracket_counter--;
            if ( bracket_counter < 0 )
                return [ first_number ];
            continue;
        }
        if ( !isNaN(token) ) {
            if ( !first_number )
                first_number = token;
            number_counter++;
            if ( number_counter != 1 )
                return [ first_number ];
            continue;
        }
        number_counter--;
        if ( number_counter )
            return [ first_number ];
    }
    
    if ( bracket_counter )
        return [ first_number ];
    
    return (number_counter == 1) ? tokens : [ first_number ];
}
function to_tokens (raw_term) {
    
    var term = raw_term.replace(/[^\d\(\)+\-\*\/\.]/g, "");
    term = term.replace(/^\-\(/, "m1*(");
    term = term.replace(/^\-/, "m");
    term = term.replace(/([+\-\*\/\(])(\-)(\d)/g, "$1m$3");
    term = term.replace(/([+\-\*\/\(])(\-\()/g, "$1m1*(");
    term = term.replace(/([+\-\*\/\(\)])/g, " $1 ");
    term = term.replace(/^\s+/, "");
    term = term.replace(/\s+$/, "");
    term = term.replace(/\s+/g, " ");
    term = term.replace(/m/g, "-");
    
    var src = term.split(" ");
    for ( var i = 0; i < src.length; i++ ) {
        if ( !isNaN(src[i]) )
            src[i] = parseFloat(src[i]);
    }
    
    return validate(src);
}

/*  */

function main (display_original) {
    
    console.log( "" );
    if ( display_original ) {
        console.log( term );
        console.log( "" );
    }
    
    var tokens = to_tokens(term);
    console.log( tokens.join(" ").replace(/\(\s?/g, "(").replace(/\s?\)/g, ")") );
    
    console.log( "" );
    
    var polish = shunting_yard(tokens);
    
    console.log( "" );
    
    var result = calculate_polish(polish);
    console.log( "Result: " + result );

    console.log( "" );
}


var term = "";
for ( var i = 2; i < process.argv.length; i++ )
	term += process.argv[i];

if ( term )
    main(true);
else {
    console.log( "" );
    const readline = require("readline");
    const rl = readline.createInterface({
        input: process.stdin,
    });
    rl.on("line", (input) => {
        term = input;
        rl.close();
        main(false);
    });
}
