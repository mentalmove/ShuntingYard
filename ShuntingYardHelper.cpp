//
//  ShuntingYardHelper.cpp
//  ShuntingYard
//
//  Created by Malte Pagel
//

#include "ShuntingYardHelper.h"


void ShuntingYardHelper::pushNumber (string stringed_value) {
	if ( polish != "" )
		polish += " ";
	polish += stringed_value;
	output.push_back(stod(stringed_value));
}

void ShuntingYardHelper::pushOperator (char op) {
	switch (op) {
		case '(':
			operators.push_back(op);
			break;
		case ')':
			while ( operators.back() != '(' ) {
				calculate(operators.back());
				operators.pop_back();
			}
			operators.pop_back();
			break;
		default:
			while ( operators.size() && precedence(operators.back()) >= precedence(op) ) {
				calculate(operators.back());
				operators.pop_back();
			}
			operators.push_back(op);
	}
}

void ShuntingYardHelper::terminate () {
	while ( operators.size() ) {
		calculate(operators.back());
		operators.pop_back();
	}
	
	result = output[0];
}

uint ShuntingYardHelper::precedence (char op) {
	switch (op) {
		case '+':
		case '-':
			return 2;
		case '*':
		case '/':
			return 3;
		default:
			return 0;
	}
}

void ShuntingYardHelper::calculate (char op) {
	
	polish += " ";
	polish += op;
	
	if ( output.size() < 2 )
		return;
	
	switch (op) {
		case '+':
			output.end()[-2] = output.end()[-2] + output.back();
			break;
		case '-':
			output.end()[-2] = output.end()[-2] - output.back();
			break;
		case '*':
			output.end()[-2] = output.end()[-2] * output.back();
			break;
		default:
			if ( !output.back() )
				output.end()[-2] = 0;
			else
				output.end()[-2] = output.end()[-2] / output.back();
	}
	
	output.pop_back();
}
