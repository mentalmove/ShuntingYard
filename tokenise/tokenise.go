package tokenise

import (
	"fmt"
	"regexp"
	"strings"
	"strconv"
)

type Token struct {
	Numeric float64
	Literal string
}

func Validate (tokens []Token) bool {
	brackets := 0
	operators := 0
	operands := 0

	for i := 0; i < len(tokens); i++ {
		if tokens[i].Literal == "(" {
			brackets++;
			continue
		}
		if tokens[i].Literal == ")" {
			brackets--;
			if brackets < 0 {
				fmt.Println( "\t Superfluous closing bracket" )
				return false;
			}
			continue
		}
		if strings.ContainsAny(tokens[i].Literal, "+-*/") {
			operators++;
			continue
		}
		operands++;
	}

	if brackets != 0 {
		fmt.Println( "\t Bracket mismatch" )
		return false;
	}
	if operands - operators != 1 {
		if operands >= operators {
			fmt.Println( "\t Too many operands" )
		} else {
			fmt.Println( "\t Too many operators" )
		}
		fmt.Printf( "\t [%v operands, %v operators]", operands, operators )
		return false;
	}
	
	return true
}

func Apply (provided string) []Token {
	var tokens []Token
	var filtered []Token
	needsFilter := false

	regOnlyAllowed := regexp.MustCompile(`[^\d\.\+-\\*/\(\)]`)
	spaceless := regOnlyAllowed.ReplaceAllString(provided, "")
	if spaceless == "" {
		return tokens
	}

	regSplit := regexp.MustCompile(`[\d\.]+|[\+-\\*/\(\)]`)
	split := regSplit.FindAllString(spaceless, -1)
	if len(split) == 0 {
		return tokens
	}

	for i := 0; i < len(split); i++ {
		if strings.ContainsAny(split[i], "+*/()") {
			if split[i] == "(" && len(tokens) > 0 {
				if ( tokens[len(tokens) - 1].Literal == "" || tokens[len(tokens) - 1].Literal == ")" ) {
					tokens = append(tokens, Token{0, "*"})
				}
			}
			tokens = append(tokens, Token{0, split[i]})
			continue
		}
		if split[i] == "-" {
			if i == 0 {
				if split[1] == "(" && len(split) > 1 {
					tokens = append(tokens, Token{-1, ""})
					tokens = append(tokens, Token{0, "*"})
				} else {
					tokens = append(tokens, Token{0, "signum"})
					needsFilter = true
				}
				continue
			}
			if strings.ContainsAny(split[i - 1], "+-*/(") {
				if len(split) - 1 > i {
					if split[i + 1] == "(" {
						tokens = append(tokens, Token{-1, ""})
						tokens = append(tokens, Token{0, "*"})
					} else {
						tokens = append(tokens, Token{0, "signum"})
						needsFilter = true
					}
				}
				continue
			}
			tokens = append(tokens, Token{0, "-"})
			continue
		}
		f, error := strconv.ParseFloat(split[i], 64)
		if error != nil {
			return filtered
		}
		tokens = append(tokens, Token{f, ""})
	}

	if needsFilter {
		for i := 0; i < len(tokens); i++ {
			if tokens[i].Literal == "signum" {
				if len(tokens) - 1 > i && tokens[i + 1].Numeric != 0 {
					tokens[i + 1].Numeric *= -1;
				}
				continue
			}
			filtered = append(filtered, tokens[i])
		}
		return filtered
	}

	return tokens
}
