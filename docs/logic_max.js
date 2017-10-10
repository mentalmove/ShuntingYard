(function (docu_ment, bo_dy) {
    function make_calculation (first, second, operator) {
        switch ( operator ) {
            case "+":
                return (first + second);
            break;
            case "-":
                return (first - second);
            break;
            case "*":
                return (first * second);
            break;
            case "/":
                if ( !second ) {
                    if ( !first )
                        return 0;
                    if ( first > 0 )
                        return Number.MAX_VALUE;
                    return (-1 * Number.MAX_VALUE);
                }
                return (first / second);
        }
    }
    function calculate_polish (stack) {

        var index = 0;
        while ( stack.length > 1 ) {
            while ( !isNaN(stack[index]) )
                index++;
            stack[index] = make_calculation(stack[index - 2], stack[index - 1], stack[index]);
            stack.splice(index - 2, 2);
            index -= 2;
        }

        return stack[0];
    }

    function shunting_yard (src) {

        var operator_precedence = {
            "+": 2,
            "-": 2,
            "*": 3,
            "/": 3
        };

        var output = [];
        var operators = [];

        var token;
        while ( src.length ) {
            token = src.shift();
            if ( !isNaN(token) ) {
                output.push(token);
                continue;
            }
            if ( operator_precedence[token] ) {
                while ( operators.length && operator_precedence[operators[operators.length - 1]] >= operator_precedence[token] )
                    output.push(operators.pop());
                operators.push(token);
                continue;
            }
            if ( token == "(" ) {
                operators.push(token);
                continue;
            }
            if ( token == ")" ) {
                while ( operators[operators.length - 1] != "(" )
                    output.push(operators.pop());
                operators.pop();
            }
        }
        while ( operators.length )
            output.push(operators.pop());

        return output;
    }

    var src = [];

    function iD (x) {
        return docu_ment.getElementById(x);
    }

    function validate (src, partial) {

        if ( src.length < 3 )
            return 0;

        var i;

        var tmp = [];
        var bracket_counter = 0;
        var operator_counter = 0;
        var number_counter = 0;
        for ( i = 0; i < src.length; i++ ) {
            if ( src[i] == "(" )
                bracket_counter++;
            if ( src[i] == ")" )
                bracket_counter--;
            if ( !isNaN(src[i]) )
                number_counter++;
            else
                if ( src[i].match(/[+\-\*\/]/) )
                    operator_counter++;

            if ( bracket_counter < 0 )
                return 0;
            if ( (number_counter - operator_counter) && (number_counter - operator_counter) != 1 )
                return 0;

            tmp.push(src[i]);
        }

        if ( (number_counter - operator_counter) != 1 ) {
            if ( partial ) {
                for ( i = tmp.length - 1; i > 1; i-- ) {
                    if ( isNaN(tmp[i]) && tmp[i].match(/[+\-\*\/]/) ) {
                        var additional = (tmp[i].match(/[+\-]/)) ? 0 : 1;
                        tmp.splice((i + 1), 0, additional);
                        break;
                    }
                }
            }
            else
                return 0;
        }

        if ( bracket_counter ) {
            if ( partial ) {
                for ( i = tmp.length - 1; i >= 0; i-- ) {
                    if ( tmp[i] == "(" ) {
                        tmp.splice(i, 1);
                        bracket_counter--;
                        if ( !bracket_counter )
                            break;
                    }
                }
            }
            else
                return 0;
        }

        for ( i = 0; i < tmp.length; i++ ) {
            if ( !isNaN(tmp[i]) )
                tmp[i] = parseFloat(tmp[i]);
        }

        return tmp;
    }

    var replacements = {
        "÷": "/",
        "c": "C",
        "AC": "C",
        "Backspace": "C",
        ",": ".",
        "x": "*"
    };
    var actual_token = "operator";
    function user_action (character, down) {
        if ( !down ) {
            buttons[character].style.backgroundColor = "silver";
            return;
        }

        buttons[character].style.backgroundColor = "gray";

        var token = replacements[character] || character;

        if ( actual_token == "finished" ) {
            if ( token != "C" )
                return;
            iD("calc_result").innerHTML = ".&nbsp;";
            iD("infix").innerHTML = "";
            iD("postfix").innerHTML = "";
            src = [];
            actual_token = "operator";
            iD("clear_it").innerHTML = "C";
        }

        var i;
        if ( token == "=" && src.length > 2 ) {
            for ( i = 0; i < src.length; i++ ) {
                if ( !isNaN(src[i]) )
                    src[i] = parseFloat(src[i]);
            }
            if ( !validate(src, 0) )
                return;
            var polish = shunting_yard(src);
            iD("postfix").innerHTML = polish.join(" ");
            iD("calc_result").innerHTML = calculate_polish(polish) + "&nbsp;";
            actual_token = "finished";
            iD("clear_it").innerHTML = "AC";
            return;
        }

        if ( token == "C" && src.length ) {
            src.pop();
            if ( !src.length ) {
                actual_token = "operator";
                iD("calc_result").innerHTML = ".&nbsp;";
            }
        }
        else {
            if ( token == "-" ) {
                src.push("-");
                if ( actual_token == "operator" || actual_token == "(" )
                    actual_token = "number";
                else
                    actual_token = "operator";
            }
            else {
                if ( token == "(" || token == ")" ) {
                    actual_token = token;
                    src.push(token);
                }
                else {
                    if ( token.match(/[+\*\/]/) ) {
                        actual_token = "operator";
                        src.push(token);
                    }
                    else {
                        if ( token.match(/[\d\.]/) ) {
                            if ( token == "." ) {
                                if ( actual_token != "number" )
                                    token = "0.";
                                else {
                                    if ( src[src.length - 1].toString().match(/\./) )
                                        return;
                                }
                            }
                            if ( actual_token != "number" )
                                src.push(token);
                            else
                                src[src.length - 1] += token;
                            actual_token = "number";
                        }
                    }
                }
            }
        }

        if ( src.length )
            iD("calc_result").innerHTML = src[src.length - 1] + "&nbsp;";
        iD("infix").innerHTML = src.join(" ").replace(/\(\s?/g, "(").replace(/\s?\)/g, ")");

        var val = validate(src, 1);
        if ( !val )
            return;

        var tmp_polish = shunting_yard(val);
        iD("postfix").innerHTML = tmp_polish.join(" ");

        if ( actual_token != "(" && actual_token != "number" ) {
            for ( i = 0; i < src.length; i++ ) {
                if ( src[i] == "(" ) {
                    iD("calc_result").innerHTML = intermediate_result(src) + "&nbsp;";
                    return;
                }
            }
            iD("calc_result").innerHTML = calculate_polish(tmp_polish) + "&nbsp;";
        }
    }

    function intermediate_result (src) {

        var tmp = [];
        var bracket_counter = 0;
        var n;
        for ( var i = src.length - 1; i >= 0; i-- ) {
            n = (!isNaN(src[i])) ? parseFloat(src[i]) : src[i];
            if ( n == ")" )
                bracket_counter++;
            if ( n == "(" ) {
                bracket_counter--;
                if ( !bracket_counter ) {
                    tmp.unshift(n);
                    break;
                }
                if ( bracket_counter < 0 )
                    return src[src.length - 1];
            }
            tmp.unshift(n);
        }

        if ( tmp.length == 1 )
            return tmp[0];

        var val = validate(tmp, 1);
        if ( !val )
            return src[src.length - 1];

        var tmp_polish = shunting_yard(val);

        return calculate_polish(tmp_polish);
    }

    var i;
    var tmp_buttons = docu_ment.getElementsByClassName("char");
    var buttons = {};
    var content, f;
    for ( i = 0; i < tmp_buttons.length; i++ ) {
        content = tmp_buttons[i].innerHTML;
        buttons[content] = tmp_buttons[i];
        tmp_buttons[i].onmousedown = function () {
            user_action(this.innerHTML, 1);
        };
        tmp_buttons[i].onmouseup = function () {
            user_action(this.innerHTML, 0);
        };
    }
    buttons["/"] = buttons["÷"];
    buttons["*"] = buttons["x"];
    buttons["c"] = buttons["C"];
    buttons["AC"] = buttons["C"];
    buttons[","] = buttons["."];
    buttons["Backspace"] = buttons["C"];

    docu_ment[bo_dy].onkeydown = function (ev) {
        if ( !buttons[ev.key] ) 
            return;
        ev.preventDefault();
        user_action(ev.key, 1);
    };
    docu_ment[bo_dy].onkeyup = function (ev) {
        if ( !buttons[ev.key] ) 
            return;
        user_action(ev.key, 0);
    };
})(document, "body");