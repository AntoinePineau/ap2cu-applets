package com.ap2cu.lcell.chiffres.solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.ap2cu.lcell.Solution;
import com.ap2cu.lcell.chiffres.operations.LigneDeCalcul;
import com.ap2cu.lcell.chiffres.operations.Operation;
import com.ap2cu.lcell.exception.SolutionIncorrecteException;

/**
 * Un calcul qui contient des lignes de calcul.
 * 
 * @author Antoine PINEAU
 */
public class SolutionChiffres implements Solution {

  // ==================================================
  // A T T R I B U T S
  // ==================================================
  private List<LigneDeCalcul> lignes;

  // ==================================================
  // C O N S T R U C T E U R S
  // ==================================================

  public SolutionChiffres() {
    this(new LinkedList<LigneDeCalcul>());
  }

  protected SolutionChiffres(List<LigneDeCalcul> lignes) {
    this.lignes = lignes;
  }

  // ==================================================
  // M E T H O D E S
  // ==================================================

  public LigneDeCalcul ajouterLigne(LigneDeCalcul ligne) {
    lignes.add(ligne);
    return ligne;
  }
  
  public LigneDeCalcul ajouterLigne(int position, LigneDeCalcul ligne) {
    lignes.add(position, ligne);
    return ligne;
  }
  
  public LigneDeCalcul ajouterLigne(int operande1, Operation operation, int operande2) {
    return ajouterLigne(operande1, operation, operande2, operation.calculer(operande1, operande2));
  }
  
  public LigneDeCalcul ajouterLigne(int operande1, Operation operation, int operande2, int resultat) {
    LigneDeCalcul ligne = new LigneDeCalcul(operande1, operation, operande2, resultat);
    lignes.add(ligne);
    return ligne;
  }
  
  public void supprimerLigne(LigneDeCalcul ligne) {
    lignes.remove(ligne);
  }

  public List<LigneDeCalcul> getLignes() {
    return lignes;
  }

  public int nombreDeLignes() {
    return lignes.size();
  }

  public int getResultat() {
    return lignes.size() == 0 ? 0 : lignes.get(lignes.size() - 1).getResultat();
  }

  public SolutionChiffres copier() {
    Vector<LigneDeCalcul> v = new Vector<LigneDeCalcul>();
    for (LigneDeCalcul ligne : lignes) {
      v.add(new LigneDeCalcul(ligne.getOperande1(), ligne.getOperation(), ligne.getOperande2(), ligne.getResultat()));
    }
    return new SolutionChiffres(v);
  }

  public void verifier() throws SolutionIncorrecteException {
    for (LigneDeCalcul ligne : lignes) {
      if (!ligne.estCorrecte())
        throw new SolutionIncorrecteException("Il y a une erreur de calcul a la ligne " + ligne);
    }
  }

  public String toString() {
    StringBuffer s = new StringBuffer();
    for (LigneDeCalcul ligne : lignes)
      s.append(ligne).append(" |");
    int n = s.length();
    return n == 0 ? s.toString() : s.substring(0, n - 1);
  }

  @Override
  public int compareTo(Solution solution) {
    if (solution instanceof SolutionChiffres) {
      SolutionChiffres solutionChiffres = (SolutionChiffres) solution;
      if (nombreDeLignes() != solutionChiffres.nombreDeLignes())
        return 1;
      for (int i = 0; i < lignes.size(); i++) {
        if (lignes.get(i).compareTo(solutionChiffres.getLignes().get(i)) != 0)
          return 1;
      }
      return 0;
    }
    return 1;
  }

  @Override
  public boolean estVide() {
    return lignes == null || lignes.isEmpty();
  }

}
