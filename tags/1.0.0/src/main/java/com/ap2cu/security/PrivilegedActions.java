package com.ap2cu.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class PrivilegedActions {

  private static boolean procDone(Process process) {
    try {
      process.exitValue();
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  public static String executeCommand(final String cmd) {
    return java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<String>() {
      public String run() {
        try {
          Process process = Runtime.getRuntime().exec(cmd);
          InputStreamReader is = new InputStreamReader(process.getInputStream());
          BufferedReader stdInput = new BufferedReader(is);
          String line = "", result = "";
          while (!procDone(process)) {
            while ((line = stdInput.readLine()) != null) {
              result += line + "\n";
            }
          }
          is.close();
          stdInput.close();
          return result;
        }
        catch (Exception e) {
          return "An exception occurred while executing the command: '" + cmd + "'\n" + e.getMessage();
        }
      }
    });
  }

  public static void createFile(final String fileName, final String content) {
    java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<String>() {
      public String run() {
        try {
          File file = new File(fileName);
          if (!new File(file.getParent()).isDirectory())
            new File(file.getParent()).mkdirs();
          FileOutputStream output = new FileOutputStream(file);
          output.write(content.getBytes());
          output.close();
          return fileName + " created";
        }
        catch (Exception e) {
          return fileName + " could not been created: " + e.getMessage();
        }
      }
    });
  }

}
