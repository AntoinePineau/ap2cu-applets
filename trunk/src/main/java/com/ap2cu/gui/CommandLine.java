package com.ap2cu.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandLine {

  public static void execute(StringBuffer command) throws Exception {
    execute(command, null);
  }
  
  public static void execute(StringBuffer command, File fromDir) throws Exception {
    execute(command.toString(), null, fromDir);
  }
  
  public static void execute(String command, String[] params, File fromDir) throws Exception {
    System.out.print("[command]");
    if(fromDir!=null)
      System.out.print(fromDir);
    System.out.print("> ");
    System.out.println(command.toString());
    if(fromDir == null)
      Runtime.getRuntime().exec(command.toString()).waitFor();
    else
      Runtime.getRuntime().exec(command.toString(), params, fromDir);
  }

  public static String readUserTyping() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String typing = null;
    try {
      typing = br.readLine();
    }
    catch (IOException ioe) {
      System.out.println("IO error trying to read your name!");
      System.exit(1);
    }
    return typing;
  }
}
