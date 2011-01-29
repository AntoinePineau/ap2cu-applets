package com.ap2cu.lcell.lettres.dico;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Une plaque pour g�rer le dictionnaire.
 * <p>Copyright: Copyright (c) 2004 CLOUET Xavier, MAS Romain, PASSAGOT Romain, PINEAU Antoine, REYNAUD Alexandre</p>
 *
 * This file is part of Des chiffres et des lettres en r�seau.
 *
 *  Des chiffres et des lettres en r�seau is free software; you can redistribute it and/or modify
 * it under the terms of the LGPL Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 *  Des chiffres et des lettres en r�seau is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LGPL Lesser General Public License for more details.
 *
 *  You should have received a copy of the LGPL Lesser General Public License
 * along with Des chiffres et des lettres en r�seau; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * @author CLOUET Xavier
 * @author MAS Romain
 * @author PASSAGOT Romain
 * @author PINEAU Antoine
 * @author REYNAUD Alexandre
 * @version 1.0
 */
public class ListeLexicale {
    private char c;
    private ListeLexicale lettreRemplacante;
    private ListeLexicale lettreSuivante;
    private boolean finDeMot;

    /** Constructor
     *@param c character of the ListeLexicale
     */
    public ListeLexicale(char c) {
        this(c,false,null,null);
    }

    /** Constructor
      *@param c character of the ListeLexicale
      *@param end boolean which specify whether this Charlist will be the end of a word or not
      */
    public ListeLexicale(char c, boolean end) {
        this(c,end,null,null);
    }

    /** Constructor
     *Create an instance of ListeLexicale and specify that this charList will be the end of a word
     *@param c character of the ListeLexicale
     *@param end boolean which specify whether this Charlist will be the end of a word or not
     *@param lettreSuivante next character of a word in the list
     *@param lettreRemplacante other character replacing this
     */
    public ListeLexicale(char c, boolean f, ListeLexicale lettreSuivante, ListeLexicale lettreRemplacante) {
        this.c=c;
        this.finDeMot = f;
        this.lettreSuivante = lettreSuivante;
        this.lettreRemplacante = lettreRemplacante;
    }

    /**
     *@return c the character of this ListeLexicale
     */
    public char getC(){
        return c;
    }

    /**
     *@return lettreRemplacante the following character in place of this in the list
     */
    public ListeLexicale getLettreRemplacante(){
        return lettreRemplacante;
    }

    /**
     *@return lettreSuivante the following character of a word
     */
    public ListeLexicale getLettreSuivante(){
        return lettreSuivante;
    }

    /**
     *@return finDeMot if this ListeLexicale is the end of a word
     */
    public boolean isFinDeMot(){
        return finDeMot;
    }

    /**
     *@param lettreRemplacante the following character in place of this in the list
     */
    public void setLettreRemplacante(ListeLexicale lettreRemplacante){
        this.lettreRemplacante=lettreRemplacante;
    }

    /**
     *@param lettreSuivante the following character of a word
     */
    public void setLettreSuivante(ListeLexicale lettreSuivante){
        this.lettreSuivante=lettreSuivante;
    }

    /**
     *@param finDeMot if this ListeLexicale is the end of a word
     */
    public void setFinDeMot(boolean end){
        finDeMot=end;
    }

    /**
     *Save a dictionary as a dictionary file
     *@param out fileWriter in which the dictionary will be saved
     *complexity: linear on the number of ListeLexicale instance of a dictionary
     *invarying: all of the ListeLexicales instance before this in the list had been written in the file
     *varying: (number of lettreRemplacantees after this, number of lettreSuivantes after this)
     */
    public void sauver(FileWriter out) throws IOException{
      out.write(LectureLettre.removeAccent(c));
      if (finDeMot) { // if this character is the end of a word
          if((lettreSuivante==null)&&(lettreRemplacante==null)) {//if this ListeLexicale points toward nothing
              out.write('*');
              return;
          }else
              out.write('$');
      }
      if (lettreSuivante==null)// if there is no lettreSuivante
          out.write('/');
      else
          lettreSuivante.sauver(out);
      if (lettreRemplacante==null)//if there is no lettreRemplacante
          out.write('/');
      else
          lettreRemplacante.sauver(out);
    }



    /**
     *Create a text file
     *@param out  fileWriter in which the list of words will be saved
      */
    public void deserialiser(FileWriter out) throws IOException{
        deserialiser(out,"");
    }

    /**
     *Create a text file
     *@param out fileWriter in which the list of words will be saved
     *@param pre prefix of the word being treated
     *complexity: linear on the number of ListeLexicale instances of a dictionary
     *invarying: words before pre in the dictionary had been written
     *varying: (number of lettreRemplacantees after this, number of lettreSuivantes after this)
     */
    private void deserialiser(FileWriter out,String pre) throws IOException{
        if (finDeMot){ //if the end of a word is reached the word is written in the output file
            out.write(pre+c+"\n");
        }
        if (lettreSuivante!=null){ // if there is a lettreSuivante
            lettreSuivante.deserialiser(out,pre+c);
        }
        if (lettreRemplacante!=null){// if there is a lettreRemplacante
            lettreRemplacante.deserialiser(out,pre);
        }
    }
}
