package com.ap2cu.postget;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Form {

  private StringBuffer action;
  private Map<String, String> postParams;
  private Map<String, String> getParams;
  private Map<String, String> headerParams;

  public Form(String action) {
    this.action = new StringBuffer(action);
    this.postParams = new HashMap<String, String>();
    this.getParams = new HashMap<String, String>();
    this.headerParams = new HashMap<String, String>();
  }

  public void addPostParameter(String key, String value) {
    postParams.put(key, value);
  }

  public void addGetParameter(String key, String value) {
    getParams.put(key, value);
  }

  public void addHeader(String key, String value) {
    headerParams.put(key, value);
  }

  public void setAction(String action) {
    this.action = new StringBuffer(action);
  }

  private String serializeParams(Map<String, String> params) throws UnsupportedEncodingException {
    StringBuffer data = new StringBuffer();
    Iterator<String> iterator = params.keySet().iterator();
    while (iterator.hasNext()) {
      String key = iterator.next();
      String value = params.get(key);
      data.append(URLEncoder.encode(key, "UTF-8")).append("=").append(URLEncoder.encode(value, "UTF-8")).append("&");
    }
    return data.toString();
  }

  private void addGetParameters() throws UnsupportedEncodingException {
    action = action.append("?").append(serializeParams(getParams));
  }

  private void addPostParameters(URLConnection connection) throws IOException {
    String postData = serializeParams(postParams);
    OutputStreamWriter writer = null;
    try {
      writer = new OutputStreamWriter(connection.getOutputStream());
      writer.write(postData);
      writer.flush();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (writer != null)
        writer.close();
    }
  }

  private void addHeaders(URLConnection connection) {
    Iterator<String> iteratorHeader = headerParams.keySet().iterator();
    while (iteratorHeader.hasNext()) {
      String key = iteratorHeader.next();
      String value = headerParams.get(key);
      connection.setRequestProperty(key, value);
    }
  }

  private URLConnection createConnection() throws MalformedURLException, IOException {
    URLConnection connection = new URL(action.toString()).openConnection();
    connection.setDoOutput(true);
    connection.setUseCaches(false);
    return connection;
  }

  private String readResult(URLConnection connection) throws IOException {
    StringBuffer result = new StringBuffer();
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;
      while ((line = reader.readLine()) != null)
        result.append(line.toString());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (reader != null)
        reader.close();
    }
    return result.toString();
  }

  public String submit() {
    return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<String>() {
      public String run() {
        try {
          addGetParameters();
          URLConnection connection = createConnection();
          addHeaders(connection);
          addPostParameters(connection);
          return readResult(connection);
        }
        catch (Exception e) {
          return "An error occurred: " + e.getMessage();
        }
      }
    });
  }
}
