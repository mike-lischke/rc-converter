/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.expressions;

import java.text.MessageFormat;

import antlr.collections.AST;

/**
 * This exception class is used in the expression evaluator to provide special information.
 */
public class EvaluationException extends RuntimeException
{
  //------------------------------------------------------------------------------------------------

  /**
   * Standard constructor for the exception.
   */
  public EvaluationException(String message) 
  {
    super(message);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Extended constructor to provide information about an invalid node.
   * 
   * @param node The node to show information for.
   */
  public static EvaluationException create(AST node)
  {
    String message = MessageFormat.format("[Expression evaluator] [{0}, {1}] Invalid token \"{2}\"",
      new Object[] 
      {
        new Integer(node.getLine()), 
        new Integer(node.getColumn()),
        node.getText()
      }
    );

    return new EvaluationException(message);
  }

  //------------------------------------------------------------------------------------------------

}
