/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.preprocessor;

import java.io.*;
import java.io.File;
import java.util.Stack;

import antlr.*;

/**
 * This class extends the standard shared input state to create a connection between the input
 * provider (the preprocessor) and the following stages (the lexer and parser). This is important
 * to provide correct line number information for error messages.
 */
public class PreprocessorInputState extends LexerSharedInputState
{
  private String currentDir;
  /** Used to keep track of nested input state activations for several preprocessors. */
  private Stack stateStack = new Stack();
  
  //------------------------------------------------------------------------------------------------

  /** This class is a pure data holder for the state stack. */
  private class StackEntry
  {
    protected int column;
    protected InputBuffer input;
    protected int line;
    protected int tokenStartColumn;
    protected int tokenStartLine;
    public String filename;
    public String lastDir;
    
  }
  
  //------------------------------------------------------------------------------------------------
  
  /** This class is a special end marker to feed an EOI marker to the lexer, which stops processing. */
  private class SentinelInputBuffer extends InputBuffer
  {

    public SentinelInputBuffer()
    {
      queue.append('\n');
      queue.append((char) -1);
    }

    /* (non-Javadoc)
     * @see antlr.InputBuffer#fill(int)
     */
    public void fill(int amount) throws CharStreamException
    {
      syncConsume();
    }
    
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Special constructor for the input state. The actual input is set via a separate method.
   */
  public PreprocessorInputState()
  {
    super((InputBuffer) null);
    
    input = new SentinelInputBuffer();
    line = -1;
    column = -1;
  }
  
  //------------------------------------------------------------------------------------------------
  
  public int getColumn()
  {
    return column;
  }

  //------------------------------------------------------------------------------------------------

  public String getFilename()
  {
    return filename;
  }

  //------------------------------------------------------------------------------------------------

  public String getFullFilename()
  {
    return currentDir + filename;
  }

  //------------------------------------------------------------------------------------------------

  public int getLine()
  {
    return line;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Called by the input converter to indicate the current line we are working on.
   */
  public void newLine()
  {
    column = 1;
    line++;
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Performs the opposite operation to pushState. It is called by the current preprocessor if it
   * has no more input and has a parent processor, which can take over again.
   */
  public void popState()
  {
    StackEntry entry = (StackEntry) stateStack.pop();
    column = entry.column;
    line = entry.line;
    tokenStartColumn = entry.tokenStartColumn;
    tokenStartLine = entry.tokenStartLine;
    input = entry.input;
    filename = entry.filename;
    currentDir = entry.lastDir;
    if (currentDir != null)
      System.setProperty("user.dir", currentDir);
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * Saves the current values of the input state for later continued use and
   * initializes everything for a fresh input start.
   * 
   * @param newInput The new preprocessor taking over the task to supply input data to the lexer.
   * @param filename The name of the file that is about to be processed.
   * @param workPath The path where the new input is located.
   */
  public void pushState(Preprocessor newInput, String filename, String workpath)
  {
    StackEntry entry = new StackEntry();
    entry.column = column;
    entry.line = line;
    entry.tokenStartColumn = tokenStartColumn;
    entry.tokenStartLine = tokenStartLine;
    entry.input = input;
    entry.lastDir = currentDir;
    entry.filename = this.filename;
    stateStack.push(entry);
    
    column = 1;
    // The input converter increases the line number before it reads a line, so we must start with 0.
    line = 0;
    tokenStartColumn = 1;
    tokenStartLine = 0;
    this.filename = filename;
    currentDir = workpath;
    if (!currentDir.endsWith("\\"))
      currentDir += "\\";
    System.setProperty("user.dir", currentDir);
    
    // Pushing the current input state and creating a new one means a new include file is
    // about to be processed. This stage is reached when the current preprocessor is 
    // filling its buffer, which in turn means the buffer is currently empty. As a result of the
    // preparation for the new inlude file the current preprocessor returns a new line character as 
    // pseudo entry back to the calling character input buffer. This will cause the lexer to 
    // process that returned character and immediately ask for this again. This time however
    // it works now on the new input state, which should return the same value as the previous
    // state found (which is the new line character). Hence this character is pushed here as
    // initial content.
    input = new PreprocessorCharBuffer(newInput, "\n");
  }
  
  //------------------------------------------------------------------------------------------------
  
}
