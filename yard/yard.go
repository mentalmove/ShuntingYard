package main

import (
	"fmt"
	"os"
	"bufio"
	"shunting/tokenise"
	"yard/shunting"
)

func listTokens (tokens []tokenise.Token, title ...string) {
	fmt.Println( "" )

	if len(title) > 0 {
		fmt.Println( title[0] )
	}
	
	for i := 0; i < len(tokens); i++ {
		if i != 0 && tokens[i - 1].Literal != "(" && tokens[i].Literal != ")" {
			fmt.Print( " " )
		}
		if tokens[i].Literal != "" {
			fmt.Print( tokens[i].Literal )
		} else {
			fmt.Print( tokens[i].Numeric )
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
	
	postfix := shunting.Apply(infix)
	if len(postfix) == 0 {
		return
	}
	listTokens(postfix, "Postfix:")

	fmt.Println( "" )
	fmt.Printf( "Result: %v", shunting.Calculate() )
	fmt.Println( "\n" )
}
