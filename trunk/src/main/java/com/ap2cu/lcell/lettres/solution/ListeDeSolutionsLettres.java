package com.ap2cu.lcell.lettres.solution;

import java.util.Collections;
import java.util.Vector;


public class ListeDeSolutionsLettres extends Vector<SolutionLettres> {

  private static final long serialVersionUID = 1L;

  public void trier() {
    ComparateurDeSolutionLettres comparateur = new ComparateurDeSolutionLettres();
    Collections.sort(this, comparateur);
  }

  public ListeDeSolutionsLettres recupererMotsAvecLeMaximumDeLettres() {
    ListeDeSolutionsLettres liste = new ListeDeSolutionsLettres();
    trier();
    if (size() == 0)
      return liste;
    int n = get(0).getMot().length();
    for (SolutionLettres s : this)
      if (s.getMot().length() < n)
        break;
      else
        liste.add(s);
    return liste;
  }

  public ListeDeSolutionsLettres recupererMotsDe_X_Lettres(int x) {
    ListeDeSolutionsLettres liste = new ListeDeSolutionsLettres();
    for (SolutionLettres s : this)
      if (s.getMot().length() == x)
        liste.add(s);
    return liste;
  }
}
