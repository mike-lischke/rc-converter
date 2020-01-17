/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.expressions;

/**
 * This interface supplies a lookup facility for symbol to value mappings.
 */
public interface ISymbolTable
{
  
  //------------------------------------------------------------------------------------------------

  /**
   * Takes the given symbol and looks for it in the internal list. If a symbol definition exists then
   * its value (which also can be <b>null</b>) and returns it to the caller.
   * 
   * @param symbol The symbol to look for.
   * @param line The line number of the actual input where this symbol was read from.
   * @param column The column number of the symbol.
   * @return A single value (Integer, String, Double etc.) for the given name or <b>null</b> if
   *          the symbol does not represent a value or is not defined.
   */
  public Object lookup(String symbol, int line, int column);
  
  //------------------------------------------------------------------------------------------------

  /**
   * Determines, whether the given symbol is defined in the symbol table.
   * 
   * @param symbol The name of the symbol to lookup for.
   * @return <b>true</b> if the symbol is defined, otherwise <b>false</b>.
   */
  public boolean isDefined(String symbol);
  
  //------------------------------------------------------------------------------------------------
  
}
