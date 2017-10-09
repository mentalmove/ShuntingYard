//
//  main.cpp
//  ShuntingYard
//
//  Created by Malte Pagel
//

#include <iostream>
#include <vector>
using namespace std;

#include "ShuntingYardHelper.h"

typedef unsigned int uint;


vector<Token> empty_tokens(void);
vector<Token> to_tokens(string);
void shunting_yard(vector<Token>);

void display_infix(vector<Token>);


ShuntingYardHelper calculator;


vector<Token> empty_tokens (void) {
	vector<Token> tokens;
	Token token = Token();
	token.op = 'n';
	token.value = "0";
	tokens.push_back(token);
	return tokens;
}
vector<Token> to_tokens (string term) {
	
	vector<Token> tokens;
	Token token;
	
	char actual_token_type = '+';
	
	int bracket_counter = 0;
	int number_counter = 0;
	
	uint j;
	unsigned short allowed;
	string allowed_characters = ".+-*/()";
	for ( uint i = 0; i < term.length(); i++ ) {
		
		if ( !isdigit(term[i]) ) {
			allowed = 0;
			for ( j = 0; j < allowed_characters.length(); j++ ) {
				if ( term[i] == allowed_characters[j] ) {
					allowed = 1;
					break;
				}
			}
			if ( !allowed )
				continue;
		}
		
		if ( !tokens.empty() )
			actual_token_type = tokens.back().op;
		
		if ( isdigit(term[i]) || term[i] == '.' || (term[i] == '-' && actual_token_type != 'n' && actual_token_type != ')') ) {
			if ( actual_token_type != 'n' ) {
				token = Token();
				token.op = 'n';
				token.value = term[i];
				tokens.push_back(token);
				number_counter++;
				if ( number_counter != 1 )
					return empty_tokens();
			}
			else
				tokens.back().value += term[i];
			continue;
		}
		
		if ( term[i] == '(' && actual_token_type == 'n' && tokens.back().value == "-" ) {
			tokens.back().value += '1';
			token = Token();
			token.op = '*';
			tokens.push_back(token);
			number_counter--;
			if ( number_counter )
				return empty_tokens();
		}
		
		token = Token();
		token.op = term[i];
		tokens.push_back(token);
		
		if ( term[i] == '(' )
			bracket_counter++;
		else {
			if ( term[i] == ')' ) {
				bracket_counter--;
				if ( bracket_counter < 0 )
					return empty_tokens();
			}
			else {
				number_counter--;
				if ( number_counter )
					return empty_tokens();
			}
		}
	}
	
	if ( bracket_counter || number_counter != 1 )
		return empty_tokens();
	
	return tokens;
}
void shunting_yard (vector<Token> tokens) {

	for ( uint i = 0; i < tokens.size(); i++ ) {
		if ( tokens[i].op == 'n' ) {
			calculator.pushNumber(tokens[i].value);
			continue;
		}
		calculator.pushOperator(tokens[i].op);
	}
	
	calculator.terminate();
}

void display_infix (vector<Token> tokens) {
	for ( uint i = 0; i < tokens.size(); i++ ) {
		if ( tokens[i].op == 'n' ) {
			cout << tokens[i].value;
			continue;
		}
		if ( tokens[i].op == '(' || tokens[i].op == ')') {
			cout << tokens[i].op;
			continue;
		}
		cout << " " << tokens[i].op << " ";
	}
	cout << endl;
}


int main (int argc, const char * argv[]) {
	
	string raw_term = "";
	
	cout << endl;
	
	if ( argc > 1 ) {
		for ( uint i = 1; i < argc; i++ )
			raw_term += argv[i];
	}
	else {
		while ( raw_term == "" )
			getline(cin, raw_term);
		 cout << endl;
	}
	
	vector<Token> tokens = to_tokens(raw_term);
	display_infix(tokens);
	
	cout << endl;
	
	shunting_yard(tokens);
	
	cout << calculator.polish << endl << endl;
	
	cout << "Result: " << calculator.result << endl << endl;
	
	
	tokens.erase(tokens.begin(), tokens.end());
	
	return 0;
}
