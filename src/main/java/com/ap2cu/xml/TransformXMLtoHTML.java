package com.ap2cu.xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class TransformXMLtoHTML {

  public static final String ENCODING = "UTF-8";
  
  private static void transform(String baseFilename) {
    try {
      StreamSource xsl = new StreamSource(new InputStreamReader(new FileInputStream("data/xsl/" + baseFilename + ".xsl"), ENCODING));
      StreamSource xml = new StreamSource(new InputStreamReader(new FileInputStream("data/xml/" + baseFilename + ".xml"), ENCODING));
      StreamResult html = new StreamResult(new OutputStreamWriter(new FileOutputStream("data/html/" + baseFilename + ".html"), ENCODING));
      Transformer t = TransformerFactory.newInstance().newTransformer(xsl);
      t.transform(xml, html);
      System.out.println("=> data/html/" + baseFilename + ".html has been generated");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    transform("contact-country");

  }

}
