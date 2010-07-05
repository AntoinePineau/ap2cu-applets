package com.ap2cu.lcell.lettres.dico;

import java.util.Vector;

import com.ap2cu.lcell.lettres.regles.ListeDeMots;
import com.ap2cu.lcell.lettres.solution.SolutionLettres;

public final class CalculDuMotLePlusLong {

  private String lettres;
  private Dictionnaire dico;
  
  public CalculDuMotLePlusLong(String lettres, Dictionnaire dico) {
    this.lettres = lettres;
    this.dico = dico;
  }
  
  public Vector<SolutionLettres> trouverSolutions() {
    return trouverMots().toListeDeSolutionLettres();
  }
  


  public ListeDeMots trouverMots() {
    Vector<Character> v = new Vector<Character>();
    for (char c : lettres.toCharArray())
      v.add(c);
    ListeDeMots mots = new ListeDeMots();
    motLePlusLong(v, dico.getDebut(), "", mots);
    mots.trier();
    return mots;
  }


  public String motLePlusLong() {
    Vector<Character> v = new Vector<Character>();
    for (char c : lettres.toCharArray())
      v.add(c);
    return motLePlusLong(v, dico.getDebut(), "", new ListeDeMots()).toUpperCase();
  }
  

  @SuppressWarnings("unchecked")
  private String motLePlusLong(Vector<Character> m, ListeLexicale position, String s, ListeDeMots mots) {
    // il y a 3 moyens d'obtenir un résultat
    String res1 = "%";// si la lettre est contenue dans le vecteur
    String res2 = "%";// si elle n'est pas dans le vecteur
    String res3 = "%";// si le mot est terminé

    Vector<Character> m2 = (Vector<Character>) m.clone();

    char c = position.getC();
    for (int i = 0; i < m.size(); i++) {
      // si la lettre courante est dans le vector
      if (c == ((Character) m.get(i)).charValue()) {

        // si c'est une fin de mot
        if (position.getFinDeMot())
          res3 = "" + c;
        m2.remove(i);

        // si on peut encore aller à droite
        if (position.getLettreSuivante() != null) {
          String tmp = motLePlusLong(m2, position.getLettreSuivante(), s + c, mots);
          if (!tmp.equals("%")) {
            res1 = c + tmp;
          }
        }
        m2 = (Vector) m.clone();
      }
    }

    // si on peut encore descendre
    if (position.getLettreRemplacante() != null) {
      res2 = motLePlusLong(m, position.getLettreRemplacante(), s, mots);
      if (dico.existe(res2) && !mots.contains(res2))
        mots.add(res2);
    }

    // cas d'arrêt de la récursivité
    if (res1.equals("%")) {
      if (res2.equals("%")) {
        if (dico.existe(res3) && !mots.contains(res3))
          mots.add(res3);
        return res3;
      }
      if (res3.equals("%")) {
        if (dico.existe(res2) && !mots.contains(res2))
          mots.add(res2);
        return res2;
      }
      if (res3.length() > res2.length()) {
        if (dico.existe(res3) && !mots.contains(res3))
          mots.add(res3);
        return res3;
      }
      if (dico.existe(res2) && !mots.contains(res2))
        mots.add(res2);
      return res2;
    }
    else {
      if (res2.equals("%")) {
        if (res3.equals("%")) {
          if (dico.existe(res1) && !mots.contains(res1))
            mots.add(res1);
          return res1;
        }
        if (res3.length() > res1.length()) {
          if (dico.existe(res3) && !mots.contains(res3))
            mots.add(res3);
          return res3;
        }
        if (dico.existe(res1) && !mots.contains(res1))
          mots.add(res1);
        return res1;
      }
      else {
        if (res3.equals("%")) {
          if (res2.length() > res1.length()) {
            if (dico.existe(res2) && !mots.contains(res2))
              mots.add(res2);
            return res2;
          }
          if (dico.existe(res1) && !mots.contains(res1))
            mots.add(res1);
          return res1;
        }
        else {
          if (res3.length() > res1.length()) {
            if (res2.length() > res3.length()) {
              if (dico.existe(res2) && !mots.contains(res2))
                mots.add(res2);
              return res2;
            }
            if (dico.existe(res3) && !mots.contains(res3))
              mots.add(res3);
            return res3;
          }
          else {
            if (res2.length() > res1.length()) {
              if (dico.existe(res2) && !mots.contains(res2))
                mots.add(res2);
              return res2;
            }
            if (dico.existe(res1) && !mots.contains(res1))
              mots.add(res1);
            return res1;
          }
        }
      }
    }
  }

}
