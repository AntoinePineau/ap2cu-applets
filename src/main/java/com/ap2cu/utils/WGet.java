package com.ap2cu.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WGet {

  /**
   * Command line component.
   * 
   * @param args
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      outputUsage();
    }
    else {
      try {
        wget(args[0]);
      }
      catch (MalformedURLException e) {
        e.printStackTrace();
        System.exit(1);
      }
      catch (Exception e) {
        e.printStackTrace();
        System.exit(2);
      }
    }
  }

  /**
   * Outputs the usage of the command line component.
   */
  private static void outputUsage() {
    System.out.println("USAGE: ");
    System.out.println("java -jar wget.jar <URL>");
  }

  /**
   * This function downloads the file specified in the URL to the current
   * working directory.
   * 
   * @param theURL
   *          The URL of the file to be downloaded.
   * @return An integer result based on the WGETJavaResults Enumeration. Values
   *         Include: FAILED_IO_EXCEPTION - Could not open a connection to the
   *         URL. FAILED_UKNOWNTYPE - Could not determine the file type.
   *         COMPLETE - Downloaded completed sucessfully.
   * @throws IOException
   */
  public static WGetResults wget(String fileUrl) throws IOException {
    fileUrl = fileUrl.replaceAll("\\\\\\\\", "/");
    URL theURL = new URL(fileUrl);
    URLConnection con;
    con = theURL.openConnection();
    con.connect();

    String type = con.getContentType();

    if (type != null) {
      byte[] buffer = new byte[4 * 1024];
      int read;
      String theFile = fileUrl.replaceAll("^.*/([^/]+)$", "$1");
      FileOutputStream os = new FileOutputStream(theFile);
      InputStream in = con.getInputStream();
      while ((read = in.read(buffer)) > 0)
        os.write(buffer, 0, read);
      os.close();
      in.close();
      System.out.println("Saved under "+theFile);

      return WGetResults.COMPLETE;
    }
    else {
      return WGetResults.FAILED_UKNOWNTYPE;
    }
  }

  /**
   * Enumeration of the return values for WGet.
   */
  public enum WGetResults {
    /**
     * Failure to connect to the URL.
     */
    FAILED_IO_EXCEPTION,

    /**
     * Failure to determine file type from the URL connection.
     */
    FAILED_UKNOWNTYPE,

    /**
     * File downloaded sucessfully.
     */
    COMPLETE
  }
}
