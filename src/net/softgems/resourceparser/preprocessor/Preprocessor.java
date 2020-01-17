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

import net.softgems.resourceparser.expressions.*;
import net.softgems.resourceparser.main.IParseEventListener;
import antlr.*;
import antlr.collections.AST;

/**
 * The preprocess class is responsible for handling every preprocessor directive in the input. It
 * reads input from the InputConverter stored in the input state and feeds the lexer with its output.
 * 
 *  @author Mike Lischke
 */
public class Preprocessor extends Reader implements IParseEventListener
{
  private static final int DEFINE = 0;
  private static final Integer DEFINE_DIRECTIVE   = new Integer(DEFINE);

  /** Some static data for quick lookups. Also the state stack needs objects. */
  private static final HashMap directives = new HashMap(10);
  private static final int ELIF   = 1;
  private static final Integer ELIF_DIRECTIVE     = new Integer(ELIF);
  private static final int ELSE   = 2;
  private static final Integer ELSE_DIRECTIVE     = new Integer(ELSE);
  private static final int ENDIF  = 3;
  private static final Integer ENDIF_DIRECTIVE    = new Integer(ENDIF);
  private static final int ERROR  = 4;
  private static final Integer ERROR_DIRECTIVE    = new Integer(ERROR);
  private static final int IF     = 5;
  private static final Integer IF_DIRECTIVE       = new Integer(IF);
  private static final int IFDEF  = 6;
  private static final Integer IFDEF_DIRECTIVE    = new Integer(IFDEF);
  private static final int IFNDEF = 7;
  private static final Integer IFNDEF_DIRECTIVE   = new Integer(IFNDEF);
  private static final int INCLUDE = 8;
  private static final Integer INCLUDE_DIRECTIVE  = new Integer(INCLUDE);
  private static final int PRAGMA = 9;
  private static final Integer PRAGMA_DIRECTIVE   = new Integer(PRAGMA);
  private static final int UNDEF  = 10;
  private static final Integer UNDEF_DIRECTIVE    = new Integer(UNDEF);
  
  /** Skip modes used in the state machine. */
  private static final int SKIP_CONDITIONAL = 0;
  private static final int SKIP_NONE        = 1;
  private static final int SKIP_PARENT      = 2;
  private static final int SKIP_SUBTREE     = 3;
  
  /** Regular expression containing allowed white spaces to split parts on a line. */
  private static final String WHITESPACES = " |\t";
  
  /**
   * This charset is assumed to be the initial encoding for the input files. Though this might not
   * always be the case and character sets can change within the file.
   */
  public static final String DEFAULT_CHARSET = "ISO-8859-1";
  
  //------------------------------------------------------------------------------------------------

  static
  {
    directives.put("define", DEFINE_DIRECTIVE);
    directives.put("undef", UNDEF_DIRECTIVE);
    directives.put("if", IF_DIRECTIVE);
    directives.put("ifdef", IFDEF_DIRECTIVE);
    directives.put("ifndef", IFNDEF_DIRECTIVE);
    directives.put("else", ELSE_DIRECTIVE);
    directives.put("elif", ELIF_DIRECTIVE);
    directives.put("endif", ENDIF_DIRECTIVE);
    directives.put("include", INCLUDE_DIRECTIVE);
    directives.put("error", ERROR_DIRECTIVE);
    directives.put("pragma", PRAGMA_DIRECTIVE);
  }
  /** The current input line that is used to feed the lexer. */
  private StringBuffer currentLine = new StringBuffer();
  
  /** The current read position in the input buffer. */
  private int currentPosition;
  
  private boolean hadErrors;
  private boolean hadWarnings;
  private ArrayList includePaths;
  
  /** Flag, which indicates pending comment lines. */
  private boolean inMultilineComment;
  
  /** The class that feeds the preprocessor. */
  private InputConverter input;

  /**
   * The input state itself has a reference to this preprocess but the processor also needs
   * a back reference in order to restore the previous input state once its input is exhausted.
   */
  private PreprocessorInputState inputState;
  
  /** List of event listeners who want to get notified about a preprocessor event. */
  private ArrayList listeners = new ArrayList();
  
  /** Contains a a mapping of identifiers to macro definitions. */
  private MacroTable macroTable;
  
  /** A stack of pending states, which must be correctly finished for a valid file. */
  private Stack pendingStates = new Stack();
  
  /** A list of already processed include files, which are marked with the #pragma once directive. */
  private HashMap processedIncludes;
  
  /** Indicates if the preprocessor is currently skipping lines. */
  private int skip;
  
  /** A stack of skip flags for the state machine. */
  private Stack skipFlags = new Stack();
  
  /** This field is <b>true</b> when the current input file is either a *.c or a *.h file. */
  private boolean skipNonPPLines;
  
  /** Only for debugging. If <b>true</b> then input and output are printed to console. */
  private boolean traceProcessing;
  
  //------------------------------------------------------------------------------------------------

  public Preprocessor(InputConverter input, Preprocessor parentProcessor, PreprocessorInputState inputState,
    boolean traceProcessing)
  {
    this.inputState = inputState;
    this.traceProcessing = traceProcessing;
    this.input = input;

    // Initial skip states.
    skip = SKIP_NONE;
    skipFlags.push(new Integer(skip));
    
    // If the parent processor is set then this is an instance called for an include file.
    // Share certain structures with the parent processor instead creating own ones.
    if (parentProcessor != null)
    {
      macroTable = parentProcessor.macroTable;
      includePaths = parentProcessor.includePaths;
      processedIncludes = parentProcessor.processedIncludes;
    }
    else
    {
      macroTable = new MacroTable();
      macroTable.addMacroEventListener(
        new IParseEventListener() 
        {
          public void handleEvent(int event, String message)
          {
            doEvent(event, message, true);
          };
        }
      );
      includePaths = new ArrayList();
      processedIncludes = new HashMap();
    }
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * Evaluates the given expression by setting up a local parser and evaluator.
   * 
   * @param expression The expression to evaluate.
   * @return <b>true</b> if the expression turns out to be true, otherwise <b>false</b>.
   */
  private boolean evaluateBooleanExpression(String expression)
  {
    // This variable holds the abstract syntax tree for the parsed expression.
    AST expressionTree = null;
    try
    {
      expressionTree = ExpressionParser.parse(expression, inputState.getFilename(), inputState.getLine(), 
        inputState.getColumn(), this);
    }
    catch (RecognitionException e)
    {
      reportError(e.getMessage()); 
    }
    catch (TokenStreamException e)
    {
      reportError(e.getMessage());
    }
    
    boolean result = false;
    try
    {
      result = Evaluator.evaluateToBoolean(expressionTree, null, true);
    }
    catch (EvaluationException e)
    {
      reportError(e.getMessage());
    }
    
    return result;
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * Converts the first identifer on the given line (which must start with a number sign) to an 
   * internal ID for quicker processing.
   * 
   * @param line The current input line, including the number sign.
   * @return An integer value for the found directive or -1 if unknown.
   */
  private int getDirective(String line)
  {
    String[] parts = splitDirective(line);
    Integer value = (Integer)directives.get(parts[0]);
    if (value == null)
      return -1;
    else
      return value.intValue();
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Called when a #define directive is found. Get the identifier and the associated expression
   * and put it into our symbol list.
   * 
   * @param definition Input containing the identifier and the associated expression (if any).
   */
  private void processDefine(String definition)
  {
    macroTable.defineMacro(definition);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Checks for any known preprocessor directive and triggers individual processing.
   * 
   * @param input The directive with the number sign.
   * @return <b>true</b> if the calling method should stop its reader loop.
   */
  private boolean processDirective(String input) throws FileNotFoundException, 
    IOException
  {
    boolean result = false;
    int directive = getDirective(input);
    
    // Remove the leading number sign and split the rest into two parts of which the first
    // one is the directive and the second one the expression to parse.
    String[] parts = splitDirective(input);
    String directiveText = parts[1].trim();
    switch (directive)
    {
      case INCLUDE:
      {
        if (skip == SKIP_NONE)
          result = processInclude(macroTable.expandMacros(directiveText));
        break;
      }
      case DEFINE:
      {
        if (skip == SKIP_NONE)
          processDefine(parts[1]);
        break;
      }
      case UNDEF:
      {
        if (skip == SKIP_NONE)
          processUndef(parts[1]);
        break;
      }
      case PRAGMA:
      {
        if (skip == SKIP_NONE)
          processPragma(parts);
        break; 
      }
      case ERROR:
      {
        if (skip == SKIP_NONE)
          reportError(parts[1]);
        break;
      }
      case IFDEF:
      {
        pendingStates.push(IF_DIRECTIVE);
        skipFlags.push(new Integer(skip));
        switch (skip)
        {
          case SKIP_NONE:
          {
            boolean condition = macroTable.isDefined(directiveText);
            if (!condition)
              skip = SKIP_CONDITIONAL;
            break;
          }
          case SKIP_CONDITIONAL:
          {
            // If there was already a previous condition that failed then we are 
            // entering a sub condition right now. Change the skip mode to parent skip to reflect that.
            skip = SKIP_PARENT;
            break;
          }
        }
        break;
      }
      case IFNDEF:
      {
        pendingStates.push(IF_DIRECTIVE);
        skipFlags.push(new Integer(skip));
        switch (skip)
        {
          case SKIP_NONE:
          {
            boolean condition = !macroTable.isDefined(directiveText);
            if (!condition)
              skip = SKIP_CONDITIONAL;
            break;
          }
          case SKIP_CONDITIONAL:
          {
            // If there was already a previous condition that failed then we are 
            // entering a sub condition right now. Change the skip mode to parent skip to reflect that.
            skip = SKIP_PARENT;
            break;
          }
        }
        break;
      }
      case IF:
      {
        pendingStates.push(IF_DIRECTIVE);
        skipFlags.push(new Integer(skip));
        switch (skip)
        {
          case SKIP_NONE:
          {
            boolean condition = evaluateBooleanExpression(macroTable.expandMacros(directiveText));
            if (!condition)
              skip = SKIP_CONDITIONAL;
            break;
          }
          case SKIP_CONDITIONAL:
          {
            // If there was already a previous condition that failed then we are 
            // entering a sub condition right now. Change the skip mode to parent skip to reflect that.
            skip = SKIP_PARENT;
            break;
          }
        }
       break;
      }
      case ELSE:
      {
        Integer pendingState = (Integer) pendingStates.pop();
        // See what the state was, which opened this conditional branch.
        switch (pendingState.intValue())
        {
          case IF:
          case IFDEF:
          case IFNDEF:
          case ELIF:
          {
            pendingStates.push(ELSE_DIRECTIVE);
            // If the superordinated conditional branch was already skipping then continue that,
            // otherwise reveverse the skip mode for this branch.
            switch (skip)
            {
              case SKIP_NONE:
              {
                skip = SKIP_CONDITIONAL;
                break;
              }
              case SKIP_CONDITIONAL:
              {
                skip = SKIP_NONE;
                break;
              }
            }
            break;
          }
          default:
          {
            reportError("Unexpected #else preprocessor directive found.");
          }
        }
        break;
      }
      case ELIF:
      {
        Integer pendingState = (Integer) pendingStates.pop();
        // See what the state was, which opened this conditional branch.
        switch (pendingState.intValue())
        {
          case IFDEF:
          case IFNDEF:
          case IF:
          case ELIF:
          {
            pendingStates.push(ELIF_DIRECTIVE);
            
            // If the parent condition already was skipping then continue that.
            // If we are in a subbranch (elif branch), which is currently skipping then continue that.
            // If the previous condition was met then start skipping the entire branch that follows now.
            // And finally if the previous condition was not met then check here again for the current condition.
            if ((skip != SKIP_PARENT) && (skip != SKIP_SUBTREE))
            {
              // If currently lines are not skipped then the previous condition was met and
              // we have to skip everything from now on until the #endif that belongs to the condition starter.
              if (skip == SKIP_NONE)
              {
                skip = SKIP_SUBTREE;
              }
              else
              {
                boolean condition = evaluateBooleanExpression(macroTable.expandMacros(parts[1]));
                if (condition)
                  skip = SKIP_NONE;
              }
            }
            break;
          }
          default:
          {
            reportError("Unexpected #elif preprocessor directive found.");
          }
        }
        break;
      }
      case ENDIF:
      {
        Integer pendingState = (Integer) pendingStates.pop();
        Integer lastSkipMode = (Integer) skipFlags.pop();
        skip = lastSkipMode.intValue();
        // See what the state was, which opened this conditional branch.
        switch (pendingState.intValue())
        {
          case IF:
          case IFDEF:
          case IFNDEF:
          case ELIF:
          case ELSE:
          {
            // Return to parent mode.
            break;
          }
          default:
          {
            reportError("Unexpected #endif preprocessor directive found.");
          }
        }
        break;
      }
    }
    
    return result;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Handles an include directive by loading the given file and creating a new (nested) instance
   * of the preprocessor.
   * 
   * @param string Contains the rest of the #include line, which contains the file name.
   * @return <b>true</b> if a new preprocessor was set up, otherwise <b>false</b>.
   */
  private boolean processInclude(String filename) throws FileNotFoundException, IOException
  {
    boolean foundError = false;
    // Filenames must be given either quoted or enclosed in angles. We'll be tolerant by ignoring
    // everything, which comes after the file name.
    int quote = filename.indexOf('"');
    if (quote > -1)
    {
      // File name in quotes.
      int unquote = filename.indexOf('"', quote + 1);
      if (unquote == -1)
      {
        reportError("Invalid include statement.");
        foundError = true;
      }
      filename = filename.substring(quote + 1, unquote);
    }
    else
    {
      int leftAngle = filename.indexOf("<");
      if (leftAngle > -1)
      {
        // File name in angles.
        int rightAngle = filename.indexOf('>', leftAngle + 1);
        if (rightAngle == -1)
        {
          reportError("Invalid include statement.");
          foundError = true;
        }
        filename = filename.substring(leftAngle + 1, rightAngle);
      }
      else
      {
        reportError("Invalid include statement.");
        foundError = true;
      }
    }
    
    if (!foundError)
      return includeFile(filename);
    else
      return false;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Processes a #pragma directive. It particularly handles #pragma once and #pragma message. 
   * Everything else ist simply passed through to the output.
   * 
   * @param parts The directive line split into two parts ("pragma" and the rest).
   */
  private void processPragma(String[] parts) throws IOException
  {
    // MSDN states the resource compile does not support the pragma directive except for
    // pragma code_page. In header files also pragma once can appear (which is handled here).
    // But otherwise no more directives are supposedly supported. I wished the documentation
    // would keep up with the reality.
    if (parts.length < 2)
      reportError("Malformed pragma directive.");
    else
    {
      String pragma = parts[1].trim();
      if (pragma.equalsIgnoreCase("once"))
        processedIncludes.put(inputState.getFilename(), null);
      else
        if (pragma.startsWith("message"))
        {
          int start = pragma.indexOf('"');
          int stop = pragma.lastIndexOf('"');
          if (start > -1 && stop > -1)
            reportInfo(pragma.substring(start + 1, stop));
          else
            reportError("Invalid pragma message format.");
        }
        else
          if (pragma.startsWith("code_page"))
          {
            // TODO: Handle code page change.
          }
      // Ignore any other #pragma directive.
    }
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Called if an #undef directive was found. Get the identifier and remove it from the symbol table.
   * 
   * @param definition Input containing the identifier.
   */
  private void processUndef(String definition)
  {
    String[] parts = definition.split(WHITESPACES, 2);
    if (parts.length == 0)
      reportError("Invalid #undef directive found.");
    else
    {
      String symbol = parts[0].trim();
      macroTable.undefineMacro(symbol);
    }
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Scans the given string for comments and removes them. If the line contains a multiline comment
   * start but no end then the private field inMulitlineComment is set to true to tell the caller
   * how to proceed with following lines.
   * 
   * @param line The input line to process.
   * @return The cleaned up string without comments. Whitespaces at both ends are removed as well.
   */
  private String removeComments(String line) throws IOException
  {
    StringBuffer buffer = new StringBuffer(line);
    
    for (int i = 0; i < buffer.length(); i++)
    {
      switch (buffer.charAt(i))
      {
        case '/': // Potential comment start.
          // There must be at least one more character to have a comment start.
          if (i + 1 < buffer.length())
          {
            switch (buffer.charAt(i + 1))
            {
              case '/': // Single line comment found. Replace the comment by one space character.
                buffer.replace(i, buffer.length(), " ");
                break;
              case '*': // Multi line comment found. Scan for end.
                int commentEnd = buffer.indexOf("*/", i + 2);
                
                if (commentEnd > -1)
                  buffer.replace(i, commentEnd + 2, " ");
                else
                {
                  // The comment does not end on this line. Replace what we have by a space char
                  // and tell the caller about the open comment.
                  inMultilineComment = true;
                  buffer.replace(i, buffer.length(), " ");
                }
                break;
            }
          }
          break;
        case '"': // String start found. Search for its end before scanning further for comments.
          int stringEnd = buffer.indexOf("\"", i + 1);
          
          // If we did not find a string end delimiter then the source is syntactically wrong.
          if (stringEnd == -1)
          {
            i = buffer.length();
            reportError("Unterminated string found.");
          }
          else
            i = stringEnd;
          break;
      }
    }
    return buffer.toString().trim();
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Splits the given line into the directive string and the rest.
   * 
   * @param line The line to split.
   */
  private String[] splitDirective(String line)
  {
    // Remove the leading number sign and split the rest into two parts of which the first
    // one is the textual form of the directive.
    int i = 1;
    
    // Skip white spaces between number sign and directive.
    while (i < line.length() && ((line.charAt(i) == ' ') || (line.charAt(i) == '\t')))
      i++;
    int nonWhitespaceStart = i;
    while (i < line.length())
    {
      // Stop scanning if there is a character not in the ranges 'a'..'z' or 'A'..'Z'.
      char letter = line.charAt(i);
      if (!((letter >= 'a') && (letter <= 'z') || (letter >= 'A') && (letter <= 'Z')))
        break;
      i++;
    }
    String[] values = new String[2];
    values[0] = line.substring(nonWhitespaceStart, i);
    values[1] = line.substring(i, line.length());
    
    return values;
  }

  //------------------------------------------------------------------------------------------------
  
  protected void doEvent(int event, String message, boolean addFileInfo)
  {
    switch (event)
    {
      case IParseEventListener.PANIC:
      case IParseEventListener.ERROR:
      {
        hadErrors = true;
        break;
      }
      case IParseEventListener.WARNING:
      {
        hadWarnings = true;
        break;
      }
    }
    
    if (addFileInfo)
    {
      message = MessageFormat.format(
        "{0}: [line {1}] {2}", 
        new Object[] 
        {
          inputState.getFullFilename(),
          new Integer(inputState.getLine()),
          message
        }
      );
    }
    
    for (int i = 0; i < listeners.size(); i++)
    {
      IParseEventListener listener = (IParseEventListener)listeners.get(i);
      listener.handleEvent(event, message);
    }
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * This method processes uncoditional input and returns when a non-preprocessor line was found.
   * 
   * @return The line, which caused the abort of unconditional input processing.
   * 
   * @throws IOException
   */
  protected String processUnconditionalInput() throws IOException
  {
    // Note: Comments are handled here because we have to make sure we do not consider outcommented
    //       preprocessor directives. Single line and multi line comments are converted to one space
    //       character each, so the output does not contain comments anymore (which somewhat 
    //       simplifies the following parser stage).
    String line = null;
    boolean canReturn = false;
    do
    {
      line = readLine();
      if (line == null)
        break;
  
      // Now that the string is cleaned-up we can start processing directives.
      if (line.startsWith("#"))
      {
        canReturn = processDirective(line);
        if (canReturn)
          line = "";
      }
      else
      {
        // No preprocessor directive in this line, so return it to the caller if it is not empty.
        if ((skip == SKIP_NONE) && !skipNonPPLines)
        {
          line = macroTable.expandMacros(line);
          if (line.length() > 0)
            canReturn = true;
        }
      }
    }
    while (!canReturn);
    
    return line;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Reads the next input line and advances the internal line counter. If we are currently in multi
   * line comment mode then skip comment lines until we find a comment end.
   * 
   * @return The read line.
   * @throws IOException
   */
  protected String readLine() throws IOException
  {
    String line = null;
    
    do
    {
      line = input.readLine();
      if (traceProcessing)
        System.out.println("   : " + line);
      if (line == null)
      {
        // If the input string empty then there is no more input available in this file.
        // Return to previous input state in this case.
        reportIncludeFile("<<< " + inputState.getFilename());
        inputState.popState();
        break;
      }
      if (skip != SKIP_NONE)
        break;
      
      // First check if we are still waiting for a multi line comment to end.
      if (inMultilineComment)
      {
        int commentEndIndex = line.indexOf("*/");
        // If the comment does not end on this line then start over with the next one.
        if (commentEndIndex == -1)
          continue;
        
        // The multi line comment ends here. Remove the remaining comment part and continue processing.
        inMultilineComment = false;
        line = line.substring(commentEndIndex + 2, line.length());
        break;
      }
    }
    while (inMultilineComment);
    
    if (line == null)
      return null;
    else
      if (skip != SKIP_NONE)
        return line.trim();
      else
        return removeComments(line);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Reads and processes all input until a non-preprocessor line is found. This line is appended
   * to the current content of the currentLine string buffer that is used to feed the lexer.
   * 
   * @throws IOException Thrown when something goes wrong with reading file data.
   */
  protected void readNextLine() throws IOException
  {
    currentLine.delete(0, currentPosition);
    currentPosition = 0;
    String result = processUnconditionalInput();
    if (result != null)
      currentLine.append(result);
    currentLine.append('\n');
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Adds the given search path for include files to the internal list.
   * Note that this preprocessor does nothing know about predefined pathes as used in MSVC (e.g.
   * as in <tchar.h>). So you must explicitely give these pathes too.
   * 
   * @param name The new search path to add.
   */
  public void addIncludePath(String name)
  {
    includePaths.add(name); 
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Adds the given macro to the internal macro table.
   * Note: The definition must not contain the <b>#define</b> part.
   * 
   * @param macro The macro definition string.
   */
  public void addMacro(String macro)
  {
    macroTable.defineMacro(macro);
  }
  
  //------------------------------------------------------------------------------------------------
  
  public void addPreprocessorEventListener(IParseEventListener listener)
  {
    listeners.add(listener);
  }

  //------------------------------------------------------------------------------------------------

  /* (non-Javadoc)
   * @see java.io.Reader#close()
   */
  public void close() throws IOException
  {
    // Nothing to do here.
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Returns the internal table of collected macro definitions.
   * 
   * @return Returns the macro table.
   */
  public MacroTable getMacroTable()
  {
    return macroTable;
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

  /* (non-Javadoc)
   * @see net.softgems.resourceparser.main.IParseEventListener#handleEvent(int, java.lang.String)
   */
  public void handleEvent(int event, String message)
  {
    // This method is called here only during an expression evaluation and is used to forward
    // the incoming events to the owner of this preprocessor.
    doEvent(event, message, false);
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Sets a new input state up so following read attempts by the lexer are directed to the new
   * preprocessor.
   * 
   * @param filename The file to include.
   * @return <b>true</b> if a new preprocessor was set up, otherwise <b>false</b>.
   * @throws IOException
   * @throws FileNotFoundException
   */
  public boolean includeFile(String filename) throws IOException, FileNotFoundException
  {
    boolean result = false;
    
    // The map processedIncludes contains files, which were marked with the "#pragma once" directive
    // and hence should automatically not be included more than once.
    if (!processedIncludes.containsKey(filename))
    {
      // To make relative includes from the included file possible switch to the resulting
      // directory of this include file. Restore the value after return.
      File canonicalFile = null;
  
      // Try to locate the include file without include path first (relative to application root)
      File file = new File(filename);

      // Convert eventual further subfolders in the file to one canonical file,
      // so we have resolved links and no special folders like "." or ".." etc. anymore.
      canonicalFile = file.getCanonicalFile();
      if (!file.isAbsolute())
      {
        // If the file is relative then first try to access it as if it is using the current
        // user dir as base folder.
        if (!canonicalFile.canRead())
        {
          // We have a relative file, which is not accessible from the current user directory.
          // Try each include path until we have access.
          for (Iterator iterator = includePaths.iterator(); iterator.hasNext();)
          {
            String path = (String) iterator.next();
            if (!path.endsWith(File.separator))
              path += File.separator;
            
            File testFile = new File(path + filename);
            if (testFile.exists())
            {
              canonicalFile = testFile.getCanonicalFile();
              break;
            }
          }
        }
      }
      
      if (canonicalFile.canRead())
      {
        reportIncludeFile(">>> " + filename);
        
        // If we can read the file then set up a new preprocessor instance. The sub preprocessor 
        // uses the same input state, include pathes and defined symbols as this one.
        InputConverter input = new InputConverter(inputState, new FileInputStream(canonicalFile), 
          DEFAULT_CHARSET);
        Preprocessor preprocessor = new Preprocessor(input, this, inputState, traceProcessing);
        inputState.pushState(preprocessor, canonicalFile.getName(), canonicalFile.getParent());
        preprocessor.addPreprocessorEventListener(
          new IParseEventListener() 
          {
            public void handleEvent(int event, String message)
            {
              doEvent(event, message, false);
            };
          }
        );
        preprocessor.init();
        result = true;
      }
      else
        reportWarning("Cannot access include file \"" + filename + "\"");
    }
    return result;
  }

  //------------------------------------------------------------------------------------------------

  public void init()
  {
    // Microsoft's resource compiler handles *.c and *.h files in a special way. Everything except 
    // preprocessor lines is automatically skipped in such files.
    // See also: "Using #include Directive with Windows Resource Compiler", KB Q80945.
    String filename = inputState.getFilename();
    skipNonPPLines = filename.toLowerCase().endsWith(".c") || filename.toLowerCase().endsWith(".h");
  }
  
  //------------------------------------------------------------------------------------------------

  /** 
   * This method is executed by the preprocessor internally when it detected an illegal
   *  state that cannot be recovered from.
   */
  public void panic(String s)
  {
    doEvent(IParseEventListener.PANIC, "RC lexer panic: " + s, true);
  }
  
  //------------------------------------------------------------------------------------------------

  /* (non-Javadoc)
   * @see java.io.Reader#read()
   */
  public int read() throws IOException
  {
    if (currentLine.length() < (currentPosition + 1))
      readNextLine();
    
    if (currentLine.length() == 0)
      return -1;
    else
      return currentLine.charAt(currentPosition++);
  }

  //------------------------------------------------------------------------------------------------

  /* (non-Javadoc)
   * @see java.io.Reader#read(char[], int, int)
   */
  public int read(char[] cbuf, int off, int len) throws IOException
  {
    if (currentLine.length() < (currentPosition + len))
      readNextLine();
    
    if (currentLine.length() == 0)
      return -1;
    else
    {
      int actualLength = Math.min(currentLine.length() - currentPosition, len);
      currentLine.getChars(currentPosition, actualLength, cbuf, off);
      currentPosition += actualLength;
      
      return actualLength;
    }
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Removes the given listener from the internal list. If the listerner is not in this list
   * then the method does nothing.
   * 
   * @param listener The listener to be removed.
   */
  public void removePreprocessorEventListener(IParseEventListener listener)
  {
    listeners.remove(listener);
  }
  
  //------------------------------------------------------------------------------------------------

  /**
   * Removes the symbol with the given name from the internal symbol table. If the symbol does not
   * exist then nothing happens.
   * 
   * @param name The name of the symbol to be removed.
   */
  public void removeSymbol(String name)
  {
    macroTable.undefineMacro(name);
  }
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * Reports an error to the calling application.
   * 
   * @param s Error message to report.
   */
  public void reportError(String s)
  {
    doEvent(IParseEventListener.ERROR, s, inputState.getFullFilename() != null);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Reports the names of all files, which are included during the preprocessing step.
   * 
   * @param filen The name of the file that is about to be processed.
   */
  public void reportIncludeFile(String file)
  {
    doEvent(IParseEventListener.INCLUDE_FILE, file, inputState.getFullFilename() != null);
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Reports general information to the calling application.
   * 
   * @param s Information message to report.
   */
  public void reportInfo(String s)
  {
    doEvent(IParseEventListener.INFORMATION, s, inputState.getFullFilename() != null);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Reports a warning to the calling application.
   * 
   * @param s Warning message to report.
   */
  public void reportWarning(String s)
  {
    doEvent(IParseEventListener.WARNING, s, inputState.getFullFilename() != null);
  }

  //------------------------------------------------------------------------------------------------

}
