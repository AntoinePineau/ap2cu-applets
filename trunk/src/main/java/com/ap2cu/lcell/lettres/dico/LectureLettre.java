package com.ap2cu.lcell.lettres.dico;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author PINEAU Antoine
 * @version 1.0
 */
public class LectureLettre {
    char dernierChar;
    boolean fin;
    InputStream inputStream;

  public LectureLettre(InputStream inputStream) throws IOException{
    this.inputStream = inputStream;
    dernierChar=(char)inputStream.read();
    fin=false;
  }


  public boolean getFin(){
    return fin;
  }

  public final static String REGEX_A = "[àáâãäåÀÁÂÃÄÅ]";
  public final static String REGEX_C = "[çÇ]";
  public final static String REGEX_E = "[èéêëÈÉÊË]";
  public final static String REGEX_I = "[ìíîïÌÍÎÏ]";
  public final static String REGEX_N = "[ñÑ]";
  public final static String REGEX_O = "[òóôõöÒÓÔÕÖ]";
  public final static String REGEX_U = "[ùúûüÙÚÛÜ]";
  public final static String REGEX_Y = "[ýÿÝ]";
  public final static String REGEX_WORD = "[A-Za-z-]";
  
  public static char removeAccent(char character) {
    String c = ""+character;
    if (c.matches(REGEX_A)) return 'A';
    if (c.matches(REGEX_C)) return 'C';
    if (c.matches(REGEX_E)) return 'E';
    if (c.matches(REGEX_I)) return 'I';
    if (c.matches(REGEX_N)) return 'N';
    if (c.matches(REGEX_O)) return 'O';
    if (c.matches(REGEX_U)) return 'U';
    if (c.matches(REGEX_Y)) return 'Y';
    return c.toUpperCase().charAt(0);
  }
  
  public boolean lireCaractereCorrect()throws IOException{
    lireCaractere();
    String c = removeAccent(dernierChar)+"";
    return c.matches(REGEX_WORD);
  }


  public char lireCaractere()throws IOException{
    char old=dernierChar;
    int i=inputStream.read();
    if ((i==-1)) fin=true;
    dernierChar=removeAccent((char)i);
    return old;
  }

  public char voirCaractere()throws IOException{
    return dernierChar;
  }
}




