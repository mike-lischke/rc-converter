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

import antlr.TokenBuffer;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.ANTLRException;
import antlr.LLkParser;
import antlr.Token;
import antlr.TokenStream;
import antlr.RecognitionException;
import antlr.NoViableAltException;
import antlr.MismatchedTokenException;
import antlr.SemanticException;
import antlr.ParserSharedInputState;
import antlr.collections.impl.BitSet;
import antlr.collections.AST;
import java.util.Hashtable;
import antlr.ASTFactory;
import antlr.ASTPair;
import antlr.collections.impl.ASTArray;

public class RCParser extends antlr.LLkParser       implements RCParserTokenTypes
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
  

protected RCParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public RCParser(TokenBuffer tokenBuf) {
  this(tokenBuf,3);
}

protected RCParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public RCParser(TokenStream lexer) {
  this(lexer,3);
}

public RCParser(ParserSharedInputState state) {
  super(state,3);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void resource_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_definition_AST = null;
		
		try {      // for error handling
			{
			_loop3:
			do {
				if ((_tokenSet_0.member(LA(1)))) {
					resource_statement();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop3;
				}
				
			} while (true);
			}
			resource_definition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_1);
		}
		returnAST = resource_definition_AST;
	}
	
	public final void resource_statement() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_statement_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUMBER_SIGN:
			{
				match(NUMBER_SIGN);
				pragma_directive();
				astFactory.addASTChild(currentAST, returnAST);
				resource_statement_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_language:
			{
				language_entry();
				astFactory.addASTChild(currentAST, returnAST);
				resource_statement_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_stringtable:
			{
				string_table();
				astFactory.addASTChild(currentAST, returnAST);
				resource_statement_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				{
				resource_identifier();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry();
				astFactory.addASTChild(currentAST, returnAST);
				resource_statement_AST = (AST)currentAST.root;
				resource_statement_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(NAMED_RESOURCE,"named resource")).add(resource_statement_AST));
				currentAST.root = resource_statement_AST;
				currentAST.child = resource_statement_AST!=null &&resource_statement_AST.getFirstChild()!=null ?
					resource_statement_AST.getFirstChild() : resource_statement_AST;
				currentAST.advanceChildToEnd();
				}
				resource_statement_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = resource_statement_AST;
	}
	
	public final void integer_literal() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST integer_literal_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUMERAL:
			{
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(NUMERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			{
				AST tmp3_AST = null;
				tmp3_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
				match(LONG_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case HEX_LITERAL:
			{
				AST tmp4_AST = null;
				tmp4_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp4_AST);
				match(HEX_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case OCTAL_LITERAL:
			{
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(OCTAL_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_3);
		}
		returnAST = integer_literal_AST;
	}
	
	public final void resource_identifier() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_identifier_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case IDENTIFIER:
			{
				AST tmp6_AST = null;
				tmp6_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(IDENTIFIER);
				resource_identifier_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				integer_literal();
				astFactory.addASTChild(currentAST, returnAST);
				resource_identifier_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
		}
		returnAST = resource_identifier_AST;
	}
	
	public final void literal() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST literal_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LONG_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				integer_literal();
				astFactory.addASTChild(currentAST, returnAST);
				literal_AST = (AST)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			case LITERAL_l:
			{
				resource_string();
				astFactory.addASTChild(currentAST, returnAST);
				literal_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_5);
		}
		returnAST = literal_AST;
	}
	
	public final void resource_string() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_string_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_l:
			{
				AST tmp7_AST = null;
				tmp7_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp7_AST);
				match(LITERAL_l);
				break;
			}
			case STRING_LITERAL:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			AST tmp8_AST = null;
			tmp8_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp8_AST);
			match(STRING_LITERAL);
			resource_string_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_6);
		}
		returnAST = resource_string_AST;
	}
	
	public final void pragma_directive() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST pragma_directive_AST = null;
		
		try {      // for error handling
			AST tmp9_AST = null;
			tmp9_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp9_AST);
			match(LITERAL_pragma);
			AST tmp10_AST = null;
			tmp10_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp10_AST);
			match(LITERAL_code_page);
			match(LEFT_PARENTHESE);
			codepage();
			astFactory.addASTChild(currentAST, returnAST);
			match(RIGHT_PARENTHESE);
			pragma_directive_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = pragma_directive_AST;
	}
	
	public final void codepage() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST codepage_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_default:
			{
				AST tmp13_AST = null;
				tmp13_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp13_AST);
				match(LITERAL_default);
				codepage_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				resource_identifier();
				astFactory.addASTChild(currentAST, returnAST);
				codepage_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_7);
		}
		returnAST = codepage_AST;
	}
	
	public final void expression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST expression_AST = null;
		
		try {      // for error handling
			assignmentExpression();
			astFactory.addASTChild(currentAST, returnAST);
			expression_AST = (AST)currentAST.root;
			expression_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(EXPR,"expression")).add(expression_AST));
			currentAST.root = expression_AST;
			currentAST.child = expression_AST!=null &&expression_AST.getFirstChild()!=null ?
				expression_AST.getFirstChild() : expression_AST;
			currentAST.advanceChildToEnd();
			expression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
		}
		returnAST = expression_AST;
	}
	
	public final void assignmentExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST assignmentExpression_AST = null;
		
		try {      // for error handling
			conditionalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			assignmentExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_9);
		}
		returnAST = assignmentExpression_AST;
	}
	
	public final void conditionalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST conditionalExpression_AST = null;
		
		try {      // for error handling
			logicalOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			conditionalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_9);
		}
		returnAST = conditionalExpression_AST;
	}
	
	public final void logicalOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalOrExpression_AST = null;
		
		try {      // for error handling
			logicalAndExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop14:
			do {
				if ((LA(1)==LOGICAL_OR)) {
					AST tmp14_AST = null;
					tmp14_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp14_AST);
					match(LOGICAL_OR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop14;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_9);
		}
		returnAST = logicalOrExpression_AST;
	}
	
	public final void logicalAndExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST logicalAndExpression_AST = null;
		
		try {      // for error handling
			inclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop17:
			do {
				if ((LA(1)==LOGICAL_AND)) {
					AST tmp15_AST = null;
					tmp15_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp15_AST);
					match(LOGICAL_AND);
					inclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop17;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_10);
		}
		returnAST = logicalAndExpression_AST;
	}
	
	public final void inclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST inclusiveOrExpression_AST = null;
		
		try {      // for error handling
			exclusiveOrExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop20:
			do {
				if ((LA(1)==BITWISE_OR)) {
					AST tmp16_AST = null;
					tmp16_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp16_AST);
					match(BITWISE_OR);
					exclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop20;
				}
				
			} while (true);
			}
			inclusiveOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_11);
		}
		returnAST = inclusiveOrExpression_AST;
	}
	
	public final void exclusiveOrExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST exclusiveOrExpression_AST = null;
		
		try {      // for error handling
			andExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop23:
			do {
				if ((LA(1)==BITWISE_XOR)) {
					AST tmp17_AST = null;
					tmp17_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp17_AST);
					match(BITWISE_XOR);
					andExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop23;
				}
				
			} while (true);
			}
			exclusiveOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_12);
		}
		returnAST = exclusiveOrExpression_AST;
	}
	
	public final void andExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST andExpression_AST = null;
		
		try {      // for error handling
			equalityExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop26:
			do {
				if ((LA(1)==BITWISE_AND)) {
					AST tmp18_AST = null;
					tmp18_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp18_AST);
					match(BITWISE_AND);
					equalityExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop26;
				}
				
			} while (true);
			}
			andExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_13);
		}
		returnAST = andExpression_AST;
	}
	
	public final void equalityExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST equalityExpression_AST = null;
		
		try {      // for error handling
			relationalExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop30:
			do {
				if ((_tokenSet_14.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case UNEQUAL:
					{
						AST tmp19_AST = null;
						tmp19_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp19_AST);
						match(UNEQUAL);
						break;
					}
					case EQUAL:
					{
						AST tmp20_AST = null;
						tmp20_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp20_AST);
						match(EQUAL);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					relationalExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop30;
				}
				
			} while (true);
			}
			equalityExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_15);
		}
		returnAST = equalityExpression_AST;
	}
	
	public final void relationalExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST relationalExpression_AST = null;
		
		try {      // for error handling
			shiftExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop34:
			do {
				if (((LA(1) >= LESS_THAN && LA(1) <= GREATER_THAN_EQUAL))) {
					{
					switch ( LA(1)) {
					case LESS_THAN:
					{
						AST tmp21_AST = null;
						tmp21_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp21_AST);
						match(LESS_THAN);
						break;
					}
					case GREATER_THAN:
					{
						AST tmp22_AST = null;
						tmp22_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp22_AST);
						match(GREATER_THAN);
						break;
					}
					case LESS_THAN_EQUAL:
					{
						AST tmp23_AST = null;
						tmp23_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp23_AST);
						match(LESS_THAN_EQUAL);
						break;
					}
					case GREATER_THAN_EQUAL:
					{
						AST tmp24_AST = null;
						tmp24_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp24_AST);
						match(GREATER_THAN_EQUAL);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					shiftExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop34;
				}
				
			} while (true);
			}
			relationalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_16);
		}
		returnAST = relationalExpression_AST;
	}
	
	public final void shiftExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST shiftExpression_AST = null;
		
		try {      // for error handling
			additiveExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop38:
			do {
				if ((_tokenSet_17.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case SHIFT_LEFT:
					{
						AST tmp25_AST = null;
						tmp25_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp25_AST);
						match(SHIFT_LEFT);
						break;
					}
					case SHIFT_RIGHT:
					{
						AST tmp26_AST = null;
						tmp26_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp26_AST);
						match(SHIFT_RIGHT);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					additiveExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop38;
				}
				
			} while (true);
			}
			shiftExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_18);
		}
		returnAST = shiftExpression_AST;
	}
	
	public final void additiveExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST additiveExpression_AST = null;
		
		try {      // for error handling
			multiplicativeExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop42:
			do {
				if ((_tokenSet_19.member(LA(1)))) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						AST tmp27_AST = null;
						tmp27_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp27_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						AST tmp28_AST = null;
						tmp28_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp28_AST);
						match(MINUS);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					multiplicativeExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop42;
				}
				
			} while (true);
			}
			additiveExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_20);
		}
		returnAST = additiveExpression_AST;
	}
	
	public final void multiplicativeExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST multiplicativeExpression_AST = null;
		
		try {      // for error handling
			unaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop46:
			do {
				if (((LA(1) >= STAR && LA(1) <= MOD))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp29_AST = null;
						tmp29_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp29_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp30_AST = null;
						tmp30_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp30_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp31_AST = null;
						tmp31_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp31_AST);
						match(MOD);
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					unaryExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop46;
				}
				
			} while (true);
			}
			multiplicativeExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_21);
		}
		returnAST = multiplicativeExpression_AST;
	}
	
	public final void unaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpression_AST = null;
		Token  minus = null;
		AST minus_AST = null;
		Token  plus = null;
		AST plus_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case INC:
			{
				AST tmp32_AST = null;
				tmp32_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp32_AST);
				match(INC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case DEC:
			{
				AST tmp33_AST = null;
				tmp33_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp33_AST);
				match(DEC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case MINUS:
			{
				minus = LT(1);
				minus_AST = astFactory.create(minus);
				astFactory.makeASTRoot(currentAST, minus_AST);
				match(MINUS);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				minus_AST.setType(UNARY_MINUS);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case PLUS:
			{
				plus = LT(1);
				plus_AST = astFactory.create(plus);
				astFactory.makeASTRoot(currentAST, plus_AST);
				match(PLUS);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				plus_AST.setType(UNARY_PLUS);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			case LEFT_PARENTHESE:
			case LOGICAL_NOT:
			case BITWISE_NOT:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_not:
			case LITERAL_l:
			{
				unaryExpressionNotPlusMinus();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_22);
		}
		returnAST = unaryExpression_AST;
	}
	
	public final void unaryExpressionNotPlusMinus() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST unaryExpressionNotPlusMinus_AST = null;
		Token  not = null;
		AST not_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BITWISE_NOT:
			{
				AST tmp34_AST = null;
				tmp34_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp34_AST);
				match(BITWISE_NOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			case LOGICAL_NOT:
			{
				AST tmp35_AST = null;
				tmp35_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp35_AST);
				match(LOGICAL_NOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_not:
			{
				not = LT(1);
				not_AST = astFactory.create(not);
				astFactory.makeASTRoot(currentAST, not_AST);
				match(LITERAL_not);
				not_AST.setType(BITWISE_NOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			case LEFT_PARENTHESE:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_l:
			{
				postfixExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_22);
		}
		returnAST = unaryExpressionNotPlusMinus_AST;
	}
	
	public final void postfixExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST postfixExpression_AST = null;
		Token  in = null;
		AST in_AST = null;
		Token  de = null;
		AST de_AST = null;
		
		try {      // for error handling
			primaryExpression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case INC:
			{
				in = LT(1);
				in_AST = astFactory.create(in);
				astFactory.makeASTRoot(currentAST, in_AST);
				match(INC);
				in_AST.setType(POST_INC);
				break;
			}
			case DEC:
			{
				de = LT(1);
				de_AST = astFactory.create(de);
				astFactory.makeASTRoot(currentAST, de_AST);
				match(DEC);
				de_AST.setType(POST_DEC);
				break;
			}
			case LONG_LITERAL:
			case RIGHT_PARENTHESE:
			case LEFT_BRACE:
			case RIGHT_BRACE:
			case COMMA:
			case LOGICAL_AND:
			case LOGICAL_OR:
			case BITWISE_AND:
			case BITWISE_OR:
			case BITWISE_XOR:
			case PLUS:
			case MINUS:
			case STAR:
			case DIV:
			case MOD:
			case SHIFT_LEFT:
			case SHIFT_RIGHT:
			case EQUAL:
			case UNEQUAL:
			case LESS_THAN:
			case GREATER_THAN:
			case LESS_THAN_EQUAL:
			case GREATER_THAN_EQUAL:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_begin:
			case LITERAL_end:
			case LITERAL_l:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			case LITERAL_caption:
			case LITERAL_class:
			case LITERAL_style:
			case LITERAL_exstyle:
			case LITERAL_font:
			case LITERAL_menu:
			case LITERAL_control:
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			case LITERAL_menuitem:
			case LITERAL_separator:
			case LITERAL_popup:
			case LITERAL_fileversion:
			case LITERAL_productversion:
			case LITERAL_fileflagsmask:
			case LITERAL_fileflags:
			case LITERAL_fileos:
			case LITERAL_filetype:
			case LITERAL_filesubtype:
			case LITERAL_button:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			postfixExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_22);
		}
		returnAST = postfixExpression_AST;
	}
	
	public final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LONG_LITERAL:
			case STRING_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_l:
			{
				literal();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case IDENTIFIER:
			{
				AST tmp36_AST = null;
				tmp36_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp36_AST);
				match(IDENTIFIER);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case LEFT_PARENTHESE:
			{
				match(LEFT_PARENTHESE);
				assignmentExpression();
				astFactory.addASTChild(currentAST, returnAST);
				match(RIGHT_PARENTHESE);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_23);
		}
		returnAST = primaryExpression_AST;
	}
	
	public final void open_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST open_definition_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LEFT_BRACE:
			{
				AST tmp39_AST = null;
				tmp39_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp39_AST);
				match(LEFT_BRACE);
				open_definition_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_begin:
			{
				AST tmp40_AST = null;
				tmp40_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp40_AST);
				match(LITERAL_begin);
				open_definition_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_24);
		}
		returnAST = open_definition_AST;
	}
	
	public final void close_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST close_definition_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case RIGHT_BRACE:
			{
				AST tmp41_AST = null;
				tmp41_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp41_AST);
				match(RIGHT_BRACE);
				close_definition_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_end:
			{
				AST tmp42_AST = null;
				tmp42_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp42_AST);
				match(LITERAL_end);
				close_definition_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_25);
		}
		returnAST = close_definition_AST;
	}
	
	public final void language_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST language_entry_AST = null;
		
		try {      // for error handling
			AST tmp43_AST = null;
			tmp43_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp43_AST);
			match(LITERAL_language);
			resource_identifier();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			resource_identifier();
			astFactory.addASTChild(currentAST, returnAST);
			language_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_26);
		}
		returnAST = language_entry_AST;
	}
	
	public final void string_table() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST string_table_AST = null;
		
		try {      // for error handling
			AST tmp45_AST = null;
			tmp45_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp45_AST);
			match(LITERAL_stringtable);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			open_definition();
			{
			_loop67:
			do {
				if ((_tokenSet_27.member(LA(1)))) {
					string_table_entry();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop67;
				}
				
			} while (true);
			}
			close_definition();
			string_table_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = string_table_AST;
	}
	
	public final void named_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST named_entry_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_accelerators:
			{
				accelerator_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_bitmap:
			{
				bitmap_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_cursor:
			{
				cursor_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_dialog:
			{
				dialog_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_dialogex:
			{
				dialogex_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_font:
			{
				font_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_icon:
			{
				icon_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_menu:
			{
				menu_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_menuex:
			{
				menuex_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_messagetable:
			{
				messagetable_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_rcdata:
			{
				rcdata_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_versioninfo:
			{
				versioninfo_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_textinclude:
			{
				textinclude_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				user_defined_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_designinfo:
			{
				design_info();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_toolbar:
			{
				toolbar_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_dlginit:
			{
				dialog_init_resource();
				astFactory.addASTChild(currentAST, returnAST);
				named_entry_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = named_entry_AST;
	}
	
	public final void common_resource_attributes() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST common_resource_attributes_AST = null;
		
		try {      // for error handling
			{
			int _cnt58=0;
			_loop58:
			do {
				switch ( LA(1)) {
				case LITERAL_preload:
				case LITERAL_loadoncall:
				{
					load_attribute();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case LITERAL_fixed:
				case LITERAL_moveable:
				case LITERAL_discardable:
				case LITERAL_pure:
				case LITERAL_impure:
				case LITERAL_shared:
				case LITERAL_nonshared:
				{
					memory_attribute();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					if ( _cnt58>=1 ) { break _loop58; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt58++;
			} while (true);
			}
			common_resource_attributes_AST = (AST)currentAST.root;
			common_resource_attributes_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(RESOURCE_ATTRIBUTES,"resource attributes")).add(common_resource_attributes_AST));
			currentAST.root = common_resource_attributes_AST;
			currentAST.child = common_resource_attributes_AST!=null &&common_resource_attributes_AST.getFirstChild()!=null ?
				common_resource_attributes_AST.getFirstChild() : common_resource_attributes_AST;
			currentAST.advanceChildToEnd();
			common_resource_attributes_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_28);
		}
		returnAST = common_resource_attributes_AST;
	}
	
	public final void load_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST load_attribute_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_preload:
			{
				AST tmp46_AST = null;
				tmp46_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp46_AST);
				match(LITERAL_preload);
				load_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_loadoncall:
			{
				AST tmp47_AST = null;
				tmp47_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp47_AST);
				match(LITERAL_loadoncall);
				load_attribute_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_29);
		}
		returnAST = load_attribute_AST;
	}
	
	public final void memory_attribute() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST memory_attribute_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_fixed:
			{
				AST tmp48_AST = null;
				tmp48_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp48_AST);
				match(LITERAL_fixed);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_moveable:
			{
				AST tmp49_AST = null;
				tmp49_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp49_AST);
				match(LITERAL_moveable);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_discardable:
			{
				AST tmp50_AST = null;
				tmp50_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp50_AST);
				match(LITERAL_discardable);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_pure:
			{
				AST tmp51_AST = null;
				tmp51_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp51_AST);
				match(LITERAL_pure);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_impure:
			{
				AST tmp52_AST = null;
				tmp52_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp52_AST);
				match(LITERAL_impure);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_shared:
			{
				AST tmp53_AST = null;
				tmp53_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp53_AST);
				match(LITERAL_shared);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_nonshared:
			{
				AST tmp54_AST = null;
				tmp54_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp54_AST);
				match(LITERAL_nonshared);
				memory_attribute_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_29);
		}
		returnAST = memory_attribute_AST;
	}
	
	public final void string_table_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST string_table_entry_AST = null;
		
		try {      // for error handling
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case STRING_LITERAL:
			case LITERAL_l:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			resource_string();
			astFactory.addASTChild(currentAST, returnAST);
			string_table_entry_AST = (AST)currentAST.root;
			string_table_entry_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(STRING_TABLE_ENTRY,"string table entry")).add(string_table_entry_AST));
			currentAST.root = string_table_entry_AST;
			currentAST.child = string_table_entry_AST!=null &&string_table_entry_AST.getFirstChild()!=null ?
				string_table_entry_AST.getFirstChild() : string_table_entry_AST;
			currentAST.advanceChildToEnd();
			string_table_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_30);
		}
		returnAST = string_table_entry_AST;
	}
	
	public final void file_name() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST file_name_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case STRING_LITERAL:
			{
				AST tmp56_AST = null;
				tmp56_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp56_AST);
				match(STRING_LITERAL);
				file_name_AST = (AST)currentAST.root;
				break;
			}
			case IDENTIFIER:
			{
				AST tmp57_AST = null;
				tmp57_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp57_AST);
				match(IDENTIFIER);
				{
				_loop72:
				do {
					if ((LA(1)==PATH_SEPARATOR)) {
						AST tmp58_AST = null;
						tmp58_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp58_AST);
						match(PATH_SEPARATOR);
						AST tmp59_AST = null;
						tmp59_AST = astFactory.create(LT(1));
						astFactory.addASTChild(currentAST, tmp59_AST);
						match(IDENTIFIER);
					}
					else {
						break _loop72;
					}
					
				} while (true);
				}
				file_name_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = file_name_AST;
	}
	
	public final void accelerator_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST accelerator_resource_AST = null;
		
		try {      // for error handling
			AST tmp60_AST = null;
			tmp60_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp60_AST);
			match(LITERAL_accelerators);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop89:
			do {
				if ((_tokenSet_31.member(LA(1)))) {
					common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop89;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop91:
			do {
				if ((_tokenSet_32.member(LA(1)))) {
					accelerator_entry();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop91;
				}
				
			} while (true);
			}
			close_definition();
			accelerator_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = accelerator_resource_AST;
	}
	
	public final void bitmap_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST bitmap_resource_AST = null;
		
		try {      // for error handling
			AST tmp61_AST = null;
			tmp61_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp61_AST);
			match(LITERAL_bitmap);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case STRING_LITERAL:
			case IDENTIFIER:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			data_block();
			astFactory.addASTChild(currentAST, returnAST);
			bitmap_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = bitmap_resource_AST;
	}
	
	public final void cursor_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST cursor_resource_AST = null;
		
		try {      // for error handling
			AST tmp62_AST = null;
			tmp62_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp62_AST);
			match(LITERAL_cursor);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case STRING_LITERAL:
			case IDENTIFIER:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			data_block();
			astFactory.addASTChild(currentAST, returnAST);
			cursor_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = cursor_resource_AST;
	}
	
	public final void dialog_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_resource_AST = null;
		
		try {      // for error handling
			AST tmp63_AST = null;
			tmp63_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp63_AST);
			match(LITERAL_dialog);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LONG_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			{
			_loop108:
			do {
				if ((_tokenSet_33.member(LA(1)))) {
					dialog_common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop108;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop110:
			do {
				if ((_tokenSet_34.member(LA(1)))) {
					dialog_control_definition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop110;
				}
				
			} while (true);
			}
			close_definition();
			dialog_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = dialog_resource_AST;
	}
	
	public final void dialogex_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialogex_resource_AST = null;
		
		try {      // for error handling
			AST tmp67_AST = null;
			tmp67_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp67_AST);
			match(LITERAL_dialogex);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LONG_LITERAL:
			case LEFT_PARENTHESE:
			case LOGICAL_NOT:
			case BITWISE_NOT:
			case PLUS:
			case MINUS:
			case INC:
			case DEC:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_not:
			case LITERAL_l:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			case LITERAL_caption:
			case LITERAL_class:
			case LITERAL_style:
			case LITERAL_exstyle:
			case LITERAL_font:
			case LITERAL_menu:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop132:
			do {
				if ((_tokenSet_33.member(LA(1)))) {
					dialog_common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop132;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop134:
			do {
				if ((_tokenSet_34.member(LA(1)))) {
					dialogex_control_definition();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop134;
				}
				
			} while (true);
			}
			close_definition();
			dialogex_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = dialogex_resource_AST;
	}
	
	public final void font_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST font_resource_AST = null;
		
		try {      // for error handling
			AST tmp72_AST = null;
			tmp72_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp72_AST);
			match(LITERAL_font);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case STRING_LITERAL:
			case IDENTIFIER:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			data_block();
			astFactory.addASTChild(currentAST, returnAST);
			font_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = font_resource_AST;
	}
	
	public final void icon_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST icon_resource_AST = null;
		
		try {      // for error handling
			AST tmp73_AST = null;
			tmp73_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp73_AST);
			match(LITERAL_icon);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case STRING_LITERAL:
			case IDENTIFIER:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			data_block();
			astFactory.addASTChild(currentAST, returnAST);
			icon_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = icon_resource_AST;
	}
	
	public final void menu_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menu_resource_AST = null;
		
		try {      // for error handling
			AST tmp74_AST = null;
			tmp74_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp74_AST);
			match(LITERAL_menu);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop159:
			do {
				if ((_tokenSet_31.member(LA(1)))) {
					common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop159;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop161:
			do {
				if ((_tokenSet_35.member(LA(1)))) {
					menu_entry_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop161;
				}
				
			} while (true);
			}
			close_definition();
			menu_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = menu_resource_AST;
	}
	
	public final void menuex_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menuex_resource_AST = null;
		
		try {      // for error handling
			AST tmp75_AST = null;
			tmp75_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp75_AST);
			match(LITERAL_menuex);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop178:
			do {
				if ((_tokenSet_31.member(LA(1)))) {
					common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop178;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop180:
			do {
				if ((_tokenSet_35.member(LA(1)))) {
					menuex_entry_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop180;
				}
				
			} while (true);
			}
			close_definition();
			menuex_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = menuex_resource_AST;
	}
	
	public final void messagetable_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST messagetable_resource_AST = null;
		
		try {      // for error handling
			AST tmp76_AST = null;
			tmp76_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp76_AST);
			match(LITERAL_messagetable);
			file_name();
			astFactory.addASTChild(currentAST, returnAST);
			messagetable_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = messagetable_resource_AST;
	}
	
	public final void rcdata_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST rcdata_resource_AST = null;
		
		try {      // for error handling
			AST tmp77_AST = null;
			tmp77_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp77_AST);
			match(LITERAL_rcdata);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case STRING_LITERAL:
			case IDENTIFIER:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop197:
			do {
				if ((_tokenSet_31.member(LA(1)))) {
					common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop197;
				}
				
			} while (true);
			}
			data_block();
			astFactory.addASTChild(currentAST, returnAST);
			rcdata_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = rcdata_resource_AST;
	}
	
	public final void versioninfo_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST versioninfo_resource_AST = null;
		
		try {      // for error handling
			AST tmp78_AST = null;
			tmp78_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp78_AST);
			match(LITERAL_versioninfo);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			case LITERAL_fileversion:
			case LITERAL_productversion:
			case LITERAL_fileflagsmask:
			case LITERAL_fileflags:
			case LITERAL_fileos:
			case LITERAL_filetype:
			case LITERAL_filesubtype:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			_loop208:
			do {
				if (((LA(1) >= LITERAL_fileversion && LA(1) <= LITERAL_filesubtype))) {
					version_fixed_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop208;
				}
				
			} while (true);
			}
			{
			_loop210:
			do {
				if ((_tokenSet_31.member(LA(1)))) {
					common_resource_info();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop210;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop212:
			do {
				if ((LA(1)==LITERAL_block)) {
					version_info_block();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop212;
				}
				
			} while (true);
			}
			close_definition();
			versioninfo_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = versioninfo_resource_AST;
	}
	
	public final void textinclude_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST textinclude_resource_AST = null;
		
		try {      // for error handling
			AST tmp79_AST = null;
			tmp79_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp79_AST);
			match(LITERAL_textinclude);
			common_textinclude_part();
			astFactory.addASTChild(currentAST, returnAST);
			textinclude_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = textinclude_resource_AST;
	}
	
	public final void user_defined_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST user_defined_resource_AST = null;
		
		try {      // for error handling
			resource_identifier();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case STRING_LITERAL:
			case IDENTIFIER:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			data_block();
			astFactory.addASTChild(currentAST, returnAST);
			user_defined_resource_AST = (AST)currentAST.root;
			user_defined_resource_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(USER_DEFINED,"user defined")).add(user_defined_resource_AST));
			currentAST.root = user_defined_resource_AST;
			currentAST.child = user_defined_resource_AST!=null &&user_defined_resource_AST.getFirstChild()!=null ?
				user_defined_resource_AST.getFirstChild() : user_defined_resource_AST;
			currentAST.advanceChildToEnd();
			user_defined_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = user_defined_resource_AST;
	}
	
	public final void design_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST design_info_AST = null;
		
		try {      // for error handling
			AST tmp80_AST = null;
			tmp80_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp80_AST);
			match(LITERAL_designinfo);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			open_definition();
			{
			_loop245:
			do {
				if ((_tokenSet_32.member(LA(1)))) {
					design_info_control_block();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop245;
				}
				
			} while (true);
			}
			close_definition();
			design_info_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = design_info_AST;
	}
	
	public final void toolbar_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST toolbar_resource_AST = null;
		
		try {      // for error handling
			AST tmp81_AST = null;
			tmp81_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp81_AST);
			match(LITERAL_toolbar);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LONG_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			open_definition();
			{
			_loop255:
			do {
				if ((_tokenSet_36.member(LA(1)))) {
					toolbar_entry();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop255;
				}
				
			} while (true);
			}
			close_definition();
			toolbar_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = toolbar_resource_AST;
	}
	
	public final void dialog_init_resource() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_init_resource_AST = null;
		
		try {      // for error handling
			AST tmp83_AST = null;
			tmp83_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp83_AST);
			match(LITERAL_dlginit);
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			open_definition();
			{
			_loop260:
			do {
				if ((_tokenSet_37.member(LA(1)))) {
					dialog_init_entries();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop260;
				}
				
			} while (true);
			}
			close_definition();
			dialog_init_resource_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = dialog_init_resource_AST;
	}
	
	public final void resource_characteristics() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_characteristics_AST = null;
		
		try {      // for error handling
			AST tmp84_AST = null;
			tmp84_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp84_AST);
			match(LITERAL_characteristics);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			resource_characteristics_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_38);
		}
		returnAST = resource_characteristics_AST;
	}
	
	public final void resource_version() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_version_AST = null;
		
		try {      // for error handling
			AST tmp85_AST = null;
			tmp85_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp85_AST);
			match(LITERAL_version);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			resource_version_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_38);
		}
		returnAST = resource_version_AST;
	}
	
	public final void common_resource_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST common_resource_info_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_characteristics:
			{
				resource_characteristics();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_language:
			{
				language_entry();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_version:
			{
				resource_version();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			common_resource_info_AST = (AST)currentAST.root;
			common_resource_info_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(COMMON_RESOURCE_INFO,"common resource info")).add(common_resource_info_AST));
			currentAST.root = common_resource_info_AST;
			currentAST.child = common_resource_info_AST!=null &&common_resource_info_AST.getFirstChild()!=null ?
				common_resource_info_AST.getFirstChild() : common_resource_info_AST;
			currentAST.advanceChildToEnd();
			common_resource_info_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_39);
		}
		returnAST = common_resource_info_AST;
	}
	
	public final void resource_caption() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_caption_AST = null;
		
		try {      // for error handling
			AST tmp86_AST = null;
			tmp86_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp86_AST);
			match(LITERAL_caption);
			AST tmp87_AST = null;
			tmp87_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp87_AST);
			match(STRING_LITERAL);
			resource_caption_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = resource_caption_AST;
	}
	
	public final void resource_class() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_class_AST = null;
		
		try {      // for error handling
			AST tmp88_AST = null;
			tmp88_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp88_AST);
			match(LITERAL_class);
			resource_identifier();
			astFactory.addASTChild(currentAST, returnAST);
			resource_class_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = resource_class_AST;
	}
	
	public final void resource_style() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_style_AST = null;
		
		try {      // for error handling
			AST tmp89_AST = null;
			tmp89_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp89_AST);
			match(LITERAL_style);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			resource_style_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = resource_style_AST;
	}
	
	public final void resource_exstyle() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_exstyle_AST = null;
		
		try {      // for error handling
			AST tmp90_AST = null;
			tmp90_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp90_AST);
			match(LITERAL_exstyle);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			resource_exstyle_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = resource_exstyle_AST;
	}
	
	public final void resource_font() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_font_AST = null;
		
		try {      // for error handling
			AST tmp91_AST = null;
			tmp91_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp91_AST);
			match(LITERAL_font);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			AST tmp93_AST = null;
			tmp93_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp93_AST);
			match(STRING_LITERAL);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				integer_literal();
				astFactory.addASTChild(currentAST, returnAST);
				match(COMMA);
				integer_literal();
				astFactory.addASTChild(currentAST, returnAST);
				match(COMMA);
				resource_identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			case LITERAL_language:
			case LITERAL_characteristics:
			case LITERAL_version:
			case LITERAL_caption:
			case LITERAL_class:
			case LITERAL_style:
			case LITERAL_exstyle:
			case LITERAL_font:
			case LITERAL_menu:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			resource_font_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = resource_font_AST;
	}
	
	public final void resource_menu_name() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_menu_name_AST = null;
		
		try {      // for error handling
			AST tmp97_AST = null;
			tmp97_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp97_AST);
			match(LITERAL_menu);
			resource_identifier();
			astFactory.addASTChild(currentAST, returnAST);
			resource_menu_name_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = resource_menu_name_AST;
	}
	
	public final void dialog_common_resource_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_common_resource_info_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_characteristics:
			{
				resource_characteristics();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_language:
			{
				language_entry();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_version:
			{
				resource_version();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_caption:
			{
				resource_caption();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_class:
			{
				resource_class();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_exstyle:
			{
				resource_exstyle();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_font:
			{
				resource_font();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_menu:
			{
				resource_menu_name();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_style:
			{
				resource_style();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_common_resource_info_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_40);
		}
		returnAST = dialog_common_resource_info_AST;
	}
	
	public final void accelerator_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST accelerator_entry_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LONG_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				resource_identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp98_AST = null;
				tmp98_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp98_AST);
				match(STRING_LITERAL);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			if ((LA(1)==COMMA) && (_tokenSet_41.member(LA(2)))) {
				match(COMMA);
				accelerator_type();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_42.member(LA(1))) && (_tokenSet_43.member(LA(2)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			_loop96:
			do {
				if ((LA(1)==COMMA)) {
					match(COMMA);
					accelerator_option();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop96;
				}
				
			} while (true);
			}
			accelerator_entry_AST = (AST)currentAST.root;
			accelerator_entry_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ACCELERATOR,"accelerator")).add(accelerator_entry_AST));
			currentAST.root = accelerator_entry_AST;
			currentAST.child = accelerator_entry_AST!=null &&accelerator_entry_AST.getFirstChild()!=null ?
				accelerator_entry_AST.getFirstChild() : accelerator_entry_AST;
			currentAST.advanceChildToEnd();
			accelerator_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_44);
		}
		returnAST = accelerator_entry_AST;
	}
	
	public final void accelerator_type() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST accelerator_type_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_ascii:
			{
				AST tmp102_AST = null;
				tmp102_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp102_AST);
				match(LITERAL_ascii);
				break;
			}
			case LITERAL_virtkey:
			{
				AST tmp103_AST = null;
				tmp103_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp103_AST);
				match(LITERAL_virtkey);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			accelerator_type_AST = (AST)currentAST.root;
			accelerator_type_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ACCELERATOR_TYPE,"accelerator type")).add(accelerator_type_AST));
			currentAST.root = accelerator_type_AST;
			currentAST.child = accelerator_type_AST!=null &&accelerator_type_AST.getFirstChild()!=null ?
				accelerator_type_AST.getFirstChild() : accelerator_type_AST;
			currentAST.advanceChildToEnd();
			accelerator_type_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_42);
		}
		returnAST = accelerator_type_AST;
	}
	
	public final void accelerator_option() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST accelerator_option_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_noinvert:
			{
				AST tmp104_AST = null;
				tmp104_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp104_AST);
				match(LITERAL_noinvert);
				break;
			}
			case LITERAL_alt:
			{
				AST tmp105_AST = null;
				tmp105_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp105_AST);
				match(LITERAL_alt);
				break;
			}
			case LITERAL_shift:
			{
				AST tmp106_AST = null;
				tmp106_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp106_AST);
				match(LITERAL_shift);
				break;
			}
			case LITERAL_control:
			{
				AST tmp107_AST = null;
				tmp107_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp107_AST);
				match(LITERAL_control);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			accelerator_option_AST = (AST)currentAST.root;
			accelerator_option_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(ACCELERATOR_OPTION,"accelerator option")).add(accelerator_option_AST));
			currentAST.root = accelerator_option_AST;
			currentAST.child = accelerator_option_AST!=null &&accelerator_option_AST.getFirstChild()!=null ?
				accelerator_option_AST.getFirstChild() : accelerator_option_AST;
			currentAST.advanceChildToEnd();
			accelerator_option_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_42);
		}
		returnAST = accelerator_option_AST;
	}
	
	public final void data_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST data_block_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LEFT_BRACE:
			case LITERAL_begin:
			{
				raw_data();
				astFactory.addASTChild(currentAST, returnAST);
				data_block_AST = (AST)currentAST.root;
				data_block_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(RAW,"raw")).add(data_block_AST));
				currentAST.root = data_block_AST;
				currentAST.child = data_block_AST!=null &&data_block_AST.getFirstChild()!=null ?
					data_block_AST.getFirstChild() : data_block_AST;
				currentAST.advanceChildToEnd();
				data_block_AST = (AST)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			case IDENTIFIER:
			{
				file_name();
				astFactory.addASTChild(currentAST, returnAST);
				data_block_AST = (AST)currentAST.root;
				data_block_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(FILE_NAME,"file name")).add(data_block_AST));
				currentAST.root = data_block_AST;
				currentAST.child = data_block_AST!=null &&data_block_AST.getFirstChild()!=null ?
					data_block_AST.getFirstChild() : data_block_AST;
				currentAST.advanceChildToEnd();
				data_block_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = data_block_AST;
	}
	
	public final void dialog_control_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_control_definition_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_control:
			{
				dialog_generic_control();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_control_definition_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				dialog_concrete_control_definition();
				astFactory.addASTChild(currentAST, returnAST);
				dialog_control_definition_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialog_control_definition_AST;
	}
	
	public final void dialog_generic_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_generic_control_AST = null;
		
		try {      // for error handling
			AST tmp108_AST = null;
			tmp108_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp108_AST);
			match(LITERAL_control);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			AST tmp111_AST = null;
			tmp111_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp111_AST);
			match(STRING_LITERAL);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_generic_control_trailing();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_generic_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialog_generic_control_AST;
	}
	
	public final void dialog_concrete_control_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_concrete_control_definition_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			{
				dialog_static_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			{
				dialog_button_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			{
				dialog_edit_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_combobox:
			case LITERAL_listbox:
			{
				dialog_common_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_icon:
			{
				dialog_icon_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_scrollbar:
			{
				dialog_scrollbar_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dialog_common_control_trailing();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_concrete_control_definition_AST = (AST)currentAST.root;
			dialog_concrete_control_definition_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(CONCRETE_CONTROL,"concrete control")).add(dialog_concrete_control_definition_AST));
			currentAST.root = dialog_concrete_control_definition_AST;
			currentAST.child = dialog_concrete_control_definition_AST!=null &&dialog_concrete_control_definition_AST.getFirstChild()!=null ?
				dialog_concrete_control_definition_AST.getFirstChild() : dialog_concrete_control_definition_AST;
			currentAST.advanceChildToEnd();
			dialog_concrete_control_definition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialog_concrete_control_definition_AST;
	}
	
	public final void dialog_static_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_static_control_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_ltext:
			{
				AST tmp113_AST = null;
				tmp113_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp113_AST);
				match(LITERAL_ltext);
				break;
			}
			case LITERAL_rtext:
			{
				AST tmp114_AST = null;
				tmp114_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp114_AST);
				match(LITERAL_rtext);
				break;
			}
			case LITERAL_ctext:
			{
				AST tmp115_AST = null;
				tmp115_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp115_AST);
				match(LITERAL_ctext);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_static_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_46);
		}
		returnAST = dialog_static_control_AST;
	}
	
	public final void dialog_button_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_button_control_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case 135:
			{
				AST tmp117_AST = null;
				tmp117_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp117_AST);
				match(135);
				break;
			}
			case LITERAL_autocheckbox:
			{
				AST tmp118_AST = null;
				tmp118_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp118_AST);
				match(LITERAL_autocheckbox);
				break;
			}
			case LITERAL_autoradiobutton:
			{
				AST tmp119_AST = null;
				tmp119_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp119_AST);
				match(LITERAL_autoradiobutton);
				break;
			}
			case LITERAL_checkbox:
			{
				AST tmp120_AST = null;
				tmp120_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp120_AST);
				match(LITERAL_checkbox);
				break;
			}
			case LITERAL_pushbox:
			{
				AST tmp121_AST = null;
				tmp121_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp121_AST);
				match(LITERAL_pushbox);
				break;
			}
			case LITERAL_pushbutton:
			{
				AST tmp122_AST = null;
				tmp122_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp122_AST);
				match(LITERAL_pushbutton);
				break;
			}
			case LITERAL_defpushbutton:
			{
				AST tmp123_AST = null;
				tmp123_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp123_AST);
				match(LITERAL_defpushbutton);
				break;
			}
			case LITERAL_radiobutton:
			{
				AST tmp124_AST = null;
				tmp124_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp124_AST);
				match(LITERAL_radiobutton);
				break;
			}
			case 143:
			{
				AST tmp125_AST = null;
				tmp125_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp125_AST);
				match(143);
				break;
			}
			case LITERAL_groupbox:
			{
				AST tmp126_AST = null;
				tmp126_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp126_AST);
				match(LITERAL_groupbox);
				break;
			}
			case LITERAL_userbutton:
			{
				AST tmp127_AST = null;
				tmp127_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp127_AST);
				match(LITERAL_userbutton);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_button_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_46);
		}
		returnAST = dialog_button_control_AST;
	}
	
	public final void dialog_edit_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_edit_control_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_edittext:
			{
				AST tmp129_AST = null;
				tmp129_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp129_AST);
				match(LITERAL_edittext);
				break;
			}
			case LITERAL_bedit:
			{
				AST tmp130_AST = null;
				tmp130_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp130_AST);
				match(LITERAL_bedit);
				break;
			}
			case LITERAL_hedit:
			{
				AST tmp131_AST = null;
				tmp131_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp131_AST);
				match(LITERAL_hedit);
				break;
			}
			case LITERAL_iedit:
			{
				AST tmp132_AST = null;
				tmp132_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp132_AST);
				match(LITERAL_iedit);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_edit_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_46);
		}
		returnAST = dialog_edit_control_AST;
	}
	
	public final void dialog_common_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_common_control_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_combobox:
			{
				AST tmp133_AST = null;
				tmp133_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp133_AST);
				match(LITERAL_combobox);
				break;
			}
			case LITERAL_listbox:
			{
				AST tmp134_AST = null;
				tmp134_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp134_AST);
				match(LITERAL_listbox);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_common_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_46);
		}
		returnAST = dialog_common_control_AST;
	}
	
	public final void dialog_icon_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_icon_control_AST = null;
		
		try {      // for error handling
			AST tmp135_AST = null;
			tmp135_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp135_AST);
			match(LITERAL_icon);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_icon_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_46);
		}
		returnAST = dialog_icon_control_AST;
	}
	
	public final void dialog_scrollbar_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_scrollbar_control_AST = null;
		
		try {      // for error handling
			AST tmp137_AST = null;
			tmp137_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp137_AST);
			match(LITERAL_scrollbar);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_scrollbar_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_46);
		}
		returnAST = dialog_scrollbar_control_AST;
	}
	
	public final void dialog_common_control_trailing() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_common_control_trailing_AST = null;
		
		try {      // for error handling
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			integer_literal();
			astFactory.addASTChild(currentAST, returnAST);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case RIGHT_BRACE:
			case LITERAL_end:
			case LITERAL_control:
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dialog_common_control_trailing_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialog_common_control_trailing_AST;
	}
	
	public final void dialog_generic_control_trailing() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_generic_control_trailing_AST = null;
		
		try {      // for error handling
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialog_generic_control_trailing_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialog_generic_control_trailing_AST;
	}
	
	public final void dialogex_control_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialogex_control_definition_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_control:
			{
				dialogex_generic_control();
				astFactory.addASTChild(currentAST, returnAST);
				dialogex_control_definition_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				dialogex_concrete_control_definition();
				astFactory.addASTChild(currentAST, returnAST);
				dialogex_control_definition_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialogex_control_definition_AST;
	}
	
	public final void dialogex_generic_control() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialogex_generic_control_AST = null;
		
		try {      // for error handling
			AST tmp148_AST = null;
			tmp148_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp148_AST);
			match(LITERAL_control);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			AST tmp151_AST = null;
			tmp151_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp151_AST);
			match(STRING_LITERAL);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			dialogex_generic_control_trailing();
			astFactory.addASTChild(currentAST, returnAST);
			dialogex_generic_control_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialogex_generic_control_AST;
	}
	
	public final void dialogex_concrete_control_definition() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialogex_concrete_control_definition_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			{
				dialog_static_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			{
				dialog_button_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			{
				dialog_edit_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_combobox:
			case LITERAL_listbox:
			{
				dialog_common_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_icon:
			{
				dialog_icon_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_scrollbar:
			{
				dialog_scrollbar_control();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dialogex_common_control_trailing();
			astFactory.addASTChild(currentAST, returnAST);
			dialogex_concrete_control_definition_AST = (AST)currentAST.root;
			dialogex_concrete_control_definition_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(CONCRETE_CONTROL,"concrete control")).add(dialogex_concrete_control_definition_AST));
			currentAST.root = dialogex_concrete_control_definition_AST;
			currentAST.child = dialogex_concrete_control_definition_AST!=null &&dialogex_concrete_control_definition_AST.getFirstChild()!=null ?
				dialogex_concrete_control_definition_AST.getFirstChild() : dialogex_concrete_control_definition_AST;
			currentAST.advanceChildToEnd();
			dialogex_concrete_control_definition_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialogex_concrete_control_definition_AST;
	}
	
	public final void dialogex_common_control_trailing() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialogex_common_control_trailing_AST = null;
		
		try {      // for error handling
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			if ((LA(1)==COMMA) && (_tokenSet_27.member(LA(2))) && (_tokenSet_47.member(LA(3)))) {
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				if ((LA(1)==COMMA) && (_tokenSet_27.member(LA(2))) && (_tokenSet_47.member(LA(3)))) {
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else if ((_tokenSet_48.member(LA(1))) && (_tokenSet_49.member(LA(2))) && (_tokenSet_50.member(LA(3)))) {
				}
				else {
					throw new NoViableAltException(LT(1), getFilename());
				}
				
				}
			}
			else if ((_tokenSet_48.member(LA(1))) && (_tokenSet_49.member(LA(2))) && (_tokenSet_50.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case RIGHT_BRACE:
			case LITERAL_end:
			case LITERAL_control:
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case LEFT_BRACE:
			{
				match(LEFT_BRACE);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop151:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop151;
					}
					
				} while (true);
				}
				match(RIGHT_BRACE);
				break;
			}
			case RIGHT_BRACE:
			case LITERAL_end:
			case LITERAL_control:
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dialogex_common_control_trailing_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialogex_common_control_trailing_AST;
	}
	
	public final void dialogex_generic_control_trailing() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialogex_generic_control_trailing_AST = null;
		
		try {      // for error handling
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			{
			if ((LA(1)==COMMA) && (_tokenSet_27.member(LA(2))) && (_tokenSet_47.member(LA(3)))) {
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
			}
			else if ((_tokenSet_48.member(LA(1))) && (_tokenSet_49.member(LA(2))) && (_tokenSet_50.member(LA(3)))) {
			}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case RIGHT_BRACE:
			case LITERAL_end:
			case LITERAL_control:
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case LEFT_BRACE:
			{
				match(LEFT_BRACE);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop144:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop144;
					}
					
				} while (true);
				}
				match(RIGHT_BRACE);
				break;
			}
			case RIGHT_BRACE:
			case LITERAL_end:
			case LITERAL_control:
			case LITERAL_ltext:
			case LITERAL_rtext:
			case LITERAL_ctext:
			case 135:
			case LITERAL_autocheckbox:
			case LITERAL_autoradiobutton:
			case LITERAL_checkbox:
			case LITERAL_pushbox:
			case LITERAL_pushbutton:
			case LITERAL_defpushbutton:
			case LITERAL_radiobutton:
			case 143:
			case LITERAL_groupbox:
			case LITERAL_userbutton:
			case LITERAL_edittext:
			case LITERAL_bedit:
			case LITERAL_hedit:
			case LITERAL_iedit:
			case LITERAL_combobox:
			case LITERAL_listbox:
			case LITERAL_icon:
			case LITERAL_scrollbar:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dialogex_generic_control_trailing_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_45);
		}
		returnAST = dialogex_generic_control_trailing_AST;
	}
	
	public final void menu_entry_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menu_entry_item_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_menuitem:
			{
				menu_item();
				astFactory.addASTChild(currentAST, returnAST);
				menu_entry_item_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_popup:
			{
				popup_entry();
				astFactory.addASTChild(currentAST, returnAST);
				menu_entry_item_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_51);
		}
		returnAST = menu_entry_item_AST;
	}
	
	public final void menu_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menu_item_AST = null;
		
		try {      // for error handling
			AST tmp172_AST = null;
			tmp172_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp172_AST);
			match(LITERAL_menuitem);
			{
			switch ( LA(1)) {
			case LITERAL_separator:
			{
				AST tmp173_AST = null;
				tmp173_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp173_AST);
				match(LITERAL_separator);
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp174_AST = null;
				tmp174_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp174_AST);
				match(STRING_LITERAL);
				match(COMMA);
				resource_identifier();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop165:
				do {
					if ((_tokenSet_52.member(LA(1)))) {
						menu_item_option();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop165;
					}
					
				} while (true);
				}
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			menu_item_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_51);
		}
		returnAST = menu_item_AST;
	}
	
	public final void menu_item_option() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menu_item_option_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case LITERAL_checked:
			case LITERAL_grayed:
			case LITERAL_help:
			case LITERAL_inactive:
			case LITERAL_menubarbreak:
			case LITERAL_menubreak:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case LITERAL_checked:
			{
				AST tmp177_AST = null;
				tmp177_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp177_AST);
				match(LITERAL_checked);
				break;
			}
			case LITERAL_grayed:
			{
				AST tmp178_AST = null;
				tmp178_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp178_AST);
				match(LITERAL_grayed);
				break;
			}
			case LITERAL_help:
			{
				AST tmp179_AST = null;
				tmp179_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp179_AST);
				match(LITERAL_help);
				break;
			}
			case LITERAL_inactive:
			{
				AST tmp180_AST = null;
				tmp180_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp180_AST);
				match(LITERAL_inactive);
				break;
			}
			case LITERAL_menubarbreak:
			{
				AST tmp181_AST = null;
				tmp181_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp181_AST);
				match(LITERAL_menubarbreak);
				break;
			}
			case LITERAL_menubreak:
			{
				AST tmp182_AST = null;
				tmp182_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp182_AST);
				match(LITERAL_menubreak);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			menu_item_option_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_53);
		}
		returnAST = menu_item_option_AST;
	}
	
	public final void popup_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST popup_entry_AST = null;
		
		try {      // for error handling
			AST tmp183_AST = null;
			tmp183_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp183_AST);
			match(LITERAL_popup);
			AST tmp184_AST = null;
			tmp184_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp184_AST);
			match(STRING_LITERAL);
			{
			_loop171:
			do {
				if ((_tokenSet_52.member(LA(1)))) {
					menu_item_option();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop171;
				}
				
			} while (true);
			}
			open_definition();
			{
			_loop173:
			do {
				if ((_tokenSet_35.member(LA(1)))) {
					menu_entry_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop173;
				}
				
			} while (true);
			}
			close_definition();
			popup_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_51);
		}
		returnAST = popup_entry_AST;
	}
	
	public final void menuex_entry_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menuex_entry_item_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_menuitem:
			{
				menuex_item();
				astFactory.addASTChild(currentAST, returnAST);
				menuex_entry_item_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_popup:
			{
				popupex_entry();
				astFactory.addASTChild(currentAST, returnAST);
				menuex_entry_item_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_51);
		}
		returnAST = menuex_entry_item_AST;
	}
	
	public final void menuex_item() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST menuex_item_AST = null;
		
		try {      // for error handling
			AST tmp185_AST = null;
			tmp185_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp185_AST);
			match(LITERAL_menuitem);
			AST tmp186_AST = null;
			tmp186_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp186_AST);
			match(STRING_LITERAL);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					{
					switch ( LA(1)) {
					case COMMA:
					{
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						break;
					}
					case RIGHT_BRACE:
					case LITERAL_end:
					case LITERAL_menuitem:
					case LITERAL_popup:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case RIGHT_BRACE:
				case LITERAL_end:
				case LITERAL_menuitem:
				case LITERAL_popup:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case RIGHT_BRACE:
			case LITERAL_end:
			case LITERAL_menuitem:
			case LITERAL_popup:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			menuex_item_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_51);
		}
		returnAST = menuex_item_AST;
	}
	
	public final void popupex_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST popupex_entry_AST = null;
		
		try {      // for error handling
			AST tmp190_AST = null;
			tmp190_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp190_AST);
			match(LITERAL_popup);
			AST tmp191_AST = null;
			tmp191_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp191_AST);
			match(STRING_LITERAL);
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					match(COMMA);
					expression();
					astFactory.addASTChild(currentAST, returnAST);
					{
					switch ( LA(1)) {
					case COMMA:
					{
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
						{
						switch ( LA(1)) {
						case COMMA:
						{
							match(COMMA);
							expression();
							astFactory.addASTChild(currentAST, returnAST);
							break;
						}
						case LEFT_BRACE:
						case LITERAL_begin:
						{
							break;
						}
						default:
						{
							throw new NoViableAltException(LT(1), getFilename());
						}
						}
						}
						break;
					}
					case LEFT_BRACE:
					case LITERAL_begin:
					{
						break;
					}
					default:
					{
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					break;
				}
				case LEFT_BRACE:
				case LITERAL_begin:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			open_definition();
			{
			_loop191:
			do {
				if ((_tokenSet_35.member(LA(1)))) {
					menuex_entry_item();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop191;
				}
				
			} while (true);
			}
			close_definition();
			popupex_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_51);
		}
		returnAST = popupex_entry_AST;
	}
	
	public final void raw_data() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST raw_data_AST = null;
		
		try {      // for error handling
			open_definition();
			{
			_loop201:
			do {
				if ((_tokenSet_54.member(LA(1)))) {
					raw_data_entry();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop201;
				}
				
			} while (true);
			}
			close_definition();
			raw_data_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = raw_data_AST;
	}
	
	public final void raw_data_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST raw_data_entry_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LONG_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				integer_literal();
				astFactory.addASTChild(currentAST, returnAST);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					match(COMMA);
					break;
				}
				case LONG_LITERAL:
				case RIGHT_BRACE:
				case STRING_LITERAL:
				case NUMERAL:
				case HEX_LITERAL:
				case OCTAL_LITERAL:
				case LITERAL_end:
				case DATA_STRING_LITERAL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				raw_data_entry_AST = (AST)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp197_AST = null;
				tmp197_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp197_AST);
				match(STRING_LITERAL);
				{
				switch ( LA(1)) {
				case COMMA:
				{
					match(COMMA);
					break;
				}
				case LONG_LITERAL:
				case RIGHT_BRACE:
				case STRING_LITERAL:
				case NUMERAL:
				case HEX_LITERAL:
				case OCTAL_LITERAL:
				case LITERAL_end:
				case DATA_STRING_LITERAL:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				raw_data_entry_AST = (AST)currentAST.root;
				break;
			}
			case DATA_STRING_LITERAL:
			{
				AST tmp199_AST = null;
				tmp199_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp199_AST);
				match(DATA_STRING_LITERAL);
				raw_data_entry_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_55);
		}
		returnAST = raw_data_entry_AST;
	}
	
	public final void version_fixed_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_fixed_info_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_fileversion:
			{
				AST tmp200_AST = null;
				tmp200_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp200_AST);
				match(LITERAL_fileversion);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop216:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop216;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_productversion:
			{
				AST tmp202_AST = null;
				tmp202_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp202_AST);
				match(LITERAL_productversion);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				{
				_loop218:
				do {
					if ((LA(1)==COMMA)) {
						match(COMMA);
						expression();
						astFactory.addASTChild(currentAST, returnAST);
					}
					else {
						break _loop218;
					}
					
				} while (true);
				}
				break;
			}
			case LITERAL_fileflagsmask:
			{
				AST tmp204_AST = null;
				tmp204_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp204_AST);
				match(LITERAL_fileflagsmask);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_fileflags:
			{
				AST tmp205_AST = null;
				tmp205_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp205_AST);
				match(LITERAL_fileflags);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_fileos:
			{
				AST tmp206_AST = null;
				tmp206_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp206_AST);
				match(LITERAL_fileos);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_filetype:
			{
				AST tmp207_AST = null;
				tmp207_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp207_AST);
				match(LITERAL_filetype);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LITERAL_filesubtype:
			{
				AST tmp208_AST = null;
				tmp208_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp208_AST);
				match(LITERAL_filesubtype);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			version_fixed_info_AST = (AST)currentAST.root;
			version_fixed_info_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(VERSION_FIXED_INFO,"version fixed info")).add(version_fixed_info_AST));
			currentAST.root = version_fixed_info_AST;
			currentAST.child = version_fixed_info_AST!=null &&version_fixed_info_AST.getFirstChild()!=null ?
				version_fixed_info_AST.getFirstChild() : version_fixed_info_AST;
			currentAST.advanceChildToEnd();
			version_fixed_info_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_56);
		}
		returnAST = version_fixed_info_AST;
	}
	
	public final void version_info_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_info_block_AST = null;
		
		try {      // for error handling
			match(LITERAL_block);
			{
			int _cnt221=0;
			_loop221:
			do {
				switch ( LA(1)) {
				case STRING_FILE_INFO:
				{
					version_string_file_info();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				case VAR_FILE_INFO:
				{
					version_var_file_info();
					astFactory.addASTChild(currentAST, returnAST);
					break;
				}
				default:
				{
					if ( _cnt221>=1 ) { break _loop221; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				}
				_cnt221++;
			} while (true);
			}
			version_info_block_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_57);
		}
		returnAST = version_info_block_AST;
	}
	
	public final void version_string_file_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_string_file_info_AST = null;
		
		try {      // for error handling
			AST tmp210_AST = null;
			tmp210_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp210_AST);
			match(STRING_FILE_INFO);
			open_definition();
			{
			_loop224:
			do {
				if ((LA(1)==LITERAL_block)) {
					version_sfi_block_content();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop224;
				}
				
			} while (true);
			}
			close_definition();
			version_string_file_info_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_58);
		}
		returnAST = version_string_file_info_AST;
	}
	
	public final void version_var_file_info() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_var_file_info_AST = null;
		
		try {      // for error handling
			AST tmp211_AST = null;
			tmp211_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp211_AST);
			match(VAR_FILE_INFO);
			open_definition();
			{
			_loop231:
			do {
				if ((LA(1)==LITERAL_value)) {
					version_vfi_entry();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop231;
				}
				
			} while (true);
			}
			close_definition();
			version_var_file_info_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_58);
		}
		returnAST = version_var_file_info_AST;
	}
	
	public final void version_sfi_block_content() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_sfi_block_content_AST = null;
		
		try {      // for error handling
			AST tmp212_AST = null;
			tmp212_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp212_AST);
			match(LITERAL_block);
			expression();
			astFactory.addASTChild(currentAST, returnAST);
			open_definition();
			{
			_loop227:
			do {
				if ((LA(1)==LITERAL_value)) {
					version_sfi_entry();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop227;
				}
				
			} while (true);
			}
			close_definition();
			version_sfi_block_content_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_57);
		}
		returnAST = version_sfi_block_content_AST;
	}
	
	public final void version_sfi_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_sfi_entry_AST = null;
		
		try {      // for error handling
			AST tmp213_AST = null;
			tmp213_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp213_AST);
			match(LITERAL_value);
			literal();
			astFactory.addASTChild(currentAST, returnAST);
			match(COMMA);
			literal();
			astFactory.addASTChild(currentAST, returnAST);
			version_sfi_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_59);
		}
		returnAST = version_sfi_entry_AST;
	}
	
	public final void version_vfi_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST version_vfi_entry_AST = null;
		
		try {      // for error handling
			AST tmp215_AST = null;
			tmp215_AST = astFactory.create(LT(1));
			astFactory.makeASTRoot(currentAST, tmp215_AST);
			match(LITERAL_value);
			AST tmp216_AST = null;
			tmp216_AST = astFactory.create(LT(1));
			astFactory.addASTChild(currentAST, tmp216_AST);
			match(TRANSLATION);
			match(COMMA);
			{
			int _cnt234=0;
			_loop234:
			do {
				if ((_tokenSet_60.member(LA(1)))) {
					integer_literal();
					astFactory.addASTChild(currentAST, returnAST);
					match(COMMA);
					integer_literal();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					if ( _cnt234>=1 ) { break _loop234; } else {throw new NoViableAltException(LT(1), getFilename());}
				}
				
				_cnt234++;
			} while (true);
			}
			version_vfi_entry_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_59);
		}
		returnAST = version_vfi_entry_AST;
	}
	
	public final void common_textinclude_part() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST common_textinclude_part_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_preload:
			case LITERAL_loadoncall:
			case LITERAL_fixed:
			case LITERAL_moveable:
			case LITERAL_discardable:
			case LITERAL_pure:
			case LITERAL_impure:
			case LITERAL_shared:
			case LITERAL_nonshared:
			{
				common_resource_attributes();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case LEFT_BRACE:
			case LITERAL_begin:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			open_definition();
			{
			_loop239:
			do {
				if ((LA(1)==STRING_LITERAL)) {
					AST tmp219_AST = null;
					tmp219_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp219_AST);
					match(STRING_LITERAL);
				}
				else {
					break _loop239;
				}
				
			} while (true);
			}
			close_definition();
			common_textinclude_part_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
		}
		returnAST = common_textinclude_part_AST;
	}
	
	public final void design_info_control_block() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST design_info_control_block_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LONG_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				resource_identifier();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp220_AST = null;
				tmp220_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp220_AST);
				match(STRING_LITERAL);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(COMMA);
			resource_entry_class();
			astFactory.addASTChild(currentAST, returnAST);
			open_definition();
			design_entries();
			astFactory.addASTChild(currentAST, returnAST);
			close_definition();
			design_info_control_block_AST = (AST)currentAST.root;
			design_info_control_block_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(DESIGN_INFO_CONTROL_BLOCK,"design info control block")).add(design_info_control_block_AST));
			currentAST.root = design_info_control_block_AST;
			currentAST.child = design_info_control_block_AST!=null &&design_info_control_block_AST.getFirstChild()!=null ?
				design_info_control_block_AST.getFirstChild() : design_info_control_block_AST;
			currentAST.advanceChildToEnd();
			design_info_control_block_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_44);
		}
		returnAST = design_info_control_block_AST;
	}
	
	public final void resource_entry_class() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST resource_entry_class_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_accelerators:
			{
				AST tmp222_AST = null;
				tmp222_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp222_AST);
				match(LITERAL_accelerators);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_bitmap:
			{
				AST tmp223_AST = null;
				tmp223_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp223_AST);
				match(LITERAL_bitmap);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_cursor:
			{
				AST tmp224_AST = null;
				tmp224_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp224_AST);
				match(LITERAL_cursor);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_dialog:
			{
				AST tmp225_AST = null;
				tmp225_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp225_AST);
				match(LITERAL_dialog);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_dialogex:
			{
				AST tmp226_AST = null;
				tmp226_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp226_AST);
				match(LITERAL_dialogex);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_font:
			{
				AST tmp227_AST = null;
				tmp227_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp227_AST);
				match(LITERAL_font);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_icon:
			{
				AST tmp228_AST = null;
				tmp228_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp228_AST);
				match(LITERAL_icon);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_menu:
			{
				AST tmp229_AST = null;
				tmp229_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp229_AST);
				match(LITERAL_menu);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_menuex:
			{
				AST tmp230_AST = null;
				tmp230_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp230_AST);
				match(LITERAL_menuex);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_messagetable:
			{
				AST tmp231_AST = null;
				tmp231_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp231_AST);
				match(LITERAL_messagetable);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_popup:
			{
				AST tmp232_AST = null;
				tmp232_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp232_AST);
				match(LITERAL_popup);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_rcdata:
			{
				AST tmp233_AST = null;
				tmp233_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp233_AST);
				match(LITERAL_rcdata);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_stringtable:
			{
				AST tmp234_AST = null;
				tmp234_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp234_AST);
				match(LITERAL_stringtable);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_versioninfo:
			{
				AST tmp235_AST = null;
				tmp235_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp235_AST);
				match(LITERAL_versioninfo);
				resource_entry_class_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_61);
		}
		returnAST = resource_entry_class_AST;
	}
	
	public final void design_entries() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST design_entries_AST = null;
		
		try {      // for error handling
			{
			_loop251:
			do {
				if ((LA(1)==IDENTIFIER)) {
					AST tmp236_AST = null;
					tmp236_AST = astFactory.create(LT(1));
					astFactory.addASTChild(currentAST, tmp236_AST);
					match(IDENTIFIER);
					match(COMMA);
					integer_literal();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop251;
				}
				
			} while (true);
			}
			design_entries_AST = (AST)currentAST.root;
			design_entries_AST = (AST)astFactory.make( (new ASTArray(2)).add(astFactory.create(DESIGN_INFO_ENTRIES,"design info entries")).add(design_entries_AST));
			currentAST.root = design_entries_AST;
			currentAST.child = design_entries_AST!=null &&design_entries_AST.getFirstChild()!=null ?
				design_entries_AST.getFirstChild() : design_entries_AST;
			currentAST.advanceChildToEnd();
			design_entries_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_62);
		}
		returnAST = design_entries_AST;
	}
	
	public final void toolbar_entry() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST toolbar_entry_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case LITERAL_button:
			{
				AST tmp238_AST = null;
				tmp238_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp238_AST);
				match(LITERAL_button);
				expression();
				astFactory.addASTChild(currentAST, returnAST);
				toolbar_entry_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_separator:
			{
				AST tmp239_AST = null;
				tmp239_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp239_AST);
				match(LITERAL_separator);
				toolbar_entry_AST = (AST)currentAST.root;
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_63);
		}
		returnAST = toolbar_entry_AST;
	}
	
	public final void dialog_init_entries() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST dialog_init_entries_AST = null;
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case IDENTIFIER:
			{
				AST tmp240_AST = null;
				tmp240_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp240_AST);
				match(IDENTIFIER);
				break;
			}
			case LONG_LITERAL:
			case STRING_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_l:
			{
				literal();
				astFactory.addASTChild(currentAST, returnAST);
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			{
			switch ( LA(1)) {
			case COMMA:
			{
				match(COMMA);
				break;
			}
			case LONG_LITERAL:
			case RIGHT_BRACE:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_end:
			case LITERAL_l:
			{
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			dialog_init_entries_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_64);
		}
		returnAST = dialog_init_entries_AST;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"FLOAT_LITERAL",
		"DOUBLE_LITERAL",
		"BIGINT_LITERAL",
		"LONG_LITERAL",
		"INTEGER_LITERAL",
		"SHORT_LITERAL",
		"BYTE_LITERAL",
		"EXPR",
		"UNARY_MINUS",
		"UNARY_PLUS",
		"POST_INC",
		"POST_DEC",
		"UNICODE_LETTER",
		"UNICODE_CHARACTER_DIGIT",
		"LETTER",
		"LETTER_OR_DIGIT",
		"LEFT_PARENTHESE",
		"RIGHT_PARENTHESE",
		"LEFT_BRACE",
		"RIGHT_BRACE",
		"LEFT_BRACKET",
		"RIGHT_BRACKET",
		"SEMICOLON",
		"COMMA",
		"APOSTROPHE",
		"DOT",
		"COLON",
		"LOGICAL_AND",
		"LOGICAL_OR",
		"BITWISE_AND",
		"BITWISE_OR",
		"LOGICAL_NOT",
		"BITWISE_NOT",
		"BITWISE_XOR",
		"PLUS",
		"MINUS",
		"STAR",
		"DIV",
		"MOD",
		"SHIFT_LEFT",
		"SHIFT_RIGHT",
		"EQUAL",
		"UNEQUAL",
		"LESS_THAN",
		"GREATER_THAN",
		"LESS_THAN_EQUAL",
		"GREATER_THAN_EQUAL",
		"INC",
		"DEC",
		"ASSIGN",
		"HORIZONTAL_TABULATOR",
		"VERTICAL_TABULATOR",
		"LINE_FEED",
		"FORM_FEED",
		"CARRIAGE_RETURN",
		"SPACE",
		"LINE_SEPARATOR",
		"PARAGRAPH_SEPARATOR",
		"DIGIT",
		"HEX_DIGIT",
		"OCTAL_DIGIT",
		"ZERO_TO_THREE",
		"OCTAL_NUMERAL",
		"EXPONENT_PART",
		"FLOAT_SUFFIX",
		"WHITE_SPACE",
		"STRING_CHARACTER",
		"CHARACTER_LITERAL",
		"STRING_LITERAL_PART",
		"STRING_LITERAL",
		"NUMERAL",
		"IDENTIFIER",
		"HEX_LITERAL",
		"OCTAL_LITERAL",
		"\"not\"",
		"\"true\"",
		"\"false\"",
		"CONCRETE_CONTROL",
		"ACCELERATOR",
		"RESOURCE_ATTRIBUTES",
		"STRING_TABLE_ENTRY",
		"DESIGN_INFO_CONTROL_BLOCK",
		"NAMED_RESOURCE",
		"RAW",
		"FILE_NAME",
		"USER_DEFINED",
		"DESIGN_INFO_ENTRIES",
		"COMMON_RESOURCE_INFO",
		"VERSION_FIXED_INFO",
		"ACCELERATOR_TYPE",
		"ACCELERATOR_OPTION",
		"\"pragma\"",
		"\"code_page\"",
		"\"default\"",
		"\"begin\"",
		"\"end\"",
		"NUMBER_SIGN",
		"\"preload\"",
		"\"loadoncall\"",
		"\"fixed\"",
		"\"moveable\"",
		"\"discardable\"",
		"\"pure\"",
		"\"impure\"",
		"\"shared\"",
		"\"nonshared\"",
		"\"l\"",
		"\"language\"",
		"\"stringtable\"",
		"PATH_SEPARATOR",
		"\"characteristics\"",
		"\"version\"",
		"\"caption\"",
		"\"class\"",
		"\"style\"",
		"\"exstyle\"",
		"\"font\"",
		"\"menu\"",
		"\"accelerators\"",
		"\"ascii\"",
		"\"virtkey\"",
		"\"noinvert\"",
		"\"alt\"",
		"\"shift\"",
		"\"control\"",
		"\"bitmap\"",
		"\"cursor\"",
		"\"dialog\"",
		"\"ltext\"",
		"\"rtext\"",
		"\"ctext\"",
		"\"auto3state\"",
		"\"autocheckbox\"",
		"\"autoradiobutton\"",
		"\"checkbox\"",
		"\"pushbox\"",
		"\"pushbutton\"",
		"\"defpushbutton\"",
		"\"radiobutton\"",
		"\"state3\"",
		"\"groupbox\"",
		"\"userbutton\"",
		"\"edittext\"",
		"\"bedit\"",
		"\"hedit\"",
		"\"iedit\"",
		"\"combobox\"",
		"\"listbox\"",
		"\"icon\"",
		"\"scrollbar\"",
		"\"dialogex\"",
		"\"menuitem\"",
		"\"separator\"",
		"\"checked\"",
		"\"grayed\"",
		"\"help\"",
		"\"inactive\"",
		"\"menubarbreak\"",
		"\"menubreak\"",
		"\"popup\"",
		"\"menuex\"",
		"\"messagetable\"",
		"\"rcdata\"",
		"DATA_STRING_LITERAL",
		"\"versioninfo\"",
		"\"fileversion\"",
		"\"productversion\"",
		"\"fileflagsmask\"",
		"\"fileflags\"",
		"\"fileos\"",
		"\"filetype\"",
		"\"filesubtype\"",
		"\"block\"",
		"STRING_FILE_INFO",
		"\"value\"",
		"VAR_FILE_INFO",
		"TRANSLATION",
		"\"textinclude\"",
		"\"designinfo\"",
		"\"toolbar\"",
		"\"button\"",
		"\"dlginit\""
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 128L, 422281184558080L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 130L, 422281184558080L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 9007094176940162L, 575897785170148864L, 280630551780524031L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 148897922L, 575827416425971200L, 207167231807717390L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 9007094176940160L, 287315634017025536L, 73462804727726065L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 9007197257203840L, 287315634017041920L, 73462804727726065L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 2097152L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 146800768L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 148897920L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 4443865216L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 6591348864L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 23771218048L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	private static final long[] mk_tokenSet_13() {
		long[] data = { 161210171520L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_13 = new BitSet(mk_tokenSet_13());
	private static final long[] mk_tokenSet_14() {
		long[] data = { 105553116266496L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_14 = new BitSet(mk_tokenSet_14());
	private static final long[] mk_tokenSet_15() {
		long[] data = { 169800106112L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_15 = new BitSet(mk_tokenSet_15());
	private static final long[] mk_tokenSet_16() {
		long[] data = { 105722916372608L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_16 = new BitSet(mk_tokenSet_16());
	private static final long[] mk_tokenSet_17() {
		long[] data = { 26388279066624L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_17 = new BitSet(mk_tokenSet_17());
	private static final long[] mk_tokenSet_18() {
		long[] data = { 2216785241702528L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_18 = new BitSet(mk_tokenSet_18());
	private static final long[] mk_tokenSet_19() {
		long[] data = { 824633720832L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_19 = new BitSet(mk_tokenSet_19());
	private static final long[] mk_tokenSet_20() {
		long[] data = { 2243173520769152L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_20 = new BitSet(mk_tokenSet_20());
	private static final long[] mk_tokenSet_21() {
		long[] data = { 2243998154489984L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_21 = new BitSet(mk_tokenSet_21());
	private static final long[] mk_tokenSet_22() {
		long[] data = { 2251694735884416L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_22 = new BitSet(mk_tokenSet_22());
	private static final long[] mk_tokenSet_23() {
		long[] data = { 9007094176940160L, 287315634017025536L, 72336904820883441L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_23 = new BitSet(mk_tokenSet_23());
	private static final long[] mk_tokenSet_24() {
		long[] data = { 6756327163428992L, 70403103948288L, 73465553506795505L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_24 = new BitSet(mk_tokenSet_24());
	private static final long[] mk_tokenSet_25() {
		long[] data = { 8388738L, 422315544296960L, 3096259237773312L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_25 = new BitSet(mk_tokenSet_25());
	private static final long[] mk_tokenSet_26() {
		long[] data = { 4194434L, 287526774609296896L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_26 = new BitSet(mk_tokenSet_26());
	private static final long[] mk_tokenSet_27() {
		long[] data = { 6756327155040384L, 70368744209920L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_27 = new BitSet(mk_tokenSet_27());
	private static final long[] mk_tokenSet_28() {
		long[] data = { 6756327159234688L, 3588823132962304L, 279275953455104L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_28 = new BitSet(mk_tokenSet_28());
	private static final long[] mk_tokenSet_29() {
		long[] data = { 6756327159234688L, 3659054438186496L, 279275953455104L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_29 = new BitSet(mk_tokenSet_29());
	private static final long[] mk_tokenSet_30() {
		long[] data = { 6756327163428992L, 70403103948288L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_30 = new BitSet(mk_tokenSet_30());
	private static final long[] mk_tokenSet_31() {
		long[] data = { 0L, 3518437208883200L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_31 = new BitSet(mk_tokenSet_31());
	private static final long[] mk_tokenSet_32() {
		long[] data = { 128L, 15872L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_32 = new BitSet(mk_tokenSet_32());
	private static final long[] mk_tokenSet_33() {
		long[] data = { 0L, 287245213733224448L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_33 = new BitSet(mk_tokenSet_33());
	private static final long[] mk_tokenSet_34() {
		long[] data = { 0L, 0L, 67108849L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_34 = new BitSet(mk_tokenSet_34());
	private static final long[] mk_tokenSet_35() {
		long[] data = { 0L, 0L, 34493956096L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_35 = new BitSet(mk_tokenSet_35());
	private static final long[] mk_tokenSet_36() {
		long[] data = { 0L, 0L, 72057594306363392L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_36 = new BitSet(mk_tokenSet_36());
	private static final long[] mk_tokenSet_37() {
		long[] data = { 128L, 70368744193536L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_37 = new BitSet(mk_tokenSet_37());
	private static final long[] mk_tokenSet_38() {
		long[] data = { 4194304L, 287245230913096192L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_38 = new BitSet(mk_tokenSet_38());
	private static final long[] mk_tokenSet_39() {
		long[] data = { 4194304L, 3518454388754944L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_39 = new BitSet(mk_tokenSet_39());
	private static final long[] mk_tokenSet_40() {
		long[] data = { 4194304L, 287245230913093632L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_40 = new BitSet(mk_tokenSet_40());
	private static final long[] mk_tokenSet_41() {
		long[] data = { 0L, 1729382256910270464L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_41 = new BitSet(mk_tokenSet_41());
	private static final long[] mk_tokenSet_42() {
		long[] data = { 142606464L, 34359754240L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_42 = new BitSet(mk_tokenSet_42());
	private static final long[] mk_tokenSet_43() {
		long[] data = { 134217858L, -2305420728029135872L, 1L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_43 = new BitSet(mk_tokenSet_43());
	private static final long[] mk_tokenSet_44() {
		long[] data = { 8388736L, 34359754240L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_44 = new BitSet(mk_tokenSet_44());
	private static final long[] mk_tokenSet_45() {
		long[] data = { 8388608L, 34359738368L, 67108849L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_45 = new BitSet(mk_tokenSet_45());
	private static final long[] mk_tokenSet_46() {
		long[] data = { 134217728L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_46 = new BitSet(mk_tokenSet_46());
	private static final long[] mk_tokenSet_47() {
		long[] data = { 9007197255106688L, 70403103948288L, 67108849L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_47 = new BitSet(mk_tokenSet_47());
	private static final long[] mk_tokenSet_48() {
		long[] data = { 146800640L, 34359738368L, 67108849L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_48 = new BitSet(mk_tokenSet_48());
	private static final long[] mk_tokenSet_49() {
		long[] data = { 6756327155040386L, 492649928752640L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_49 = new BitSet(mk_tokenSet_49());
	private static final long[] mk_tokenSet_50() {
		long[] data = { 9007197255106690L, 504543812002020864L, 207167163541225471L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_50 = new BitSet(mk_tokenSet_50());
	private static final long[] mk_tokenSet_51() {
		long[] data = { 8388608L, 34359738368L, 34493956096L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_51 = new BitSet(mk_tokenSet_51());
	private static final long[] mk_tokenSet_52() {
		long[] data = { 134217728L, 0L, 33822867456L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_52 = new BitSet(mk_tokenSet_52());
	private static final long[] mk_tokenSet_53() {
		long[] data = { 146800640L, 51539607552L, 68316823552L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_53 = new BitSet(mk_tokenSet_53());
	private static final long[] mk_tokenSet_54() {
		long[] data = { 128L, 13824L, 549755813888L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_54 = new BitSet(mk_tokenSet_54());
	private static final long[] mk_tokenSet_55() {
		long[] data = { 8388736L, 34359752192L, 549755813888L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_55 = new BitSet(mk_tokenSet_55());
	private static final long[] mk_tokenSet_56() {
		long[] data = { 4194304L, 3518454388752384L, 279275953455104L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_56 = new BitSet(mk_tokenSet_56());
	private static final long[] mk_tokenSet_57() {
		long[] data = { 8388608L, 34359738368L, 281474976710656L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_57 = new BitSet(mk_tokenSet_57());
	private static final long[] mk_tokenSet_58() {
		long[] data = { 8388608L, 34359738368L, 3096224743817216L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_58 = new BitSet(mk_tokenSet_58());
	private static final long[] mk_tokenSet_59() {
		long[] data = { 8388608L, 34359738368L, 1125899906842624L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_59 = new BitSet(mk_tokenSet_59());
	private static final long[] mk_tokenSet_60() {
		long[] data = { 128L, 13312L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_60 = new BitSet(mk_tokenSet_60());
	private static final long[] mk_tokenSet_61() {
		long[] data = { 4194304L, 17179869184L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_61 = new BitSet(mk_tokenSet_61());
	private static final long[] mk_tokenSet_62() {
		long[] data = { 8388608L, 34359738368L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_62 = new BitSet(mk_tokenSet_62());
	private static final long[] mk_tokenSet_63() {
		long[] data = { 8388608L, 34359738368L, 72057594306363392L, 0L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_63 = new BitSet(mk_tokenSet_63());
	private static final long[] mk_tokenSet_64() {
		long[] data = { 8388736L, 70403103931904L, 0L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_64 = new BitSet(mk_tokenSet_64());
	
	}
