/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.preprocessor;

import java.io.Reader;

import antlr.CharBuffer;

/**
 * Extended character buffer class that takes initial input.
 */
public class PreprocessorCharBuffer extends CharBuffer
{

  //------------------------------------------------------------------------------------------------
  
  /**
   * Extended constructor of the class.
   * 
   * @param input The new input source.
   * @param initalContent The string to be used as initial content for the queue.
   */
  public PreprocessorCharBuffer(Reader input, String initialContent)
  {
    super(input);
    
    if (initialContent != null)
      for (int i = 0; i < initialContent.length(); i++)
        queue.append(initialContent.charAt(i));
  }

  //------------------------------------------------------------------------------------------------
  
}
