/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.main;

/**
 * The ParseEventListener interface serves as interface for messages between the various parse parts
 * and the application.
 *
 * @author Mike Lischke
 */
public interface IParseEventListener
{
  /** Used to indicate a fatal error condition (value is 0). */
  public final int PANIC = 0;
  /** Used to indicate an error condition (value is 1). */
  public final int ERROR = 1;
  /** Used to tell the receiver about a potentially dangerous condition (value is 2). */
  public final int WARNING = 2;
  /** Used to tell the receiver about something interesting (value is 3). */
  public final int INFORMATION = 3;
  /** Used to tell the receiver that a new file has been included (value is 4). */
  public final int NEW_LINE = 4;
  /** Used to indicate a fatal error condition (value is 5). */
  public final int INCLUDE_FILE = 5;
  
  //------------------------------------------------------------------------------------------------
  
  /**
   * The handleEvent method must be implemented by a descendant and is called when an
   * event occurs of which the owner should know about.
   *
   * @param event One of the constants defined above to identify the type of message.
   * @param message A plain text describing the event. It can be empty, e.g. for new line events.
   */
  public abstract void handleEvent(int event, String message);
  
  //------------------------------------------------------------------------------------------------

}
