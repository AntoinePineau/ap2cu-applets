package com.ap2cu.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ap2cu.utils.Encoding;

public class XMLUtils {

  private static XMLUtils singleton;
  
  public static XMLUtils getXMLUtils() {
    if(singleton==null)
      singleton = new XMLUtils();
    return singleton;
  }
  
  /**
   * Create a {@link Document} from the given XML file path.
   * 
   * @param filepath
   *          the absolute path to the XML file
   * @return the XML {@link Document}
   * @throws DocumentException
   *           if the loading of the document failed
   * @throws MalformedURLException
   */
  public Document loadXMLDocument(String filepath) throws DocumentException, MalformedURLException {
    Document doc = null;
    SAXReader reader = new SAXReader();
    if (filepath.indexOf("://") > -1)
      doc = reader.read(new URL(filepath));
    else
      // filepath is a file path
      doc = reader.read(filepath);
    return doc;
  }

  /**
   * Transform a XML into human readable format
   * 
   * @param document
   *          the document to format
   * @return A String representing the XML document
   */
  public String prettyPrint(Document document) {
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

  public String prettyPrint(String s) throws DocumentException {
    if (s == null || s.trim().equals(""))
      return s;
    return prettyPrint(DocumentHelper.parseText(s));
  }

  public String transform(String xmlString, String xslString) throws Exception {
    try {
      DocumentHelper.parseText(xmlString);
    }
    catch (Exception e) {
      throw new Exception("the XML String is not a valid XML document");
    }
    try {
      DocumentHelper.parseText(xslString);
    }
    catch (Exception e) {
      throw new Exception("the XSL String is not a valid XML document");
    }
    StreamSource xml = stringToStreamSource(xmlString);
    StreamSource xsl = stringToStreamSource(xslString);

    StringWriter html = new StringWriter();
    StreamResult result = new StreamResult(html);

    transform(xml, xsl, result);
    return html.getBuffer().toString();
  }

  private InputStream stringToInputStream(String source) {
    return new ByteArrayInputStream(source.getBytes());
  }

  private StreamSource stringToStreamSource(String source) {
    return new StreamSource(stringToInputStream(source));
  }

  public void transform(StreamSource xml, StreamSource xsl, StreamResult html) throws TransformerException {
    Transformer t = TransformerFactory.newInstance().newTransformer(xsl);
    t.transform(xml, html);
  }

  public void transform(String baseFilename) {
    transform(baseFilename + ".xml", baseFilename + ".xsl", baseFilename + ".html");
  }

  public void transform(String xmlFilepath, String xslFilepath, String htmlFilepath) {
    try {
      StreamSource xsl = new StreamSource(new InputStreamReader(new FileInputStream(xslFilepath), Encoding.DEFAULT));
      StreamSource xml = new StreamSource(new InputStreamReader(new FileInputStream(xmlFilepath), Encoding.DEFAULT));
      StreamResult html = new StreamResult(new OutputStreamWriter(new FileOutputStream(htmlFilepath), Encoding.DEFAULT));
      transform(xml, xsl, html);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}
