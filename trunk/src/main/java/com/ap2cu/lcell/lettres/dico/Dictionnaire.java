package com.ap2cu.lcell.lettres.dico;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;

import com.ap2cu.lcell.exception.DictionnaireException;

/**
 * @author PINEAU Antoine
 * @version 1.0
 */
public class Dictionnaire {

  // ==================================================
  // A T T R I B U T S
  // ==================================================

  private int nbr_lettres;

  private ListeLexicale debut;

  private boolean modifie = false;

  private String fichierDictionnaire;

  private boolean nomPropresAutorises;

  private static HashMap<Locale, Dictionnaire> dictionnaires = new HashMap<Locale, Dictionnaire>();
  
  // ==================================================
  // C O N S T R U C T E U R S
  // ==================================================

  public static Dictionnaire getDictionnaire(Locale locale) throws DictionnaireException {
    Dictionnaire d = dictionnaires.get(locale);
    if(d==null) {
      d = new Dictionnaire(locale, false);
      dictionnaires.put(locale, d);
    }
    return d;
  }
  
  public static Dictionnaire getDictionnaire() throws DictionnaireException {
    return getDictionnaire(Locale.FRANCE);
  }
  
  public Dictionnaire(Locale locale, boolean autoriserNomsPropres) throws DictionnaireException {
    this("config/" + locale.getLanguage() + ".dico", 9, autoriserNomsPropres);
  }

  protected Dictionnaire(String filename, int nbr_lettres, boolean autoriserNomsPropres) throws DictionnaireException {
    try {
      InputStream inputStream = getClass().getResourceAsStream(filename);
      this.fichierDictionnaire = filename;
      this.nbr_lettres = nbr_lettres;
      this.debut = null;
      this.nomPropresAutorises = autoriserNomsPropres;
      LectureLettre dico = new LectureLettre(inputStream);
      debut = charger(dico);
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new DictionnaireException("Dictionnaire " + filename + " could not be read", e);
    }
  }

  // ==================================================
  // M E T H O D E S
  // ==================================================

  public ListeLexicale getDebut() {
    return debut;
  }

  public boolean isModifie() {
    return this.modifie;
  }

  private ListeLexicale charger(LectureLettre dico) throws IOException {
    char c = dico.lireCaractere();
    if (c == '/')
      return null;
    switch (dico.voirCaractere()) {
      case '*':
        dico.lireCaractere();
        return new ListeLexicale(c, true, null, null);

      case '$':
        dico.lireCaractere();
        return new ListeLexicale(c, true, charger(dico), charger(dico));

      default:
        return new ListeLexicale(c, false, charger(dico), charger(dico));
    }
  }

  public boolean existe(String word) {
    if (debut == null)
      return false;
    else
      return existe(word, 0, debut);
  }

  private boolean existe(String word, int cursor, ListeLexicale position) {
    if ((word.charAt(cursor) == position.getC()) || (position.getC() == word.charAt(cursor) && cursor == 0)) {
      if (word.length() - 1 == cursor)
        return position.getFinDeMot();
      if (position.getLettreSuivante() != null)
        return existe(word, cursor + 1, position.getLettreSuivante());
    }
    else {
      if ((position.getLettreRemplacante() != null))
        return existe(word, cursor, position.getLettreRemplacante());
    }
    return false;
  }

  public void insert(String word) {
    modifie = true;
    if (debut == null)
      debut = new ListeLexicale(word.charAt(0));
    insert(word, 0, debut);
  }

  private void insert(String word, int cursor, ListeLexicale position) {
    if (word.charAt(cursor) == position.getC()) {
      if (word.length() - 1 == cursor)
        position.setFinDeMot(true);
      else {
        if (position.getLettreSuivante() == null)
          position.setLettreSuivante(new ListeLexicale(word.charAt(cursor + 1)));
        insert(word, cursor + 1, position.getLettreSuivante());
      }
    }
    else {
      if (position.getLettreRemplacante() == null)
        position.setLettreRemplacante(new ListeLexicale(word.charAt(cursor)));
      insert(word, cursor, position.getLettreRemplacante());
    }
  }

  public void ajouterFichier(InputStream f) {
    try {
      LectureMot lecture = new LectureMot(f);
      String m;
      while (!lecture.getFin()) {
        m = lecture.lireMotEntier(nbr_lettres);
        if (!m.equals("#")) {
          if (nomPropresAutorises || ((m.charAt(0) <= 'Z') && (m.charAt(0) >= 'A')))
            insert(m);
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deserialiser() throws DictionnaireException {
    try {
      FileWriter c = new FileWriter(fichierDictionnaire);
      debut.deserialiser(c);
      c.close();
    }
    catch (IOException e) {
      throw new DictionnaireException("Error while writing the output file");
    }
  }

  public void reset() {
    debut = null;
  }

  public void print() {
    if (debut != null)
      print("", debut);
  }

  private void print(String pre, ListeLexicale c) {
    if (c.getFinDeMot()) {
      System.out.println(pre + c.getC());
    }
    if (c.getLettreSuivante() != null) {
      print(pre + c.getC(), c.getLettreSuivante());
    }
    if (c.getLettreRemplacante() != null) {
      print(pre, c.getLettreRemplacante());
    }
  }

  public void sauver() throws DictionnaireException {
    if (debut != null) {
      try {
        FileWriter c = new FileWriter(fichierDictionnaire);
        debut.sauver(c);
        c.close();
      }
      catch (IOException e) {
        throw new DictionnaireException("Le dictionnaire n'a pas pu être sauvé dans " + fichierDictionnaire, e);
      }
      modifie = false;
    }
  }

  public void fusion(Dictionnaire dico) {
    if (debut == null)
      debut = dico.debut;
    else
      fusion(dico.debut, "");
  }

  private void fusion(ListeLexicale c, String pre) {
    if (c.getFinDeMot()) {
      insert(pre + c.getC());
    }
    if (c.getLettreSuivante() != null) {
      fusion(c.getLettreSuivante(), pre + c.getC());
    }
    if (c.getLettreRemplacante() != null) {
      fusion(c.getLettreRemplacante(), pre);
    }
  }

}
