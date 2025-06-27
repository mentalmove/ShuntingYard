package shunting

import (
	"strings"
	"shunting/tokenise"
)

var precedence = map[string]uint8 {
	"+": 2,
	"-": 2,
	"*": 3,
	"/": 3,
}

type ExtendedToken tokenise.Token
func (t ExtendedToken) GetNumeric () float64 {
	return t.Numeric
}
func (t ExtendedToken) GetLiteral () string {
	return t.Literal
}
func (t ExtendedToken) distribute (literal string) {
	var popped ExtendedToken
	if strings.ContainsAny(literal, "+-*/") && precedence[literal] != 0 {
		for len(operators) != 0 && precedence[operators[len(operators) - 1].Literal] >= precedence[literal] {
			popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
			Output = append(Output, popped)
		}
		operators = append(operators , t)
		return
	}
	switch literal {
		case "(":
			operators  = append(operators , t)
		case ")":
			for len(operators) != 0 && operators[len(operators) - 1].Literal != "(" {
				popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
				Output = append(Output, popped)
			}
			popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
		default:
			Output = append(Output, t)
	}
}
func (t *ExtendedToken) changeValue (operator string, value float64) {
	switch operator {
		case "+":
			t.Numeric += value
		case "-":
			t.Numeric -= value
		case "*":
			t.Numeric *= value
		case "/":
			if value == 0 {
				t.Numeric = 0
			} else {
				t.Numeric /= value
			}
	}
}

var Output []ExtendedToken
var operators []ExtendedToken

func Calculate () float64 {
	var resultTokens []ExtendedToken
	for i := 0; i < len(Output); i++ {
		if Output[i].Literal == "" {
			resultTokens = append(resultTokens, Output[i])
			continue
		}
		if len(resultTokens) > 1 {
			resultTokens[len(resultTokens)-2].changeValue(Output[i].Literal, resultTokens[len(resultTokens)-1].Numeric)
			_, resultTokens = resultTokens[len(resultTokens)-1], resultTokens[:len(resultTokens)-1]
		}
	}

	if len(resultTokens) > 0 {
		return resultTokens[0].Numeric
	}

	return 0
}

func Apply (tokens []tokenise.Token) {

	var token ExtendedToken

	for i := 0; i < len(tokens); i++ {
		token = ExtendedToken{tokens[i].Numeric, tokens[i].Literal}
		token.distribute(token.Literal)
	}
	var popped ExtendedToken
	for len(operators) != 0 {
		popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
		Output = append(Output, popped)
	}
}
