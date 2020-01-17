/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser;

import java.io.*;
import java.util.ArrayList;

import net.softgems.resourceparser.main.*;
import net.softgems.resourceparser.preprocessor.*;
import net.softgems.resourceparser.xml.ResourceStatement;

import org.jdom.*;
import org.jdom.output.*;

import antlr.TokenStreamRecognitionException;
import antlr.collections.AST;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class RCMainFrame extends org.eclipse.swt.widgets.Composite {
	private Button browseOutputFolderButton;
	private Text outputPathEdit;
	private Label button1;
	private Button browseIniFileButton;
	private Text txtFileNameEdit;
	private Button multipleFilesRadioButton;
	private Button browseRCFileButton;
	private Text rcFileNameEdit;
	private Button singleFileRadioButton;
	private Composite composite7;
	private Button addIncludePathButton;
	private Button addSymbolButton;
  private String[] commandLine;
	private Composite composite1;
	private Composite composite2;
	private Composite composite3;
	private Composite composite4;
	private Composite composite5;
	private Composite composite6;
  private ArrayList defines = new ArrayList();
	private Group group1;
	private List includePathList;
  private ArrayList includes = new ArrayList();
	private Label label2;
	private Label label3;
	private Label label4;
	private Label label5;
	private Label label6;
	private Label label7;
	private Button parseButton;
	private Button removeIncludePathButton;
	private Button removeSymbolButton;
	private Shell shell;
	private TabFolder tabFolder1;
	private TabItem tabItem1;
	private TabItem tabItem2;
	private TabItem tabItem3;
	private TableColumn tableColumn1;
	private TableColumn tableColumn2;
  protected TableCursor cursor;
	protected int errorCount;
  protected boolean isEditing;
	protected List log;
	protected Label parseLineLabel;
	protected Tree parseTree;
  protected int preProcessedLines;
	protected Label preprocessLineLabel;
	protected List processedIncludesList;
	protected int processedLines;
	protected Table symbolsTable;
	protected int tokenCount;
  
  /** Mapper to convert a parse event into a textual representation. */
  protected String[] eventStrings = new String[] {
    "(PANIC) ", "(Error) ", "(Warning) ", "(Information) "
  };
  
	public RCMainFrame(Composite parent, int style, String[] args) {
		super(parent, style);
		this.shell = (Shell) parent;
    commandLine = args;
		initGUI();
	}
  
  //------------------------------------------------------------------------------------------------

  /**
   * Converts the given file, which must be an rc file to XML.
   * 
   * @param file The file to convert.
   * @param writeIntermediate If <b>true</b> then the intermediate text that comes out from the
   *         preprocessing step is written in a separate file with extension *.rcX. Since this
   *         intermediate content is the base for the parser all error line numbers correspond to
   *         this file, not the original input.
   * @param createVisualAST If <b>true</b> then the abstract syntax tree is loaded into a treeviewer
   *         for examination. This step is not needed for the conversion process.
   * @param targetPath The path where to write the resulting XML file to. If this path is <b>null</b>
   *         then the XML file is written to the folder where the source file is located.
   * @return <b>true</b> if no error/warning was reported, otherwise <b>false</b>.
   * @throws Exception Thrown for various reasons (IO error, recognition error etc.).
   */
  private boolean convertFile(String filename, boolean writeIntermediate, boolean createVisualAST,
    String targetPath) throws Exception
  {
    boolean result = true;
    
    File file = new File(filename);
    logMessage("Parsing file: " + file.getCanonicalPath());
    
    // Set the current user directory to be the folder that contains the current rc file.
    // This is necessary to make inclusion of file with relative path possible.
    String folder = file.getParent();
    System.setProperty("user.dir", folder);
    logMessage("Set working folder to " + folder);
    
    // The translation of a resource file is done very similar to the phases described in MSDN:
    // "C/C++ Preprocessor Reference" -> "Phases of Translation".
    // Note: None of the steps described below is done on its own. It is rather a big chain
    //       driven by the parser, which reads tokens from the lexer, which reads characters from
    //       the preprocessor, which reads lines from the input converter, which reads raw bytes.
    
    PreprocessorInputState inputState = new PreprocessorInputState();

    // 1) Convert the input data into our internal representation (Unicode). 
    //    Convert trigraph sequences and do line splicing in this process too.
    //    For conversion it is necessary to know in which character set the input is encoded.
    //    Standard C files are all ANSI (ISO-8859-1, Latin-1) encoded, but resource files may have
    //    any other encoding including Unicode, so it is important to specify the right encoding.
    //    However usually also resource files are ANSI encoded, hence this charset can be used as 
    //    a good first guess. Resource files may contain a #pragma code_page directive to switch
    //    the current codepage.
    InputConverter converter = new InputConverter(inputState, new FileInputStream(filename), 
      Preprocessor.DEFAULT_CHARSET);
    
    // 2) Handle preprocessor directives.
    //    The preprocessor takes a list of include pathes and a list of predefined symbols.
    //    Output will be cleaned source code without any preprocessor stuff and without comments.
    //    Include files will directly be imported by the preprocessor.
    Preprocessor preprocessor = new Preprocessor(converter, null, inputState, false);
    for (int i = 0; i < includePathList.getItemCount(); i++)
      preprocessor.addIncludePath(includePathList.getItem(i));
    
    for (int i = 0; i < symbolsTable.getItemCount(); i++)
    {
      TableItem item = symbolsTable.getItem(i);
      preprocessor.addMacro(item.getText(0) + ' ' + item.getText(1));
    }
    
    preprocessor.addPreprocessorEventListener(
      new IParseEventListener() 
      {
        public void handleEvent(int event, String message)
        {
          switch (event)
          {
            case IParseEventListener.NEW_LINE:
            {
              preProcessedLines++;
              if (preProcessedLines % 5000 == 0)
              {
                preprocessLineLabel.setText(Integer.toString(preProcessedLines));
                preprocessLineLabel.update();
              }
              break;
            }
            case IParseEventListener.INCLUDE_FILE:
              processedIncludesList.add(message);
              processedIncludesList.update();
              break;
            default:
              log.add("[Preprocessor] " + eventStrings[event] + message);
              log.update();
          };
        };
      }
    );
    
    // 3) Tokenize the input and feed the parser.
    inputState.pushState(preprocessor, file.getName(), file.getParent());

    // Once the initial state is set we can add extra files we need to be parsed in advance.
    // Using Winresrc.h as standard include will make sure everything else, which is usually
    // included by the resource compiler is also included here.
    preprocessor.includeFile("Winresrc.h");
    
    RCLexer lexer = new RCLexer(inputState);
    lexer.addLexerEventListener(
      new IParseEventListener()
      {
        public void handleEvent(int event, String message)
        {
          switch (event)
          {
            case IParseEventListener.NEW_LINE:
            {
              processedLines++;
              parseLineLabel.setText("" + processedLines);
              parseLineLabel.update();
              
              break;
            }
            default:
            {
              log.add("[Lexer] " + eventStrings[event] + message);
              log.update();
            }
          };
        };
      }
    );

    // 4) Here comes the actual parsing. This step converts the rc source code into an
    //    abstract syntax tree (AST).
    RCParserSharedInputState parserInputState = new RCParserSharedInputState();
    parserInputState.setInput(lexer);
    RCParser parser = new RCParser(parserInputState);
    parser.setFilename(filename);
    parser.addParserEventListener(
      new IParseEventListener() 
      {
        public void handleEvent(int event, String message)
        {
          log.add("[Parser] " + eventStrings[event] + message);
          log.update();
        };
      }
    );

    // This call will actually start the parsing process.
    parser.resource_definition();
    if (preprocessor.hadErrors() || lexer.hadErrors() || parser.hadErrors())
      result = false;
    
    logMessage("Parsing finished.");
    
    if (createVisualAST)
    {
      logMessage("Filling abstract syntax tree.");
      fillParseTree(parser);
    }
    
    // Finally write out the XML data.
    exportXML(parser, file, preprocessor.getMacroTable(), targetPath);
    
    // Free unused objects in case there is a large bunch of files to convert.
    System.gc();
    
    return result;
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Exports all parsed content to an XML file named like the input file but with xml extension.
   * 
   * @param parser The parser containing the parsed data.
   * @param sourceFile The input file (only need to create the target file name from).
   * @param macroTable The macro class that allows to resolve expressions.
   * @param targetPath The path where to write the resulting XML file to. If this path is <b>null</b>
   *         then the XML file is written to the folder where the source file is located.
   */
  private void exportXML(RCParser parser, File sourceFile, MacroTable macroTable, String targetPath)
  {
    String filename;
    try
    {
      filename = sourceFile.getName();
      int index = filename.lastIndexOf('.');
      if (index == -1)
        filename += ".xml";
      else
        filename = filename.substring(0, index) + ".xml";

      File targetFile;
      if (targetPath == null)
      {
        targetPath = sourceFile.getParent();
      }
      if (!targetPath.endsWith("\\") && !targetPath.endsWith("/"))
        targetPath += "/";
      targetFile = new File(targetPath + filename);
      if (targetFile.exists())
        targetFile.delete();
      if (targetFile.createNewFile())
      {
        logMessage("Exporting result to " + filename);
        
        Document document = new Document();
        Element rootElement = new Element("dialog-definition");
        document.setRootElement(rootElement);
        
        AST node = parser.getAST();
        while (node != null)
        {
          ResourceStatement statement = new ResourceStatement(this, node);
          statement.setMacroTable(macroTable);
          statement.convert(rootElement);
          node = node.getNextSibling();
        }
       
        // Let JDOM format the output and write the result to disc.
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        OutputStream stream = new FileOutputStream(targetFile);
        outputter.output(document, stream);
        stream.close();
      }
      else
      {
        logMessage("Could not create target file " + filename);
      }
    }
    catch (IOException e)
    {
      logMessage("Error while creating output file " + sourceFile.getAbsolutePath());
    }
  }

  //------------------------------------------------------------------------------------------------

  private void fillParseTree(RCParser parser)
  {
    parseTree.removeAll();
    AST root = parser.getAST();
    while (root != null)
    {
      TreeItem item = new TreeItem(parseTree, SWT.NULL);
      item.setText(root.getText() + " (" + parser.getTokenName(root.getType()) + ")");
      fillParseTreeNode(parser, root, item);
      
      root = root.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------

  private void fillParseTreeNode(RCParser parser, AST astNode, TreeItem treeNode)
  {
    AST run = astNode.getFirstChild();
    while (run != null)
    {
      TreeItem item = new TreeItem(treeNode, SWT.NULL);
      item.setText(run.getText() + " (" + parser.getTokenName(run.getType()) + ")");
      fillParseTreeNode(parser, run, item);
      
      run = run.getNextSibling();
    }
  }
	
  //------------------------------------------------------------------------------------------------
  
  /**
   * Checks the java command line for include path and symbol specifications. We accept two entry 
   * types. One is 
   *   -include=path 
   * and the other is
   *   -symbol="name value"
   * Note: Pathes (in fact any command line argument, which contains spaces) often need to be wrapped 
   *       by quotes and since it does not harm if they are quoted even if unnecessary it is good 
   *       practice to always quote them.
   */
  private void parseCommandLine()
  {
    for (int i = 0; i < commandLine.length; i++)
    {
      String entry = commandLine[i].trim();
      if (entry.startsWith("-include=") || entry.startsWith("-i="))
      {
        String[] parts = entry.split("=");
        if (parts.length > 1)
          includes.add(parts[1].trim());
      }
      else
        if (entry.startsWith("-symbol=") || entry.startsWith("-s="))
        {
          String[] parts = entry.split("=");
          if (parts.length > 1)
            defines.add(parts[1].trim());
        }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Creates a cursor for the given table and adds an inplace editor to allow editing the values.
   * 
   * @param table The table for which the cursor is to be created.
   * @return The new table cursor.
   */
  private TableCursor setupTableCursor(final Table table)
  {
    final TableCursor cursor = new TableCursor(table, SWT.NONE);
 
    final ControlEditor editor = new ControlEditor(cursor);
    editor.grabHorizontal = true;
    editor.grabVertical = true;
 
    cursor.addSelectionListener(
      new SelectionAdapter()
      {
 
        //------------------------------------------------------------------------------------------
        
        /**
         *  When the user presses <enter> then show the inplace editor.
         */
        public void widgetDefaultSelected(SelectionEvent e)
        {
          startInplaceEditing(editor, cursor);
        }
        //------------------------------------------------------------------------------------------
        
        /**
         *  Select the according line in the table when the editor is over it.
         */
        public void widgetSelected(SelectionEvent e)
        {
          table.setSelection(new TableItem[] {cursor.getRow()});
        }
        //------------------------------------------------------------------------------------------
        
      }
    );
 
    cursor.addMouseListener(
      new MouseListener()
      {
        long lastClick = 0;
 
        //------------------------------------------------------------------------------------------
        
        /**
         *  Start editing also for double clicks.
         */
        public void mouseDoubleClick(MouseEvent e)
        {
          startInplaceEditing(editor, cursor);
        }

        //------------------------------------------------------------------------------------------
        
        public void mouseDown(MouseEvent e)
        {
          long currentClick = System.currentTimeMillis();
          if (currentClick - lastClick > 1000)
          {
            if (isEditing)
            {
              stopInplaceEditing(editor, cursor, true);
            }
          }
          else
          {
            startInplaceEditing(editor, cursor);
          }
          lastClick = currentClick;
        }

        //------------------------------------------------------------------------------------------
        
        public void mouseUp(MouseEvent e)
        {
          // Nothing to do.
        }

        //------------------------------------------------------------------------------------------
        
      }
    );
 
    cursor.addKeyListener(
      new KeyListener()
      {

        //------------------------------------------------------------------------------------------
        
        public void keyPressed(KeyEvent e)
        {
          if (e.keyCode == SWT.F2)
          {
            startInplaceEditing(editor, cursor);
          }
        }

        //------------------------------------------------------------------------------------------
        
        public void keyReleased(KeyEvent e)
        {
          // Nothing to do.
        }
      //------------------------------------------------------------------------------------------
      
      }
    );
 
    return cursor;
  }
  
  //------------------------------------------------------------------------------------------------

	protected void addIncludePathButtonWidgetSelected(SelectionEvent evt)
  {
    DirectoryDialog folderPicker = new DirectoryDialog(shell, SWT.NULL);
    String folder = folderPicker.open();
    if (folder != null)
      includePathList.add(folder);
	}

  //------------------------------------------------------------------------------------------------

	protected void addSymbolButtonWidgetSelected(SelectionEvent evt)
  {
    TableItem item = new TableItem(symbolsTable, SWT.NULL);
    item.setText(0, "New symbol");
    item.setText(1, "New value");
    symbolsTable.setFocus();
    symbolsTable.setSelection(new TableItem[] {item});
    cursor.setSelection(item, 0);
	}
 
  //------------------------------------------------------------------------------------------------

	protected void includePathListWidgetSelected(SelectionEvent evt)
  {
    if (includePathList.getSelectionIndex() > -1)
      removeIncludePathButton.setEnabled(true);
	}
	
	//------------------------------------------------------------------------------------------------
	
	 protected void parseButtonWidgetSelected(SelectionEvent evt) 
	 {
    tokenCount = 0;
    processedLines = 0;
    preProcessedLines = 0;
    log.removeAll();
    processedIncludesList.removeAll();
    errorCount = 0;

    // Switch to second (parse result) page.
    tabFolder1.setSelection(1);
    tabFolder1.update();
    logMessage("Parsing started.");
    try
    {
      // Depending on the settings there is either only one file to parse or a list of them.
      boolean createASTVisualization;
      ArrayList fileList = new ArrayList();
      if (singleFileRadioButton.getSelection())
      {
        fileList.add(rcFileNameEdit.getText());
        createASTVisualization = true;
      }
      else
      {
        FileReader fileReader = new FileReader(txtFileNameEdit.getText());
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        do
        {
          line = reader.readLine();
          if (line == null)
            break;

          line = line.trim();
          if (line.length() > 0)
            fileList.add(line);
        }
        while (true);
        
        createASTVisualization = false;
      }
      
      boolean breakOnError = true;
      
      for (int i = 0; i < fileList.size(); i++)
        if (!convertFile((String) fileList.get(i), true, createASTVisualization, 
            outputPathEdit.getText()) &&
          breakOnError)
          break;
    }
    catch(TokenStreamRecognitionException e)
    {
      log.add(e.toString());
      e.recog.printStackTrace();
    }
    catch(Exception e)
    {
      log.add(e.toString());
      if (e.getCause() != null)
        e.getCause().printStackTrace();
      else
        e.printStackTrace();
    }
    logMessage("Parsing done.");
  }

  //------------------------------------------------------------------------------------------------

	protected void removeIncludePathButtonWidgetSelected(SelectionEvent evt)
  {
    int index = includePathList.getSelectionIndex();
    if (index > -1)
    {
      includePathList.remove(index);
      if (includePathList.getItemCount() == 0)
        removeIncludePathButton.setEnabled(false);
      else
        if (index < includePathList.getItemCount())
          includePathList.setSelection(index);
        else
          includePathList.setSelection(index - 1);
    }
	}

  //------------------------------------------------------------------------------------------------

	protected void removeSymbolButtonWidgetSelected(SelectionEvent evt)
  {
    int index = symbolsTable.getSelectionIndex();
    if (index > -1)
    {
      symbolsTable.remove(index);
      if (symbolsTable.getItemCount() == 0)
        removeSymbolButton.setEnabled(false);
      else
        if (index < symbolsTable.getItemCount())
          symbolsTable.setSelection(index);
        else
          symbolsTable.setSelection(index - 1);
    }
	}
 
  //------------------------------------------------------------------------------------------------

  protected void startInplaceEditing(final ControlEditor editor, final TableCursor cursor)
  {
    if (!isEditing)
    {
      isEditing = true;
      TableItem row = cursor.getRow();
      int column = cursor.getColumn();
 
      final Text text = new Text(cursor, SWT.RIGHT);
      text.setText(row.getText(column));
      text.addKeyListener(
        new KeyAdapter()
        {
          public void keyPressed(KeyEvent e)
          {
            // When the user presses <enter> the changes will be accepted.
            switch (e.character)
            {
              case SWT.CR:
                stopInplaceEditing(editor, cursor, true);
                break;
              case SWT.ESC:
                stopInplaceEditing(editor, cursor, false);
                break;
            }
          }
        }
      );
      editor.setEditor(text);
      text.selectAll();
      text.setFocus();
    }
  }
 
  //------------------------------------------------------------------------------------------------
 
  protected void stopInplaceEditing(ControlEditor editor, TableCursor cursor, boolean accept)
  {
    if (isEditing)
    {
      isEditing = false;
      Text text = (Text)editor.getEditor();
      if (accept)
      {
        TableItem row = cursor.getRow();
        row.setText(cursor.getColumn(), text.getText());
      }
      text.dispose();
    }
  }

  //------------------------------------------------------------------------------------------------

	protected void symbolsTableWidgetSelected(SelectionEvent evt)
  {
    if (symbolsTable.getSelectionIndex() > -1)
      removeSymbolButton.setEnabled(true);
	}
	
	/**
	* Initializes the GUI.
	* Auto-generated code - any changes you make will disappear.
	*/
	public void initGUI(){
		try {
			preInitGUI();
	
			composite6 = new Composite(this,SWT.NULL);
			composite7 = new Composite(composite6,SWT.NULL);
			singleFileRadioButton = new Button(composite7,SWT.RADIO| SWT.LEFT);
			rcFileNameEdit = new Text(composite7,SWT.BORDER);
			browseRCFileButton = new Button(composite7,SWT.PUSH| SWT.CENTER);
			multipleFilesRadioButton = new Button(composite7,SWT.RADIO| SWT.LEFT);
			txtFileNameEdit = new Text(composite7,SWT.BORDER);
			browseIniFileButton = new Button(composite7,SWT.PUSH| SWT.CENTER);
			button1 = new Label(composite7,SWT.LEFT);
			outputPathEdit = new Text(composite7,SWT.BORDER);
			browseOutputFolderButton = new Button(composite7,SWT.PUSH| SWT.CENTER);
			parseButton = new Button(composite6,SWT.PUSH| SWT.CENTER);
			group1 = new Group(this,SWT.NULL);
			tabFolder1 = new TabFolder(group1,SWT.NULL);
			tabItem3 = new TabItem(tabFolder1,SWT.NULL);
			composite3 = new Composite(tabFolder1,SWT.NULL);
			label6 = new Label(composite3,SWT.NULL);
			includePathList = new List(composite3,SWT.H_SCROLL| SWT.V_SCROLL| SWT.BORDER);
			composite4 = new Composite(composite3,SWT.NULL);
			addIncludePathButton = new Button(composite4,SWT.PUSH| SWT.CENTER);
			removeIncludePathButton = new Button(composite4,SWT.PUSH| SWT.CENTER);
			label7 = new Label(composite3,SWT.NULL);
			symbolsTable = new Table(composite3,SWT.SINGLE| SWT.FULL_SELECTION| SWT.H_SCROLL| SWT.V_SCROLL| SWT.BORDER);
			tableColumn1 = new TableColumn(symbolsTable,SWT.NULL);
			tableColumn2 = new TableColumn(symbolsTable,SWT.NULL);
			composite5 = new Composite(composite3,SWT.NULL);
			addSymbolButton = new Button(composite5,SWT.PUSH| SWT.CENTER);
			removeSymbolButton = new Button(composite5,SWT.PUSH| SWT.CENTER);
			tabItem1 = new TabItem(tabFolder1,SWT.NULL);
			composite1 = new Composite(tabFolder1,SWT.NULL);
			label4 = new Label(composite1,SWT.NULL);
			preprocessLineLabel = new Label(composite1,SWT.NULL);
			label3 = new Label(composite1,SWT.NULL);
			parseLineLabel = new Label(composite1,SWT.NULL);
			label5 = new Label(composite1,SWT.NULL);
			processedIncludesList = new List(composite1,SWT.H_SCROLL| SWT.V_SCROLL| SWT.BORDER);
			label2 = new Label(composite1,SWT.NULL);
			log = new List(composite1,SWT.H_SCROLL| SWT.V_SCROLL| SWT.BORDER);
			tabItem2 = new TabItem(tabFolder1,SWT.NULL);
			composite2 = new Composite(tabFolder1,SWT.NULL);
			parseTree = new Tree(composite2,SWT.H_SCROLL| SWT.V_SCROLL| SWT.BORDER);
	
			this.setSize(653, 562);
			final Font RCMainFramefont = new Font(Display.getDefault(),"Tahoma",8,0);
			this.setFont(RCMainFramefont);
	
			GridData composite6LData = new GridData();
			composite6LData.verticalAlignment = GridData.FILL;
			composite6LData.horizontalAlignment = GridData.FILL;
			composite6LData.widthHint = -1;
			composite6LData.heightHint = -1;
			composite6LData.horizontalIndent = 0;
			composite6LData.horizontalSpan = 1;
			composite6LData.verticalSpan = 1;
			composite6LData.grabExcessHorizontalSpace = true;
			composite6LData.grabExcessVerticalSpace = false;
			composite6.setLayoutData(composite6LData);
			composite6.setSize(new org.eclipse.swt.graphics.Point(643,147));
	
			GridData composite7LData = new GridData();
			composite7LData.verticalAlignment = GridData.CENTER;
			composite7LData.horizontalAlignment = GridData.BEGINNING;
			composite7LData.widthHint = -1;
			composite7LData.heightHint = -1;
			composite7LData.horizontalIndent = 0;
			composite7LData.horizontalSpan = 1;
			composite7LData.verticalSpan = 1;
			composite7LData.grabExcessHorizontalSpace = false;
			composite7LData.grabExcessVerticalSpace = false;
			composite7.setLayoutData(composite7LData);
			composite7.setSize(new org.eclipse.swt.graphics.Point(494,143));
	
			GridData singleFileRadioButtonLData = new GridData();
			singleFileRadioButtonLData.verticalAlignment = GridData.CENTER;
			singleFileRadioButtonLData.horizontalAlignment = GridData.BEGINNING;
			singleFileRadioButtonLData.widthHint = 477;
			singleFileRadioButtonLData.heightHint = 16;
			singleFileRadioButtonLData.horizontalIndent = 0;
			singleFileRadioButtonLData.horizontalSpan = 2;
			singleFileRadioButtonLData.verticalSpan = 1;
			singleFileRadioButtonLData.grabExcessHorizontalSpace = false;
			singleFileRadioButtonLData.grabExcessVerticalSpace = false;
			singleFileRadioButton.setLayoutData(singleFileRadioButtonLData);
			singleFileRadioButton.setSelection(false);
			singleFileRadioButton.setText("Single file, Select the resource script (*.rc file) you like to convert:");
			singleFileRadioButton.setSize(new org.eclipse.swt.graphics.Point(477,16));
			singleFileRadioButton.setEnabled(true);
			final Font singleFileRadioButtonfont = new Font(Display.getDefault(),"Tahoma",8,1);
			singleFileRadioButton.setFont(singleFileRadioButtonfont);
			singleFileRadioButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					singleFileRadioButtonWidgetSelected(evt);
				}
			});
	
			GridData rcFileNameEditLData = new GridData();
			rcFileNameEditLData.verticalAlignment = GridData.CENTER;
			rcFileNameEditLData.horizontalAlignment = GridData.BEGINNING;
			rcFileNameEditLData.widthHint = 340;
			rcFileNameEditLData.heightHint = 13;
			rcFileNameEditLData.horizontalIndent = 10;
			rcFileNameEditLData.horizontalSpan = 1;
			rcFileNameEditLData.verticalSpan = 1;
			rcFileNameEditLData.grabExcessHorizontalSpace = false;
			rcFileNameEditLData.grabExcessVerticalSpace = false;
			rcFileNameEdit.setLayoutData(rcFileNameEditLData);
			rcFileNameEdit.setSize(new org.eclipse.swt.graphics.Point(340,13));
			rcFileNameEdit.setEnabled(false);
			rcFileNameEdit.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent evt) {
					rcFileNameEditModifyText(evt);
				}
			});
	
			GridData browseRCFileButtonLData = new GridData();
			browseRCFileButtonLData.verticalAlignment = GridData.CENTER;
			browseRCFileButtonLData.horizontalAlignment = GridData.BEGINNING;
			browseRCFileButtonLData.widthHint = 78;
			browseRCFileButtonLData.heightHint = 20;
			browseRCFileButtonLData.horizontalIndent = 0;
			browseRCFileButtonLData.horizontalSpan = 1;
			browseRCFileButtonLData.verticalSpan = 1;
			browseRCFileButtonLData.grabExcessHorizontalSpace = false;
			browseRCFileButtonLData.grabExcessVerticalSpace = false;
			browseRCFileButton.setLayoutData(browseRCFileButtonLData);
			browseRCFileButton.setText("Browse...");
			browseRCFileButton.setSize(new org.eclipse.swt.graphics.Point(78,20));
			browseRCFileButton.setEnabled(false);
			browseRCFileButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browseRCFileButtonWidgetSelected(evt);
				}
			});
	
			GridData multipleFilesRadioButtonLData = new GridData();
			multipleFilesRadioButtonLData.verticalAlignment = GridData.CENTER;
			multipleFilesRadioButtonLData.horizontalAlignment = GridData.BEGINNING;
			multipleFilesRadioButtonLData.widthHint = 484;
			multipleFilesRadioButtonLData.heightHint = 16;
			multipleFilesRadioButtonLData.horizontalIndent = 0;
			multipleFilesRadioButtonLData.horizontalSpan = 2;
			multipleFilesRadioButtonLData.verticalSpan = 1;
			multipleFilesRadioButtonLData.grabExcessHorizontalSpace = false;
			multipleFilesRadioButtonLData.grabExcessVerticalSpace = false;
			multipleFilesRadioButton.setLayoutData(multipleFilesRadioButtonLData);
			multipleFilesRadioButton.setSelection(true);
			multipleFilesRadioButton.setText("Multiple files (via list), select the *.txt file that contains a list of files to convert:");
			multipleFilesRadioButton.setSize(new org.eclipse.swt.graphics.Point(484,16));
			multipleFilesRadioButton.setFont(singleFileRadioButtonfont);
			multipleFilesRadioButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					multipleFilesRadioButtonWidgetSelected(evt);
				}
			});
	
			GridData txtFileNameEditLData = new GridData();
			txtFileNameEditLData.verticalAlignment = GridData.CENTER;
			txtFileNameEditLData.horizontalAlignment = GridData.BEGINNING;
			txtFileNameEditLData.widthHint = 340;
			txtFileNameEditLData.heightHint = 13;
			txtFileNameEditLData.horizontalIndent = 10;
			txtFileNameEditLData.horizontalSpan = 1;
			txtFileNameEditLData.verticalSpan = 1;
			txtFileNameEditLData.grabExcessHorizontalSpace = false;
			txtFileNameEditLData.grabExcessVerticalSpace = false;
			txtFileNameEdit.setLayoutData(txtFileNameEditLData);
			txtFileNameEdit.setSize(new org.eclipse.swt.graphics.Point(340,13));
			txtFileNameEdit.setEnabled(true);
			txtFileNameEdit.addModifyListener( new ModifyListener() {
				public void modifyText(ModifyEvent evt) {
					txtFileNameEditModifyText(evt);
				}
			});
	
			GridData browseIniFileButtonLData = new GridData();
			browseIniFileButtonLData.verticalAlignment = GridData.CENTER;
			browseIniFileButtonLData.horizontalAlignment = GridData.BEGINNING;
			browseIniFileButtonLData.widthHint = 78;
			browseIniFileButtonLData.heightHint = 20;
			browseIniFileButtonLData.horizontalIndent = 0;
			browseIniFileButtonLData.horizontalSpan = 1;
			browseIniFileButtonLData.verticalSpan = 1;
			browseIniFileButtonLData.grabExcessHorizontalSpace = false;
			browseIniFileButtonLData.grabExcessVerticalSpace = false;
			browseIniFileButton.setLayoutData(browseIniFileButtonLData);
			browseIniFileButton.setText("Browse...");
			browseIniFileButton.setSize(new org.eclipse.swt.graphics.Point(78,20));
			browseIniFileButton.setEnabled(true);
			browseIniFileButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browseIniFileButtonWidgetSelected(evt);
				}
			});
	
			GridData button1LData = new GridData();
			button1LData.verticalAlignment = GridData.CENTER;
			button1LData.horizontalAlignment = GridData.BEGINNING;
			button1LData.widthHint = 484;
			button1LData.heightHint = 16;
			button1LData.horizontalIndent = 0;
			button1LData.horizontalSpan = 2;
			button1LData.verticalSpan = 1;
			button1LData.grabExcessHorizontalSpace = false;
			button1LData.grabExcessVerticalSpace = false;
			button1.setLayoutData(button1LData);
			button1.setText("Select output folder:");
			button1.setSize(new org.eclipse.swt.graphics.Point(484,16));
			button1.setFont(singleFileRadioButtonfont);
	
			GridData outputPathEditLData = new GridData();
			outputPathEditLData.verticalAlignment = GridData.CENTER;
			outputPathEditLData.horizontalAlignment = GridData.BEGINNING;
			outputPathEditLData.widthHint = 340;
			outputPathEditLData.heightHint = 13;
			outputPathEditLData.horizontalIndent = 10;
			outputPathEditLData.horizontalSpan = 1;
			outputPathEditLData.verticalSpan = 1;
			outputPathEditLData.grabExcessHorizontalSpace = false;
			outputPathEditLData.grabExcessVerticalSpace = false;
			outputPathEdit.setLayoutData(outputPathEditLData);
			outputPathEdit.setSize(new org.eclipse.swt.graphics.Point(340,13));
			outputPathEdit.setEnabled(true);
	
			GridData browseOutputFolderButtonLData = new GridData();
			browseOutputFolderButtonLData.verticalAlignment = GridData.CENTER;
			browseOutputFolderButtonLData.horizontalAlignment = GridData.BEGINNING;
			browseOutputFolderButtonLData.widthHint = 78;
			browseOutputFolderButtonLData.heightHint = 20;
			browseOutputFolderButtonLData.horizontalIndent = 0;
			browseOutputFolderButtonLData.horizontalSpan = 1;
			browseOutputFolderButtonLData.verticalSpan = 1;
			browseOutputFolderButtonLData.grabExcessHorizontalSpace = false;
			browseOutputFolderButtonLData.grabExcessVerticalSpace = false;
			browseOutputFolderButton.setLayoutData(browseOutputFolderButtonLData);
			browseOutputFolderButton.setText("Browse...");
			browseOutputFolderButton.setSize(new org.eclipse.swt.graphics.Point(78,20));
			browseOutputFolderButton.setEnabled(true);
			browseOutputFolderButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					browseOutputFolderButtonWidgetSelected(evt);
				}
			});
			GridLayout composite7Layout = new GridLayout(2, true);
			composite7.setLayout(composite7Layout);
			composite7Layout.marginWidth = 5;
			composite7Layout.marginHeight = 5;
			composite7Layout.numColumns = 2;
			composite7Layout.makeColumnsEqualWidth = false;
			composite7Layout.horizontalSpacing = 5;
			composite7Layout.verticalSpacing = 5;
			composite7.layout();
	
			GridData parseButtonLData = new GridData();
			parseButtonLData.verticalAlignment = GridData.CENTER;
			parseButtonLData.horizontalAlignment = GridData.FILL;
			parseButtonLData.widthHint = -1;
			parseButtonLData.heightHint = 37;
			parseButtonLData.horizontalIndent = 0;
			parseButtonLData.horizontalSpan = 1;
			parseButtonLData.verticalSpan = 1;
			parseButtonLData.grabExcessHorizontalSpace = true;
			parseButtonLData.grabExcessVerticalSpace = true;
			parseButton.setLayoutData(parseButtonLData);
			parseButton.setText("Start parsing...");
			parseButton.setSize(new org.eclipse.swt.graphics.Point(135,37));
			parseButton.setEnabled(false);
			parseButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					parseButtonWidgetSelected(evt);
				}
			});
			GridLayout composite6Layout = new GridLayout(2, true);
			composite6.setLayout(composite6Layout);
			composite6Layout.marginWidth = 2;
			composite6Layout.marginHeight = 2;
			composite6Layout.numColumns = 2;
			composite6Layout.makeColumnsEqualWidth = false;
			composite6Layout.horizontalSpacing = 10;
			composite6Layout.verticalSpacing = 2;
			composite6.layout();
	
			GridData group1LData = new GridData();
			group1LData.verticalAlignment = GridData.FILL;
			group1LData.horizontalAlignment = GridData.FILL;
			group1LData.widthHint = -1;
			group1LData.heightHint = -1;
			group1LData.horizontalIndent = 0;
			group1LData.horizontalSpan = 1;
			group1LData.verticalSpan = 1;
			group1LData.grabExcessHorizontalSpace = true;
			group1LData.grabExcessVerticalSpace = true;
			group1.setLayoutData(group1LData);
			group1.setText(" Parse process: ");
			group1.setSize(new org.eclipse.swt.graphics.Point(637,387));
	
			GridData tabFolder1LData = new GridData();
			tabFolder1LData.verticalAlignment = GridData.FILL;
			tabFolder1LData.horizontalAlignment = GridData.FILL;
			tabFolder1LData.widthHint = -1;
			tabFolder1LData.heightHint = -1;
			tabFolder1LData.horizontalIndent = 0;
			tabFolder1LData.horizontalSpan = 1;
			tabFolder1LData.verticalSpan = 1;
			tabFolder1LData.grabExcessHorizontalSpace = true;
			tabFolder1LData.grabExcessVerticalSpace = true;
			tabFolder1.setLayoutData(tabFolder1LData);
			tabFolder1.setSize(new org.eclipse.swt.graphics.Point(619,351));
	
			tabItem3.setControl(composite3);
			tabItem3.setText("Includes and defines");
	
	
			GridData label6LData = new GridData();
			label6LData.verticalAlignment = GridData.CENTER;
			label6LData.horizontalAlignment = GridData.BEGINNING;
			label6LData.widthHint = 145;
			label6LData.heightHint = 15;
			label6LData.horizontalIndent = 0;
			label6LData.horizontalSpan = 4;
			label6LData.verticalSpan = 1;
			label6LData.grabExcessHorizontalSpace = false;
			label6LData.grabExcessVerticalSpace = false;
			label6.setLayoutData(label6LData);
			label6.setText("Include pathes:");
			label6.setSize(new org.eclipse.swt.graphics.Point(145,15));
			label6.setFont(singleFileRadioButtonfont);
	
			GridData includePathListLData = new GridData();
			includePathListLData.verticalAlignment = GridData.FILL;
			includePathListLData.horizontalAlignment = GridData.FILL;
			includePathListLData.widthHint = -1;
			includePathListLData.heightHint = -1;
			includePathListLData.horizontalIndent = 0;
			includePathListLData.horizontalSpan = 3;
			includePathListLData.verticalSpan = 1;
			includePathListLData.grabExcessHorizontalSpace = true;
			includePathListLData.grabExcessVerticalSpace = true;
			includePathList.setLayoutData(includePathListLData);
			includePathList.setSize(new org.eclipse.swt.graphics.Point(465,132));
			includePathList.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					includePathListWidgetSelected(evt);
				}
			});
	
			GridData composite4LData = new GridData();
			composite4LData.verticalAlignment = GridData.BEGINNING;
			composite4LData.horizontalAlignment = GridData.BEGINNING;
			composite4LData.widthHint = 139;
			composite4LData.heightHint = 52;
			composite4LData.horizontalIndent = 0;
			composite4LData.horizontalSpan = 1;
			composite4LData.verticalSpan = 1;
			composite4LData.grabExcessHorizontalSpace = true;
			composite4LData.grabExcessVerticalSpace = true;
			composite4.setLayoutData(composite4LData);
			composite4.setSize(new org.eclipse.swt.graphics.Point(139,52));
	
			addIncludePathButton.setText("Add include path...");
			addIncludePathButton.setSize(new org.eclipse.swt.graphics.Point(135,23));
			addIncludePathButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					addIncludePathButtonWidgetSelected(evt);
				}
			});
	
			removeIncludePathButton.setText("Remove include path...");
			removeIncludePathButton.setSize(new org.eclipse.swt.graphics.Point(135,23));
			removeIncludePathButton.setEnabled(false);
			removeIncludePathButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					removeIncludePathButtonWidgetSelected(evt);
				}
			});
			FillLayout composite4Layout = new FillLayout(512);
			composite4.setLayout(composite4Layout);
			composite4Layout.type = SWT.VERTICAL;
			composite4Layout.marginWidth = 2;
			composite4Layout.marginHeight = 2;
			composite4Layout.spacing = 2;
			composite4.layout();
	
			GridData label7LData = new GridData();
			label7LData.verticalAlignment = GridData.CENTER;
			label7LData.horizontalAlignment = GridData.BEGINNING;
			label7LData.widthHint = 145;
			label7LData.heightHint = 15;
			label7LData.horizontalIndent = 0;
			label7LData.horizontalSpan = 4;
			label7LData.verticalSpan = 1;
			label7LData.grabExcessHorizontalSpace = false;
			label7LData.grabExcessVerticalSpace = false;
			label7.setLayoutData(label7LData);
			label7.setText("Defined symbols:");
			label7.setSize(new org.eclipse.swt.graphics.Point(145,15));
			label7.setFont(singleFileRadioButtonfont);
	
			GridData symbolsTableLData = new GridData();
			symbolsTableLData.verticalAlignment = GridData.FILL;
			symbolsTableLData.horizontalAlignment = GridData.FILL;
			symbolsTableLData.widthHint = -1;
			symbolsTableLData.heightHint = -1;
			symbolsTableLData.horizontalIndent = 0;
			symbolsTableLData.horizontalSpan = 3;
			symbolsTableLData.verticalSpan = 1;
			symbolsTableLData.grabExcessHorizontalSpace = true;
			symbolsTableLData.grabExcessVerticalSpace = true;
			symbolsTable.setLayoutData(symbolsTableLData);
			symbolsTable.setHeaderVisible(true);
			symbolsTable.setLinesVisible(false);
			symbolsTable.setSize(new org.eclipse.swt.graphics.Point(468,101));
			symbolsTable.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					symbolsTableWidgetSelected(evt);
				}
			});
	
			tableColumn1.setText("Symbol name");
			tableColumn1.setWidth(200);
	
			tableColumn2.setText("Substitution");
			tableColumn2.setWidth(200);
	
			GridData composite5LData = new GridData();
			composite5LData.verticalAlignment = GridData.BEGINNING;
			composite5LData.horizontalAlignment = GridData.BEGINNING;
			composite5LData.widthHint = -1;
			composite5LData.heightHint = -1;
			composite5LData.horizontalIndent = 0;
			composite5LData.horizontalSpan = 1;
			composite5LData.verticalSpan = 1;
			composite5LData.grabExcessHorizontalSpace = true;
			composite5LData.grabExcessVerticalSpace = true;
			composite5.setLayoutData(composite5LData);
	
			addSymbolButton.setText("Add symbol");
			addSymbolButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					addSymbolButtonWidgetSelected(evt);
				}
			});
	
			removeSymbolButton.setText("Remove symbol");
			removeSymbolButton.setEnabled(false);
			removeSymbolButton.addSelectionListener( new SelectionAdapter() {
				public void widgetSelected(SelectionEvent evt) {
					removeSymbolButtonWidgetSelected(evt);
				}
			});
			FillLayout composite5Layout = new FillLayout(512);
			composite5.setLayout(composite5Layout);
			composite5Layout.type = SWT.VERTICAL;
			composite5Layout.marginWidth = 2;
			composite5Layout.marginHeight = 2;
			composite5Layout.spacing = 2;
			composite5.layout();
			GridLayout composite3Layout = new GridLayout(4, true);
			composite3.setLayout(composite3Layout);
			composite3Layout.marginWidth = 5;
			composite3Layout.marginHeight = 5;
			composite3Layout.numColumns = 4;
			composite3Layout.makeColumnsEqualWidth = true;
			composite3Layout.horizontalSpacing = 5;
			composite3Layout.verticalSpacing = 5;
			composite3.layout();
	
			tabItem1.setControl(composite1);
			tabItem1.setText("Parse information");
	
			final Color composite1background = new Color(Display.getDefault(),236,233,216);
			composite1.setBackground(composite1background);
	
			GridData label4LData = new GridData();
			label4LData.verticalAlignment = GridData.CENTER;
			label4LData.horizontalAlignment = GridData.BEGINNING;
			label4LData.widthHint = 125;
			label4LData.heightHint = 15;
			label4LData.horizontalIndent = 0;
			label4LData.horizontalSpan = 1;
			label4LData.verticalSpan = 1;
			label4LData.grabExcessHorizontalSpace = false;
			label4LData.grabExcessVerticalSpace = false;
			label4.setLayoutData(label4LData);
			label4.setText("Lines pre-processed:");
			label4.setSize(new org.eclipse.swt.graphics.Point(125,15));
	
			GridData preprocessLineLabelLData = new GridData();
			preprocessLineLabelLData.verticalAlignment = GridData.CENTER;
			preprocessLineLabelLData.horizontalAlignment = GridData.BEGINNING;
			preprocessLineLabelLData.widthHint = 86;
			preprocessLineLabelLData.heightHint = 15;
			preprocessLineLabelLData.horizontalIndent = 0;
			preprocessLineLabelLData.horizontalSpan = 1;
			preprocessLineLabelLData.verticalSpan = 1;
			preprocessLineLabelLData.grabExcessHorizontalSpace = false;
			preprocessLineLabelLData.grabExcessVerticalSpace = false;
			preprocessLineLabel.setLayoutData(preprocessLineLabelLData);
			preprocessLineLabel.setAlignment(SWT.RIGHT);
			preprocessLineLabel.setText("0");
			preprocessLineLabel.setSize(new org.eclipse.swt.graphics.Point(86,15));
	
			GridData label3LData = new GridData();
			label3LData.verticalAlignment = GridData.CENTER;
			label3LData.horizontalAlignment = GridData.BEGINNING;
			label3LData.widthHint = 90;
			label3LData.heightHint = 15;
			label3LData.horizontalIndent = 0;
			label3LData.horizontalSpan = 1;
			label3LData.verticalSpan = 1;
			label3LData.grabExcessHorizontalSpace = false;
			label3LData.grabExcessVerticalSpace = false;
			label3.setLayoutData(label3LData);
			label3.setText("Lines processed:");
			label3.setSize(new org.eclipse.swt.graphics.Point(90,15));
	
			GridData parseLineLabelLData = new GridData();
			parseLineLabelLData.verticalAlignment = GridData.CENTER;
			parseLineLabelLData.horizontalAlignment = GridData.BEGINNING;
			parseLineLabelLData.widthHint = 79;
			parseLineLabelLData.heightHint = 15;
			parseLineLabelLData.horizontalIndent = 0;
			parseLineLabelLData.horizontalSpan = 1;
			parseLineLabelLData.verticalSpan = 1;
			parseLineLabelLData.grabExcessHorizontalSpace = false;
			parseLineLabelLData.grabExcessVerticalSpace = false;
			parseLineLabel.setLayoutData(parseLineLabelLData);
			parseLineLabel.setAlignment(SWT.RIGHT);
			parseLineLabel.setText("0");
			parseLineLabel.setSize(new org.eclipse.swt.graphics.Point(79,15));
	
			GridData label5LData = new GridData();
			label5LData.verticalAlignment = GridData.CENTER;
			label5LData.horizontalAlignment = GridData.BEGINNING;
			label5LData.widthHint = 145;
			label5LData.heightHint = 15;
			label5LData.horizontalIndent = 0;
			label5LData.horizontalSpan = 4;
			label5LData.verticalSpan = 1;
			label5LData.grabExcessHorizontalSpace = false;
			label5LData.grabExcessVerticalSpace = false;
			label5.setLayoutData(label5LData);
			label5.setText("Include files processed:");
			label5.setSize(new org.eclipse.swt.graphics.Point(145,15));
			label5.setFont(singleFileRadioButtonfont);
	
			GridData processedIncludesListLData = new GridData();
			processedIncludesListLData.verticalAlignment = GridData.FILL;
			processedIncludesListLData.horizontalAlignment = GridData.FILL;
			processedIncludesListLData.widthHint = -1;
			processedIncludesListLData.heightHint = -1;
			processedIncludesListLData.horizontalIndent = 0;
			processedIncludesListLData.horizontalSpan = 4;
			processedIncludesListLData.verticalSpan = 1;
			processedIncludesListLData.grabExcessHorizontalSpace = true;
			processedIncludesListLData.grabExcessVerticalSpace = true;
			processedIncludesList.setLayoutData(processedIncludesListLData);
			processedIncludesList.setSize(new org.eclipse.swt.graphics.Point(585,119));
	
			GridData label2LData = new GridData();
			label2LData.verticalAlignment = GridData.CENTER;
			label2LData.horizontalAlignment = GridData.BEGINNING;
			label2LData.widthHint = 145;
			label2LData.heightHint = 15;
			label2LData.horizontalIndent = 0;
			label2LData.horizontalSpan = 4;
			label2LData.verticalSpan = 1;
			label2LData.grabExcessHorizontalSpace = false;
			label2LData.grabExcessVerticalSpace = false;
			label2.setLayoutData(label2LData);
			label2.setText("Parse log:");
			label2.setSize(new org.eclipse.swt.graphics.Point(145,15));
			label2.setFont(singleFileRadioButtonfont);
	
			GridData logLData = new GridData();
			logLData.verticalAlignment = GridData.FILL;
			logLData.horizontalAlignment = GridData.FILL;
			logLData.widthHint = -1;
			logLData.heightHint = -1;
			logLData.horizontalIndent = 0;
			logLData.horizontalSpan = 4;
			logLData.verticalSpan = 1;
			logLData.grabExcessHorizontalSpace = true;
			logLData.grabExcessVerticalSpace = true;
			log.setLayoutData(logLData);
			log.setSize(new org.eclipse.swt.graphics.Point(585,119));
			GridLayout composite1Layout = new GridLayout(4, true);
			composite1.setLayout(composite1Layout);
			composite1Layout.marginWidth = 5;
			composite1Layout.marginHeight = 5;
			composite1Layout.numColumns = 4;
			composite1Layout.makeColumnsEqualWidth = true;
			composite1Layout.horizontalSpacing = 5;
			composite1Layout.verticalSpacing = 5;
			composite1.layout();
	
			tabItem2.setControl(composite2);
			tabItem2.setText("Sytax tree");
	
	
			parseTree.setSize(new org.eclipse.swt.graphics.Point(590,322));
			FillLayout composite2Layout = new FillLayout(256);
			composite2.setLayout(composite2Layout);
			composite2Layout.type = SWT.HORIZONTAL;
			composite2Layout.marginWidth = 5;
			composite2Layout.marginHeight = 5;
			composite2Layout.spacing = 0;
			composite2.layout();
			GridLayout group1Layout = new GridLayout(1, true);
			group1.setLayout(group1Layout);
			group1Layout.marginWidth = 5;
			group1Layout.marginHeight = 5;
			group1Layout.numColumns = 1;
			group1Layout.makeColumnsEqualWidth = false;
			group1Layout.horizontalSpacing = 10;
			group1Layout.verticalSpacing = 10;
			group1.layout();
			GridLayout thisLayout = new GridLayout(1, true);
			this.setLayout(thisLayout);
			thisLayout.marginWidth = 5;
			thisLayout.marginHeight = 5;
			thisLayout.numColumns = 1;
			thisLayout.makeColumnsEqualWidth = true;
			thisLayout.horizontalSpacing = 5;
			thisLayout.verticalSpacing = 5;
			this.layout();
			addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					RCMainFramefont.dispose();
					singleFileRadioButtonfont.dispose();
					composite1background.dispose();
				}
			});
	
			postInitGUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  //------------------------------------------------------------------------------------------------

  public void logMessage(String message)
  {
    log.add(message);
    log.update();
  }

  //------------------------------------------------------------------------------------------------
  
  /**
	 * Add your post-init code in here
	 */
	public void postInitGUI()
  {
    // TODO: Remove debug path before release.
    outputPathEdit.setText("V:\\Packaging\\RCConverter\\XML");

    // If the user gave the system's include path environment variable to us (as VM argument) then
    // use this for the initial entries. Here's how you would specify the VM argument:
    //   -Dinclude-paths="${env_var:include}"
    // (don't forget the quotes!). "include" is the Windows environment variable 
    // for the current include path.
    String includePaths = System.getProperty("include-paths");
    String[] pathes = includePaths.split(";");
    for (int i = 0; i < pathes.length; i++)
      includes.add(pathes[i]);

    // Check also the java command line for settings.
    if (commandLine != null)
      parseCommandLine();

    // Now fill the controls with their initial values from our internal lists.
    for (int i = 0; i < includes.size(); i++)
      includePathList.add((String) includes.get(i));
    for (int i = 0; i < defines.size(); i++)
    {
      TableItem item = new TableItem(symbolsTable, SWT.NULL);
      String define = (String) defines.get(i);
      String[] parts = define.split(" |\t");
      item.setText(0, parts[0].trim()); 
      if (parts.length > 1)
        item.setText(1, parts[1].trim());
    }
    cursor = setupTableCursor(symbolsTable);
	}
  
  //------------------------------------------------------------------------------------------------

	/**
	 * Add your pre-init code in here
	 */
	public void preInitGUI()
  {
	}
  
  //------------------------------------------------------------------------------------------------

	protected void browseRCFileButtonWidgetSelected(SelectionEvent evt)
  {
    FileDialog rcFilePicker = new FileDialog(shell, SWT.NULL);
    rcFilePicker.setFilterExtensions(new String[] { "*.rc", "*.*" });
    rcFilePicker.setFileName(rcFileNameEdit.getText());
    String newFile = rcFilePicker.open();
    if (newFile != null)
      rcFileNameEdit.setText(newFile);
    
	}

  //------------------------------------------------------------------------------------------------

	protected void singleFileRadioButtonWidgetSelected(SelectionEvent evt)
  {
    txtFileNameEdit.setEnabled(false);
    browseIniFileButton.setEnabled(false);

    rcFileNameEdit.setEnabled(true);
    browseRCFileButton.setEnabled(true);

    File rcFile = new File(rcFileNameEdit.getText());
    parseButton.setEnabled((rcFile.exists() && rcFile.isFile()));
  }

  //------------------------------------------------------------------------------------------------

	protected void rcFileNameEditModifyText(ModifyEvent evt)
  {
    File rcFile = new File(rcFileNameEdit.getText());
    parseButton.setEnabled((rcFile.exists() && rcFile.isFile()));
    parseButton.setEnabled(true);
	}

  //------------------------------------------------------------------------------------------------

	protected void multipleFilesRadioButtonWidgetSelected(SelectionEvent evt)
  {
    txtFileNameEdit.setEnabled(true);
    browseIniFileButton.setEnabled(true);

    rcFileNameEdit.setEnabled(false);
    browseRCFileButton.setEnabled(false);

    File configFile = new File(txtFileNameEdit.getText());
    parseButton.setEnabled((configFile.exists() && configFile.isFile()));
	}

  //------------------------------------------------------------------------------------------------

	protected void txtFileNameEditModifyText(ModifyEvent evt)
  {
    File txtFile = new File(txtFileNameEdit.getText());
    parseButton.setEnabled((txtFile.exists() && txtFile.isFile()));
    parseButton.setEnabled(true);
	}

  //------------------------------------------------------------------------------------------------

	protected void browseIniFileButtonWidgetSelected(SelectionEvent evt)
  {
    FileDialog iniFilePicker = new FileDialog(shell, SWT.NULL);
    iniFilePicker.setFilterExtensions(new String[] { "*.txt", "*.*" });
    iniFilePicker.setFileName(txtFileNameEdit.getText());
    String newFile = iniFilePicker.open();
    if (newFile != null)
      txtFileNameEdit.setText(newFile);
	}

  //------------------------------------------------------------------------------------------------

  protected void browseOutputFolderButtonWidgetSelected(SelectionEvent evt)
  {
    DirectoryDialog outputPathPicker = new DirectoryDialog(shell, SWT.NULL);
    outputPathPicker.setMessage("Select the path you want the resulting xml files to go to.");
    
    outputPathPicker.setFilterPath(outputPathEdit.getText());
    String newFolder = outputPathPicker.open();
    if (newFolder != null)
      outputPathEdit.setText(newFolder);
  }

  //------------------------------------------------------------------------------------------------
  
}
