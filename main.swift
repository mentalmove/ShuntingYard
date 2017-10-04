//
//  main.swift
//  ShuntingYard
//
//  Created by Malte Pagel
//

import Foundation


var raw_term = String()


print( "" )

if CommandLine.argc > 1 {
	for i in 1...CommandLine.argc - 1 {
		raw_term += " " + CommandLine.arguments[Int(i)]
	}
}
else {
	raw_term = readLine()!
	print( "" )
}


var rpl:ReversePolishNotation = ReversePolishNotation(from: raw_term)

rpl.display_infix()

print( "" )

rpl.display_postfix()

print( "" )

print( "Result: \(rpl.show_result())" )

print( "" )
