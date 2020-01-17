// $ANTLR 2.7.4: "Resource file parser.g" -> "RCParser.java"$

/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.main;

import java.util.ArrayList;

import net.softgems.resourceparser.main.*;

public interface RCParserTokenTypes {
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
	int CONCRETE_CONTROL = 81;
	int ACCELERATOR = 82;
	int RESOURCE_ATTRIBUTES = 83;
	int STRING_TABLE_ENTRY = 84;
	int DESIGN_INFO_CONTROL_BLOCK = 85;
	int NAMED_RESOURCE = 86;
	int RAW = 87;
	int FILE_NAME = 88;
	int USER_DEFINED = 89;
	int DESIGN_INFO_ENTRIES = 90;
	int COMMON_RESOURCE_INFO = 91;
	int VERSION_FIXED_INFO = 92;
	int ACCELERATOR_TYPE = 93;
	int ACCELERATOR_OPTION = 94;
	int LITERAL_pragma = 95;
	int LITERAL_code_page = 96;
	int LITERAL_default = 97;
	int LITERAL_begin = 98;
	int LITERAL_end = 99;
	int NUMBER_SIGN = 100;
	int LITERAL_preload = 101;
	int LITERAL_loadoncall = 102;
	int LITERAL_fixed = 103;
	int LITERAL_moveable = 104;
	int LITERAL_discardable = 105;
	int LITERAL_pure = 106;
	int LITERAL_impure = 107;
	int LITERAL_shared = 108;
	int LITERAL_nonshared = 109;
	int LITERAL_l = 110;
	int LITERAL_language = 111;
	int LITERAL_stringtable = 112;
	int PATH_SEPARATOR = 113;
	int LITERAL_characteristics = 114;
	int LITERAL_version = 115;
	int LITERAL_caption = 116;
	int LITERAL_class = 117;
	int LITERAL_style = 118;
	int LITERAL_exstyle = 119;
	int LITERAL_font = 120;
	int LITERAL_menu = 121;
	int LITERAL_accelerators = 122;
	int LITERAL_ascii = 123;
	int LITERAL_virtkey = 124;
	int LITERAL_noinvert = 125;
	int LITERAL_alt = 126;
	int LITERAL_shift = 127;
	int LITERAL_control = 128;
	int LITERAL_bitmap = 129;
	int LITERAL_cursor = 130;
	int LITERAL_dialog = 131;
	int LITERAL_ltext = 132;
	int LITERAL_rtext = 133;
	int LITERAL_ctext = 134;
	// "auto3state" = 135
	int LITERAL_autocheckbox = 136;
	int LITERAL_autoradiobutton = 137;
	int LITERAL_checkbox = 138;
	int LITERAL_pushbox = 139;
	int LITERAL_pushbutton = 140;
	int LITERAL_defpushbutton = 141;
	int LITERAL_radiobutton = 142;
	// "state3" = 143
	int LITERAL_groupbox = 144;
	int LITERAL_userbutton = 145;
	int LITERAL_edittext = 146;
	int LITERAL_bedit = 147;
	int LITERAL_hedit = 148;
	int LITERAL_iedit = 149;
	int LITERAL_combobox = 150;
	int LITERAL_listbox = 151;
	int LITERAL_icon = 152;
	int LITERAL_scrollbar = 153;
	int LITERAL_dialogex = 154;
	int LITERAL_menuitem = 155;
	int LITERAL_separator = 156;
	int LITERAL_checked = 157;
	int LITERAL_grayed = 158;
	int LITERAL_help = 159;
	int LITERAL_inactive = 160;
	int LITERAL_menubarbreak = 161;
	int LITERAL_menubreak = 162;
	int LITERAL_popup = 163;
	int LITERAL_menuex = 164;
	int LITERAL_messagetable = 165;
	int LITERAL_rcdata = 166;
	int DATA_STRING_LITERAL = 167;
	int LITERAL_versioninfo = 168;
	int LITERAL_fileversion = 169;
	int LITERAL_productversion = 170;
	int LITERAL_fileflagsmask = 171;
	int LITERAL_fileflags = 172;
	int LITERAL_fileos = 173;
	int LITERAL_filetype = 174;
	int LITERAL_filesubtype = 175;
	int LITERAL_block = 176;
	int STRING_FILE_INFO = 177;
	int LITERAL_value = 178;
	int VAR_FILE_INFO = 179;
	int TRANSLATION = 180;
	int LITERAL_textinclude = 181;
	int LITERAL_designinfo = 182;
	int LITERAL_toolbar = 183;
	int LITERAL_button = 184;
	int LITERAL_dlginit = 185;
}