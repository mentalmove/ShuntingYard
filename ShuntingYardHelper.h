//
//  ShuntingYardHelper.hpp
//  ShuntingYard
//
//  Created by Malte Pagel
//

#ifndef SHUNTINGYARDHELPER_H
#define	SHUNTINGYARDHELPER_H

#include <iostream>
#include <vector>
using namespace std;


struct Token {
	char op;
	string value;
};


class ShuntingYardHelper {
	
public:
	double result = 0.0;
	string polish = "";
	
	void pushNumber(string);
	void pushOperator(char);
	void terminate(void);
	
	ShuntingYardHelper(){};
	
private:
	vector<double> output;
	vector<char> operators;
	
	uint precedence(char);
	void calculate(char);
};


#endif	/* SHUNTINGYARDHELPER_H */
