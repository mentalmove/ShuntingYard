//
//  ReversePolishNotation.swift
//  ShuntingYard
//
//  Created by Malte Pagel
//

struct Token {
	var op: Character = Character(" ")
	var token_type: String = String()
	var stringed_number: String = String()
	var numeric_value: Double = Double()
}

class ReversePolishNotation {
	
	private var tokens = [Token]()
	private var polish_tokens = [Token]()
	
	public var result: Double = 0
	
	
	func to_list (term: String) -> [Token] {
		
		var tokens = [Token]()
		
		var bracket_counter = 0
		var number_counter = 0
		
		var actual_token_type = "operator"
		for char in term.characters {
			
			if !"0123456789.+-*/()".contains(char) {
				continue;
			}
			if tokens.count != 0 {
				actual_token_type = tokens[tokens.count - 1].token_type
			}
			
			if "0123456789.".contains(char) || (char == "-" && (actual_token_type == "operator" || actual_token_type == "(")) {
				if actual_token_type != "number" {
					tokens.append( Token() )
					number_counter += 1
					if ( number_counter != 1 ) {
						return [Token]()
					}
				}
				else {
					if char == "." && tokens[tokens.count - 1].stringed_number.contains(".") {
						continue
					}
				}
				tokens[tokens.count - 1].stringed_number.append(char)
				tokens[tokens.count - 1].token_type = "number"
				continue
			}
			
			tokens.append( Token() )
			
			if "+-*/".contains(char) {
				tokens[tokens.count - 1].token_type = "operator"
				tokens[tokens.count - 1].op = char
				number_counter -= 1
				if ( number_counter != 0 ) {
					return [Token]()
				}
				continue
			}
			
			if char == "(" || char == ")" {
				tokens[tokens.count - 1].token_type = String(char)
				tokens[tokens.count - 1].op = char
				bracket_counter += (char == "(") ? 1 : -1
				if bracket_counter < 0 {
					return [Token]()
				}
			}
		}
		
		if ( number_counter != 1 ) {
			return [Token]()
		}
		if ( bracket_counter != 0 ) {
			return [Token]()
		}
		
		for i in 0...tokens.count - 1 {
			if tokens[i].token_type == "number" {
				if tokens[i].stringed_number == "-" {
					return [Token]()
				}
				tokens[i].numeric_value = Double(tokens[i].stringed_number)!
			}
		}
		
		return tokens
	}
	
	private func shunting_yard () -> [Token] {
		
		var output = [Token]()
		var operators = [Token]()
		
		let operator_precedence: [Character: Int] = [
			"+": 2,
			"-": 2,
			"*": 3,
			"/": 3
		]
		
		for token in tokens {
			if token.token_type == "number" {
				output.append(token)
				continue
			}
			if token.token_type == "operator" {
				while operators.count > 0 && operators[operators.count - 1].token_type == "operator" && operator_precedence[operators[operators.count - 1].op]! >= operator_precedence[token.op]! {
					output.append(operators.removeLast())
				}
				operators.append(token)
				continue
			}
			if token.token_type == "(" {
				operators.append(token)
				continue
			}
			if token.token_type == ")" {
				while operators[operators.count - 1].op != "(" {
					output.append(operators.removeLast())
				}
				operators.removeLast()
			}
		}
		
		while operators.count > 0 {
			output.append(operators.removeLast())
		}
		
		return output
	}
	
	private func make_calculation (first: Double, second: Double, op: Character) -> Double {
		switch op {
		case "+":
			return first + second
		case "-":
			return first - second
		case "*":
			return first * second
		default:
			if second == 0 {
				return 0
			}
			return first / second
		}
	}
	private func calculate_polish () -> Double {
		
		if polish_tokens.count < 3 {
			if polish_tokens.count > 0 && polish_tokens[0].token_type == "number" {
				return polish_tokens[0].numeric_value
			}
			return 0.0
		}
		
		var calculated = [Token]()
		for token in polish_tokens {
			calculated.append(token)
		}
		
		var index = 0
		while calculated.count > 1 {
			while calculated[index].token_type == "number" {
				index += 1
			}
			calculated[index].token_type = "number"
			calculated[index].numeric_value = make_calculation(first: calculated[index - 2].numeric_value, second: calculated[index - 1].numeric_value, op: calculated[index].op)
			calculated[(index - 2)...(index - 1)] = []
			index -= 1
		}
		
		return calculated[0].numeric_value
	}
	
	
	public func display_infix () {
		for token in self.tokens {
			if token.token_type == "operator" {
				print( " \(token.op) ", terminator: "" )
				continue
			}
			if token.token_type == "(" || token.token_type == ")" {
				print( "\(token.op)", terminator: "" )
				continue
			}
			if token.numeric_value == Double(Int(token.numeric_value)) {
				print( "\(Int(token.numeric_value))", terminator: "" )
			}
			else {
				print( "\(token.numeric_value)", terminator: "" )
			}
		}
		print( "" )
	}
	public func display_postfix () {
		for token in polish_tokens {
			if token.token_type == "operator" {
				print( "\(token.op)", terminator: "" )
			}
			else {
				if token.numeric_value == Double(Int(token.numeric_value)) {
					print( "\(Int(token.numeric_value))", terminator: "" )
				}
				else {
					print( "\(token.numeric_value)", terminator: "" )
				}
			}
			print( " ", terminator: "" )
		}
		print( "" )
	}
	public func show_result () -> String {
		
		let int_result = Int(result)
		
		if result == Double(int_result) {
			return String(int_result)
		}
		
		return String(result)
	}
	
	
	init (from: String) {
		tokens = to_list(term: from)
		polish_tokens = shunting_yard()
		result = calculate_polish()
	}
}
