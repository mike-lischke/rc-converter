/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;


/**
 * This is the entry point for the application. It creates the a simple UI for selecting files
 * and showing parser results.
 *
 * Created on 05.02.2004
 * @author Mike Lischke
 */
public class RCConverter
{
  private Display display;
  private Shell shell;
  private RCMainFrame mainFrame;

  //------------------------------------------------------------------------------------------------
  
  public static void main(String[] args)
  {
    RCConverter converter = new RCConverter();
    converter.setupUI(args);
    converter.shell.setText("Test frame for *.rc file converter");
    converter.shell.open();
    converter.processMessages();
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Dispatch loop for the application.
   */
  private void processMessages()
  {
    while (!shell.isDisposed())
    {
      if (!display.readAndDispatch())
        display.sleep();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   *  Create all necessary controls for the UI.
   */
  private void setupUI(String[] args)
  {
    display = new Display();
    shell = new Shell();
    shell.setSize(700, 600);
    mainFrame = new RCMainFrame(shell, SWT.NULL, args);
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * @return Returns the mainFrame.
   */
  public RCMainFrame getMainFrame()
  {
    return mainFrame;
  }

  //------------------------------------------------------------------------------------------------
  
}
