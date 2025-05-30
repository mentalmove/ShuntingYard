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
func (t ExtendedToken) distribute (literal string) {
	var popped ExtendedToken
	if strings.ContainsAny(literal, "+-*/") && precedence[literal] != 0 {
		for len(operators) != 0 && precedence[operators[len(operators) - 1].Literal] >= precedence[literal] {
			popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
			output = append(output, popped)
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
				output = append(output, popped)
			}
			popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
		default:
			output = append(output, t)
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

var output []ExtendedToken
var operators []ExtendedToken

func Calculate () float64 {
	var resultTokens []ExtendedToken
	for i := 0; i < len(output); i++ {
		if output[i].Literal == "" {
			resultTokens = append(resultTokens, output[i])
			continue
		}
		if len(resultTokens) > 1 {
			resultTokens[len(resultTokens)-2].changeValue(output[i].Literal, resultTokens[len(resultTokens)-1].Numeric)
			_, resultTokens = resultTokens[len(resultTokens)-1], resultTokens[:len(resultTokens)-1]
		}
	}

	if len(resultTokens) > 0 {
		return resultTokens[0].Numeric
	}

	return 0
}

func Apply (tokens []tokenise.Token) []tokenise.Token {

	var token ExtendedToken

	for i := 0; i < len(tokens); i++ {
		token = ExtendedToken{tokens[i].Numeric, tokens[i].Literal}
		token.distribute(token.Literal)
	}
	var popped ExtendedToken
	for len(operators) != 0 {
		popped, operators = operators[len(operators)-1], operators[:len(operators)-1]
		output  = append(output, popped)
	}

	// A simple bool (`success`) would be enough from a functional perspective
	var outputToShow []tokenise.Token
	for i := 0; i < len(output); i++ {
		outputToShow = append(outputToShow, tokenise.Token{output[i].Numeric, output[i].Literal})
	}
	return outputToShow
}
