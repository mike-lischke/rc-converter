/*
 * This file is released under the MIT license.
 * Copyright (c) 2004, 2020, Mike Lischke
 *
 * See LICENSE file for more info.
 */

package net.softgems.resourceparser.xml;

import java.util.HashMap;

import net.softgems.resourceparser.RCMainFrame;
import net.softgems.resourceparser.main.RCParserTokenTypes;

import org.jdom.Element;

import antlr.collections.AST;

/**
 * This class converts a resource statement AST to XML.
 */
public class ResourceStatement extends AST2XMLConverter
{

  /** This map allows for a quick lookup of resource types. */
  private HashMap resourceMap = new HashMap();

  //------------------------------------------------------------------------------------------------
  
  /**
   * The constructor of the class.
   * 
   * @param astNode The root node of the subtree we will working on.
   */
  public ResourceStatement(RCMainFrame owner, AST astNode)
  {
    super(owner, astNode);

    resourceMap.put("accelerators", new Integer(RCParserTokenTypes.LITERAL_accelerators));
    resourceMap.put("bitmap", new Integer(RCParserTokenTypes.LITERAL_bitmap));
    resourceMap.put("cursor", new Integer(RCParserTokenTypes.LITERAL_cursor));
    resourceMap.put("dialog", new Integer(RCParserTokenTypes.LITERAL_dialog));
    resourceMap.put("dialogex", new Integer(RCParserTokenTypes.LITERAL_dialogex));
    resourceMap.put("font", new Integer(RCParserTokenTypes.LITERAL_font));
    resourceMap.put("icon", new Integer(RCParserTokenTypes.LITERAL_icon));
    resourceMap.put("menu", new Integer(RCParserTokenTypes.LITERAL_menu));
    resourceMap.put("menuex", new Integer(RCParserTokenTypes.LITERAL_menuex));
    resourceMap.put("messagetable", new Integer(RCParserTokenTypes.LITERAL_messagetable));
    resourceMap.put("rcdata", new Integer(RCParserTokenTypes.LITERAL_rcdata));
    resourceMap.put("versioninfo", new Integer(RCParserTokenTypes.LITERAL_versioninfo));
    resourceMap.put("textinclude", new Integer(RCParserTokenTypes.LITERAL_textinclude));
    resourceMap.put("designinfo", new Integer(RCParserTokenTypes.LITERAL_designinfo));
    resourceMap.put("toolbar", new Integer(RCParserTokenTypes.LITERAL_toolbar));
    resourceMap.put("dlginit", new Integer(RCParserTokenTypes.LITERAL_dlginit));
    resourceMap.put("user defined", new Integer(RCParserTokenTypes.USER_DEFINED));
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "accelerators" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertAcceleratorsEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "accelerators");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
    }    

    // Optional common resource info part.
    if (currentNode != null)
    {
      while (currentNode.getType() == RCParserTokenTypes.COMMON_RESOURCE_INFO)
      {
        convertCommonResourceInfo(currentNode, resourceElement);
        currentNode = currentNode.getNextSibling();
      }
    }

    while (currentNode != null)
    {
      AST acceleratorNode = currentNode.getFirstChild();
      
      Element acceleratorElement = new Element("entry");
      resourceElement.addContent(acceleratorElement);
      processEntryWithEvaluationAsAttribute(acceleratorNode, "key", acceleratorElement);
      acceleratorNode = acceleratorNode.getNextSibling();
      processEntryWithEvaluationAsAttribute(acceleratorNode, "id", acceleratorElement);
      acceleratorNode = acceleratorNode.getNextSibling();
      
      if ((acceleratorNode != null) && (acceleratorNode.getType() == RCParserTokenTypes.ACCELERATOR_TYPE))
      {
        acceleratorElement.setAttribute("type", acceleratorNode.getFirstChild().getText());
        acceleratorNode = acceleratorNode.getNextSibling();
      }

      while (acceleratorNode != null)
      {
        Element extraElement = new Element("option");
        acceleratorElement.addContent(extraElement);
        extraElement.setAttribute("value", acceleratorNode.getFirstChild().getText());
        
        acceleratorNode = acceleratorNode.getNextSibling();
      }
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "bitamp" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertBitmapEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "bitmap");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
    }
    
    if (currentNode.getType() == RCParserTokenTypes.FILE_NAME)
    {
      String filename = currentNode.getFirstChild().getText();
      if (filename.startsWith("\""))
        filename = filename.substring(1, filename.length() - 1);
      resourceElement.setAttribute("file-name", filename);
    }
    else
    {
      // Must be raw data, e.g. single literals (string, character, integer etc.) or
      // a collection of hex numbers enclosed by single quotes.
      convertRawDataToXML(currentNode, resourceElement);
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a set of common dialog resource info (e.g. characteristics, ex-styles etc.) to XML.
   * 
   * @param node The first node in the list to convert.
   * @param element The target element to add the converted stuff to.
   * @return The first node after the common info.
   */
  private AST convertCommonDialogInfo(AST node, Element element)
  {
    AST currentNode = node;
    boolean doBreak = false;
    while (currentNode != null && !doBreak)
    {
      switch (currentNode.getType())
      {
        case RCParserTokenTypes.LITERAL_characteristics:
        {
          processEntryWithEvaluation(currentNode.getFirstChild(), "characteristics", element);
          break;
        }
        case RCParserTokenTypes.LITERAL_language:
        {
          convertLanguageEntry(currentNode, element);
          break;
        }
        case RCParserTokenTypes.LITERAL_version:
        {
          processEntryWithEvaluation(currentNode.getFirstChild(), "version", element);
          break;
        }
        case RCParserTokenTypes.LITERAL_caption:
        {
          Element subElement = new Element("caption");
          element.addContent(subElement);
          subElement.setAttribute("value", convertToString(currentNode.getFirstChild()));
          break;
        }
        case RCParserTokenTypes.LITERAL_class:
        {
          processEntryWithEvaluation(currentNode.getFirstChild(), "class", element);
          break;
        }
        case RCParserTokenTypes.LITERAL_exstyle:
        {
          processEntryWithEvaluation(currentNode.getFirstChild(), "exstyle", element);
          break;
        }
        case RCParserTokenTypes.LITERAL_menu:
        {
          processEntryWithEvaluation(currentNode.getFirstChild(), "menu", element);
          break;
        }
        case RCParserTokenTypes.LITERAL_font:
        {
          Element fontElement = new Element("font");
          element.addContent(fontElement);

          AST fontDataNode = currentNode.getFirstChild();
          processEntryWithEvaluation(fontDataNode, "point-size", fontElement);
          
          fontDataNode = fontDataNode.getNextSibling();
          processEntryWithEvaluation(fontDataNode, "type-face", fontElement);
          
          fontDataNode = fontDataNode.getNextSibling();
          if (fontDataNode != null)
          {
            processEntryWithEvaluation(fontDataNode, "weight", fontElement);

            fontDataNode = fontDataNode.getNextSibling();
            processEntryWithEvaluation(fontDataNode, "italic", fontElement);

            fontDataNode = fontDataNode.getNextSibling();
            processEntryWithEvaluation(fontDataNode, "charset", fontElement);
          }
          
          break;
        }
        case RCParserTokenTypes.LITERAL_style:
        {
          processEntryWithEvaluation(currentNode.getFirstChild(), "style", element);
          break;
        }
        default:
        {
          doBreak = true;
        }
      }
      currentNode = currentNode.getNextSibling();
    }
    return currentNode;
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a common resource info entry to XML.
   * 
   * @param node The entry to convert.
   * @param element The target XML element where to add the converted stuff.
   */
  private void convertCommonResourceInfo(AST node, Element element)
  {
    AST currentNode = node.getFirstChild();
    if (currentNode != null)
    {
      Element infoElement = new Element("common-resource-info");
      element.addContent(infoElement);
      infoElement.setAttribute("name", currentNode.getText());
      if (currentNode.getType() == RCParserTokenTypes.LITERAL_language)
        convertLanguageEntry(currentNode, infoElement);
      else
        convertExpressionList(currentNode.getFirstChild(), infoElement);
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts the given node, which must be a control node in a dialog, to XML
   * 
   * @param node The node to convert.
   * @param element The target element where to add the result to.
   */
  private void convertConcreteControl(AST node, Element element)
  {
    Element controlElement = new Element("control");
    element.addContent(controlElement);
    controlElement.setAttribute("type", node.getText());
    switch (node.getType())
    {
      case RCParserTokenTypes.LITERAL_ltext:
      case RCParserTokenTypes.LITERAL_rtext:
      case RCParserTokenTypes.LITERAL_ctext:
      case 135: // RCParserTokenTypes.LITERAL_auto3state
      case RCParserTokenTypes.LITERAL_autocheckbox:
      case RCParserTokenTypes.LITERAL_autoradiobutton:
      case RCParserTokenTypes.LITERAL_checkbox:
      case RCParserTokenTypes.LITERAL_pushbox:
      case RCParserTokenTypes.LITERAL_pushbutton:
      case RCParserTokenTypes.LITERAL_defpushbutton:
      case RCParserTokenTypes.LITERAL_radiobutton:
      case 143: // RCParserTokenTypes.LITERAL_state3
      case RCParserTokenTypes.LITERAL_groupbox:
      case RCParserTokenTypes.LITERAL_userbutton:
      case RCParserTokenTypes.LITERAL_icon:
      case RCParserTokenTypes.LITERAL_scrollbar:
      {
        AST child = node.getFirstChild();
        processEntryWithEvaluation(child, "control-text", controlElement);
        child = child.getNextSibling();
        processEntryWithEvaluation(child, "id", controlElement);
        
        break;
      }
      case RCParserTokenTypes.LITERAL_edittext:
      case RCParserTokenTypes.LITERAL_bedit:
      case RCParserTokenTypes.LITERAL_hedit:
      case RCParserTokenTypes.LITERAL_iedit:
      case RCParserTokenTypes.LITERAL_combobox:
      case RCParserTokenTypes.LITERAL_listbox:
      {
        AST child = node.getFirstChild();
        processEntryWithEvaluation(child, "id", controlElement);
        
        break;
      }
    }
    
    // Common control trailing.
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "left", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "top", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "width", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "height", controlElement);
    node = node.getNextSibling();
    if (node != null)
    {
      processEntryWithEvaluation(node, "style", controlElement);
      
      // Everything after this point can only appear in DialogEx resource entries.
      node = node.getNextSibling();
      if (node != null)
      {
        processEntryWithEvaluation(node, "exstyle", controlElement);
        node = node.getNextSibling();
        if (node != null)
        {
          processEntryWithEvaluation(node, "help-id", controlElement);
  
          if (node.getNextSibling() != null)
          {
            Element dataElement = new Element("control-data");
            controlElement.addContent(dataElement);
            do
            {
              node = node.getNextSibling();
              processEntryWithEvaluation(node, "data-element", dataElement);
            }
            while (node != null);
          }
        }
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "cursor" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertCursorEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "cursor");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
    }    

    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.FILE_NAME)
      {
        String filename = currentNode.getFirstChild().getText();
        if (filename.startsWith("\""))
          filename = filename.substring(1, filename.length() - 1);
        resourceElement.setAttribute("file-name", filename);
      }
      else
      {
        // Must be raw data, e.g. single literals (string, character, integer etc.) or
        // a collection of hex numbers enclosed by single quotes.
        convertRawDataToXML(currentNode, resourceElement);
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "designinfo" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertDesignInfoEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "design-info");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
      
      // The design info consists of a list of blocks for a certain control/dialog etc.
      // where each block itself consists of a list of design info values.
      while (currentNode != null)
      {
        // currentNode must always be of type DESIGN_INFO_CONTROL_BLOCK. The parser takes care for this.
        AST blockNode = currentNode.getFirstChild();
        
        // Add the block identification.
        Element blockElement = processEntryWithEvaluation(blockNode, "control-block", resourceElement);
        
        // Next is the resource entry class.
        blockNode = blockNode.getNextSibling();
        blockElement.setAttribute("class", blockNode.getText());
        
        blockNode = blockNode.getNextSibling();
        AST entryNode = blockNode.getFirstChild();
        while (entryNode != null)
        {
          Element entryElement = new Element("entry");
          entryElement.setAttribute("name", entryNode.getText());
          blockElement.addContent(entryElement);
          entryNode = entryNode.getNextSibling();
          Object value = evaluate(entryNode.getText(), entryNode.getLine(), entryNode.getColumn());
          if (value != null)
            entryElement.setAttribute("value", value.toString());
          
          entryNode = entryNode.getNextSibling();
        }
        
        currentNode = currentNode.getNextSibling();
      }
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "dialog" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertDialogEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "dialog");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
    }    
    
    // Four fix expressions come now, giving left, top, width and height coordinates.
    processEntryWithEvaluation(currentNode, "left", resourceElement);
    currentNode = currentNode.getNextSibling();
    processEntryWithEvaluation(currentNode, "top", resourceElement);
    currentNode = currentNode.getNextSibling();
    processEntryWithEvaluation(currentNode, "width", resourceElement);
    currentNode = currentNode.getNextSibling();
    processEntryWithEvaluation(currentNode, "height", resourceElement);
    currentNode = currentNode.getNextSibling();
    
    // There might also be an optional set of common resource info.
    currentNode = convertCommonDialogInfo(currentNode, resourceElement);
    
    // Now there can be two ways how definition continues, generic controls or concrete controls.
    // The chain is finished when we encounter the common trailing (which starts with an expression).
    while (currentNode != null && currentNode.getType() != RCParserTokenTypes.EXPR)
    {
      if (currentNode.getType() == RCParserTokenTypes.CONCRETE_CONTROL)
        convertConcreteControl(currentNode.getFirstChild(), resourceElement);
      else
        convertGenericControl(currentNode.getFirstChild(), resourceElement);
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "dialogex" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertDialogExEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "dialogex");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }

      // Four fix expressions come now, giving left, top, width and height coordinates.
      processEntryWithEvaluation(currentNode.getFirstChild(), "left", resourceElement);
      currentNode = currentNode.getNextSibling();
      processEntryWithEvaluation(currentNode.getFirstChild(), "top", resourceElement);
      currentNode = currentNode.getNextSibling();
      processEntryWithEvaluation(currentNode.getFirstChild(), "width", resourceElement);
      currentNode = currentNode.getNextSibling();
      processEntryWithEvaluation(currentNode.getFirstChild(), "height", resourceElement);
      
      // There might be an optional help id value.
      currentNode = currentNode.getNextSibling();
      if (currentNode != null && currentNode.getType() == RCParserTokenTypes.EXPR)
      {
        processEntryWithEvaluation(currentNode.getFirstChild(), "help-id", resourceElement);
        currentNode = currentNode.getNextSibling();
      }
      
      // There might also be an optional set of common resource info.
      currentNode = convertCommonDialogInfo(currentNode, resourceElement);
      
      // Now there can be two ways how definition continues, generic controls or concrete controls.
      // The chain is finished when we encounter the common trailing (which starts with an expression).
      while (currentNode != null && currentNode.getType() != RCParserTokenTypes.EXPR)
      {
        if (currentNode.getType() == RCParserTokenTypes.CONCRETE_CONTROL)
          convertConcreteControl(currentNode.getFirstChild(), resourceElement);
        else
          convertGenericControl(currentNode.getFirstChild(), resourceElement);
        currentNode = currentNode.getNextSibling();
      }
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "dlginit" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertDlgInitEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "dlginit");
    
    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
      
      // DLGINIT values are organized in chunks each beginning with an identifier.
      // One or more values follow this identifier.
      while (currentNode != null)
      {
        String symbol = currentNode.getText();
        Element dataElement = new Element("data");
        resourceElement.addContent(dataElement);
        dataElement.setAttribute("name", symbol);
        currentNode = currentNode.getNextSibling();
        while (currentNode != null)
        {
          // An identifier indicates the start of a new set of values.
          if (currentNode.getType() == RCParserTokenTypes.IDENTIFIER)
            break;
          
          processEntryWithEvaluation(currentNode, "entry", dataElement);
          currentNode = currentNode.getNextSibling(); 
        }
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a list of expressions (addressed by node) to XML subelements of <b>element</b>. The
   * method loops through all siblings of the given node until there aren't anymore.
   * 
   * @param node The node to convert.
   * @param element The target node to add the converted elements to.
   */
  private void convertExpressionList(AST node, Element element)
  {
    AST currentNode = node;
    while (currentNode != null)
    {
      Element valueElement = new Element("entry");
      element.addContent(valueElement);
      Object value = null;
      if (currentNode.getType() == RCParserTokenTypes.EXPR)
        value = evaluate(currentNode);
      else
      {
        String expression = currentNode.getText();
        value = evaluate(expression, currentNode.getLine(), currentNode.getColumn());
        valueElement.setAttribute("expression", expression);
      }
      if (value != null)
        valueElement.setAttribute("value", value.toString());
      else
        valueElement.setAttribute("value", convertExpressionToText(currentNode.getFirstChild()));
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * In the case an expression cannot be evaluated (e.g. because a macro cannot be resolved)
   * it is useful to print out at least the expression in text form. This method converts an AST
   * expression tree into plain text.
   * 
   * @param node
   * @return
   */
  private String convertExpressionToText(AST node)
  {
    if (node == null)
      return "";
    else
    {
      StringBuffer buffer = new StringBuffer();
      
      AST left = node.getFirstChild();
      AST right = null;
      if (left != null)
        right = left.getNextSibling();
      if (left != null)
      {
        String leftString = convertExpressionToText(left);
        buffer.append(leftString);
        buffer.append(" ");
      }
      buffer.append(node.getText());
      if (right != null)
      {
        buffer.append(" ");
        buffer.append(convertExpressionToText(right));
      }
      
      return buffer.toString();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "font" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertFontEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "font");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }

      // Must be a file name node.
      String filename = currentNode.getText();
      if (filename.startsWith("\""))
        filename = filename.substring(1, filename.length());
      resourceElement.setAttribute("file-name", filename);
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts the given node, which must be a generic control node in a dialog, to XML
   * 
   * @param node The node to convert.
   * @param element The target element where to add the result to.
   */
  private void convertGenericControl(AST node, Element element)
  {
    Element controlElement = new Element("control");
    element.addContent(controlElement);
    controlElement.setAttribute("type", "generic");
    
    processEntryWithEvaluation(node, "control-text", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "id", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "class-name", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "style", controlElement);

    // Dialog(Ex) generic control trailing.
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "left", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "top", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "width", controlElement);
    node = node.getNextSibling();
    processEntryWithEvaluation(node, "height", controlElement);
    
    // The following entries only appear for DialogEx resource entries.
    node = node.getNextSibling();
    if (node != null)
    {
      processEntryWithEvaluation(node, "exstyle", controlElement);
      node = node.getNextSibling();
      if (node != null)
      {
        processEntryWithEvaluation(node, "help-id", controlElement);

        if (node.getNextSibling() != null)
        {
          Element dataElement = new Element("control-data");
          controlElement.addContent(dataElement);
          do
          {
            node = node.getNextSibling();
            processEntryWithEvaluation(node, "data-element", dataElement);
          }
          while (node != null);
        }
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "icon" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertIconEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "icon");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }

      // Must be a file name node.
      String filename = currentNode.getText();
      if (filename.startsWith("\""))
        filename = filename.substring(1, filename.length());
      resourceElement.setAttribute("file-name", filename);
    }    
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Converts a language entry to XML.
   * 
   * @param node The node to convert. Must be a language node.
   * @param target The XML node where to add the new content to.
   */
  private void convertLanguageEntry(AST node, Element target)
  {
    String languageID = "LANG_DEFAULT";
    AST languageNode = node.getFirstChild();
    languageID = languageNode.getText();
    String subLanguageID = "SUBLANG_DEFAULT";
    AST subLanguageNode = null;
    subLanguageNode = languageNode.getNextSibling();
    subLanguageID = subLanguageNode.getText();
    
    Element baseElement = new Element("language");
    target.addContent(baseElement);
    
    Element languageElement = new Element("language-value");
    baseElement.addContent(languageElement);
    languageElement.setAttribute("name", languageID);
    Object value = evaluate(languageID, languageNode.getLine(), languageNode.getColumn());
    if (value == null)
      reportError("Could not evaluate \"" + languageID + "\".");
    else
      languageElement.setAttribute("value", value.toString());

    Element subLanguageElement = new Element("sub-language-value");
    baseElement.addContent(subLanguageElement);
    subLanguageElement.setAttribute("name", subLanguageID);
    value = evaluate(subLanguageID, subLanguageNode.getLine(), subLanguageNode.getColumn());
    if (value == null)
      reportError("Could not evaluate \"" + subLanguageID + "\".");
    else
      subLanguageElement.setAttribute("value", value.toString());
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "menu" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertMenuEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "menu");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }

      // Optional common resource info part.
      if (currentNode != null)
      {
        while (currentNode.getType() == RCParserTokenTypes.COMMON_RESOURCE_INFO)
        {
          convertCommonResourceInfo(currentNode, resourceElement);
          currentNode = currentNode.getNextSibling();
        }
      }

      while (currentNode != null)
      {
        convertMenuItem(currentNode, resourceElement);
        currentNode = currentNode.getNextSibling();
      }
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "menuex" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertMenuExEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "menuex");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }

      // Optional common resource info part.
      if (currentNode != null)
      {
        while (currentNode.getType() == RCParserTokenTypes.COMMON_RESOURCE_INFO)
        {
          convertCommonResourceInfo(currentNode, resourceElement);
          currentNode = currentNode.getNextSibling();
        }
      }

      while (currentNode != null)
      {
        convertMenuExItem(currentNode, resourceElement);
        currentNode = currentNode.getNextSibling();
      }
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Recursively called method to create a nested structure of menu items.
   * 
   * @param node The node containing the entry to convert.
   * @param element The target XML element to add the converted stuff to.
   */
  private void convertMenuExItem(AST node, Element element)
  {
    if (node.getType() == RCParserTokenTypes.LITERAL_menuitem)
    {
      node = node.getFirstChild();
      Element menuItemElement = new Element("menu-item");
      element.addContent(menuItemElement);
      menuItemElement.setAttribute("text", node.getText());
      
      node = node.getNextSibling();
      if (node != null && node.getType() == RCParserTokenTypes.EXPR)
      {
        Object value = evaluate(node);
        if (value != null)
          menuItemElement.setAttribute("id", value.toString());
        else
          menuItemElement.setAttribute("id", convertExpressionToText(node.getFirstChild()));
        node = node.getNextSibling();
        if (node != null && node.getType() == RCParserTokenTypes.EXPR)
        {
          value = evaluate(node);
          if (value != null)
            menuItemElement.setAttribute("type", value.toString());
          else
            menuItemElement.setAttribute("type", convertExpressionToText(node.getFirstChild()));
          node = node.getNextSibling();
          if (node != null && node.getType() == RCParserTokenTypes.EXPR)
          {
            value = evaluate(node);
            if (value != null)
              menuItemElement.setAttribute("state", value.toString());
            else
              menuItemElement.setAttribute("state", convertExpressionToText(node.getFirstChild()));
          }
        }
      }
    }
    else
    {
      // Found a popup menu definition.
      node = node.getFirstChild();
      Element menuItemElement = new Element("popup");
      element.addContent(menuItemElement);
      menuItemElement.setAttribute("text", node.getText());
      
      node = node.getNextSibling();
      if (node != null && node.getType() == RCParserTokenTypes.EXPR)
      {
        Object value = evaluate(node);
        if (value != null)
          menuItemElement.setAttribute("id", value.toString());
        else
          menuItemElement.setAttribute("id", convertExpressionToText(node.getFirstChild()));
        node = node.getNextSibling();
        if (node != null && node.getType() == RCParserTokenTypes.EXPR)
        {
          value = evaluate(node);
          if (value != null)
            menuItemElement.setAttribute("type", value.toString());
          else
            menuItemElement.setAttribute("type", convertExpressionToText(node.getFirstChild()));
          node = node.getNextSibling();
          if (node != null && node.getType() == RCParserTokenTypes.EXPR)
          {
            value = evaluate(node);
            if (value != null)
              menuItemElement.setAttribute("state", value.toString());
            else
              menuItemElement.setAttribute("state", convertExpressionToText(node.getFirstChild()));
            node = node.getNextSibling();
            if (node != null && node.getType() == RCParserTokenTypes.EXPR)
            {
              value = evaluate(node);
              if (value != null)
                menuItemElement.setAttribute("help-id", value.toString());
              else
                menuItemElement.setAttribute("help-id", convertExpressionToText(node.getFirstChild()));
            }
          }
        }
      }
      while (node != null)
      {
        convertMenuExItem(node, menuItemElement);
        node = node.getNextSibling();
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Recursively called method to create a nested structure of menu items.
   * 
   * @param node The node containing the entry to convert.
   * @param element The target XML element to add the converted stuff to.
   */
  private void convertMenuItem(AST node, Element element)
  {
    if (node.getType() == RCParserTokenTypes.LITERAL_menuitem)
    {
      node = node.getFirstChild();
      Element menuItemElement = new Element("menu-item");
      element.addContent(menuItemElement);
      menuItemElement.setAttribute("text", node.getText());
      
      node = node.getNextSibling();
      if (node != null)
      {
        String expression = node.getText();
        Object value = evaluate(expression, node.getLine(), node.getColumn());
        menuItemElement.setAttribute("result", expression);
        if (value != null)
          menuItemElement.setAttribute("result-value", value.toString());
  
        // Optional menu options.
        node = node.getNextSibling();
        while (node != null)
        {
          Element optionElement = new Element("option");
          menuItemElement.addContent(optionElement);
          optionElement.setAttribute("value", node.getText());
          node = node.getNextSibling();
        }
      }
    }
    else
    {
      // Found a popup menu definition.
      node = node.getFirstChild();
      Element menuItemElement = new Element("popup");
      element.addContent(menuItemElement);
      menuItemElement.setAttribute("text", node.getText());
      
      // Optional menu options.
      node = node.getNextSibling();
      while ((node != null) && (node.getType() != RCParserTokenTypes.LITERAL_menuitem) && 
        (node.getType() != RCParserTokenTypes.LITERAL_popup))
      {
        Element optionElement = new Element("option");
        menuItemElement.addContent(optionElement);
        optionElement.setAttribute("value", node.getText());
        node = node.getNextSibling();
      }

      while (node != null)
      {
        convertMenuItem(node, menuItemElement);
        node = node.getNextSibling();
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "messagetable" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertMessageTableEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "message-table");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      String filename = currentNode.getText();
      if (filename.startsWith("\""))
        filename = filename.substring(1, filename.length());
      resourceElement.setAttribute("file-name", filename);
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a named resource entry (dialog, user defined data etc.) to XML.
   * 
   * @param target The XML node where to add the new content to.
   */
  private void convertNamedResourceEntry(Element target)
  {
    Element resourceElement = new Element("named-resource");
    target.addContent(resourceElement);

    AST resourceNode = getAstNode();
    if (resourceNode != null)
    {
      AST idNode = resourceNode.getFirstChild();
      if (idNode != null)
      {
        resourceElement.setAttribute("name", idNode.getText());
        
        AST resourceEntry = idNode.getNextSibling();
        if (resourceEntry != null)
        {
          Integer resourceType = (Integer) resourceMap.get(resourceEntry.getText().toLowerCase());
          if (resourceType == null)
            reportError("Unrecognized resource type: \"" + resourceEntry.getText() + "\".");
          else
          {
            AST entry = idNode.getNextSibling();
            if (entry != null)
            {
              switch (resourceType.intValue())
              {
                case RCParserTokenTypes.LITERAL_accelerators:
                {
                  convertAcceleratorsEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_bitmap:
                {
                  convertBitmapEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_cursor:
                {
                  convertCursorEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_dialog:
                {
                  convertDialogEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_dialogex:
                {
                  convertDialogExEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_font:
                {
                  convertFontEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_icon:
                {
                  convertIconEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_menu:
                {
                  convertMenuEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_menuex:
                {
                  convertMenuExEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_messagetable:
                {
                  convertMessageTableEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_rcdata:
                {
                  convertRCDataEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_versioninfo:
                {
                  convertVersionInfoEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_textinclude:
                {
                  convertTextincludeEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_designinfo:
                {
                  convertDesignInfoEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_toolbar:
                {
                  convertToolbarEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.LITERAL_dlginit:
                {
                  convertDlgInitEntry(entry, resourceElement);
                  break;
                }
                case RCParserTokenTypes.USER_DEFINED:
                {
                  convertUserDefinedEntry(entry, resourceElement);
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a pragma code_page entry to XML.
   * 
   * @param target The XML node where to add the new content to.
   */
  private void convertPragmaEntry(Element target)
  {
    Element codepageElement = new Element("codepage");
    target.addContent(codepageElement);

    AST codepageNode = getAstNode().getFirstChild();
    if (codepageNode != null)
    {
      AST valueNode = codepageNode.getNextSibling();
      if (valueNode != null)
      {
        String codepageValue = valueNode.getText();
        if (!codepageValue.equalsIgnoreCase("default"))
        {  
          Object value = evaluate(codepageValue, valueNode.getLine(), valueNode.getColumn());
          if (value == null)
            reportError("Could not evaluate \"" + codepageValue + "\".");
          else
            codepageElement.setAttribute("value", ((Integer) value).toString());
        }
        else
          codepageElement.setAttribute("value", "default");
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts raw resource data to XML. Each entry can be either an integer or a string of data,
   * separated by comma.
   * Note: We are converting MSVC resource files here but Borland does uses the same rc format and 
   *       added (at least) one special feature not supported by MS. With BRCC you can specify a 
   *       list of hex numbers enclosed by single quotes. The parser is made to handled that and
   *       we simply pass the data along here.
   * 
   * @param node The node containing the raw data.
   * @param element The XML element to add the new nodes to.
   */
  private void convertRawDataToXML(AST node, Element element)
  {
    AST currentNode = node.getFirstChild();
    while (currentNode != null)
    {
      Element dataElement = new Element("entry");
      element.addContent(dataElement);
      dataElement.setAttribute("value", convertToString(currentNode.getText()));
      
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "rcdata" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertRCDataEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "rcdata");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
      
      // Optional common resource info part.
      if (currentNode != null)
      {
        while (currentNode.getType() == RCParserTokenTypes.COMMON_RESOURCE_INFO)
        {
          convertCommonResourceInfo(currentNode, resourceElement);
          currentNode = currentNode.getNextSibling();
        }
      }

      if (currentNode != null)
      {
        if (currentNode.getType() == RCParserTokenTypes.FILE_NAME)
        {
          String filename = currentNode.getFirstChild().getText();
          if (filename.startsWith("\""))
            filename = filename.substring(1, filename.length() - 1);
          resourceElement.setAttribute("file-name", filename);
        }
        else
        {
          // Must be raw data, e.g. single literals (string, character, integer etc.) or
          // a collection of hex numbers enclosed by single quotes.
          convertRawDataToXML(currentNode, resourceElement);
        }
      }
    }    
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Converts resource attributes to XML.
   * 
   * @param element The XML element to add the new nodes to.
   * @param node The AST node to convert.
   */
  private void convertResourceAttributes(Element element, AST node)
  {
    Element attributesELement = new Element("resource-attributes");
    element.setContent(attributesELement);
    
    AST currentNode = node.getFirstChild();
    while (currentNode != null)
    {
      Element attributeElement = new Element("attribute");
      attributeElement.setAttribute("value", currentNode.getText());
      attributesELement.addContent(attributeElement);
      
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a string table to XML.
   * 
   * @param target The XML node where to add the new content to.
   */
  private void convertStringTableEntry(Element target)
  {
    AST stringTableNode = getAstNode();

    Element stringTableElement = new Element("string-table");
    target.addContent(stringTableElement);

    // Check if there is are (optional) resource attributes.
    AST currentNode = stringTableNode.getFirstChild();
    if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
    {
      convertResourceAttributes(stringTableElement, currentNode);
      currentNode = currentNode.getNextSibling();
    }
    
    while (currentNode != null)
    {
      AST symbolNode = currentNode.getFirstChild();

      Element entryElement = processEntryWithEvaluation(symbolNode, "string-entry", stringTableElement);
      
      AST valueNode = symbolNode.getNextSibling();
      String value = valueNode.getText();
      if (value.startsWith("\""))
        value = value.substring(1, value.length() - 1);
      entryElement.setAttribute("line", value);
      
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "textinclude" entry to XML. Not sure if this info is at all relevant for
   * converters and external tools, but it's there for completeness.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertTextincludeEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "text-include");
    
    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
    }    
    
    // Textinclude entries are just strings, simple to parse.
    while (currentNode != null)
    {
      Element entry = new Element("entry");
      String expression = currentNode.getText();
      Object value = evaluate(expression, currentNode.getLine(), currentNode.getColumn());
      entry.setAttribute("value", convertToString(value));
      resourceElement.addContent(entry);
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "toolbar" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertToolbarEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "toolbar");

    AST currentNode = sourceNode.getFirstChild();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
      
      // The next two entries are button width and height.
      processEntryWithEvaluation(currentNode, "button-width", resourceElement);
      currentNode = currentNode.getNextSibling();
      processEntryWithEvaluation(currentNode, "button-height", resourceElement);
      currentNode = currentNode.getNextSibling();
      
      // After the button sizes follows the list of toolbar buttons.
      while (currentNode != null)
      {
        switch (currentNode.getType())
        {
          case RCParserTokenTypes.LITERAL_button:
          {
            AST idNode = currentNode.getFirstChild();
            processEntryWithEvaluation(idNode, "button", resourceElement);
            break;
          }
          case RCParserTokenTypes.LITERAL_separator:
          {
            Element separatorElement = new Element("Separator");
            resourceElement.addContent(separatorElement);
            break;
          }
          default:
            // Actually this should never happen as the parser already takes care to find the
            // correct syntax.
            reportError("Invalid toolbar content definition.");
        }
        currentNode = currentNode.getNextSibling();
      }
    }  
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts the given symbol value into a string that can be placed into a DOM element.
   * If the object is a string literal then it gets unquoted first.
   * 
   * @param value The value to convert.
   * @return The converted value as string.
   */
  private String convertToString(Object value)
  {
    if (value instanceof AST)
    {
      value = evaluate((AST) value);
    }
    else
      if (value instanceof String)
      {
        String string = (String) value;
        if (string.startsWith("\""))
          return string.substring(1, string.length() - 1);
        else
          return string;
      }
    return value.toString();
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a user defined entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertUserDefinedEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "user-defined");
    AST currentNode = sourceNode.getFirstChild();
    resourceElement.setAttribute("resource-type", currentNode.getText());
    currentNode = currentNode.getNextSibling();
    if (currentNode != null)
    {
      if (currentNode.getType() == RCParserTokenTypes.RESOURCE_ATTRIBUTES)
      {
        convertResourceAttributes(resourceElement, currentNode);
        currentNode = currentNode.getNextSibling();
      }
      
      // Optional common resource info part.
      if (currentNode != null)
      {
        while (currentNode.getType() == RCParserTokenTypes.COMMON_RESOURCE_INFO)
        {
          convertCommonResourceInfo(currentNode, resourceElement);
          currentNode = currentNode.getNextSibling();
        }
      }

      if (currentNode != null)
      {
        if (currentNode.getType() == RCParserTokenTypes.FILE_NAME)
        {
          Element fileNameElement = new Element("data");
          resourceElement.addContent(fileNameElement);
          String filename = currentNode.getFirstChild().getText();
          if (filename.startsWith("\""))
            filename = filename.substring(1, filename.length());
          fileNameElement.setAttribute("file-name", filename);
        }
        else
        {
          // Must be raw data, e.g. single literals (string, character, integer etc.) or
          // a collection of hex numbers enclosed by single quotes.
          convertRawDataToXML(currentNode, resourceElement);
        }
      }
    }    
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * Converts a "versioninfo" entry to XML.
   * 
   * @param sourceNode The node containing the AST data to convert.
   * @param resourceElement The entry to which the new nodes must be added.
   */
  private void convertVersionInfoEntry(AST sourceNode, Element resourceElement)
  {
    resourceElement.setAttribute("type", "version-info");

    AST currentNode = sourceNode.getFirstChild();

    // Optional fixed version info part.
    if (currentNode != null)
    {
      while (currentNode.getType() == RCParserTokenTypes.VERSION_FIXED_INFO)
      {
        AST infoNode = currentNode.getFirstChild();
        Element infoElement = new Element("info-detail");
        infoElement.setAttribute("name", infoNode.getText());
        resourceElement.addContent(infoElement);
        convertExpressionList(infoNode.getFirstChild(), infoElement);

        currentNode = currentNode.getNextSibling();
      }
    }

    // Optional common resource info part.
    if (currentNode != null)
    {
      while (currentNode.getType() == RCParserTokenTypes.COMMON_RESOURCE_INFO)
      {
        convertCommonResourceInfo(currentNode, resourceElement);
        currentNode = currentNode.getNextSibling();
      }
    }
    
    // Finally the main content.
    while (currentNode != null)
    {
      switch (currentNode.getType())
      {
        case RCParserTokenTypes.STRING_FILE_INFO:
        {
          Element infoElement = new Element("string-file-info");
          resourceElement.addContent(infoElement);

          AST blockNode = currentNode.getFirstChild();
          while (blockNode != null)
          {
            Element blockElement = new Element("block");
            infoElement.addContent(blockElement);
            
            AST entryNode = blockNode.getFirstChild();
            processEntryWithEvaluationAsAttribute(entryNode, "charset", blockElement);
            entryNode = entryNode.getNextSibling();
            while (entryNode != null)
            {
              Element valueElement = new Element("value");
              blockElement.addContent(valueElement);
              
              AST valueNode = entryNode.getFirstChild();
              valueElement.setAttribute("name", convertToString(valueNode));
              valueNode = valueNode.getNextSibling();
              valueElement.setAttribute("value", convertToString(valueNode));
              
              entryNode = entryNode.getNextSibling();
            }
            
            blockNode = blockNode.getNextSibling();
          }
          break;
        }
        case RCParserTokenTypes.VAR_FILE_INFO:
        {
          Element infoElement = new Element("var-file-info");
          resourceElement.addContent(infoElement);

          AST entryNode = currentNode.getFirstChild();
          while (entryNode != null)
          {
            Element valueElement = new Element("value");
            infoElement.addContent(valueElement);
            
            AST valueNode = entryNode.getFirstChild();
            valueElement.setAttribute("name", convertToString(valueNode.getText()));
            convertExpressionList(valueNode.getNextSibling(), valueElement);
            entryNode = entryNode.getNextSibling();
          }
          break;
        }
      }
      
      currentNode = currentNode.getNextSibling();
    }
  }

  //------------------------------------------------------------------------------------------------

  /**
   * Adds a new XML node with name <b>name</b> to target. The AST node's value is added as
   * an attribute with name <b>symbol</b> and if that symbol can be evaluated to a single value
   * then this value is also added as attribute with name <b>symbol-value</b>.
   * If the current AST node does not represent a symbol then it is taken as expression, evaluated
   * and added as single attribute <b>value</b>.
   * 
   * @param node The AST node to parse.
   * @param name The name for the new XML node to create.
   * @param target The target XML node to which the new XML node must be added.
   * @return The newly created child XML node.
   */
  private Element processEntryWithEvaluation(AST node, String name, Element target)
  {
    String symbol = node.getText();
    Element element = new Element(name);
    target.addContent(element);
    if (node != null)
    {
      if (node.getType() == RCParserTokenTypes.IDENTIFIER)
      {
        element.setAttribute("symbol", symbol);
        Object symbolValue = evaluate(symbol, node.getLine(), node.getColumn());
        if (symbolValue != null)
          element.setAttribute("symbol-value", convertToString(symbolValue));
      }
      else
        if (node.getType() == RCParserTokenTypes.EXPR)
        {
          Object value = evaluate(node);
          if (value != null)
            element.setAttribute("value", convertToString(value));
          else
            element.setAttribute("value", convertExpressionToText(node.getFirstChild()));
        }
        else
        {
          // There is only a scalar value or an expression. Try to evaluate it and add the result as 
          // attribute to the new XML node. If it cannot be evaluated then add the pure text data.
          Object symbolValue = evaluate(symbol, node.getLine(), node.getColumn());
          if (symbolValue == null)
            element.setAttribute("value", symbol);
          else
            element.setAttribute("value", convertToString(symbolValue));
        }
    }

    return element;
  }

  //------------------------------------------------------------------------------------------------
  
  /**
   * This method is very similar as {@see ResourceStatement#processEntryWithEvaluation}. What is
   * different here is that the result is added as attribute to the target node.
   * 
   * @param node The AST node to parse.
   * @param name The name for the attribute to use.
   * @param target The target XML node to which the attribute is to be attached.
   */
  private void processEntryWithEvaluationAsAttribute(AST node, String name, Element target)
  {
    if (node != null)
    {
      if (node.getType() == RCParserTokenTypes.IDENTIFIER)
      {
        Object value = evaluate(node.getText(), node.getLine(), node.getColumn());
        if (value != null)
          target.setAttribute(name, convertToString(value));
        else
          target.setAttribute(name, convertExpressionToText(node.getFirstChild()));
      }
      else
        if (node.getType() == RCParserTokenTypes.EXPR)
        {
          Object value = evaluate(node);
          if (value != null)
            target.setAttribute(name, convertToString(value));
          else
            target.setAttribute(name, convertExpressionToText(node.getFirstChild()));
        }
        else
        {
          // There is only a scalar value or an expression.
          Object symbolValue = evaluate(node.getText(), node.getLine(), node.getColumn());
          if (symbolValue == null)
            target.setAttribute("value", node.getText());
          else
            target.setAttribute("value", convertToString(symbolValue));
        }
    }
  }

  //------------------------------------------------------------------------------------------------
  
  /** 
   * Conversion routine to create an XML subtree from the current AST subtree.
   * 
   * @param target The target node in the XML DOM where to add the converted data.
   */
  public void convert(Element target)
  {
    switch (getAstNode().getType())
    {
      case RCParserTokenTypes.LITERAL_language:
      {
        convertLanguageEntry(getAstNode(), target);
        break;
      }
      case RCParserTokenTypes.LITERAL_pragma:
      {
        convertPragmaEntry(target);
        break;
      }
      case RCParserTokenTypes.LITERAL_stringtable:
      {
        convertStringTableEntry(target);
        break;
      }
      case RCParserTokenTypes.NAMED_RESOURCE:
      {
        convertNamedResourceEntry(target);
        break;
      }
    }
  }

  //------------------------------------------------------------------------------------------------
  
}
