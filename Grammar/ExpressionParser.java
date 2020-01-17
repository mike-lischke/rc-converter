// $ANTLR : "Expression parser.g" -> "ExpressionParser.java"$

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

public class ExpressionParser extends antlr.LLkParser       implements ExpressionLexerTokenTypes
 {

  /** List of event listeners who want to get notified about an parser event. */
  ArrayList listeners = new ArrayList();
  boolean hadErrors;
  boolean hadWarnings;
  
  //------------------------------------------------------------------------------------------------
  
  public static AST parse(String expression, String filename, int line, int column, 
    IParseEventListener listener) throws RecognitionException, TokenStreamException
  {
    StringReader input = new StringReader(expression);
    ExpressionLexer lexer = new ExpressionLexer(input);
    lexer.addLexerEventListener(listener);
    lexer.setFilename(filename);
    lexer.setLine(line);
    lexer.setColumn(column);
    ExpressionParser parser = new ExpressionParser(lexer);
    parser.addParserEventListener(listener);
    parser.setFilename(filename);
    
    // Start parsing.
    parser.expression();
    
    return parser.getAST();
  }
  
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
  

protected ExpressionParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public ExpressionParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected ExpressionParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

public ExpressionParser(TokenStream lexer) {
  this(lexer,2);
}

public ExpressionParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
  buildTokenTypeASTClassMap();
  astFactory = new ASTFactory(getTokenTypeToASTClassMap());
}

	public final void integer_literal() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST integer_literal_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case NUMERAL:
			{
				AST tmp1_AST = null;
				tmp1_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp1_AST);
				match(NUMERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case BYTE_LITERAL:
			{
				AST tmp2_AST = null;
				tmp2_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp2_AST);
				match(BYTE_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case SHORT_LITERAL:
			{
				AST tmp3_AST = null;
				tmp3_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp3_AST);
				match(SHORT_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case INTEGER_LITERAL:
			{
				AST tmp4_AST = null;
				tmp4_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp4_AST);
				match(INTEGER_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case LONG_LITERAL:
			{
				AST tmp5_AST = null;
				tmp5_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp5_AST);
				match(LONG_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case BIGINT_LITERAL:
			{
				AST tmp6_AST = null;
				tmp6_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp6_AST);
				match(BIGINT_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case HEX_LITERAL:
			{
				AST tmp7_AST = null;
				tmp7_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp7_AST);
				match(HEX_LITERAL);
				integer_literal_AST = (AST)currentAST.root;
				break;
			}
			case OCTAL_LITERAL:
			{
				AST tmp8_AST = null;
				tmp8_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp8_AST);
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
			consumeUntil(_tokenSet_0);
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
				AST tmp9_AST = null;
				tmp9_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp9_AST);
				match(IDENTIFIER);
				resource_identifier_AST = (AST)currentAST.root;
				break;
			}
			case BIGINT_LITERAL:
			case LONG_LITERAL:
			case INTEGER_LITERAL:
			case SHORT_LITERAL:
			case BYTE_LITERAL:
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
			consumeUntil(_tokenSet_1);
		}
		returnAST = resource_identifier_AST;
	}
	
	public final void literal() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST literal_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BIGINT_LITERAL:
			case LONG_LITERAL:
			case INTEGER_LITERAL:
			case SHORT_LITERAL:
			case BYTE_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				integer_literal();
				astFactory.addASTChild(currentAST, returnAST);
				literal_AST = (AST)currentAST.root;
				break;
			}
			case CHARACTER_LITERAL:
			{
				AST tmp10_AST = null;
				tmp10_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp10_AST);
				match(CHARACTER_LITERAL);
				literal_AST = (AST)currentAST.root;
				break;
			}
			case STRING_LITERAL:
			{
				AST tmp11_AST = null;
				tmp11_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp11_AST);
				match(STRING_LITERAL);
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
			consumeUntil(_tokenSet_0);
		}
		returnAST = literal_AST;
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
			consumeUntil(_tokenSet_1);
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
			consumeUntil(_tokenSet_2);
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
			consumeUntil(_tokenSet_2);
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
			_loop113:
			do {
				if ((LA(1)==LOGICAL_OR)) {
					AST tmp12_AST = null;
					tmp12_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp12_AST);
					match(LOGICAL_OR);
					logicalAndExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop113;
				}
				
			} while (true);
			}
			logicalOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_2);
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
			_loop116:
			do {
				if ((LA(1)==LOGICAL_AND)) {
					AST tmp13_AST = null;
					tmp13_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp13_AST);
					match(LOGICAL_AND);
					inclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop116;
				}
				
			} while (true);
			}
			logicalAndExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_3);
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
			_loop119:
			do {
				if ((LA(1)==BITWISE_OR)) {
					AST tmp14_AST = null;
					tmp14_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp14_AST);
					match(BITWISE_OR);
					exclusiveOrExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop119;
				}
				
			} while (true);
			}
			inclusiveOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_4);
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
			_loop122:
			do {
				if ((LA(1)==BITWISE_XOR)) {
					AST tmp15_AST = null;
					tmp15_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp15_AST);
					match(BITWISE_XOR);
					andExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop122;
				}
				
			} while (true);
			}
			exclusiveOrExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_5);
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
			_loop125:
			do {
				if ((LA(1)==BITWISE_AND)) {
					AST tmp16_AST = null;
					tmp16_AST = astFactory.create(LT(1));
					astFactory.makeASTRoot(currentAST, tmp16_AST);
					match(BITWISE_AND);
					equalityExpression();
					astFactory.addASTChild(currentAST, returnAST);
				}
				else {
					break _loop125;
				}
				
			} while (true);
			}
			andExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_6);
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
			_loop129:
			do {
				if ((LA(1)==EQUAL||LA(1)==UNEQUAL)) {
					{
					switch ( LA(1)) {
					case UNEQUAL:
					{
						AST tmp17_AST = null;
						tmp17_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp17_AST);
						match(UNEQUAL);
						break;
					}
					case EQUAL:
					{
						AST tmp18_AST = null;
						tmp18_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp18_AST);
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
					break _loop129;
				}
				
			} while (true);
			}
			equalityExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_7);
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
			_loop133:
			do {
				if (((LA(1) >= LESS_THAN && LA(1) <= GREATER_THAN_EQUAL))) {
					{
					switch ( LA(1)) {
					case LESS_THAN:
					{
						AST tmp19_AST = null;
						tmp19_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp19_AST);
						match(LESS_THAN);
						break;
					}
					case GREATER_THAN:
					{
						AST tmp20_AST = null;
						tmp20_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp20_AST);
						match(GREATER_THAN);
						break;
					}
					case LESS_THAN_EQUAL:
					{
						AST tmp21_AST = null;
						tmp21_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp21_AST);
						match(LESS_THAN_EQUAL);
						break;
					}
					case GREATER_THAN_EQUAL:
					{
						AST tmp22_AST = null;
						tmp22_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp22_AST);
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
					break _loop133;
				}
				
			} while (true);
			}
			relationalExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_8);
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
			_loop137:
			do {
				if ((LA(1)==SHIFT_LEFT||LA(1)==SHIFT_RIGHT)) {
					{
					switch ( LA(1)) {
					case SHIFT_LEFT:
					{
						AST tmp23_AST = null;
						tmp23_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp23_AST);
						match(SHIFT_LEFT);
						break;
					}
					case SHIFT_RIGHT:
					{
						AST tmp24_AST = null;
						tmp24_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp24_AST);
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
					break _loop137;
				}
				
			} while (true);
			}
			shiftExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_9);
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
			_loop141:
			do {
				if ((LA(1)==PLUS||LA(1)==MINUS)) {
					{
					switch ( LA(1)) {
					case PLUS:
					{
						AST tmp25_AST = null;
						tmp25_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp25_AST);
						match(PLUS);
						break;
					}
					case MINUS:
					{
						AST tmp26_AST = null;
						tmp26_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp26_AST);
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
					break _loop141;
				}
				
			} while (true);
			}
			additiveExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_10);
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
			_loop145:
			do {
				if (((LA(1) >= STAR && LA(1) <= MOD))) {
					{
					switch ( LA(1)) {
					case STAR:
					{
						AST tmp27_AST = null;
						tmp27_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp27_AST);
						match(STAR);
						break;
					}
					case DIV:
					{
						AST tmp28_AST = null;
						tmp28_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp28_AST);
						match(DIV);
						break;
					}
					case MOD:
					{
						AST tmp29_AST = null;
						tmp29_AST = astFactory.create(LT(1));
						astFactory.makeASTRoot(currentAST, tmp29_AST);
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
					break _loop145;
				}
				
			} while (true);
			}
			multiplicativeExpression_AST = (AST)currentAST.root;
		}
		catch (RecognitionException ex) {
			reportError(ex);
			consume();
			consumeUntil(_tokenSet_11);
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
				AST tmp30_AST = null;
				tmp30_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp30_AST);
				match(INC);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case DEC:
			{
				AST tmp31_AST = null;
				tmp31_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp31_AST);
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
			case BIGINT_LITERAL:
			case LONG_LITERAL:
			case INTEGER_LITERAL:
			case SHORT_LITERAL:
			case BYTE_LITERAL:
			case LEFT_PARENTHESE:
			case LOGICAL_NOT:
			case BITWISE_NOT:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_not:
			case LITERAL_true:
			case LITERAL_false:
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
			consumeUntil(_tokenSet_12);
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
				AST tmp32_AST = null;
				tmp32_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp32_AST);
				match(BITWISE_NOT);
				unaryExpression();
				astFactory.addASTChild(currentAST, returnAST);
				unaryExpressionNotPlusMinus_AST = (AST)currentAST.root;
				break;
			}
			case LOGICAL_NOT:
			{
				AST tmp33_AST = null;
				tmp33_AST = astFactory.create(LT(1));
				astFactory.makeASTRoot(currentAST, tmp33_AST);
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
			case BIGINT_LITERAL:
			case LONG_LITERAL:
			case INTEGER_LITERAL:
			case SHORT_LITERAL:
			case BYTE_LITERAL:
			case LEFT_PARENTHESE:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case NUMERAL:
			case IDENTIFIER:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			case LITERAL_true:
			case LITERAL_false:
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
			consumeUntil(_tokenSet_12);
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
			case EOF:
			case RIGHT_PARENTHESE:
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
			consumeUntil(_tokenSet_12);
		}
		returnAST = postfixExpression_AST;
	}
	
	public final void primaryExpression() throws RecognitionException, TokenStreamException {
		
		returnAST = null;
		ASTPair currentAST = new ASTPair();
		AST primaryExpression_AST = null;
		
		try {      // for error handling
			switch ( LA(1)) {
			case BIGINT_LITERAL:
			case LONG_LITERAL:
			case INTEGER_LITERAL:
			case SHORT_LITERAL:
			case BYTE_LITERAL:
			case CHARACTER_LITERAL:
			case STRING_LITERAL:
			case NUMERAL:
			case HEX_LITERAL:
			case OCTAL_LITERAL:
			{
				literal();
				astFactory.addASTChild(currentAST, returnAST);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case IDENTIFIER:
			{
				AST tmp34_AST = null;
				tmp34_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp34_AST);
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
			case LITERAL_true:
			{
				AST tmp37_AST = null;
				tmp37_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp37_AST);
				match(LITERAL_true);
				primaryExpression_AST = (AST)currentAST.root;
				break;
			}
			case LITERAL_false:
			{
				AST tmp38_AST = null;
				tmp38_AST = astFactory.create(LT(1));
				astFactory.addASTChild(currentAST, tmp38_AST);
				match(LITERAL_false);
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
			consumeUntil(_tokenSet_0);
		}
		returnAST = primaryExpression_AST;
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
		"\"false\""
	};
	
	protected void buildTokenTypeASTClassMap() {
		tokenTypeToASTClassMap=null;
	};
	
	private static final long[] mk_tokenSet_0() {
		long[] data = { 9007094030139394L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
	private static final long[] mk_tokenSet_1() {
		long[] data = { 2L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
	private static final long[] mk_tokenSet_2() {
		long[] data = { 2097154L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
	private static final long[] mk_tokenSet_3() {
		long[] data = { 4297064450L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
	private static final long[] mk_tokenSet_4() {
		long[] data = { 6444548098L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
	private static final long[] mk_tokenSet_5() {
		long[] data = { 23624417282L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
	private static final long[] mk_tokenSet_6() {
		long[] data = { 161063370754L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
	private static final long[] mk_tokenSet_7() {
		long[] data = { 169653305346L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_7 = new BitSet(mk_tokenSet_7());
	private static final long[] mk_tokenSet_8() {
		long[] data = { 105722769571842L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_8 = new BitSet(mk_tokenSet_8());
	private static final long[] mk_tokenSet_9() {
		long[] data = { 2216785094901762L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_9 = new BitSet(mk_tokenSet_9());
	private static final long[] mk_tokenSet_10() {
		long[] data = { 2243173373968386L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_10 = new BitSet(mk_tokenSet_10());
	private static final long[] mk_tokenSet_11() {
		long[] data = { 2243998007689218L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_11 = new BitSet(mk_tokenSet_11());
	private static final long[] mk_tokenSet_12() {
		long[] data = { 2251694589083650L, 0L};
		return data;
	}
	public static final BitSet _tokenSet_12 = new BitSet(mk_tokenSet_12());
	
	}
