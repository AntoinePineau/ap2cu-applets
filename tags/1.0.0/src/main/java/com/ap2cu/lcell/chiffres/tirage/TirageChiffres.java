package com.ap2cu.lcell.chiffres.tirage;

import java.util.List;
import java.util.Vector;

import com.ap2cu.lcell.Regle;
import com.ap2cu.lcell.Solution;
import com.ap2cu.lcell.Tirage;
import com.ap2cu.lcell.chiffres.operations.CalculDuCompteEstBon;
import com.ap2cu.lcell.chiffres.operations.LigneDeCalcul;
import com.ap2cu.lcell.chiffres.solution.SolutionChiffres;
import com.ap2cu.lcell.exception.SolutionIncorrecteException;

public class TirageChiffres extends Tirage {

  // ==================================================
  // A T T R I B U T S
  // ==================================================

  public static final int NOMBRES_DE_CHIFFRES = 6;

  private List<Integer> chiffres;

  private int resultatAAtteindre;

  // ==================================================
  // C O N S T R U C T E U R
  // ==================================================

  public TirageChiffres(List<Integer> chiffres, int resultatAAtteindre) {
    this.chiffres = chiffres;
    this.resultatAAtteindre = resultatAAtteindre;
  }

  public TirageChiffres(Regle regle) {
    super(regle);
  }

  // ==================================================
  // M E T H O D E S
  // ==================================================

  public List<Integer> getChiffres() {
    return chiffres;
  }

  public int getResultatAAtteindre() {
    return resultatAAtteindre;
  }

  @Override
  public Solution donnerSolution() {
    CalculDuCompteEstBon calcul = new CalculDuCompteEstBon(getChiffres(), getResultatAAtteindre());
    return supprimerOperationsInutiles(calcul.trouverSolutions().get(0));
  }

  private SolutionChiffres supprimerOperationsInutiles(SolutionChiffres solutionANettoyer) {
    SolutionChiffres solutionNettoyee = new SolutionChiffres();
    supprimerOperationsInutiles(chiffres, solutionANettoyer.getResultat(), solutionANettoyer, solutionNettoyee);
    return solutionNettoyee;
  }

  private void supprimerOperationsInutiles(List<Integer> chiffres, int resultat, SolutionChiffres solutionANettoyer, SolutionChiffres solutionNettoyee) {
    for (int chiffre : chiffres)
      if (resultat == chiffre)
        return;

    for (LigneDeCalcul ligne : solutionANettoyer.getLignes())
      if (resultat == ligne.getResultat()) {
        solutionNettoyee.ajouterLigne(0, ligne);
        supprimerOperationsInutiles(chiffres, ligne.getOperande1(), solutionANettoyer, solutionNettoyee);
        supprimerOperationsInutiles(chiffres, ligne.getOperande2(), solutionANettoyer, solutionNettoyee);
      }
  }
  @Override
  public void verifierSolutionProposee(Solution solution) throws SolutionIncorrecteException {
    SolutionChiffres calcul = (SolutionChiffres) solution;
    // vérifier que ce calcul utilise des nombres autorisés
    Vector<Integer> nombresAutorises = new Vector<Integer>();
    for (int i : chiffres)
      nombresAutorises.add(i);

    for (LigneDeCalcul ligne : calcul.getLignes()) {
      Vector<Integer> v = new Vector<Integer>();
      v.add(ligne.getOperande1());
      v.add(ligne.getOperande2());
      boolean op1_trouve = false;
      boolean op2_trouve = false;
      for (int i = 0; i < nombresAutorises.size(); i++) {
        int n = nombresAutorises.get(i);
        if (!op1_trouve && n == ligne.getOperande1()) {
          op1_trouve = true;
          nombresAutorises.remove(i);
          i--;
        }
        else if (!op2_trouve && n == ligne.getOperande2()) {
          op2_trouve = true;
          nombresAutorises.remove(i);
          i--;
        }
        if (op1_trouve && op2_trouve) {
          nombresAutorises.add(ligne.getResultat());
          break;
        }
      }
      if (!op1_trouve || !op2_trouve) {
        throw new SolutionIncorrecteException("Des nombres non-autorisées ont été choisis");
      }
    }

    // vérifier qu'il n'y a pas d'erreur de calcul
    calcul.verifier();
  }

  public void ajouterChiffre(int chiffre) {
    chiffres.add(chiffre);
  }

  public String toString() {
    String s = "            " + resultatAAtteindre + "\n| ";
    for (int i : chiffres)
      s += i + " | ";
    return s;
  }
}
