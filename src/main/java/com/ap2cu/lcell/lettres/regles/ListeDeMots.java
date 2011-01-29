package com.ap2cu.lcell.lettres.regles;

import java.util.ArrayList;
import java.util.Collections;

import com.ap2cu.lcell.lettres.dico.ComparateurDeMots;
import com.ap2cu.lcell.lettres.dico.Ordre;
import com.ap2cu.lcell.lettres.solution.ListeDeSolutionsLettres;
import com.ap2cu.lcell.lettres.solution.SolutionLettres;

public class ListeDeMots extends ArrayList<String> {

  private static final long serialVersionUID = 1L;

  public void trier() {
    ComparateurDeMots comparateur = new ComparateurDeMots(Ordre.DESCENDANT);
    Collections.sort(this, comparateur);
  }

  public ListeDeMots recupererMotsAvecLeMaximumDeLettres() {
    ListeDeMots liste = new ListeDeMots();
    trier();
    if (size() == 0)
      return liste;
    int n = get(0).length();
    for (String s : this)
      if (s.length() < n)
        break;
      else
        liste.add(s);
    return liste;
  }

  public ListeDeMots recupererMotsDe_X_Lettres(int x) {
    ListeDeMots liste = new ListeDeMots();
    for (String s : this)
      if (s.length() == x)
        liste.add(s);
    return liste;
  }
  
  public ListeDeSolutionsLettres toListeDeSolutionLettres() {
    ListeDeSolutionsLettres l = new ListeDeSolutionsLettres();
    for (String s : this)
      l.add(new SolutionLettres(s));
    return l;
  }
}
