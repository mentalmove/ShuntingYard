//
//  main.c
//  ShuntingYard
//
//  Created by Malte Pagel
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>

#define MAX_TERM_LENGTH 2048

typedef unsigned int uint;


struct token {
	char op;
	char token_type;
	char *stringed_number;
	uint stringed_number_counter;
	double numeric_value;
	
	struct token *next;
};


double string_to_double(char*);
void delete_tokens(struct token**);
void generate_new_token(struct token**, struct token**);
struct token* to_tokens(char*);
uint operator_precedence(char);
struct token* shunting_yard(struct token*);
double make_calculation(double, double, char);
struct token* calculate_polish(struct token*);

void display_infix(struct token*);
void display_postfix(struct token*);


double string_to_double (char* stringed_number) {
	
	long integer_part = 0;
	double fractal_part = 0.0;
	
	uint start = (stringed_number[0] == '-') ? 1 : 0;
	uint i;

	int point = -1;
	for ( i = start; i < strlen(stringed_number); i++ ) {
		if ( point >= 0 ) {
			if ( stringed_number[i] != '.' )
				point++;
			continue;
		}
		if ( stringed_number[i] == '.' ) {
			point = 0;
			continue;
		}
		integer_part *= 10;
		integer_part += stringed_number[i] - 48;
	}
	
	i = (uint) strlen(stringed_number) - 1;
	while ( point > 0 ) {
		if ( stringed_number[i] == '.' && i > 1 )
			i--;
		fractal_part += (stringed_number[i] - 48) * pow(10, 0 - point);
		i--;
		point--;
	}
	
	fractal_part += integer_part;
	
	if ( start )
		fractal_part *= -1;
	
	return fractal_part;
}
void delete_tokens (struct token** first) {
	struct token *tmp = *first;
	while ( tmp ) {
		tmp = tmp->next;
		free(*first);
		*first = tmp;
	}
}
void generate_new_token (struct token** first, struct token** last) {

	struct token *tmp = malloc(sizeof(struct token));
	tmp->token_type = 'n';
	tmp->stringed_number = NULL;
	tmp->stringed_number_counter = 0;
	tmp->numeric_value = 0.0;
	tmp->next = NULL;
	
	if ( *first ) {
		(*last)->next = tmp;
		*last = (*last)->next;
	}
	else {
		*first = tmp;
		*last = tmp;
	}
}
struct token* to_tokens (char *term) {
	
	struct token *first = NULL;
	struct token *last = NULL;
	
	int bracket_counter = 0;
	int number_counter = 0;
	struct token *tmp = NULL;
	
	char actual_token_type = 'o';
	for ( uint i = 0; i < strlen(term); i++ ) {
		
		if ( (term[i] < 48 || term[i] > 57) && term[i] != '.' && term[i] != '+' && term[i] != '-' && term[i] != '*' && term[i] != '/' && term[i] != '(' && term[i] != ')' )
			continue;
		
		if ( last )
			actual_token_type = last->token_type;
		
		if ( (term[i] >= 48 && term[i] <= 57) || term[i] == '.' || (term[i] == '-' && (actual_token_type == 'o' || actual_token_type == '(')) ) {
			
			if ( actual_token_type != 'n' ) {
				generate_new_token(&first, &last);
				number_counter++;
				if ( number_counter != 1 ) {
					delete_tokens(&first);
					generate_new_token(&first, &last);
					return first;
				}
			}
			
			last->stringed_number_counter++;
			if ( last->stringed_number_counter == 1 )
				last->stringed_number = malloc(2 * sizeof(char));
			else
				last->stringed_number = realloc(last->stringed_number, (last->stringed_number_counter + 1) * sizeof(char));
			last->stringed_number[last->stringed_number_counter - 1] = term[i];
			last->stringed_number[last->stringed_number_counter] = '\0';
			continue;
		}
		
		generate_new_token(&first, &last);
		
		if ( term[i] == '+' || term[i] == '-' || term[i] == '*' || term[i] == '/' ) {
			number_counter--;
			if ( number_counter ) {
				delete_tokens(&first);
				generate_new_token(&first, &last);
				return first;
			}
			last->op = term[i];
			last->token_type = 'o';
			continue;
		}
		
		if ( term[i] == '(' || term[i] == ')' ) {
			if ( actual_token_type == 'n' && term[i] == '(' ) {
				tmp = first;
				while ( tmp->next && tmp->next != last )
					tmp = tmp->next;
				if ( tmp->token_type == 'n' && tmp->stringed_number_counter == 1 && tmp->stringed_number[0] == '-' ) {
					tmp->stringed_number_counter++;
					tmp->stringed_number = realloc(tmp->stringed_number, 3 * sizeof(char));
					tmp->stringed_number[1] = '1';
					tmp->stringed_number[2] = '\0';
					last->op = '*';
					last->token_type = 'o';
					number_counter--;
					generate_new_token(&first, &last);
				}
			}
			last->op = term[i];
			last->token_type = term[i];
			bracket_counter += (term[i] == '(') ? 1 : -1;
			if ( bracket_counter < 0 ) {
				delete_tokens(&first);
				generate_new_token(&first, &last);
				return first;
			}
		}
	}
	
	if ( bracket_counter != 0 ) {
		delete_tokens(&first);
		generate_new_token(&first, &last);
		return first;
	}
	
	tmp = first;
	while ( tmp ) {
		if ( tmp->token_type == 'n' ) {
			tmp->numeric_value = string_to_double(tmp->stringed_number);
			free(tmp->stringed_number);
		}
		tmp = tmp->next;
	}
	
	if ( !first )
		generate_new_token(&first, &last);
	
	return first;
}

uint operator_precedence (char op) {
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
struct token* shunting_yard (struct token* given) {
	
	struct token *first_output = NULL;
	struct token *last_output = NULL;
	struct token *last_operator = NULL;
	
	struct token *next = NULL;
	struct token *tmp = NULL;
	
	while ( given ) {
		next = given->next;
		given->next = NULL;
		
		if ( given->token_type == 'n' ) {
			if ( first_output ) {
				last_output->next = given;
				last_output = last_output->next;
			}
			else {
				first_output = given;
				last_output = given;
			}
		}
		/**
		 * Operators run in reverse order;
		 * renaming the property 'next' to 'previous' would be suitable
		 * under these circumstances (but too expensive)
		 */
		if ( given->token_type == 'o' ) {
			while ( last_operator && operator_precedence(last_operator->op) >= operator_precedence(given->op) ) {
				tmp = last_operator;
				last_operator = last_operator->next;
				last_output->next = tmp;
				last_output = last_output->next;
			}
			if ( last_operator ) {
				given->next = last_operator;
				last_operator = given;
			}
			else
				last_operator = given;
		}
		if ( given->token_type == '(' ) {
			if ( last_operator ) {
				given->next = last_operator;
				last_operator = given;
			}
			else
				last_operator = given;
		}
		if ( given->token_type == ')' ) {
			while ( last_operator && last_operator->token_type != '(' ) {
				tmp = last_operator;
				last_operator = last_operator->next;
				last_output->next = tmp;
				last_output = last_output->next;
			}
			if ( last_operator ) {
				tmp = last_operator;
				last_operator = last_operator->next;
				free(tmp);
			}
			free(given);
		}
		
		given = next;
	}
	
	while ( last_operator ) {
		tmp = last_operator;
		last_operator = last_operator->next;
		last_output->next = tmp;
		last_output = last_output->next;
	}
	
	/**
	 * If the last token was a bracket,
	 * it does not exist any more
	 */
	last_output->next = NULL;
	
	return first_output;
}

double make_calculation (double first, double second, char op) {
	switch (op) {
		case '+':
			return (first + second);
		case '-':
			return (first - second);
		case '*':
			return (first * second);
		default:
			if ( !second )
				return 0;
			return (first / second);
	}
}
struct token* calculate_polish (struct token* calculated) {
	
	struct token *first, *second, *op;
	
	first = calculated;
	second = calculated->next;
	op = second->next;
	
	while ( calculated->next ) {
		if ( second->token_type != 'n' ) {
			first = calculated;
			second = first->next;
			op = second->next;
		}
		while ( op->token_type != 'o' ) {
			first = second;
			second = op;
			op = op->next;
		}
		first->numeric_value = make_calculation(first->numeric_value, second->numeric_value, op->op);
		first->next = op->next;
		free(second);
		free(op);
		second = first->next;
		if ( second )
			op = second->next;
	}
	
	return calculated;
}

void display_infix (struct token *tmp) {
	while ( tmp ) {
		switch (tmp->token_type) {
			case 'o':
				printf(" %c ", tmp->op);
			break;
			case '(':
			case ')':
				printf("%c", tmp->op);
			break;
			default:
				if ( tmp->numeric_value == (int) tmp->numeric_value )
					printf("%d", (int) tmp->numeric_value);
				else
					printf("%f", tmp->numeric_value);
		}
		tmp = tmp->next;
	}
}
void display_postfix (struct token *tmp) {
	while ( tmp ) {
		if ( tmp->token_type == 'n' ) {
			if ( tmp->numeric_value == (int) tmp->numeric_value )
				printf("%d ", (int) tmp->numeric_value);
			else
				printf("%f ", tmp->numeric_value);
		}
		else
			printf("%c ", tmp->op);
		
		tmp = tmp->next;
	}
}


int main (int argc, const char * argv[]) {
	
	char raw_term[MAX_TERM_LENGTH];
	
	printf( "\n" );
	
	if ( argc > 1 ) {
		for ( uint i = 1; i < argc; i++ ) {
			if ( i == 1 )
				strcpy(raw_term, argv[i]);
			else {
				strcat(raw_term, argv[i]);
			}
		}
	}
	else {
		fgets(raw_term, MAX_TERM_LENGTH, stdin);
		printf( "\n" );
	}
	
	printf( "\n" );
	
	struct token *tokens = to_tokens(raw_term);
	display_infix(tokens);
	
	printf( "\n\n" );
	
	tokens = shunting_yard(tokens);
	display_postfix(tokens);
	
	printf( "\n\n" );
	
	struct token *result = calculate_polish(tokens);
	if ( result->numeric_value == (int) result->numeric_value )
		printf( "Result: %d", (int) result->numeric_value );
	else
		printf( "Result: %f", result->numeric_value );
	
	printf( "\n\n" );
	
	
	delete_tokens(&tokens);
	
	
	return 0;
}
