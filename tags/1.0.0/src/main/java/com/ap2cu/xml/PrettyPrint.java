package com.ap2cu.xml;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * This class embeds a static method dedicated to XML Formatting
 * 
 * @author Sebastien Bianco
 */
public class PrettyPrint {

  /**
   * Transform a XML into human readable format
   * 
   * @param document
   *          the document to format
   * @return A String representing the XML document
   */
  public static String prettyPrint(Document document) {
    XMLWriter writer = null;
    OutputStream out = new ByteArrayOutputStream();
    try {
      OutputFormat format = OutputFormat.createPrettyPrint();
      writer = new XMLWriter(out, format);
      writer.write(document);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (writer != null) {
        try {
          writer.close();
        }
        catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    }
    return out.toString();
  }
  

  public static String prettyPrint(String s) throws DocumentException {
    if(s==null || s.trim().equals(""))
      return s;
    return prettyPrint(DocumentHelper.parseText(s));
  }
}
