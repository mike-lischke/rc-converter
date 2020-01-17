/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.main;

import antlr.*;

/**
 * Helper class to make the input field from its ancestor publicly accessible.
 */
public class RCParserSharedInputState extends ParserSharedInputState
{
  //------------------------------------------------------------------------------------------------
  
  public TokenBuffer getInput()
  {
    return input;
  }

  //------------------------------------------------------------------------------------------------
  
  public void setInput(TokenStream lexer)
  {
    input = new TokenBuffer(lexer);
  }

  //------------------------------------------------------------------------------------------------
  
}
