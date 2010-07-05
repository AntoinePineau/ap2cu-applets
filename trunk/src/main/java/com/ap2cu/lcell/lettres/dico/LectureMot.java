package com.ap2cu.lcell.lettres.dico;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author PINEAU Antoine
 * @version 1.0
 */
public class LectureMot extends LectureLettre{
    String dernierMot;

    public LectureMot(InputStream f) throws IOException{
      super(f);
      dernierMot="";
      do{
        dernierMot+=dernierChar;
      }while (lireCaractereCorrect());
    }

    public String lireMot() throws IOException{
      String m=dernierMot;
      dernierMot="";
      while (!lireCaractereCorrect()){}
      do{
        dernierMot+=dernierChar;
      }while (lireCaractereCorrect());
      return m;
    }

    public String voirMot() throws IOException{
      return dernierMot;
    }


    public String lireMotEntier(int n)throws IOException{
      String m=lireMot();
      if(m.length()>n) return "#";
      for(int i=0; i<m.length();i++){
        if (m.charAt(i)=='-') return "#";
      }
      return m;
    }
}
