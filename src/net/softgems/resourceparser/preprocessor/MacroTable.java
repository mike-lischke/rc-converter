/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.preprocessor;

import java.io.*;
import java.text.MessageFormat;
import java.util.*;

import net.softgems.resourceparser.main.IParseEventListener;

/**
 * This class provides support for macro substitution and symbol lookup. The implementation is
 * based on the C/C++ preprocessor language description in MSDN:
 *   Visual Studio -> Visual C++ -> Visual C++ Reference -> C/C++ Languages -> 
 *   C/C++ Preprocessor Reference -> The Preprocessor -> Macros.
 */
public class MacroTable
{
  /** A list of macros, which are currently being expanded (to avoid endless recursions). */
  protected ArrayList evaluationList = new ArrayList();
  /** List of event listeners who want to get notified about a preprocessor event. */
  private ArrayList listeners = new ArrayList();
  /** A list of Macro classes, sorted by the name of the macros. */
  private HashMap macros = new HashMap();
  
  //------------------------------------------------------------------------------------------------

  /**
   * This private class is the actual representation of a macro in the macro table.
   */
  private class Macro
  {
    protected int formalParameterCount;
    protected String[] formalParameters;
    protected String name;
    protected String substitution;
    
    //----------------------------------------------------------------------------------------------

    /**
     * Constructor of the Macro class.
     * 
     * @param theName The name (identification) of the macro.
     * @param theParameters The formal parameters of the macro or <b>null</b> if there aren't any.
     * @param theSubstitution The string to use instead of the macro identification, when calling
     *         {@see getSubstitution}.
     */
    public Macro(String theName, String[] theParameters, String theSubstition)
    {
      name = theName;
      formalParameters = theParameters;
      if (formalParameters == null)
        formalParameterCount = 0;
      else
        formalParameterCount = formalParameters.length;
      substitution = theSubstition;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Looks through the list of formal parameters to find the given symbol and returns the 
     * actual parameter from the <b>parameters</b> list if there is one.
     * 
     * @param parameters The list of actual parameters.
     * @return The actual parameter, which corresponds to the given symbol.
     */
    private String getActualParameter(ArrayList parameters, String symbol)
    {
      // Look whether this symbol is a parameter and get its index in the parameter list if so.
      int index = -1;
      for (int i = 0; i < formalParameters.length; i++)
        if (formalParameters[i].equals(symbol))
        {
          index = i;
          break;
        }
       
      // Replace the formal parameter by its actual equivalent if there is one.
      if (index > -1 && index < parameters.size())
        return (String) parameters.get(index);
      else
        return symbol;
    }
    
    //----------------------------------------------------------------------------------------------

    /**
     * This method handles stringizing or charizing of parameters.
     * 
     * @param parameters The list of actual parameters.
     * @param tokenizer The currently used tokenizer.
     */
    private String handleNumberSign(ArrayList parameters, MacroTokenizer tokenizer)
    {
      StringBuffer result = new StringBuffer();
      int lookAhead = tokenizer.nextToken();
      if (lookAhead == '@')
      {
        // Skip leading whitespaces.
        do
        {
          lookAhead = tokenizer.nextToken();
        }
        while (lookAhead == ' ' || lookAhead == '\t');

        // Charizing is requested.
        result.append('\'');
        // The next token must be a single character.
        if (lookAhead == StreamTokenizer.TT_WORD)
        {
          String actualParameter = getActualParameter(parameters, tokenizer.getStringValue());
          if (actualParameter.length() == 1)
            result.append(expandMacros(actualParameter));
          else
            reportError("Invalid charizing sequence in macro \"" + name + "\".");
        }
        else
          reportError("Invalid charizing sequence in macro \"" + name + "\".");
        result.append('\'');
      }
      else
      {
        // Skip leading whitespaces.
        while (lookAhead == ' ' || lookAhead == '\t')
        {
          lookAhead = tokenizer.nextToken();
        }
        if (lookAhead == StreamTokenizer.TT_WORD)
        {
          result.append("\"");
          // Expand any macro before making it a string literal.
          String actualParameter = getActualParameter(parameters, tokenizer.getStringValue());
          {
            StringBuffer expandedValue = new StringBuffer(expandMacros(actualParameter));
            // Mask any character, which would make the string literal invalid.
            for (int i = expandedValue.length() - 1; i >= 0; i--)
            {
              switch (expandedValue.charAt(i))
              {
                case '\\':
                case '"':
                {
                  expandedValue.insert(i, '\\');
                  break;
                }
              }
            }
            result.append(expandedValue.toString());
          }
          result.append("\"");
        }
        else
          reportError("Invalid macro operator in \"" + name + "\"");
      }
      
      return result.toString();
    }
    
    //----------------------------------------------------------------------------------------------

    /**
     * Replaces the formal parameters in the macro definition by the given actual parameters and
     * returns the result.
     */
    public String getSubstition(String[] actualParameters)
    {
      StringBuffer result;
      
      if (substitution == null)
        result = null;
      else
      {
        result = new StringBuffer();
        
        // Convert the actual parameters into a list of non-empty entries.
        // Note: MSVC allows for strange constructs here like
        //    macro(1,,,,,,,,,3)
        // where any empty entry is skipped. So the 3 would actually be used as second parameter.
        ArrayList parameters = new ArrayList();
        if (actualParameters != null)
        {
          for (int i = 0; i < actualParameters.length; i++)
            if ((actualParameters[i] != null) && (actualParameters[i].length() > 0))
              parameters.add(actualParameters[i].trim());
        }        
        
        if (parameters.size() > formalParameterCount)
          reportWarning("Macro \"" + name + "\": too many actual parameters in macro call.");
        if (parameters.size() < formalParameterCount)
          reportWarning("Macro \"" + name + "\": too few actual parameters in macro call.");
        
        boolean pastingPending = false;
        if (formalParameterCount == 0)
          // Shortcut if there is nothing to substitution.
          result.append(substitution);
        else
        {
          // Scan the list of formal parameters and replace any occurance in the substitution by the
          // matching actual parameter.
          MacroTokenizer tokenizer = new MacroTokenizer(substitution, true);
          
          // Copy everything from the substition to the output until the opening parenthesis is found.
          int token = 0;
          if (substitution.indexOf('(') > -1)
          {
            boolean stopPreLoop = false;
            do
            {
              token = tokenizer.nextToken();
              switch (token)
              {
                case StreamTokenizer.TT_EOF:
                case '(':
                {
                  if (token == '(')
                    result.append((char) token);
                  stopPreLoop = true;
                  break;
                }
                case StreamTokenizer.TT_WORD:
                {
                  result.append(tokenizer.getStringValue());
                  break;
                }
                case '"':
                {
                  result.append('"');
                  result.append(tokenizer.getStringValue());
                  result.append('"');
                  break;
                }
                case '\'':
                {
                  result.append('\'');
                  result.append(tokenizer.getStringValue());
                  result.append('\'');
                  break;
                }
                default:
                  result.append((char) token);
              }
            }
            while (!stopPreLoop);
          }
          
          do
          { 
            // Collect leading whitespace but do not write them to the result yet.
            boolean whiteSpacePending = false;
            do
            {
              token = tokenizer.nextToken();
              if (token == ' ' || token == '\t')
                whiteSpacePending = true;
              else
                break;
            }
            while (true);
            
            if (token == StreamTokenizer.TT_EOF)
              break;
  
            switch (token) 
            {
              case StreamTokenizer.TT_WORD:
              {
                if (whiteSpacePending && !pastingPending)
                  result.append(" ");
                String actualParameter = getActualParameter(parameters, tokenizer.getStringValue());
                result.append(expandMacros(actualParameter));
                pastingPending = false;
                break;
              }
              case '#': // Macro operator.
              {
                token = tokenizer.nextToken();
                switch (token)
                {
                  case '#':
                  {
                    // Token-pasting operator. Leave out any pending white space and keep a flag to
                    // indicate that he next token is written directly to the previous token.
                    // Check that this operator is not the first token in the substitution.
                    if (result.length() == 0)
                      reportError("\'##\' cannot occur at the beginning of a macro definition");
                    pastingPending = true;
                    break;
                  }
                  case '@':
                  {
                    // Charizing operator.
                    if (whiteSpacePending)
                      result.append(" ");
                    tokenizer.pushBack();
                    result.append(handleNumberSign(parameters, tokenizer));
                    break;
                  }
                  default:
                  {
                    // Stringizing operator.
                    if (whiteSpacePending)
                      result.append(" ");
                    tokenizer.pushBack();
                    result.append(handleNumberSign(parameters, tokenizer));
                  }
                }
                break;
              }
              case '"':
              {
                if (whiteSpacePending)
                  result.append(" ");
                result.append('"');
                result.append(tokenizer.getStringValue());
                result.append('"');
                break;
              }
              case '\'':
              {
                if (whiteSpacePending)
                  result.append(" ");
                result.append('\'');
                result.append(tokenizer.getStringValue());
                result.append('\'');
                break;
              }
              default:
              {
                if (whiteSpacePending)
                  result.append(" ");
                result.append((char)token);
              }
            }
          }
          while (true);
        }
        
        if (pastingPending)
          reportError("\'##\' cannot occur at the end of a macro definition");
      }
      
      if (result == null)
        return null;
      else
        return result.toString();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Determines if this macro has got replacement text or is just a defined symbol.
     * 
     * @return <b>true</b> if there is replacement text, <b>false</b> if it is only a symbol.
     */
    public boolean isEmpty()
    {
      return (substitution != null && substitution.length() > 0);
    }

    //----------------------------------------------------------------------------------------------

  }

  //------------------------------------------------------------------------------------------------

  /**
   * This private class serves as convenience class for setting up a tokenizer.
   */
  private class MacroTokenizer
  {
    private Reader reader;
    private StreamTokenizer tokenizer;
    
    //----------------------------------------------------------------------------------------------

    /**
     * Constructor for the tokenizer that takes a string as input.
     * 
     * @param input The input.
     * @param numberSignSeparate Determines if number signs (#) are treated separately or as part
     *                            of an identifier.
     */
    public MacroTokenizer(String input, boolean numberSignSeparate)
    {
      reader = new StringReader(input);
      tokenizer = new StreamTokenizer(reader);
      tokenizer.resetSyntax();
      tokenizer.lowerCaseMode(false);
      tokenizer.slashSlashComments(true);
      tokenizer.slashStarComments(true);
      tokenizer.wordChars('a', 'z');
      tokenizer.wordChars('A', 'Z');
      tokenizer.wordChars('_', '_');
      tokenizer.wordChars('0', '9');

      // Add the number sign as word char too. In our context it can only appear as part of
      // a preprocessor definition, which never can be a macro.
      if (!numberSignSeparate)
        tokenizer.wordChars('#', '#');
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Scans the characters between two quote chars and returns them (without the quotes).
     * 
     * @param quoteChar The character to use for end recognition.
     * @return The scanned string literal.
     */
    private String readString(char quoteChar)
    {
      StringBuffer buffer = new StringBuffer();
      boolean skipNext = false;
      do
      {
        char c = 0;
        try
        {
          c = (char)reader.read();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        if (c == '\uFFFF')
          break;
        if (skipNext)
        {
          skipNext = false;
          buffer.append(c);
          continue;
        }
        if (c == '\\')
          skipNext = true;
        else
          if (c == quoteChar)
            break;
        buffer.append(c);
      }
      while (true);
      
      return buffer.toString();
    }

    //----------------------------------------------------------------------------------------------

    /**
     * This method extracts text between two parentheses (also with nesting) and must only be
     * called if the current token is an opening parenthesis. In this process no token retrieval
     * takes place but instead characters are read directly from the input.
     * 
     * @return The text between the parentheses.
     */
    public String getInnerText()
    {
      int level = 1;
      StringBuffer buffer = new StringBuffer();
      do
      {
        char c = 0;
        try
        {
          c = (char) reader.read();
        }
        catch (IOException e)
        {
          // Since we are reading from a string there can never be an IOException.
          // However rules are to have code for the exception, regardless of whether it appears or not.
          e.printStackTrace();
        }
        if (c == '\uFFFF')
          reportError("Unexpected end of input.");
        
        if (c == '(')
          level++;
        else
          if (c == ')')
            level--;
        // The level tracker becomes 0 when the end of the list was found.
        if (level == 0)
          break;
        buffer.append(c);
      }
      while (true);

      return buffer.toString();
    }
    
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the current token as numeric value. This is only valid if the current token
     * is TT_NUMERAL.
     * 
     * @return The numeric value.
     */
    public double getNumericValue()
    {
      return tokenizer.nval;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Reads from the current input position up to count characters without tokenizing them and
     * returns the substring. Escape sequences are converted, too.
     * 
     * @param count The number of characters to read. This can also be a very high value to indicate
     *               that everything remaining on the input should be returned.
     * @return The substring with a length of either <b>count</b> or the remaining number of input
     *          characters, whichever is smaller.
     */
    public String getRawInput(int count)
    {
      StringBuffer buffer = new StringBuffer();
      while (count > 0)
      {
        char c = 0;
        try
        {
          c = (char) reader.read();
          
          // Convert escape sequence.
          if (c == '\\')
          {
            c = (char) reader.read();
            if(c == 'u')
            {
              // Read the xxxx.
              int value = 0;
              for (int i = 0; i < 4; i++)
              {
                c = (char) reader.read();
                switch (c)
                {
                  case '0': case '1': case '2': case '3': case '4':case '5':
                  case '6': case '7': case '8': case '9':
                  {
                    value = (value << 4) + c - '0';
                    break;
                  }
                  case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
                  {
                    value = (value << 4) + 10 + c - 'a';
                    break;
                  }
                  case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
                  {
                    value = (value << 4) + 10 + c - 'A';
                    break;
                  }
                  default:
                  {
                    throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
                  }
                }
              }
              c = (char) value;
            }
            else
            {
              switch (c)
              {
                case 'a':
                {
                  c = 0x7;
                  break;
                }
                case 'b':
                {
                  c = '\b';
                  break;
                }
                case 'f':
                {
                  c = 0xC;
                  break;
                }
                case 'n':
                {
                  c = '\n';
                  break;
                }
                case 'r':
                {
                  c = '\r';
                  break;
                }
                case 't':
                {
                  c = '\t';
                  break;
                }
                case 'v':
                {
                  c = 0xB;
                  break;
                }
              }
            }
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
        if (c == '\uFFFF')
          break;
        buffer.append(c);
      }
      return buffer.toString();
    }
    
    //----------------------------------------------------------------------------------------------

    /**
     * Returns the current token as string value. If the current token is a quote char then
     * the text included in the quotes is returned instead.
     * 
     * @return The string value.
     */
    public String getStringValue()
    {
      String result = "";
      switch (tokenizer.ttype)
      {
        case StreamTokenizer.TT_WORD:
        {
          result = tokenizer.sval;
          break;
        }
        case '"':
        {
          // Note: we cannot use the built-in feature of StreamTokenizer for strings as it
          //       tries to be too smart and converts all escape sequences to characters.
          //       This conflicts however with the following parser stage. Hence we do a raw scan
          //       on the underlying input stream.
          result = readString('"');
          break;
        }
        case '\'':
        {
          // See note for double quote case.
          result = readString('\'');
          break;
        }
      }
      return result;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Returns the next token from the internal tokenizer.
     * 
     * @return The next token.
     */
    public int nextToken()
    {
      try
      {
        return tokenizer.nextToken();
      }
      catch (IOException e)
      {
        // Since we are reading from a string there can never be an IOException.
        // However rules are to have code for the exception, regardless of whether it appears or not.
        e.printStackTrace();
        return StreamTokenizer.TT_EOF;
      }
    }

    //----------------------------------------------------------------------------------------------

    /**
     * Helper method to undo the last nextToken() call.
     */
    public void pushBack()
    {
      tokenizer.pushBack();
    }

    //----------------------------------------------------------------------------------------------

  }

  //------------------------------------------------------------------------------------------------

  protected void doEvent(int event, String message)
  {
    for (int i = 0; i < listeners.size(); i++)
    {
      IParseEventListener listener = (IParseEventListener)listeners.get(i);
      listener.handleEvent(event, message);
    }
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * Substitutes the given macro definition (which might simply be an identifier without any 
   * associated value) and returns the substitution string for it.
   * The method does not raise an exception or show an error otherwise. The caller is responsible
   * to take the appropriate action when null is returned.
   * 
   * @param macroName A macro call or simple identifier to be replaced.
   * @param parameters A list of parameters, if <b>macro</b> refers to a macro call (can be null).
   * @return A string containing the textual replacement for the input or null if
   *          there is no replacement.
   */
  protected String getMacroSubstitution(String macroName, String[] parameters)
  {
    String result = null;
    
    Macro macro = (Macro) macros.get(macroName);
    if (macro != null)
      result = macro.getSubstition(parameters);
    
    return result;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Adds the given event listener to the internal listener list.
   * 
   * @param listener The listener to add.
   */
  public void addMacroEventListener(IParseEventListener listener)
  {
    listeners.add(listener);
  }

  //----------------------------------------------------------------------------------------------
  
  /**
   * Adds the given macro definition to the table. Such a definition must consist solely of
   * an identifier and an optional replacement text separated by one or more white spaces (space, tab).
   * Additionally, as with C/C++, the macro identifier might be followed by a parameter list 
   * enclosed in parenthesis, similar to a function call.
   * 
   * @param definition The macro definition to parse.
   * @throws A runtime exception is thrown if the definition contains errors.
   */
  public void defineMacro(String definition)
  {
    if (definition.trim().length() != 0)
    {
      // Split the definition into the main parts. 
      // Note: If the macro contains parentheses then there must be no white space between the
      //       macro identifier and the parameter list. This is necessary to distinct between a 
      //       function macro and a symbolic constant enclosed in parentheses.
      String[] parts = null; 
      Macro macro = null;
      MacroTokenizer tokenizer = new MacroTokenizer(definition, false);
      
      // Skip to the first identifier.
      int token;
      do
      {
        token = tokenizer.nextToken();
      }
      while (token != StreamTokenizer.TT_WORD);
      String macroName = tokenizer.getStringValue();
      token = tokenizer.nextToken();
      if (token == '(')
      {
        // Found a macro call.
        String parameters = tokenizer.getInnerText();
        String[] parameterList = parameters.split(",");
        for (int i = 0; i < parameterList.length; i++)
          parameterList[i] = parameterList[i].trim();
        String substitution = tokenizer.getRawInput(Integer.MAX_VALUE);
  
        // Check if the macro is already defined with another value.
        macro = (Macro) macros.get(macroName);
        if (macro != null)
        {
          if (!macro.substitution.equals(substitution))
            reportWarning("Macro " + macroName + " is being redefined.");
        }
        else
          macro = new Macro(macroName, parameterList, substitution.trim());
      }
      else
      {
        // We are dealing with a simple macro definition (a.k.a macro object).
        parts = definition.trim().split(" |\t", 2);
        String substitution = null;
        if (parts.length == 0)
          reportError("Empty macro definition found.");
        else
          if (parts.length > 1)
            substitution = parts[1].trim();
        macroName = parts[0].trim();
        
        // Check if the macro is already defined with another value.
        macro = (Macro) macros.get(macroName);
        if (macro != null)
        {
          if ((macro.substitution == null) != (substitution == null)) 
            reportWarning("Macro " + macroName + " is being redefined.");
          else
            if (macro.substitution != null && !macro.substitution.equals(substitution))
              reportWarning("Macro " + macroName + " is being redefined.");
        }
        else
          macro = new Macro(macroName, null, substitution);
      }
      macros.put(macroName, macro);
    }
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Takes the input string and scans it for macro identifiers. Each occurence is recursively expanded.
   */
  public String expandMacros(String input)
  {
    if (input == null)
      return null;
    else
    {
      String expandedString = "";
      if (input.length() > 0)
      {
        StringBuffer buffer = new StringBuffer();
        MacroTokenizer tokenizer = new MacroTokenizer(input, false);
        boolean endReached = false;
        while (!endReached)
        {
          int token = tokenizer.nextToken();
          switch (token)
          {
            case StreamTokenizer.TT_EOF:
            {
              endReached = true;
              break;
            }
            case StreamTokenizer.TT_WORD:
            {
              String symbol = tokenizer.getStringValue();
              
              // Identifier found, which means that's something we have to expand or it is a
              // preprocessor directive. Could well be a normal identifier, though.
              if (!symbol.equals("defined") && (!isDefined(symbol) || evaluationList.contains(symbol)))
              {
                // If there is no known macro definition then keep the name without scanning
                // further. If the symbol is already being expanded then we found an endless
                // recursion. In this case the symbol also can be added to the output as it
                // will then be catched by the evaluator as an undefined symbol.
                // This is the same behavior like that of MSVC.
                buffer.append(symbol);
              }
              else
              {
                // Make sure we do not get into an endless definition loop.
                evaluationList.add(symbol);
                // Note: The tokenizer is prepared so that number signs (#) belong to identifiers.
                //       In valid rc and header files number signs can only appear as part of a
                //       preprocessor directive, which is never macro-expanded.
                if (symbol.charAt(0) == '#')
                {
                  buffer.append(symbol);
                  break;
                }
                // Check if the identifier is followed by a parenthesis as this is
                // then a list of parameters and separates a macro symbol from a macro call.
                // Note: We are now going to directly manipulate the underlying reader of our tokenizer,
                //       so everything we consume here is never seen by the tokenizer.
                int localToken = tokenizer.nextToken();
                
                // Skip any whitespace but note if there were some and output a single space for them.
                boolean outputWhiteSpace = false;
                while (localToken == ' ' || localToken == '\t')
                {
                  outputWhiteSpace = true;
                  localToken = tokenizer.nextToken();
                }
                if (localToken == '(')
                {
                  if (symbol.equals("defined"))
                  {
                    // Special case: "defined(symbol)".
                    localToken = tokenizer.nextToken();
                    if (localToken != StreamTokenizer.TT_WORD)
                      reportError("Invalid macro definition");
                    symbol = tokenizer.getStringValue();
                    localToken = tokenizer.nextToken();
                    if (localToken != ')')
                      reportError("Invalid macro definition");
                    if (isDefined(symbol))
                      buffer.append("true");
                    else
                      buffer.append("false");
                    
                    break;
                  }
                    
                  String[] parameters = null;
                  // Collect all actual parameters into a list of strings and use this to 
                  // resolve the macro call.
                  String paramList = tokenizer.getInnerText();
                  if (paramList == null)
                    parameters = null;
                  else
                    parameters = paramList.split(",");
                  
                  // Recurse here for nested macro definitions.
                  String substitution = expandMacros(getMacroSubstitution(symbol, parameters));
                  if (substitution != null)
                    buffer.append(substitution);
                }
                else
                {
                  tokenizer.pushBack();
                  
                  // Just a simple macro symbol.
                  String substitution = expandMacros(getMacroSubstitution(symbol, null));
                  if (substitution != null)
                    buffer.append(substitution);
                  
                  if (outputWhiteSpace)
                    buffer.append(" ");
                }
                evaluationList.remove(symbol);
             }
              break;
            }
            case '"':
            {
              buffer.append('"');
              buffer.append(tokenizer.getStringValue());
              buffer.append('"');
              break;
            }
            case '\'':
            {
              buffer.append('\'');
              buffer.append(tokenizer.getStringValue());
              buffer.append('\'');
              break;
            }
            default:
            {
              buffer.append((char)token);
              break;
            }
          }
        }
        expandedString = buffer.toString();
      }
      
      return expandedString;
    }
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if the given identifier is defined in the table.
   * 
   * @param identifier The name of the potential macro.
   * @return <b>true</b> if the identifier is defined, otherwise <b>false</b>.
   */
  public boolean isDefined(String identifier)
  {
    return macros.containsKey(identifier);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Determines if the given identifier is defined in the table and has a non-empty expansion text.
   * 
   * @param identifier The name of the potential macro.
   * @return <b>true</b> if the identifier is defined and non-empty, otherwise <b>false</b>.
   */
  public boolean isDefinedNonEmpty(String identifier)
  {
    Macro macro = (Macro) macros.get(identifier);
    if (macro != null && !macro.isEmpty())
      return true;
    else
      return false;
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Reports an error to the calling application.
   * 
   * @param s Error message to report.
   */
  public void reportError(String s)
  {
    doEvent(IParseEventListener.ERROR, s);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Reports general information to the calling application.
   * 
   * @param s Information message to report.
   */
  public void reportInfo(String s)
  {
    doEvent(IParseEventListener.INFORMATION, s);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Reports a warning to the calling application.
   * 
   * @param s Warning message to report.
   */
  public void reportWarning(String s)
  {
    doEvent(IParseEventListener.WARNING, s);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Removes the macro definition with the given name from the definition list.
   * If the macro does not exist then nothing happens.
   */
  public void undefineMacro(String identifier)
  {
    macros.remove(identifier);
  }

  //------------------------------------------------------------------------------------------------
  
}
