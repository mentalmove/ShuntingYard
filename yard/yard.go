package main

import (
	"fmt"
	"os"
	"bufio"
	"shunting/tokenise"
	"yard/shunting"
)

func listTokens [T tokenise.GeneralToken] (tokens []T, title ...string) {
	fmt.Println( "" )

	if len(title) > 0 {
		fmt.Println( title[0] )
	}
	
	for i := 0; i < len(tokens); i++ {
		if i != 0 && tokens[i - 1].GetLiteral() != "(" && tokens[i].GetLiteral() != ")" {
			fmt.Print( " " )
		}
		if tokens[i].GetLiteral() != "" {
			fmt.Print( tokens[i].GetLiteral() )
		} else {
			fmt.Print( tokens[i].GetNumeric() )
		}
	}
	fmt.Println( "" )
}

func main() {

	raw := ""

	args := os.Args[1:]
	if len(args) != 0 {
		for i := 0; i < len(args); i++ {
			raw += args[i]
		}
	} else {
		fmt.Println( "Enter term:" )
		reader := bufio.NewReader(os.Stdin)
		text, _ := reader.ReadString('\n')
		raw = text
	}

	if raw == "" {
		return
	}

	infix := tokenise.Apply(raw)

	if len(infix) == 0 {
		return
	}

	listTokens(infix, "Infix:")

	valid := tokenise.Validate(infix)
	if !valid {
		return
	}
	
	shunting.Apply(infix)
	if len(shunting.Output) == 0 {
		return
	}
	listTokens(shunting.Output, "Postfix:")

	fmt.Println( "" )
	fmt.Printf( "Result: %v", shunting.Calculate() )
	fmt.Println( "\n" )
}
