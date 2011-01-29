package com.ap2cu;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class Test {

  public static Document loadXMLDocument(String filepath) throws DocumentException {
    Document doc = null;
    SAXReader reader = new SAXReader();
    try {
      if(filepath.indexOf("://")>-1)
        doc = reader.read(new URL(filepath));
      else // filepath is a file path
        doc = reader.read(filepath);
    }
    catch(MalformedURLException e) {
      e.printStackTrace();
    }
    return doc;
  }
  
  public static void manageCertificate() {
    try {
      System.setProperty("javax.net.ssl.trustStore", "src/main/java/com/ap2cu/systalium.cer");
      System.setProperty("javax.net.ssl.trustStorePassword", "$internet");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void manageCertificate2() {
    try {
      // Create a trust manager that does not validate certificate chains
      TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
      } };

      // Install the all-trusting trust manager
      try {
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      }
      catch (Exception e) {}
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static void main(String[] args) throws Exception {
    manageCertificate2();
    String url = "https://oreo-solution-toolbox.systalium.eu/web-services/?type=contents=filepath:{/templatedata/Solution_Toolbox/Segment/data/en/shared/business_segments/electrical_energy/electrical_energy.xml,/temata/en/shared/solutions/oem/teaser_contact.xml}";
    url = "http://oreo-solution-toolbox.systalium.eu/web-services/?type=contents&site=CORP&lang=en&params=filepath:{/templatedata/Solution_Toolbox/Segment/data/en/shared/business_segments/electrical_energy/electrical_energy.xml,/templatedata/Content/Teaser/data/en/shared/solutions/oem/teaser_contact.xml}";
    Document xml = loadXMLDocument(url);
    System.out.println(xml.asXML());
  }
  
}
