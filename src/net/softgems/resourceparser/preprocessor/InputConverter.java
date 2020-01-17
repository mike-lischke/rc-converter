/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.preprocessor;

import java.io.*;

/**
 * The InputConverter class takes raw stream input and converts this to a Unicode character stream.
 * As it is a special class to support the resource parser it also does some additional tasks like
 * trigraph sequence replacement and line splicing.
 *
 *  @author Mike
 */
public class InputConverter
{
  /** This reader provides the raw input. */
  private BufferedReader reader;
  /** The byte to char converter. */
  private InputStreamReaderEx streamReader;
  /** The connection between this class (together with the preprocessor) and the lexer. */
  private PreprocessorInputState inputState;

  //------------------------------------------------------------------------------------------------

  /**
   * Constructor of the InputConverter class.
   * 
   * @param inputState The connection class, which keeps track of the current line number.
   * @param input The raw byte stream to be converted.
   * @param initialCharset The character set to be initially used for conversion to Unicode.
   * @throws UnsupportedEncodingException
   */
  public InputConverter(PreprocessorInputState inputState, InputStream input, String initialCharset) 
    throws UnsupportedEncodingException
  {
    streamReader = new InputStreamReaderEx(input, initialCharset);
    reader = new BufferedReader(streamReader);
    this.inputState = inputState;
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * @return Returns the inputState.
   */
  public PreprocessorInputState getInputState()
  {
    return inputState;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Changes the currently active charset to a new charset.
   * 
   * @param The new charset to be used from now on.
   * @throws UnsupportedEncodingException
   */
  public void setCurrentCharset(String charset) throws UnsupportedEncodingException
  {
    streamReader.setCharset(charset);
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Processes the given input stream until a complete line is read. The input is converted to 
   * Unicode using the current character set and line splicing as well as trigraph conversion 
   * is done.
   *
   * @return A line of text, which can be <b>null</b> if input is exhausted.
   */
  public String readLine() throws IOException
  {
    StringBuffer result = new StringBuffer();
    boolean lineFinished;
    do
    {
      lineFinished = true;
      String line = reader.readLine();
      if (line == null)
      {
        result = null;
        break;
      }
  
      inputState.newLine();

      boolean ignoreNextQuestionMark = false;
  
      // Scan for trigraph sequences.
      for (int i = 0; i < line.length(); i++)
      {
        char nextChar = line.charAt(i);

        if (nextChar == '?')
        {
          if (ignoreNextQuestionMark)
          {
            // If this flag is true then we found a masked question mark in the previous run.
            ignoreNextQuestionMark = false;
            continue;
          } 
          else 
            if (((i + 2) < line.length()) && (line.charAt(i + 1) == '?'))
            {
              switch (line.charAt(i + 2))
              {
                case '=':
                  nextChar = '#';
                  break;
                case '(':
                  nextChar = '[';
                  break;
                case '/':
                  nextChar = '\\';
                  break;
                case ')':
                  nextChar = ']';
                  break;
                case '\'':
                  nextChar = '^';
                  break;
                case '<':
                  nextChar = '{';
                  break;
                case '!':
                  nextChar = '|';
                  break;
                case '>':
                  nextChar = '}';
                  break;
                case '-':
                  nextChar = '~';
                  break;
                default:
                  // Everything else. Don't care about it.
                  nextChar = '?';
              }
            }
        } 
        else
        {
          // Because the sequence ??any-char is reserved for trigraph sequences it is necessary
          // to mask one or both of the question marks if you need such a sequence literally.
          // Note: The MSDN documentation is not clear about what should happen when the \? sequence
          //       is found in reqular text. Hence this will get converted to a single ? here.
          if ((nextChar == '\\') && ((i + 1) < line.length()) && (line.charAt(i + 1) == '?'))
          {
            nextChar = '?';
            ignoreNextQuestionMark = true;
          }
        }

        // Do line splicing. Do not use line.endsWith because the nextChar could have been
        // created by the trigraph replacement.
        if ((nextChar == '\\') && ((i + 1) == line.length()))
          lineFinished = false;
        else
          result.append(nextChar);
      }
    } 
    while (!lineFinished);
    
    if (result == null)
      return null;
    else
      return result.toString();
  }
  
  //------------------------------------------------------------------------------------------------
}
