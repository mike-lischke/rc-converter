/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.expressions;

import java.math.BigInteger;
import java.util.StringTokenizer;

import antlr.collections.AST;

/** 
 * Evaluates an expression contained in an AST.
 */
public class Evaluator
{
  private static ISymbolTable symbols;
  /** If <b>true</b> then unresolvable symbols are assumed to be integers with value 0. */
  private static boolean implicitSymbols;
  
  //------------------------------------------------------------------------------------------------
  
  private Evaluator()
  {
    // Using a private contructor to prevent instantiation.
    // Using class as a simple static utility class.
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is of type boolen and throws an exception if not.
   * 
   * @param value The value to check.
   */
  private static void checkBoolean(Object value)
  {
    checkEmpty(value);
    if (!isBoolean(value))
      showError("Boolean value expected, but " + value.toString() + " found.");
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Checks if value is assigned.
   * 
   * @param value The value to check.
   */
  private static void checkEmpty(Object value)
  {
    if (value == null)
      showError("Internal error. Empty expression value encountered.");
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is an integer type and throws an exception if not.
   * 
   * @param value The class to check.
   */
  private static void checkInteger(Object value)
  {
    checkEmpty(value);
    if (!isInteger(value))
      showError("Integer value expected, but " + value.toString() + " found.");
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is a float or integer type and throws an exception if not.
   * 
   * @param value The class to check.
   */
  private static void checkNumber(Object value)
  {
    checkEmpty(value);
    if (!isNumber(value))
      showError("Number value expected, but " + value.toString() + " found.");
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is a character or string type and throws an exception if not.
   * 
   * @param value The class to check.
   */
  private static void checkString(Object value)
  {
    checkEmpty(value);
    if (!isString(value))
      showError("Character literal or string value expected, but " + value.toString() + " found.");
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Checks the class of the given value whether it is derived from the class given in <b>classType</b>.
   * It throws an exception if it does not correspond.
   * 
   * @param value The value to check.
   * @param classType The type to check against.
   */
  private static void checkType(Object value, Class classType)
  {
    checkEmpty(value);
    if (!isBoolean(value))
      showError(classType.toString() + " expected but " + value.toString() + " found.");
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is a boolean type.
   * 
   * @param value The class to check.
   * @return <b>true</b> if the value is a boolean value, otherwise <b>false</b>.
   */
  private static boolean isBoolean(Object value)
  {
    return (value instanceof Boolean);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is of type float, that is, Float or Double.
   * 
   * @param value The class to check.
   * @return <b>true</b> if the value is a float value, otherwise <b>false</b>.
   */
  private static boolean isFloat(Object value)
  {
    return (value instanceof Float || value instanceof Double);
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is an integer type, that is, Byte, Integer, Long or Short.
   * 
   * @param value The class to check.
   * @return <b>true</b> if the value is an integer value, otherwise <b>false</b>.
   */
  private static boolean isInteger(Object value)
  {
    return (value instanceof Byte || value instanceof Integer || value instanceof Long || 
      value instanceof Short);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is an integer type, that is, Byte, Integer, Long or Short.
   * 
   * @param value The class to check.
   * @return <b>true</b> if the value is a number value, otherwise <b>false</b>.
   */
  private static boolean isNumber(Object value)
  {
    return (isFloat(value) || isInteger(value));
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if value is an integer type, that is, Byte, Integer, Long or Short.
   * 
   * @param value The class to check.
   * @return <b>true</b> if the value is a character or string value, otherwise <b>false</b>.
   */
  private static boolean isString(Object value)
  {
    return (value instanceof Character || value instanceof String);
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Does a lookup on the symbol table to find the predefined value for the given identifier.
   * 
   * @param symbol The identifier to look up.
   * @param node The node for which a value must be looked up. Contains location info for error messages.
   * @return The value of the identifier.
   */
  private static Object lookupValue(String symbol, AST node)
  {
    if (symbols == null || !symbols.isDefined(symbol))
    {
      if (implicitSymbols)
        return new Integer(0);
      else
      {
        showError("Undeclared identifier \"" + symbol + "\"");
        return null;
      }
    }
    
    return symbols.lookup(symbol, node.getLine(), node.getColumn());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Handles all forms of a long integer. Due to various suffixes this requires a bit extra work.
   * Fortunately, hexadecimal and octal number parsing is done by the decode method implicitly.
   * 
   * @param node The node containing the value as string.
   * @return The parsed Integer.
   */
  private static Object parserIntegerType(AST node)
  {
    String raw = node.getText().toLowerCase();
    
    // Remove any suffix. We cannot distinct between pure negative and positive types anyway.
    if (raw.endsWith("i128") || raw.endsWith("u128"))
      return new BigInteger(raw.substring(0, raw.length() - 4));
    else
      if (raw.endsWith("i64") || raw.endsWith("u64"))
        return Long.decode(raw.substring(0, raw.length() - 3));
      else
        if (raw.endsWith("i32") || raw.endsWith("u32") || raw.endsWith("ul"))
          return Integer.decode(raw.substring(0, raw.length() - 3));
        else
          if (raw.endsWith("i16") || raw.endsWith("u16"))
            return Short.decode(raw.substring(0, raw.length() - 3));
          else
            if (raw.endsWith("i8") || raw.endsWith("u8"))
              return Byte.decode(raw.substring(0, raw.length() - 2));
            else
              if (raw.endsWith("l") || raw.endsWith("u"))
                return Long.decode(raw.substring(0, raw.length() - 1));
              else
                return Long.decode(raw);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Evaluates the given AST into a scalar value.
   * 
   * @param node The (sub) AST to evaluate.
   * @return An object, which encapsulates a scalar value.
   */
  private static Object process(AST node)
  {
    if (node == null)
      return null;
    else
    {
      AST left = node.getFirstChild();
      AST right = null;
      if (left != null)
        right = left.getNextSibling();
      Object leftValue = null;
      Object result = null;
      try
      {
        switch (node.getType())
        {
          // Node values.
          case ExpressionLexerTokenTypes.CHARACTER_LITERAL:
            result = new Character(node.getText().charAt(0));
            break;
          case ExpressionLexerTokenTypes.STRING_LITERAL:
            result = processStringLiteral(node.getText());
            break;
          case ExpressionLexerTokenTypes.IDENTIFIER:
            result = lookupValue(node.getText(), node);
            break;
          case ExpressionLexerTokenTypes.FLOAT_LITERAL:
            result = new Float(node.getText());
            break;
          case ExpressionLexerTokenTypes.DOUBLE_LITERAL:
            result = new Double(node.getText());
            break;
          case ExpressionLexerTokenTypes.HEX_LITERAL:
          case ExpressionLexerTokenTypes.OCTAL_LITERAL:
          case ExpressionLexerTokenTypes.BYTE_LITERAL:
          case ExpressionLexerTokenTypes.SHORT_LITERAL:
          case ExpressionLexerTokenTypes.INTEGER_LITERAL:
          case ExpressionLexerTokenTypes.LONG_LITERAL:
          case ExpressionLexerTokenTypes.BIGINT_LITERAL:
            result = parserIntegerType(node);
            break;
          case ExpressionLexerTokenTypes.NUMERAL:
            result = new Integer(node.getText());
            break;
          case ExpressionLexerTokenTypes.LITERAL_true:
            result = new Boolean(true);
            break;
          case ExpressionLexerTokenTypes.LITERAL_false:
            result = new Boolean(false);
            break;
          default:
          {
            leftValue = process(left);

            // Handle non-terminals.
            switch (node.getType())
            {
              // Unary operations.
              case ExpressionLexerTokenTypes.LOGICAL_NOT:
                result = processLogicalNot(leftValue);
                break;
              case ExpressionLexerTokenTypes.BITWISE_NOT:
                result = processBitwiseNot(leftValue);
                break;
              case ExpressionLexerTokenTypes.INC:
              case ExpressionLexerTokenTypes.POST_INC:
                result = processInc(leftValue);
                break;
              case ExpressionLexerTokenTypes.DEC:
              case ExpressionLexerTokenTypes.POST_DEC:
                result = processDec(leftValue);
                break;
              case ExpressionLexerTokenTypes.UNARY_MINUS:
                result = processUnaryMinus(leftValue);
                break;
              case ExpressionLexerTokenTypes.UNARY_PLUS:
                result = leftValue;
                break;

              // Binary operations.
              // We can optimize some boolean operations.  
              case ExpressionLexerTokenTypes.LOGICAL_AND:
                result = processLogicalAnd(leftValue, right);
                break;
              case ExpressionLexerTokenTypes.LOGICAL_OR:
                result = processLogicalOr(leftValue, right);
                break;
              default: 
              {
                Object rightValue = process(right);

                switch (node.getType())
                {
                  case ExpressionLexerTokenTypes.BITWISE_AND:
                    result = processBitwiseAnd(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.BITWISE_OR:
                    result = processBitwiseOr(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.BITWISE_XOR:
                    result = processXor(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.PLUS:
                    result = processPlus(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.MINUS:
                    result = processMinus(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.STAR:
                    result = processMultiply(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.DIV:
                    result = processDivide(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.MOD:
                    result = processMod(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.SHIFT_LEFT:
                    result = processShiftLeft(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.SHIFT_RIGHT:
                    result = processShiftRight(leftValue, rightValue);
                    break;
                  case ExpressionLexerTokenTypes.EQUAL:
                  case ExpressionLexerTokenTypes.UNEQUAL:
                  case ExpressionLexerTokenTypes.LESS_THAN:
                  case ExpressionLexerTokenTypes.GREATER_THAN:
                  case ExpressionLexerTokenTypes.LESS_THAN_EQUAL:
                  case ExpressionLexerTokenTypes.GREATER_THAN_EQUAL:
                    result = processRelation(leftValue, rightValue, node.getType());
                    break;
                  default:
                    throw EvaluationException.create(node);
                }
              }
            }
          }
        }
      }
      catch (NumberFormatException e)
      {
        throw new EvaluationException(e.getMessage());
      }

      return result;
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a string literal, which can be a composition of one or more string parts each
   * enclosed in double quotes and separated by zero or more space or tabulator characters.
   * 
   * @param literal The string literal to process.
   * @return A single string without quotes with all literal parts concatenated.
   */
  private static String processStringLiteral(String literal)
  {
    StringBuffer result = new StringBuffer();

    int subStringEnd = -1;

    do
    {
      int subStringStart = literal.indexOf('"', subStringEnd + 1);
      subStringEnd = literal.indexOf('"', subStringStart + 1);
      result.append(literal.substring(subStringStart + 1, subStringEnd));
    }
    while (subStringEnd < literal.length() - 1);
    
    return result.toString();
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the bitwise and between both values.
   * 
   * @param leftValue An integer value.
   * @param rightValue A second integer value.
   * @return The result of the computation. It is always of type Integer.
   */
  private static Object processBitwiseAnd(Object leftValue, Object rightValue)
  {
    checkInteger(leftValue);
    checkInteger(rightValue);
    
    Number leftNumber = (Number)leftValue;
    Number rightNumber = (Number)rightValue;
    
    return new Integer(leftNumber.intValue() & rightNumber.intValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the bitwise negation of the input value.
   * 
   * @param leftValue An integer value.
   * 
   * @return The bitwise negation of the input.
   */
  private static Object processBitwiseNot(Object leftValue)
  {
    checkInteger(leftValue);
    
    Number value = (Number)leftValue;
    if (value instanceof Byte)
      return new Byte((byte)~value.byteValue());
    else
      if (value instanceof Integer)
        return new Integer(~value.intValue());
      else
        if (value instanceof Long)
          return new Long(~value.longValue());
        else
          return new Short((short)(~value.shortValue()));
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the bitwise or between both values.
   * 
   * @param leftValue An integer value.
   * @param rightValue A second integer value.
   * @return The result of the computation. It is always of type Integer.
   */
  private static Object processBitwiseOr(Object leftValue, Object rightValue)
  {
    checkInteger(leftValue);
    checkInteger(rightValue);
    
    Number leftNumber = (Number)leftValue;
    Number rightNumber = (Number)rightValue;
    
    return new Integer(leftNumber.intValue() | rightNumber.intValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the decrement of the input value.
   * 
   * @param leftValue An integer value.
   * @return The input value - 1.
   */
  private static Object processDec(Object leftValue)
  {
    checkInteger(leftValue);
    
    Number value = (Number)leftValue;
    if (value instanceof Byte)
      return new Byte((byte)(value.byteValue() - 1));
    else
      if (value instanceof Integer)
        return new Integer(value.intValue() - 1);
      else
        if (value instanceof Long)
          return new Long(value.longValue() - 1);
        else
          return new Short((short)(value.shortValue() - 1));
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the division of both input values.
   * 
   * @param leftValue Any number value.
   * @param rightValue Any number value.
   * @return The result is a double value if any of the values is a float value. For byte, int, 
   *          short and long Long is returned.
   */
  private static Object processDivide(Object leftValue, Object rightValue)
  {
    checkNumber(leftValue);
    checkNumber(rightValue);
    
    // Float types.
    if (isFloat(leftValue) || isFloat(rightValue))
      return new Double(((Number)leftValue).doubleValue() / ((Number)rightValue).doubleValue());
    else
      // Integer values.
      return new Long(((Number)leftValue).longValue() / ((Number)rightValue).longValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the increment of the input value.
   * 
   * @param leftValue An integer value.
   * @return The input value + 1.
   */
  private static Object processInc(Object leftValue)
  {
    checkInteger(leftValue);
    
    Number value = (Number)leftValue;
    if (value instanceof Byte)
      return new Byte((byte)(value.byteValue() + 1));
    else
      if (value instanceof Integer)
        return new Integer(value.intValue() + 1);
      else
        if (value instanceof Long)
          return new Long(value.longValue() + 1);
        else
          return new Short((short)(value.shortValue() + 1));
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the result of doing leftValue && rightValue.
   * 
   * @param leftValue A boolean value.
   * @param right The AST node of the second value. It has not yet been processed in case we can
   *               take the short path (if leftValue is false).
   * @return The result of the computation.
   */
  private static Object processLogicalAnd(Object leftValue, AST right)
  {
    checkBoolean(leftValue);
    
    // Optimize evaluation here. Return immediately if leftValue is already false, otherwise
    // evaluate also right to a value and do the and'ing.
    if (!((Boolean)leftValue).booleanValue())
      return new Boolean(false);

    Object rightValue = process(right);
    checkBoolean(rightValue);
    
    return new Boolean(((Boolean)leftValue).booleanValue() && ((Boolean)rightValue).booleanValue());
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the logical negation of the input value.
   * 
   * @param leftValue A boolean value.
   * 
   * @return The logical negation of the input.
   */
  private static Object processLogicalNot(Object leftValue)
  {
    if (isInteger(leftValue))
    {
      return new Boolean(((Number) leftValue).intValue() == 0);
    }
    else
    {
      checkType(leftValue, Boolean.class);
      
      return new Boolean(!((Boolean)leftValue).booleanValue());
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the result of doing leftValue || rightValue.
   * 
   * @param leftValue A boolean value.
   * @param right The AST node of the second value. It has not yet been processed in case we can
   *               take the short path (if leftValue is true).
   * @return The result of the computation.
   */
  private static Object processLogicalOr(Object leftValue, AST right)
  {
    checkBoolean(leftValue);

    // Optimize evaluation here. Return immediately if leftValue is already true, otherwise
    // evaluate also right to a value and do the or'ing.
    if (((Boolean)leftValue).booleanValue())
      return new Boolean(true);

    Object rightValue = process(right);
    checkBoolean(rightValue);
    
    return new Boolean(((Boolean)leftValue).booleanValue() || ((Boolean)rightValue).booleanValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the difference of both input values.
   * 
   * @param leftValue Any number value.
   * @param rightValue Any number value.
   * @return The result is a double value if any of the values is a float value. For byte, int, 
   *          short and long Long is returned.
   */
  private static Object processMinus(Object leftValue, Object rightValue)
  {
    checkNumber(leftValue);
    checkNumber(rightValue);
    
    // Float types.
    if (isFloat(leftValue) || isFloat(rightValue))
      return new Double(((Number)leftValue).doubleValue() - ((Number)rightValue).doubleValue());
    else
      // Integer values.
      return new Long(((Number)leftValue).longValue() - ((Number)rightValue).longValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the modulo of both input values.
   * 
   * @param leftValue Any integer value.
   * @param rightValue Any integer value.
   * @return The result is a Long value.
   */
  private static Object processMod(Object leftValue, Object rightValue)
  {
    checkInteger(leftValue);
    checkInteger(rightValue);
    
    // Integer values.
    return new Long(((Number)leftValue).longValue() % ((Number)rightValue).longValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the multiplication of both input values.
   * 
   * @param leftValue Any number value.
   * @param rightValue Any number value.
   * @return The result is a double value if any of the values is a float value. For byte, int, 
   *          short and long Long is returned.
   */
  private static Object processMultiply(Object leftValue, Object rightValue)
  {
    checkNumber(leftValue);
    checkNumber(rightValue);
    
    // Float types.
    if (isFloat(leftValue) || isFloat(rightValue))
      return new Double(((Number)leftValue).doubleValue() * ((Number)rightValue).doubleValue());
    else
      // Integer values.
      return new Long(((Number)leftValue).longValue() * ((Number)rightValue).longValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the sum or concatenation of both input values.
   * 
   * @param leftValue Any number or string/char value.
   * @param rightValue Any number or string/char value.
   * @return The result is a double value if any of the values is a float value (but the other must 
   *          not be a string). For byte, int, short and long Long is returned and for string 
   *          and character input a string output is genererated.
   */
  private static Object processPlus(Object leftValue, Object rightValue)
  {
    // Start with string concatenation. No automatic conversion takes place. Both values must be
    // strings or characters.
    if (isString(leftValue) || isString(rightValue))
    {
      checkString(leftValue);
      checkString(rightValue);
      
      String left = (leftValue instanceof String) ? (String)leftValue : ((Character)leftValue).toString();
      String right = (rightValue instanceof String) ? (String)rightValue : ((Character)rightValue).toString();
      
      return (left + right);
    }
    else
    {
      checkNumber(leftValue);
      checkNumber(rightValue);
      
      // Float types.
      if (isFloat(leftValue) || isFloat(rightValue))
        return new Double(((Number)leftValue).doubleValue() + ((Number)rightValue).doubleValue());
      else
        // Integer values.
        return new Long(((Number)leftValue).longValue() + ((Number)rightValue).longValue());
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Evaluates both values with regard to the given logical operation. String types cannot be compared.
   * 
   * @param leftValue Any number value.
   * @param rightValue Any number value.
   * @param operation The logical operation to perform.
   * @return <b>true</b> if the relation turns out to be true, otherwise <b>false</b>.
   */
  private static Object processRelation(Object leftValue, Object rightValue, int operation)
  {
    checkNumber(leftValue);
    checkNumber(rightValue);
    
    boolean useDouble = (leftValue instanceof Float) || (leftValue instanceof Double) ||
      (rightValue instanceof Float) || (rightValue instanceof Double);

    if (useDouble)
    {
      switch (operation)
      {
        case ExpressionLexerTokenTypes.EQUAL:
          return new Boolean(((Number)leftValue).doubleValue() == ((Number)rightValue).doubleValue());
        case ExpressionLexerTokenTypes.UNEQUAL:
          return new Boolean(((Number)leftValue).doubleValue() != ((Number)rightValue).doubleValue());
        case ExpressionLexerTokenTypes.LESS_THAN:
          return new Boolean(((Number)leftValue).doubleValue() < ((Number)rightValue).doubleValue());
        case ExpressionLexerTokenTypes.GREATER_THAN:
          return new Boolean(((Number)leftValue).doubleValue() > ((Number)rightValue).doubleValue());
        case ExpressionLexerTokenTypes.LESS_THAN_EQUAL:
          return new Boolean(((Number)leftValue).doubleValue() <= ((Number)rightValue).doubleValue());
        case ExpressionLexerTokenTypes.GREATER_THAN_EQUAL:
          return new Boolean(((Number)leftValue).doubleValue() >= ((Number)rightValue).doubleValue());
        default:
          return new Boolean(false);
      }
    }
    else
    {
      switch (operation)
      {
        case ExpressionLexerTokenTypes.EQUAL:
          return new Boolean(((Number)leftValue).intValue() == ((Number)rightValue).intValue());
        case ExpressionLexerTokenTypes.UNEQUAL:
          return new Boolean(((Number)leftValue).intValue() != ((Number)rightValue).intValue());
        case ExpressionLexerTokenTypes.LESS_THAN:
          return new Boolean(((Number)leftValue).intValue() < ((Number)rightValue).intValue());
        case ExpressionLexerTokenTypes.GREATER_THAN:
          return new Boolean(((Number)leftValue).intValue() > ((Number)rightValue).intValue());
        case ExpressionLexerTokenTypes.LESS_THAN_EQUAL:
          return new Boolean(((Number)leftValue).intValue() <= ((Number)rightValue).intValue());
        case ExpressionLexerTokenTypes.GREATER_THAN_EQUAL:
          return new Boolean(((Number)leftValue).intValue() >= ((Number)rightValue).intValue());
        default:
          return new Boolean(false);
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Shifts the left value by the number of digits given by the right value to the left.
   * 
   * @param leftValue Any integer value.
   * @param rightValue Any integer value.
   * @return The result is a Long value.
   */
  private static Object processShiftLeft(Object leftValue, Object rightValue)
  {
    checkInteger(leftValue);
    checkInteger(rightValue);
    
    // Integer values.
    return new Long(((Number)leftValue).longValue() << ((Number)rightValue).longValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Shifts the left value by the number of digits given by the right value to the right.
   * 
   * @param leftValue Any integer value.
   * @param rightValue Any integer value.
   * @return The result is a Long value.
   */
  private static Object processShiftRight(Object leftValue, Object rightValue)
  {
    checkInteger(leftValue);
    checkInteger(rightValue);
    
    // Integer values.
    return new Long(((Number)leftValue).longValue() >> ((Number)rightValue).longValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the negation of the input value.
   * 
   * @param leftValue An numerical value.
   * @return -leftValue.
   */
  private static Object processUnaryMinus(Object leftValue)
  {
    checkNumber(leftValue);
    
    Number value = (Number)leftValue;
    if (value instanceof Byte)
      return new Byte((byte)-value.byteValue());
    else
      if (value instanceof Integer)
        return new Integer(-value.intValue());
      else
        if (value instanceof Long)
          return new Long(-value.longValue());
        else
          if (value instanceof Short)
            return new Short((short)(-value.shortValue()));
          else
            if (value instanceof Float)
              return new Float(-value.floatValue());
            else
              return new Double(-value.doubleValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Computes the eclusive or between both values.
   * 
   * @param leftValue An integer value.
   * @param rightValue A second integer value.
   * @return The result of the computation. It is always of type Integer.
   */
  private static Object processXor(Object leftValue, Object rightValue)
  {
    checkInteger(leftValue);
    checkInteger(rightValue);
    
    Number leftNumber = (Number)leftValue;
    Number rightNumber = (Number)rightValue;
    
    return new Integer(leftNumber.intValue() ^ rightNumber.intValue());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Throws an exception with the given message.
   * 
   * @param message The message.
   */
  private static void showError(String message)
  {
    throw new EvaluationException(message);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Takes the given abstract syntax tree and tries to evaluate it to one single value.
   * The result will be a scalar type (Integer, String, Float etc.) or null if either expression
   * is null or an error occured. Evaluation order is left to right.
   * 
   * @param expression The abstract syntax tree into which the expression must be parsed.
   * @param resolver An interface able to provide support for macro substitution.
   * @param useImplicitSymbols When <b>true</b> then unresolvable symbols are considered as integers
   *         with a value of 0.
   * 
   * @return An object, which encapsulates a scalar value.
   */
  public static Object evaluate(AST expression, ISymbolTable symbolTable, boolean useImplicitSymbols)
  {
    symbols = symbolTable;
    implicitSymbols = useImplicitSymbols;
    
    Object result = null;
    
    // The root node (the expression itself) is always a simple node without sibling and must be
    // of type EXPR.
    if (expression != null)
      if (expression.getType() == ExpressionLexerTokenTypes.EXPR)
        result = process(expression.getFirstChild());
      else
        result = process(expression);
    
    return result;
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Takes the given abstract syntax tree and tries to evaluate it to a boolean value.
   * 
   * @param expression The abstract syntax tree into which the expression must be parsed.
   * @param resolver An interface able to provide support for macro substitution.
   * @param useImplicitSymbols When <b>true</b> then unresolvable symbols are considered as integers
   *         with a value of 0.
   * 
   * @return <b>true</b>, if the expression either itself evaluates to true or to a non-zero 
   *          numerical value, otherwise <b>false</b>. <b>false</b> is also returned if 
   *          <b>expression</b> is null or an error occured.
   */
  public static boolean evaluateToBoolean(AST expression, ISymbolTable symbolTable, 
    boolean useImplicitSymbols)
  {
    boolean evaluation = false;

    if (expression != null)
    {    
      Object result = evaluate(expression, symbolTable, useImplicitSymbols);
      if (result instanceof Boolean)
        evaluation = ((Boolean)result).booleanValue();
      else
        if (result instanceof Number)
          evaluation = ((Number)result).floatValue() != 0;
    }
    
    return evaluation;
  }
  
  //------------------------------------------------------------------------------------------------
  
}
