package com.ap2cu.xml;

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import com.ap2cu.utils.Encoding;

public class XML {

  private Document document;

  public XML(String filepath) throws DocumentException {
    SAXReader reader = new SAXReader(false);
    document = reader.read(new File(filepath));
  }

  public XML(Document document) {
    this.document = document;
  }

  public XML fromDTD(String dtdFilepath) {
    return null;
  }

  public XML fromXSD(String xsdFilepath) {
    return null;
  }

  public static String toDTD() {
    return null;
  }

  public static String toXSD() {
    return null;
  }

  private void addDTDreference(String dtdFilepath) {
    document.addDocType(document.getRootElement().getName(), null, dtdFilepath);
  }
  
  public boolean validateWithDTD(String dtdFilepath) {
    try {
      Document docTmp = (Document) document.clone();
      addDTDreference(dtdFilepath);
      String content = document.asXML();
      SAXReader reader = new SAXReader(true);
      reader.read(new ByteArrayInputStream(content.getBytes(Encoding.DEFAULT)));
      document = docTmp;
      return true;
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return false;
  }

  public boolean validateWithXSDandDTD(String dtdFilepath, String xsdFilepath) {
    try {
      Document docTmp = (Document) document.clone();
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
      factory.setSchema(schemaFactory.newSchema(new Source[] { new StreamSource(xsdFilepath) }));
      SAXParser parser = factory.newSAXParser();
      SAXReader reader = new SAXReader(parser.getXMLReader(), true);
      addDTDreference(dtdFilepath);
      String content = document.asXML();
      reader.read(new ByteArrayInputStream(content.getBytes(Encoding.DEFAULT)));
      document = docTmp;
      return true;
    }
    catch (Exception e) {
      System.err.println(e.getMessage());
    }
    return false;
  }

  @Override
  public String toString() {
    return toString(false);
  }

  public String toString(boolean prettyPrint) {
    return prettyPrint ? XMLUtils.getXMLUtils().prettyPrint(document) : document.asXML();
  }

  public static void main(String[] args) throws DocumentException {
    // XML xml = new XML("properties.xml");
    // System.out.println("DTD ok: "+xml.validateWithDTD("properties.dtd"));
    XML xml = new XML("note.xml");
    System.out.println("DTD ok: " + xml.validateWithDTD("note.dtd"));
    System.out.println("XSD ok: " + xml.validateWithXSDandDTD("note.xsd", "note.dtd"));
  }
}
