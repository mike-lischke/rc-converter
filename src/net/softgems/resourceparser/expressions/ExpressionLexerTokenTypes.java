// $ANTLR 2.7.4: "Expression parser.g" -> "ExpressionLexer.java"$

/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.expressions;

import java.io.StringReader;
import java.util.ArrayList;

import antlr.collections.AST;

import net.softgems.resourceparser.main.IParseEventListener;

public interface ExpressionLexerTokenTypes {
	int EOF = 1;
	int NULL_TREE_LOOKAHEAD = 3;
	int FLOAT_LITERAL = 4;
	int DOUBLE_LITERAL = 5;
	int BIGINT_LITERAL = 6;
	int LONG_LITERAL = 7;
	int INTEGER_LITERAL = 8;
	int SHORT_LITERAL = 9;
	int BYTE_LITERAL = 10;
	int EXPR = 11;
	int UNARY_MINUS = 12;
	int UNARY_PLUS = 13;
	int POST_INC = 14;
	int POST_DEC = 15;
	int UNICODE_LETTER = 16;
	int UNICODE_CHARACTER_DIGIT = 17;
	int LETTER = 18;
	int LETTER_OR_DIGIT = 19;
	int LEFT_PARENTHESE = 20;
	int RIGHT_PARENTHESE = 21;
	int LEFT_BRACE = 22;
	int RIGHT_BRACE = 23;
	int LEFT_BRACKET = 24;
	int RIGHT_BRACKET = 25;
	int SEMICOLON = 26;
	int COMMA = 27;
	int APOSTROPHE = 28;
	int DOT = 29;
	int COLON = 30;
	int LOGICAL_AND = 31;
	int LOGICAL_OR = 32;
	int BITWISE_AND = 33;
	int BITWISE_OR = 34;
	int LOGICAL_NOT = 35;
	int BITWISE_NOT = 36;
	int BITWISE_XOR = 37;
	int PLUS = 38;
	int MINUS = 39;
	int STAR = 40;
	int DIV = 41;
	int MOD = 42;
	int SHIFT_LEFT = 43;
	int SHIFT_RIGHT = 44;
	int EQUAL = 45;
	int UNEQUAL = 46;
	int LESS_THAN = 47;
	int GREATER_THAN = 48;
	int LESS_THAN_EQUAL = 49;
	int GREATER_THAN_EQUAL = 50;
	int INC = 51;
	int DEC = 52;
	int ASSIGN = 53;
	int HORIZONTAL_TABULATOR = 54;
	int VERTICAL_TABULATOR = 55;
	int LINE_FEED = 56;
	int FORM_FEED = 57;
	int CARRIAGE_RETURN = 58;
	int SPACE = 59;
	int LINE_SEPARATOR = 60;
	int PARAGRAPH_SEPARATOR = 61;
	int DIGIT = 62;
	int HEX_DIGIT = 63;
	int OCTAL_DIGIT = 64;
	int ZERO_TO_THREE = 65;
	int OCTAL_NUMERAL = 66;
	int EXPONENT_PART = 67;
	int FLOAT_SUFFIX = 68;
	int WHITE_SPACE = 69;
	int STRING_CHARACTER = 70;
	int CHARACTER_LITERAL = 71;
	int STRING_LITERAL_PART = 72;
	int STRING_LITERAL = 73;
	int NUMERAL = 74;
	int IDENTIFIER = 75;
	int HEX_LITERAL = 76;
	int OCTAL_LITERAL = 77;
	int LITERAL_not = 78;
	int LITERAL_true = 79;
	int LITERAL_false = 80;
}
