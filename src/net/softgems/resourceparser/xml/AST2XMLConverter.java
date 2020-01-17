/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.xml;

import java.util.HashMap;

import net.softgems.resourceparser.RCMainFrame;
import net.softgems.resourceparser.expressions.*;
import net.softgems.resourceparser.main.IParseEventListener;
import net.softgems.resourceparser.preprocessor.MacroTable;

import org.jdom.Element;

import antlr.*;
import antlr.collections.AST;

/**
 * This class is the generic base class for all AST to XML conversion classes.
 * It provides some generic functionality.
 */
public abstract class AST2XMLConverter implements ISymbolTable, IParseEventListener
{

  /** The root node of the subtree, which gets converted by this class. */
  private AST astNode;
  /** The class to resolve macros. */
  private MacroTable macroTable;
  /** The target to report errors to. */
  private RCMainFrame owner;
  /** A list of already resolved symbols. */
  private HashMap symbolTable = new HashMap();

  //------------------------------------------------------------------------------------------------

  public AST2XMLConverter(RCMainFrame owner, AST astNode)
  {
    this.owner = owner;
    this.astNode = astNode;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Helper method to convert a string of text into a valid xml name.
   * 
   * @param text The text to convert.
   * @return The beautified text.
   */
  protected String beautifyName(String text)
  {
    StringBuffer buffer = new StringBuffer(text.replace(' ', '-').toLowerCase());

    return buffer.toString();
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Evaluates the expression tree into a single scalar value.
   * 
   * @param expression
   *          The expression to evaluate.
   * @return A scalar value resulting from the given expression.
   */
  protected Object evaluate(AST tree)
  {
    Object result = null;
    try
    {
      result = Evaluator.evaluate(tree, this, false);
    }
    catch (EvaluationException e)
    {
      reportError(e.getMessage());
    }

    return result;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Evaluates the given expression into a single scalar value.
   * 
   * @param expression The expression to evaluate.
   * @param line The line number of the actual input where this symbol was read from.
   * @param column The column number of the symbol.
   * @return A scalar value resulting from the given expression.
   */
  protected Object evaluate(String expression, int line, int column)
  {
    // This variable holds the abstract syntax tree for the parsed expression.
    AST expressionTree = null;
    try
    {
      expressionTree = ExpressionParser.parse(expression, null, line, column, this);
    }
    catch (RecognitionException e)
    {
      reportError(e.getMessage());
    }
    catch (TokenStreamException e)
    {
      reportError(e.getMessage());
    }

    Object result = null;
    try
    {
      result = Evaluator.evaluate(expressionTree, this, false);
    }
    catch (EvaluationException e)
    {
      reportError(e.getMessage());
    }

    return result;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Used to report an error back to the RC converter.
   * 
   * @param message The error message.
   */
  protected void reportError(String message)
  {
    owner.logMessage("[XML converter] " + message);
  }

  //------------------------------------------------------------------------------------------------

  public abstract void convert(Element target);

  //------------------------------------------------------------------------------------------------

  /**
   * Returns the internal AST root node for the converter.
   * 
   * @return Returns the AST root node.
   */
  public AST getAstNode()
  {
    return astNode;
  }
  //------------------------------------------------------------------------------------------------

  /* (non-Javadoc)
   * @see net.softgems.resourceparser.main.IParseEventListener#handleEvent(int, java.lang.String)
   */
  public void handleEvent(int event, String message)
  {
    // This method is called here only during an expression evaluation and is used to forward
    // the incoming events to the owner of this preprocessor.
    owner.logMessage(message);
  }

  //------------------------------------------------------------------------------------------------

  /*
   * (non-Javadoc)
   * 
   * @see net.softgems.resourceparser.expressions.ISymbolTable#isDefined(java.lang.String)
   */
  public boolean isDefined(String symbol)
  {
    if (macroTable != null)
      return macroTable.isDefined(symbol);
    else
      return false;
  }

  //------------------------------------------------------------------------------------------------

  /*
   * (non-Javadoc)
   * 
   * @see net.softgems.resourceparser.expressions.ISymbolTable#lookup(java.lang.String)
   */
  public Object lookup(String symbol, int line, int column)
  {
    // If there is no macro table then we cannot resolve any symbol.
    if (macroTable == null || !macroTable.isDefined(symbol))
      return null;
    
    if (!symbolTable.containsKey(symbol))
    {
      String expression = macroTable.expandMacros(symbol);
      Object value = evaluate(expression, line, column);
      symbolTable.put(symbol, value);
    }
    return symbolTable.get(symbol);
  }

  //------------------------------------------------------------------------------------------------

  public void setMacroTable(MacroTable table)
  {
    macroTable = table;
  }

  //------------------------------------------------------------------------------------------------

}