/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

// This file contains the parser grammar for the Windows *.rc file language used for resources on this
// platform. The grammar is based on MSDN, Platform SDK Tools, Performance Tools, Resource compiler.
// TEXTINCLUDE definition is based on an additional chapter in MSDN:
//   "TN035: Using Multiple Resource Files and Header Files with Visual C++"
// A few other definitions are found by trial and error (e.g. dlginit).
//
// Note: Currently the grammar does not cover meta information as found in a few resource files.
//       Meta information are either makefile-like constructs like:
//         IDD_DIALOG$(_MAC)
//       or even stranger constructs like:
//         "#include ""[!output RES_PATH]\\[!output SAFE_PROJECT_NAME].rc2"".
//       Both variants are rather seldom and there is no documentation about them.

header {
/**
 * Soft Gems Resource parser. Created by Mike Lischke.
 * 
 * The source code in this file can freely be used for any purpose provided this notice remains 
 * unchanged in the file.
 * 
 * Copyright 2004 by Mike Lischke, www.soft-gems.net, public@soft-gems.net. All rights reserved.
 */

package net.softgems.resourceparser.main;

import java.util.ArrayList;

import net.softgems.resourceparser.main.*;
}

// Parser rules (productions).
class RCParser extends Parser;
options {
  // NOTE: The parser does not handle comments or preprocessor directives except for #pragma.
  //       So this must be done in a previous step!
  
  k = 3; 
  codeGenMakeSwitchThreshold = 2;
  codeGenBitsetTestThreshold = 2;
  buildAST = true;
  
  importVocab = ExpressionLexer;
}

tokens {
	 CONCRETE_CONTROL; ACCELERATOR; RESOURCE_ATTRIBUTES; STRING_TABLE_ENTRY;
	 DESIGN_INFO_CONTROL_BLOCK; NAMED_RESOURCE; RAW; FILE_NAME;
	 USER_DEFINED; DESIGN_INFO_ENTRIES; COMMON_RESOURCE_INFO; VERSION_FIXED_INFO;
	 ACCELERATOR_TYPE; ACCELERATOR_OPTION;
}

// The following section contains some Java code we need in the final parser class.	
{

  /** List of event listeners who want to get notified about an parser event. */
  ArrayList listeners = new ArrayList();
  boolean hadErrors;
  boolean hadWarnings;
  
  //------------------------------------------------------------------------------------------------
  
  public void addParserEventListener(IParseEventListener listener)
  {
    listeners.add(listener);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void removeParserEventListener(IParseEventListener listener)
  {
    listeners.remove(listener);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void reportError(RecognitionException ex)
  {
  	hadErrors = true;
    doEvent(IParseEventListener.ERROR, ex.toString());
  }

  //------------------------------------------------------------------------------------------------
  
  public void reportError(String s)
  {
  	hadErrors = true;
    if (getFilename() == null)
    {
      doEvent(IParseEventListener.ERROR, s);
    }
    else
    {
      doEvent(IParseEventListener.ERROR, getFilename() + ": " + s);
    }
  }

  //------------------------------------------------------------------------------------------------
  
  public void reportWarning(String s)
  {
  	hadWarnings = true;
    if (getFilename() == null)
    {
      doEvent(IParseEventListener.WARNING, s);
    }
    else
    {
      doEvent(IParseEventListener.WARNING, getFilename() + ": " + s);
    }
  }

  //------------------------------------------------------------------------------------------------
  
  private void doEvent(int event, String message)
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      IParseEventListener listener = (IParseEventListener)listeners.get(i);
      listener.handleEvent(event, message);
    }
  }
  
  //------------------------------------------------------------------------------------------------

	public String getFilename()
	{
		// This method must be overidden because the parser class maintains an own file name variable
		// and does not consider that the lexer could switch the files. So we ask the lexer for the
		// actual file name.
    RCParserSharedInputState state = (RCParserSharedInputState) getInputState();
    RCLexer lexer = (RCLexer) state.getInput().getInput();
		return lexer.getFilename();
	}
		  
  //------------------------------------------------------------------------------------------------
  
  public boolean hadErrors()
  {
    return hadErrors;
  }
  
  //------------------------------------------------------------------------------------------------
  
  public boolean hadWarnings()
  {
    return hadWarnings;
  }
  
  //------------------------------------------------------------------------------------------------
  
}

  // ----- The main entry point -----
  resource_definition: 
    (resource_statement)*
  ;
  
  integer_literal:
    NUMERAL
    | LONG_LITERAL
    | HEX_LITERAL
    | OCTAL_LITERAL
  ;
  
  resource_identifier:
  	IDENTIFIER
    | integer_literal
  ;
  
  literal:    
    integer_literal // This part includes integer, hex, octal, long, float and double.
    | resource_string
  ;
  
	 // There is only one allowed pragma directive in resource files:
	 // #pragma code_page(DEFAULT | code page number);
	 // This preprocessor directive is the only one handled here because it influences how
	 // following strings are to be converted. Every other preprocessor stuff must already be
	 // handled before the input is handed over to the parser.
 	pragma_directive: 
	   "pragma"^ "code_page" LEFT_PARENTHESE! codepage RIGHT_PARENTHESE!
	 ;

	 codepage:
 	 	"default"
 	 	| resource_identifier
 	;

 	// ----- conditional and mathematical expressions -----
  // The expression rules were taken form the java.g grammar, which comes with ANTLR and slightly
  // adjusted to fit here.
  // Note: the parser process defined by these rules is "too well" in that it allows expressions
  //       that are likely not allowed in regular rc files. It doesn't hurt anyone, though.
  //
  // Note that most of these expressions follow the pattern
  //   thisLevelExpression :
  //       nextHigherPrecedenceExpression
  //           (OPERATOR nextHigherPrecedenceExpression)*
  // which is a standard recursive definition for a parsing an expression.
  // The operators in java have the following precedences:
  //    lowest  (13)  = *= /= %= += -= <<= >>= >>>= &= ^= |=
  //            (12)  ?:
  //            (11)  ||
  //            (10)  &&
  //            ( 9)  |
  //            ( 8)  ^
  //            ( 7)  &
  //            ( 6)  == !=
  //            ( 5)  < <= > >=
  //            ( 4)  << >>
  //            ( 3)  +(binary) -(binary)
  //            ( 2)  * / %
  //            ( 1)  ++ -- +(unary) -(unary)  ~  !  (type)
  //                  []   () (method call)
  //
  // Function/method calls are usually not allowed in rc files.
  //
  // Note that the above precedence levels map to the rules below...
  // Once you have a precedence chart, writing the appropriate rules as below
  //   is usually very straightfoward
  
  // The mother of all expressions.
  expression:
    assignmentExpression
    {#expression = #(#[EXPR,"expression"], #expression);}
  ;
  
  // Assignment expression (level 13). Not used in rc files.
  assignmentExpression:
    conditionalExpression
    /*
    (
      (ASSIGN^
              |   PLUS_ASSIGN^
              |   MINUS_ASSIGN^
              |   STAR_ASSIGN^
              |   DIV_ASSIGN^
              |   MOD_ASSIGN^
              |   SR_ASSIGN^
              |   BSR_ASSIGN^
              |   SL_ASSIGN^
              |   BAND_ASSIGN^
              |   BXOR_ASSIGN^
              |   BOR_ASSIGN^
      )
      assignmentExpression
    )?
    */
  ;
  
  // Conditional test (level 12).
  conditionalExpression:
    logicalOrExpression
  ;
  
  // Logical or (||) (level 11).
  logicalOrExpression:
    logicalAndExpression (LOGICAL_OR^ logicalAndExpression)*
  ;
  
  // Logical and (&&) (level 10).
  logicalAndExpression:
    inclusiveOrExpression (LOGICAL_AND^ inclusiveOrExpression)*
  ;
  
  // Bitwise or non-short-circuiting or (|) (level 9).
  inclusiveOrExpression:
    exclusiveOrExpression (BITWISE_OR^ exclusiveOrExpression)*
  ;
  
  // Exclusive or (^) (level 8).
  exclusiveOrExpression:
    andExpression (BITWISE_XOR^ andExpression)*
  ;
  
  // Bitwise or non-short-circuiting and (&) (level 7).
  andExpression:
    equalityExpression (BITWISE_AND^ equalityExpression)*
  ;
  
  // Equality/inequality (==/!=) (level 6).
  equalityExpression:
    relationalExpression ((UNEQUAL^ | EQUAL^) relationalExpression)*
  ;
  
  // Boolean relational expressions (level 5).
  relationalExpression:
    shiftExpression ((LESS_THAN^ | GREATER_THAN^ | LESS_THAN_EQUAL^ | GREATER_THAN_EQUAL^) shiftExpression)*
  ;
  
  // Bit shift expressions (level 4).
  shiftExpression:
    additiveExpression ((SHIFT_LEFT^ | SHIFT_RIGHT^) additiveExpression)*
  ;
  
  // Binary addition/subtraction (level 3)
  additiveExpression:
    multiplicativeExpression ((PLUS^ | MINUS^) multiplicativeExpression)*
  ;
  
  // Multiplication/division/modulo (level 2)
  multiplicativeExpression:
    // Note: We have to use a string literal for / instead of the lexer token DIV as this conflicts
    //       with the file name separator token. Once we get macro like support for ANTLR this might
    //       be superfluous.
    unaryExpression ((STAR^ | DIV^ | MOD^ ) unaryExpression)*
  ;
  
  unaryExpression:
    INC^ unaryExpression
    | DEC^ unaryExpression
    | minus: MINUS^ unaryExpression {#minus.setType(UNARY_MINUS);}
    | plus: PLUS^ unaryExpression {#plus.setType(UNARY_PLUS);}
    | unaryExpressionNotPlusMinus
  ;
  
  unaryExpressionNotPlusMinus:
    BITWISE_NOT^ unaryExpression
    | LOGICAL_NOT^ unaryExpression
    // The "not" keyword is another oddity in rc files as it does not belong to the language
    // in any sensible way. Since there is no documentation about its exact meaning it is 
    // assumed to be a bitwise not, so it seems to be used at least. It still does not make
    // much sense, though, when there is only one style value, although it appears this way sometimes.
    | not: "not"^ {#not.setType(BITWISE_NOT);} unaryExpression
    | postfixExpression
  ;
  
  // Qualified names, array expressions, method invocation, post inc/dec.
  postfixExpression:
    primaryExpression
    (
      // Possibly add on a post-increment or post-decrement.
      // Allows INC/DEC on too much, but semantics can check.
      in: INC^ {#in.setType(POST_INC);}
      | de: DEC^ {#de.setType(POST_DEC);}
    )?
  ;
  
  // The basic element of an expression.
  primaryExpression:
    literal
    | IDENTIFIER
    | LEFT_PARENTHESE! assignmentExpression RIGHT_PARENTHESE!
  ;

	 // ----- resource statements -----
  open_definition:
		LEFT_BRACE 
		| "begin"
	;
	
	close_definition:
		RIGHT_BRACE
		| "end"
	;
	
	 resource_statement:
	   NUMBER_SIGN! pragma_directive
	   | language_entry
	   | string_table
	   |
   	  (
			  resource_identifier named_entry
   	    {#resource_statement = #(#[NAMED_RESOURCE, "named resource"], resource_statement);}
   	  )
	 ;
	
  common_resource_attributes:
    (load_attribute | memory_attribute)+
    {#common_resource_attributes = #(#[RESOURCE_ATTRIBUTES, "resource attributes"], common_resource_attributes);}
  ; 
	
 	load_attribute:
 	  "preload" | "loadoncall"
 	;
	
 	memory_attribute:
 	  "fixed" | "moveable" | "discardable" | "pure" | "impure" | "shared" | "nonshared"
 	;
	
 	resource_string:
 	  ("l")? STRING_LITERAL
 	;
 	
 	// -- language
 	language_entry:
 	  "language"^ resource_identifier COMMA! resource_identifier
 	;
 	
 	// -- string table
 	string_table:
 	  "stringtable"^ (common_resource_attributes)? open_definition! (string_table_entry)* close_definition! 
 	;
 	
 	string_table_entry:
 		expression (COMMA!)? resource_string
    {#string_table_entry = #(#[STRING_TABLE_ENTRY, "string table entry"], string_table_entry);}
 	;
 	
  file_name:
    STRING_LITERAL
    | IDENTIFIER (PATH_SEPARATOR IDENTIFIER)*
  ;

 	// -- any named entry 
 	named_entry:
 		accelerator_resource
 		| bitmap_resource
 		| cursor_resource
 		| dialog_resource 
 		| dialogex_resource
 		| font_resource
 		| icon_resource
 		| menu_resource
 		| menuex_resource
 		| messagetable_resource
 		| rcdata_resource
 		| versioninfo_resource
	  // Note: textinclude entries may only use 1, 2 and 3 as their name/identifier. This special
	  //       requirement is not considered here but must be catched in the semantic phase.
    | textinclude_resource
 		| user_defined_resource
 		| design_info
 		// The definitions below describe resource entries, which are not mentioned in MSDN.
 		// Hence they had to be found by trial and error.
 		| toolbar_resource
 		| dialog_init_resource
 	;
 	
 	resource_characteristics:
 		 "characteristics"^ expression
 	;
 	
 	resource_version:
 		 "version"^ expression
 	;

  common_resource_info:
    (
      resource_characteristics
      | language_entry
      | resource_version
    )
    {#common_resource_info = #(#[COMMON_RESOURCE_INFO, "common resource info"], common_resource_info);}
  ;
	
  resource_caption:
    "caption"^ STRING_LITERAL
  ;
  
  resource_class:
    "class"^ resource_identifier
  ;
  
  resource_style:
    "style"^ expression
  ;

  resource_exstyle:
    "exstyle"^ expression
  ;
  
  resource_font:
    // FONT pointsize, typeface, weight, italic, charset
    // The last three values are only valid for dialogex entries.
    "font"^ integer_literal COMMA! STRING_LITERAL (COMMA! integer_literal COMMA! integer_literal 
      COMMA! resource_identifier)?
  ;
  
  resource_menu_name:
    "menu"^ resource_identifier
  ;
  
  dialog_common_resource_info:
    resource_characteristics
    | language_entry
    | resource_version
    | resource_caption
    | resource_class
    | resource_exstyle
    | resource_font
    | resource_menu_name
    | resource_style
  ;
	
  // -- accelerator resource
  accelerator_resource:
    "accelerators"^ (common_resource_attributes)? (common_resource_info)* open_definition!
      (accelerator_entry)* close_definition!
  ;
	
  accelerator_entry:
    (resource_identifier | STRING_LITERAL) COMMA! expression (COMMA! accelerator_type)? 
      (COMMA! accelerator_option)*
    {#accelerator_entry = #([ACCELERATOR, "accelerator"], accelerator_entry);}
  ;
	
  accelerator_type:
    (
      "ascii"
      | "virtkey"
    )
    {#accelerator_type = #([ACCELERATOR_TYPE, "accelerator type"], accelerator_type);}
	;
	
  accelerator_option:
    (
      "noinvert"
      | "alt"
      | "shift"
      | "control"
    )
    {#accelerator_option = #([ACCELERATOR_OPTION, "accelerator option"], accelerator_option);}
  ;
	
  // -- bitmap resource
  bitmap_resource:
    "bitmap"^ (common_resource_attributes)? data_block
  ;
	
  // -- cursor resource
  cursor_resource:
    "cursor"^ (common_resource_attributes)? data_block
  ;
	
  // -- dialog resource
  dialog_resource:
    // DIALOG x, y, width, height [[ optional-statements]] {control-statements}
    "dialog"^ (common_resource_attributes)? integer_literal COMMA! integer_literal COMMA! 
      integer_literal COMMA! integer_literal (dialog_common_resource_info)* 
      open_definition! (dialog_control_definition)* close_definition!
  ;
	
  dialog_control_definition:
    dialog_generic_control | dialog_concrete_control_definition
  ;
	 
	dialog_concrete_control_definition:
    (
      dialog_static_control
      | dialog_button_control
      | dialog_edit_control
      | dialog_common_control
      | dialog_icon_control
      | dialog_scrollbar_control
    )
    dialog_common_control_trailing
    {#dialog_concrete_control_definition = #([CONCRETE_CONTROL, "concrete control"], 
      #dialog_concrete_control_definition);}
	;
	
	dialog_generic_control:
    // CONTROL controlText, id, className, style
    // The last string literal may only be one of the classes registered with the system
    // (e.g. button, static, SysTreeView32 etc.). This must be checked in the the semantic phase.
    "control"^ expression COMMA! expression COMMA! STRING_LITERAL COMMA! expression
      dialog_generic_control_trailing
	;

  dialog_generic_control_trailing:
    // , x, y, width, height 
    COMMA! expression COMMA! expression COMMA! expression COMMA! expression
  ;
  
  dialog_common_control_trailing:
    // , x, y, width, height [[, style]]
    COMMA! integer_literal COMMA! integer_literal COMMA! integer_literal COMMA! integer_literal 
      (COMMA! expression)?
  ;
  
  dialog_static_control:
    // staticClass controlText, id
    ("ltext"^ 
    | "rtext"^ 
    | "ctext"^
    )
    expression COMMA! expression
  ;
  	
  dialog_button_control:
    // buttonClass controlText, id
    ("auto3state"^
    | "autocheckbox"^
    | "autoradiobutton"^
    | "checkbox"^
    | "pushbox"^
    | "pushbutton"^
    | "defpushbutton"^
    | "radiobutton"^
    | "state3"^
    | "groupbox"^
    | "userbutton"^
    )
    expression COMMA! expression
  ;
  
  dialog_edit_control:
    // editClass id
    ("edittext"^
    | "bedit"^
    | "hedit"^
    | "iedit"^
    )
    expression
  ;
  
  dialog_common_control:
    ("combobox"^
    | "listbox"^
    )
    expression
  ;
    
  dialog_icon_control:
    "icon"^ expression COMMA! expression
  ;
    
  dialog_scrollbar_control:
    "scrollbar"^ expression COMMA! expression
  ;
    
  // -- dialogex resource
  dialogex_resource:
    // DIALOGEX x, y, width, height [ , helpID]] [[ optional-statements]] {control-statements}
    "dialogex"^ (common_resource_attributes)? expression COMMA! expression COMMA! expression COMMA!
      expression (COMMA! expression)? (dialog_common_resource_info)* 
      open_definition! (dialogex_control_definition)* close_definition!
  ;
	
  dialogex_control_definition:
    dialogex_generic_control | dialogex_concrete_control_definition
  ;
	
	dialogex_concrete_control_definition:
    (
      dialog_static_control
      | dialog_button_control
      | dialog_edit_control
      | dialog_common_control
      | dialog_icon_control
      | dialog_scrollbar_control
    )
    dialogex_common_control_trailing
    {#dialogex_concrete_control_definition = #([CONCRETE_CONTROL, "concrete control"], 
      #dialogex_concrete_control_definition);}
	;
	
	dialogex_generic_control:
    // CONTROL controlText, id, className, style
    // The last string literal may only be one of a few predefined values (button, static, edit
    // listbox, scrollbar, combobox). This must be checked in the the semantic phase.
    "control"^ expression COMMA! expression COMMA! STRING_LITERAL COMMA! expression
      dialogex_generic_control_trailing
	;

  dialogex_generic_control_trailing:
    // , x, y, width, height [[, extended-style]]
    // [, helpId]
    // [{data-element-1 [, data-element-2 [, ... ]]}]
    COMMA! expression COMMA! expression COMMA! expression COMMA! expression
      (
        // The following part is inherently ambiquous as the language allows several optional parts
        // with the same start token to follow each other. The ambiguity is solved by the 
        // additional declaration that there must be a style definition if an extended style
        // definition is defined and that it is assumed a help ID expression cannot be defined
        // if there is no style definition (read also the remark section for DIALOGEX control
        // structures in MSDN). Since we know what we are doing we can shut off the warnings.
        options {warnWhenFollowAmbig = false;}: COMMA! expression 
      )?
      (COMMA! expression)? (LEFT_BRACE! expression (COMMA! expression)* RIGHT_BRACE!)?
  ;
  
  dialogex_common_control_trailing:
    // , x, y, width, height [[, style[[, extended-style]]]]
    // [, helpId]
    // [{data-element-1 [, data-element-2 [, ... ]]}]
    COMMA! expression COMMA! expression COMMA! expression COMMA! expression
      (
        // The following part is inherently ambiquous as the language allows several optional parts
        // with the same start token to follow each other. The ambiguity is solved by the 
        // additional declaration that there must be a style definition if an extended style
        // definition is defined and that it is assumed a help ID expression cannot be defined
        // if there is no style definition (read also the remark section for DIALOGEX control
        // structures in MSDN). Since we know what we are doing we can shut off the warnings.
        options {warnWhenFollowAmbig = false;}: COMMA! expression 
        (
          options {warnWhenFollowAmbig = false;}: COMMA! expression
        )?
      )?
      (COMMA! expression)?
      (LEFT_BRACE! expression (COMMA! expression)* RIGHT_BRACE!)?
  ;
  
  // -- font resource
  font_resource:
    "font"^ (common_resource_attributes)? data_block
  ;
	
  // -- icon resource
  icon_resource:
    "icon"^ (common_resource_attributes)? data_block
  ;
	
  // -- menu resource
  menu_resource:
    // MENU  [[optional-statements]] {item-definitions...}
    "menu"^ (common_resource_attributes)? (common_resource_info)* open_definition!
      (menu_entry_item)* close_definition!
  ;
  
  menu_item:
    // MENUITEM text, result, [[optionlist]] 
    //   or
    // MENUITEM SEPARATOR
    "menuitem"^ 
      (
        "separator"
        | STRING_LITERAL COMMA! resource_identifier (menu_item_option)*
      )
  ;
  
  menu_item_option:
    (COMMA!)? 
      (
        "checked"
        | "grayed"
        | "help"
        | "inactive"
        | "menubarbreak"
        | "menubreak"
      )
  ;
  
  popup_entry:
    // POPUP text, [[optionlist]] {item-definitions...}
    "popup"^ STRING_LITERAL (menu_item_option)* open_definition! (menu_entry_item)* close_definition!
  ;
  
  menu_entry_item:
    menu_item | popup_entry
  ;
  
  // -- menuex resource
  menuex_resource:
    // MENUEX {
    //   [{[MENUITEM itemText [,[id][, [type][, state]]]] 
    // | [POPUP itemText [,[id][, [type][, [state][, helpID]]]] {popupBody}} ...]
    // }
    // MSDN's description of MENUEX is very unprecise. The formal description (which is not even 
    // correct) does not mention things like the optional resource statements but the text
    // says MENUEX is an extension to MENU and offers additional functionality.
    "menuex"^ (common_resource_attributes)? (common_resource_info)* open_definition!
      (menuex_entry_item)* close_definition!
  ;
  
  menuex_item:
    // MENUITEM itemText [,[id][, [type][, state]]]
    "menuitem"^ STRING_LITERAL (COMMA! expression (COMMA! expression  (COMMA! expression )?)?)?
  ;
  
  popupex_entry:
    // POPUP itemText [,[id][, [type][, [state][, helpID]]]] {popupBody}} ...
    "popup"^ STRING_LITERAL 
    (
      COMMA! expression 
      (
        COMMA! expression 
        (
          COMMA! expression
          (
            COMMA! expression
          )?
        )?
      )?
    )?
    open_definition! (menuex_entry_item)* close_definition!
  ;
  
  menuex_entry_item:
    menuex_item | popupex_entry
  ;
  
  // -- message table resource
  messagetable_resource:
    "messagetable"^ file_name
  ;
  
  // -- rcdata resource
  rcdata_resource:
    "rcdata"^ (common_resource_attributes)? (common_resource_info)* data_block
  ;
  
  data_block:
    raw_data
      {#data_block = #([RAW, "raw"], #data_block);}
    | file_name
      {#data_block = #([FILE_NAME, "file name"], #data_block);}
  ;
  
  raw_data:
    open_definition! (raw_data_entry)* close_definition!
  ;
  
  raw_data_entry:
    integer_literal (COMMA!)? 
    | STRING_LITERAL (COMMA!)?
    | DATA_STRING_LITERAL
  ;
	
  // -- version info resource
  versioninfo_resource:
    // VERSIONINFO fixed-info {block-statement...}
    "versioninfo"^ (common_resource_attributes)? (version_fixed_info)* (common_resource_info)* 
      open_definition! (version_info_block)* close_definition!
  ;
  
  version_fixed_info:
    (
      "fileversion"^ expression (COMMA! expression)*
      | "productversion"^ expression (COMMA! expression)*
      | "fileflagsmask"^ expression
      | "fileflags"^ expression
      | "fileos"^ expression
      | "filetype"^ expression
      | "filesubtype"^ expression
    )
    {#version_fixed_info = #(#[VERSION_FIXED_INFO, "version fixed info"], version_fixed_info);}
  ;
  
  version_info_block:
    // BLOCK "block-id"
    //   {BLOCK "lang-charset" {VALUE "string-name", "value"...}}
    // | {VALUE "string-name", "value"}
    "block"! (version_string_file_info | version_var_file_info)+
  ;
    
  version_string_file_info:
    STRING_FILE_INFO^ open_definition! (version_sfi_block_content)* close_definition!
  ;
  
  version_sfi_block_content:
    "block"^ expression open_definition! (version_sfi_entry)* close_definition!
  ;
  
  version_sfi_entry:
    "value"^ literal COMMA! literal
  ;
  
  version_var_file_info:
    VAR_FILE_INFO^ open_definition! (version_vfi_entry)* close_definition!
  ;
    
  version_vfi_entry:
    "value"^ TRANSLATION COMMA! (integer_literal COMMA! integer_literal)+
  ;
  
  // -- text include resource
  // Note: TEXTINCLUDE resources is an addition to the common resource specification and is
  //       described in MSDN "TN035: Using Multiple Resource Files and Header Files with Visual C++"
  textinclude_resource:
    "textinclude"^ common_textinclude_part
  ;
	
  common_textinclude_part:
    (common_resource_attributes)? open_definition! (STRING_LITERAL)* close_definition!
  ;
  
  // -- user defined resource
  // This kind of resource is defined as a name/id (already consumed) a resource type id (or any
  // integer) plus either a file name (with or w/o quotes) or a list of raw values.
  user_defined_resource:
    resource_identifier (common_resource_attributes)? data_block
    {#user_defined_resource = #(#[USER_DEFINED, "user defined"], user_defined_resource);}
  ;
  
  design_info:
    // Design infos are extra sections in the resource file only read by the MSVC designer.
    // There is no formal description in MSDN so only a "reversed-engineered" definition can be
    // specified here.
    "designinfo"^ (common_resource_attributes)?
    open_definition! (design_info_control_block)* close_definition!
  ;
	
  design_info_control_block:
    // The first identifier must be an existing resource entry. The type given here (in the second
    // identifier) must match that of the actual resource entry definition.
    // The resource class to be specified makes very likely only sense for visual components
    // (e.g. dialogs, not the version info etc.) but because there is no formal description we can't
    // be 100% sure.
    (resource_identifier | STRING_LITERAL) COMMA! resource_entry_class 
      open_definition! design_entries close_definition!
      {#design_info_control_block = #(#[DESIGN_INFO_CONTROL_BLOCK, "design info control block"], design_info_control_block);}
  ;
	
 	resource_entry_class:
 	  "accelerators"
    | "bitmap"
    | "cursor"
    | "dialog"
    | "dialogex"
    | "font"
    | "icon"
    | "menu"
    | "menuex"
    | "messagetable"
    | "popup"
    | "rcdata"
    | "stringtable"
    | "versioninfo"  
 	;
	
 	design_entries:
 	  // A design info entry detail consists of an identifier (e.g. RIGHTMARGIN) and a numerical value.
 	  // Since there is no formal description it is not clear if this numerical value can also be
 	  // a full mathematical expression.
 	  (IDENTIFIER COMMA! integer_literal)*
    {#design_entries = #(#[DESIGN_INFO_ENTRIES, "design info entries"], design_entries);}
 	;

 	toolbar_resource:
	   // TOOLBAR button-width, button-height
    "toolbar"^ (common_resource_attributes)? integer_literal COMMA! integer_literal
      open_definition! (toolbar_entry)* close_definition!
 	;
	
	 toolbar_entry:
	   "button"^ expression
	   | "separator"^
	 ;
	
	 dialog_init_resource:
	   // Dialog init resources are used for dialogs with ActiveX controls on them. Read more about this
	   // in the article: "HOWTO: Use a Dialog Template to Create a MFC Dialog with an ActiveX Control"
	   // (Q231591).
	   // These resources specify data for an dialog and use the same identifier as the actual dialog.
	   "dlginit"^ (common_resource_attributes)? open_definition! (dialog_init_entries)* close_definition!
	 ;
	
	 dialog_init_entries:
	   // There is no documentation about how dialog init entries should look like however it seems
	   // they always start with an identifier followed by a list of literals each seperated by a comma
	   // (except for the last value in a source line).
	   (IDENTIFIER | literal) (COMMA!)?
	 ; 
	
	