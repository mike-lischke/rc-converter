/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

// This file contains the lexer grammar for the Windows *.rc file language used for resources on this
// platform. The grammar is based on MSDN, Platform SDK Tools, Performance Tools, Resource compiler.
// TEXTINCLUDE definition is based on an additional chapter in MSDN:
// "TN035: Using Multiple Resource Files and Header Files with Visual C++"

header {
/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.main;

import java.util.ArrayList;

import net.softgems.resourceparser.main.*;
}

class RCLexer extends Lexer; 
options {
  k = 3;
  
  // NOTE: The lexer does not handle comments or preprocessor directives except for #pragma.
  //       So this must be done in a previous step!
  
  // RC files can be Unicode encoded, however there is no mention about which characters are
  // actually allowed. However, the format of RC files is quite old so it is safe to assume only 
  // the BMP is supported. The generated lexer can only read 16 bit Unicode anyway.
  charVocabulary = '\u0010'..'\uFFFE';
  caseSensitive = false;
  caseSensitiveLiterals = false;
  
  codeGenBitsetTestThreshold = 2;
  
  // Import all tokens from the parser and add them to the lexer token list. This is necessary
  // to get our literals table build correctly.
  importVocab = RCParser;
}

tokens { 
  STRING_FILE_INFO = "\"stringfileinfo\"";
  VAR_FILE_INFO    = "\"varfileinfo\"";
  TRANSLATION      = "\"translation\"";
}

// The following section contains some Java code we need in the final lexer class.	
{
  /** List of event listeners who want to get notified about an lexer event. */
  private ArrayList listeners = new ArrayList();
  boolean hadErrors;
  boolean hadWarnings;
  
  //------------------------------------------------------------------------------------------------
  
  public void addLexerEventListener(IParseEventListener listener)
  {
    listeners.add(listener);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void removeLexerEventListener(IParseEventListener listener)
  {
    listeners.remove(listener);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void newline()
  {
    super.newline();
    doEvent(IParseEventListener.NEW_LINE, null);
  }
  
  //------------------------------------------------------------------------------------------------

  public void panic()
  {
  	hadErrors = true;
    doEvent(IParseEventListener.PANIC, "RC lexer panic");
  }

  //------------------------------------------------------------------------------------------------
  
  /** 
   * This method is executed by ANTLR internally when it detected an illegal
   *  state that cannot be recovered from.
   */
  public void panic(String s)
  {
  	hadErrors = true;
    doEvent(IParseEventListener.PANIC, "RC lexer panic: " + s);
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
      IParseEventListener listener = (IParseEventListener) listeners.get(i);
      listener.handleEvent(event, message);
    }
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

	NUMBER_SIGN:         "#";
	LEFT_PARENTHESE:    "(";
 	RIGHT_PARENTHESE:   ")";
 	LEFT_BRACE:         "{";
 	RIGHT_BRACE:        "}";
 	LEFT_BRACKET:       "["; 
 	RIGHT_BRACKET:      "]";
 	SEMICOLON:          ";";
 	COMMA:              ",";
 	APOSTROPHE:         "'";
protected
 	DOT:                ".";
 	COLON:              ":";
 	LOGICAL_AND:        "&&";
  LOGICAL_OR:         "||";
  BITWISE_AND:        "&";
 	BITWISE_OR:         "|";
 	LOGICAL_NOT:        "!";
 	BITWISE_NOT:        "~";
 	BITWISE_XOR:        "^";
 	PLUS:               "+";
 	MINUS:              "-";
  STAR:               "*";
protected
  DIV:                "/";
  MOD:                "%";
  SHIFT_LEFT:         "<<";
  SHIFT_RIGHT:        ">>";
  EQUAL:              "==";
  UNEQUAL:            "!=";
  LESS_THAN:          "<";
  GREATER_THAN:       ">"; 
  LESS_THAN_EQUAL:    "<=";
  GREATER_THAN_EQUAL: ">=";
  INC:                "++";
  DEC:                "--";
  ASSIGN:             "=";
  PATH_SEPARATOR:     "\\";
protected
  HORIZONTAL_TABULATOR: '\t';
protected
  VERTICAL_TABULATOR:   '\u0011';
protected
  LINE_FEED:            '\n';
protected
  FORM_FEED:            '\u000C';
protected
  CARRIAGE_RETURN:      '\r';
protected
  SPACE:                ' ';
protected
  LINE_SEPARATOR:       '\u2028';
protected
  PARAGRAPH_SEPARATOR:  '\u2029';

protected
  INPUT_CHARACTER: ~('\n' | '\r' | '\u2028' | '\u2029');

protected
  UNICODE_LETTER: // Don't include ASCII upper case letters. The parser is case insensitive.
    /*'\u0041' .. '\u005A' |*/ '\u0061' .. '\u007A' | '\u00AA' .. '\u00AA' | '\u00B5' .. '\u00B5' |
    '\u00BA' .. '\u00BA' | '\u00C0' .. '\u00D6' | '\u00D8' .. '\u00F6' | '\u00F8' .. '\u01F5' |
    '\u01FA' .. '\u0217' | '\u0250' .. '\u02A8' | '\u02B0' .. '\u02B8' | '\u02BB' .. '\u02C1' |
    '\u02D0' .. '\u02D1' | '\u02E0' .. '\u02E4' | '\u037A' .. '\u037A' | '\u0386' .. '\u0386' |
    '\u0388' .. '\u038A' | '\u038C' .. '\u038C' | '\u038E' .. '\u03A1' | '\u03A3' .. '\u03CE' |
    '\u03D0' .. '\u03D6' | '\u03DA' .. '\u03DA' | '\u03DC' .. '\u03DC' | '\u03DE' .. '\u03DE' |
    '\u03E0' .. '\u03E0' | '\u03E2' .. '\u03F3' | '\u0401' .. '\u040C' | '\u040E' .. '\u044F' |
    '\u0451' .. '\u045C' | '\u045E' .. '\u0481' | '\u0490' .. '\u04C4' | '\u04C7' .. '\u04C8' |
    '\u04CB' .. '\u04CC' | '\u04D0' .. '\u04EB' | '\u04EE' .. '\u04F5' | '\u04F8' .. '\u04F9' |
    '\u0531' .. '\u0556' | '\u0559' .. '\u0559' | '\u0561' .. '\u0587' | '\u05D0' .. '\u05EA' |
    '\u05F0' .. '\u05F2' | '\u0621' .. '\u063A' | '\u0640' .. '\u064A' | '\u0671' .. '\u06B7' |
    '\u06BA' .. '\u06BE' | '\u06C0' .. '\u06CE' | '\u06D0' .. '\u06D3' | '\u06D5' .. '\u06D5' |
    '\u06E5' .. '\u06E6' | '\u0905' .. '\u0939' | '\u093D' .. '\u093D' | '\u0958' .. '\u0961' |
    '\u0985' .. '\u098C' | '\u098F' .. '\u0990' | '\u0993' .. '\u09A8' | '\u09AA' .. '\u09B0' |
    '\u09B2' .. '\u09B2' | '\u09B6' .. '\u09B9' | '\u09DC' .. '\u09DD' | '\u09DF' .. '\u09E1' |
    '\u09F0' .. '\u09F1' | '\u0A05' .. '\u0A0A' | '\u0A0F' .. '\u0A10' | '\u0A13' .. '\u0A28' |
    '\u0A2A' .. '\u0A30' | '\u0A32' .. '\u0A33' | '\u0A35' .. '\u0A36' | '\u0A38' .. '\u0A39' |
    '\u0A59' .. '\u0A5C' | '\u0A5E' .. '\u0A5E' | '\u0A72' .. '\u0A74' | '\u0A85' .. '\u0A8B' |
    '\u0A8D' .. '\u0A8D' | '\u0A8F' .. '\u0A91' | '\u0A93' .. '\u0AA8' | '\u0AAA' .. '\u0AB0' |
    '\u0AB2' .. '\u0AB3' | '\u0AB5' .. '\u0AB9' | '\u0ABD' .. '\u0ABD' | '\u0AE0' .. '\u0AE0' |
    '\u0B05' .. '\u0B0C' | '\u0B0F' .. '\u0B10' | '\u0B13' .. '\u0B28' | '\u0B2A' .. '\u0B30' |
    '\u0B32' .. '\u0B33' | '\u0B36' .. '\u0B39' | '\u0B3D' .. '\u0B3D' | '\u0B5C' .. '\u0B5D' |
    '\u0B5F' .. '\u0B61' | '\u0B85' .. '\u0B8A' | '\u0B8E' .. '\u0B90' | '\u0B92' .. '\u0B95' |
    '\u0B99' .. '\u0B9A' | '\u0B9C' .. '\u0B9C' | '\u0B9E' .. '\u0B9F' | '\u0BA3' .. '\u0BA4' |
    '\u0BA8' .. '\u0BAA' | '\u0BAE' .. '\u0BB5' | '\u0BB7' .. '\u0BB9' | '\u0C05' .. '\u0C0C' |
    '\u0C0E' .. '\u0C10' | '\u0C12' .. '\u0C28' | '\u0C2A' .. '\u0C33' | '\u0C35' .. '\u0C39' |
    '\u0C60' .. '\u0C61' | '\u0C85' .. '\u0C8C' | '\u0C8E' .. '\u0C90' | '\u0C92' .. '\u0CA8' |
    '\u0CAA' .. '\u0CB3' | '\u0CB5' .. '\u0CB9' | '\u0CDE' .. '\u0CDE' | '\u0CE0' .. '\u0CE1' |
    '\u0D05' .. '\u0D0C' | '\u0D0E' .. '\u0D10' | '\u0D12' .. '\u0D28' | '\u0D2A' .. '\u0D39' |
    '\u0D60' .. '\u0D61' | '\u0E01' .. '\u0E2E' | '\u0E30' .. '\u0E30' | '\u0E32' .. '\u0E33' |
    '\u0E40' .. '\u0E46' | '\u0E81' .. '\u0E82' | '\u0E84' .. '\u0E84' | '\u0E87' .. '\u0E88' |
    '\u0E8A' .. '\u0E8A' | '\u0E8D' .. '\u0E8D' | '\u0E94' .. '\u0E97' | '\u0E99' .. '\u0E9F' |
    '\u0EA1' .. '\u0EA3' | '\u0EA5' .. '\u0EA5' | '\u0EA7' .. '\u0EA7' | '\u0EAA' .. '\u0EAB' |
    '\u0EAD' .. '\u0EAE' | '\u0EB0' .. '\u0EB0' | '\u0EB2' .. '\u0EB3' | '\u0EBD' .. '\u0EBD' |
    '\u0EC0' .. '\u0EC4' | '\u0EC6' .. '\u0EC6' | '\u0EDC' .. '\u0EDD' | '\u0F40' .. '\u0F47' |
    '\u0F49' .. '\u0F69' | '\u10A0' .. '\u10C5' | '\u10D0' .. '\u10F6' | '\u1100' .. '\u1159' |
    '\u115F' .. '\u11A2' | '\u11A8' .. '\u11F9' | '\u1E00' .. '\u1E9B' | '\u1EA0' .. '\u1EF9' |
    '\u1F00' .. '\u1F15' | '\u1F18' .. '\u1F1D' | '\u1F20' .. '\u1F45' | '\u1F48' .. '\u1F4D' |
    '\u1F50' .. '\u1F57' | '\u1F59' .. '\u1F59' | '\u1F5B' .. '\u1F5B' | '\u1F5D' .. '\u1F5D' |
    '\u1F5F' .. '\u1F7D' | '\u1F80' .. '\u1FB4' | '\u1FB6' .. '\u1FBC' | '\u1FBE' .. '\u1FBE' |
    '\u1FC2' .. '\u1FC4' | '\u1FC6' .. '\u1FCC' | '\u1FD0' .. '\u1FD3' | '\u1FD6' .. '\u1FDB' |
    '\u1FE0' .. '\u1FEC' | '\u1FF2' .. '\u1FF4' | '\u1FF6' .. '\u1FFC' | '\u207F' .. '\u207F' |
    '\u2102' .. '\u2102' | '\u2107' .. '\u2107' | '\u210A' .. '\u2113' | '\u2115' .. '\u2115' |
    '\u2118' .. '\u211D' | '\u2124' .. '\u2124' | '\u2126' .. '\u2126' | '\u2128' .. '\u2128' |
    '\u212A' .. '\u2131' | '\u2133' .. '\u2138' | '\u3005' .. '\u3005' | '\u3031' .. '\u3035' |
    '\u3041' .. '\u3094' | '\u309B' .. '\u309E' | '\u30A1' .. '\u30FA' | '\u30FC' .. '\u30FE' |
    '\u3105' .. '\u312C' | '\u3131' .. '\u318E' | '\u4E00' .. '\u9FA5' | '\uAC00' .. '\uD7A3' |
    '\uF900' .. '\uFA2D' | '\uFB00' .. '\uFB06' | '\uFB13' .. '\uFB17' | '\uFB1F' .. '\uFB28' |
    '\uFB2A' .. '\uFB36' | '\uFB38' .. '\uFB3C' | '\uFB3E' .. '\uFB3E' | '\uFB40' .. '\uFB41' |
    '\uFB43' .. '\uFB44' | '\uFB46' .. '\uFBB1' | '\uFBD3' .. '\uFD3D' | '\uFD50' .. '\uFD8F' |
    '\uFD92' .. '\uFDC7' | '\uFDF0' .. '\uFDFB' | '\uFE70' .. '\uFE72' | '\uFE74' .. '\uFE74' |
    '\uFE76' .. '\uFEFC' | '\uFF21' .. '\uFF3A' | '\uFF41' .. '\uFF5A' | '\uFF66' .. '\uFFBE' |
    '\uFFC2' .. '\uFFC7' | '\uFFCA' .. '\uFFCF' | '\uFFD2' .. '\uFFD7' | '\uFFDA' .. '\uFFDC';

protected
  UNICODE_CHARACTER_DIGIT:
    '\u0030' .. '\u0039' | '\u0660' .. '\u0669' | '\u06F0' .. '\u06F9' | '\u0966' .. '\u096F' |
    '\u09E6' .. '\u09EF' | '\u0A66' .. '\u0A6F' | '\u0AE6' .. '\u0AEF' | '\u0B66' .. '\u0B6F' |
    '\u0BE7' .. '\u0BEF' | '\u0C66' .. '\u0C6F' | '\u0CE6' .. '\u0CEF' | '\u0D66' .. '\u0D6F' |
    '\u0E50' .. '\u0E59' | '\u0ED0' .. '\u0ED9' | '\u0F20' .. '\u0F29' | '\uFF10' .. '\uFF19';

protected
	LETTER:          UNICODE_LETTER | '_';
protected
	LETTER_OR_DIGIT: LETTER | UNICODE_CHARACTER_DIGIT;
	
protected
  DIGIT:           '0'..'9';
protected
	 HEX_DIGIT:       DIGIT | 'a'..'f';
protected
	 OCTAL_DIGIT:     '0'..'7';
protected
	 ZERO_TO_THREE:   '0'..'3';
protected	
	 OCTAL_NUMERAL:   ZERO_TO_THREE (options {greedy = true;}: OCTAL_DIGIT)*;
	
protected
	 EXPONENT_PART:   'e' ('+' | '-')? (DIGIT)+;
protected
	 FLOAT_SUFFIX:    'f' | 'd';

protected
	 STRING_CHARACTER: ~('"' | '\\') | "\\" .;

protected
  STRING_LITERAL_PART:
    '"' (STRING_CHARACTER)* '"'
  ;

	STRING_LITERAL:
	  (STRING_LITERAL_PART (SPACE | HORIZONTAL_TABULATOR)*!)+
	;
	
  DATA_STRING_LITERAL:
    '\'' (HEX_DIGIT)+ ((SPACE | HORIZONTAL_TABULATOR)+ (HEX_DIGIT)+)* '\''
  ;
 
protected
  LINE_TERMINATOR: 
    LINE_FEED 
    | CARRIAGE_RETURN 
    | CARRIAGE_RETURN LINE_FEED 
    | LINE_SEPARATOR 
    | PARAGRAPH_SEPARATOR
  ;

	// With this definition white spaces are skipped (new lines are counted in the input converter).
  WHITE_SPACE: 
    (
      SPACE 
      | HORIZONTAL_TABULATOR 
      | VERTICAL_TABULATOR 
      | FORM_FEED 
      | LINE_TERMINATOR
    )  
    {$setType(Token.SKIP);}
  ;
  
  // The complex distinction between float, integer and hex literals was taken from java.g.
  // Support for special suffixes has been added.
  NUMERAL
  	// Helper variables for the distinction.
  	{boolean isDecimal = false; Token t = null;}:   
  	(
    	// Expression starts with a dot. 
    	'.' {$setType(DOT);} 
    	( // Check for standalone dot or float expression starting only with a dot.
    	  (DIGIT)+ (EXPONENT_PART)? (f1: FLOAT_SUFFIX {t = f1;})?
        {
        	// Decide in code whether we have a float or a double here.
        	if (t != null && t.getText().toUpperCase().indexOf('F') >= 0) 
        	{
        		$setType(FLOAT_LITERAL);
          }
          else
          {
          	$setType(DOUBLE_LITERAL); // assume double
          }
        }
      )?
  		// Another branch: expressions starts with a digit.
    	|	
        ('0' {isDecimal = true;} // Special case for just '0'.
          ('x' // Hex literal started.
            (
    					// The 'e'|'E' and float suffix stuff look
    					// like hex digits, hence the (...)+ doesn't
    					// know when to stop: ambig.  ANTLR resolves
    					// it correctly by matching immediately.  It
    					// is therefor ok to hush warning.
    					options {
    						warnWhenFollowAmbig = false;
    					}: HEX_DIGIT
            )+ // End of hex digit loop.
              {$setType(HEX_LITERAL);}
          // Alternative subrule for input starting with '0'.
          |
            ((DIGIT)+ (DOT | EXPONENT_PART | FLOAT_SUFFIX)) => (DIGIT)+
          |
            (OCTAL_DIGIT)+ {$setType(OCTAL_LITERAL);}
          )?
        // Alternative subrule for non-zero numbers.
        |	
          ('1'..'9') (DIGIT)* {isDecimal = true;}
        ) // Close the branch with the leading '0';
        
        // Is the number followed by a suffix?
        ('l' {$setType(LONG_LITERAL);}
          | "ul" {$setType(LONG_LITERAL);}
          | 'u' {$setType(LONG_LITERAL);}
          // Microsoft specific suffixes.
          | "i8" {$setType(BYTE_LITERAL);}
          | "i16" {$setType(SHORT_LITERAL);}
          | "i32" {$setType(INTEGER_LITERAL);}
          | "i64" {$setType(LONG_LITERAL);}
          | "i128" {$setType(BIGINT_LITERAL);}
          | "u8" {$setType(BYTE_LITERAL);}
          | "u16" {$setType(SHORT_LITERAL);}
          | "u32" {$setType(INTEGER_LITERAL);}
          | "u64" {$setType(LONG_LITERAL);}
          | "u128" {$setType(BIGINT_LITERAL);}
          // Alternatively, check to see if it's a float if it looks like decimal so far.
          |
            {isDecimal}?
            (
              // Does the next input match a float part too?
              // Check for input with and w/o decimal dot.
              DOT (DIGIT)* (EXPONENT_PART)? (f2: FLOAT_SUFFIX {t = f2;})?
              |
                EXPONENT_PART (f3: FLOAT_SUFFIX {t = f3;})?
              |
                f4: FLOAT_SUFFIX {t = f4;}
            )
            {
            	// Again, decide in code whether we have float or double here.
              if (t != null && t.getText().toUpperCase().indexOf('F') >= 0) 
              {
              	$setType(FLOAT_LITERAL);
              }
              else
              {
              	$setType(DOUBLE_LITERAL); // assume double
              }
             } // Close code part of the float suffix stuff.
        )?
    )
  ;

  IDENTIFIER:
    LETTER (LETTER_OR_DIGIT | '.')*
  ;
