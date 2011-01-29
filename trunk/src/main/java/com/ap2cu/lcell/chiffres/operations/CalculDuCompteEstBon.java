package com.ap2cu.lcell.chiffres.operations;

import java.util.List;
import java.util.Vector;

import com.ap2cu.lcell.chiffres.solution.SolutionChiffres;

public final class CalculDuCompteEstBon {

  private int plaque[][]; // tableau pour traiter les plaques
  private int ecart; // ecart entre le resultat en cours et a trouver au cas ou
  // le compte est impossible
  private int resultatAAtteindre; // resultat a trouver
  private SolutionChiffres solutionTemporaire; // sauvegarde
  // de la
  // solution
  // en
  // cours
  private SolutionChiffres solutionApprochee;
  private Vector<SolutionChiffres> solutions; // vecteur contenant les solutions

  public CalculDuCompteEstBon(List<Integer> chiffres, int resultatAAtteindre) {
    this.resultatAAtteindre = resultatAAtteindre;
    this.plaque = new int[chiffres.size()][chiffres.size()];
    this.solutions = new Vector<SolutionChiffres>();
    this.ecart = Integer.MAX_VALUE;
    for (int i = 0; i < chiffres.size(); i++)
      plaque[chiffres.size() - 1][i] = chiffres.get(i);
  }

  public Vector<SolutionChiffres> trouverSolutions() {
    solutions.clear();
    solutionTemporaire = new SolutionChiffres();
    solutionApprochee = new SolutionChiffres();
    trouverSolutions(plaque.length - 1);
    if(solutions.isEmpty())
      solutions.add(solutionApprochee);
    return solutions;
  }

  private void trouverSolutions(int niveau) {
    boolean resultatTrouve = false;
    /* comparaison de resultat */
//    if (plaque[niveau][0] == resultatAAtteindre) {
    if (solutionTemporaire.getResultat() == resultatAAtteindre) {
      resultatTrouve = true;
      SolutionChiffres rr = solutionTemporaire.copier();

      // si aucune solution creation.
      if (solutions.size() == 0) {
        ajouterSolution(solutionTemporaire);
      }
      else {
        boolean ajout = false;
        for (int i = 0; i < solutions.size(); i++) {
          if (rr.compareTo(solutions.elementAt(i)) == 0) { // doit remplacer
                                                           // l'existant cas des
                                                           // solutions avec
            // operations inutiles
            if (rr.nombreDeLignes() >= solutions.elementAt(i).nombreDeLignes()) {
              solutions.removeElementAt(i);
              i--;
              ajout = true;
            }
            else {
              rr = solutions.elementAt(i).copier();
              i = 0;
            }
          }
          else {
            ajout = true;
          }
        }
        if (ajout) {
          ajouterSolution(rr);
        }
      }
    }
    else {
      if (Math.abs(solutionTemporaire.getResultat() - resultatAAtteindre) < ecart) {
        ecart = Math.abs(solutionTemporaire.getResultat() - resultatAAtteindre);
        solutionApprochee = solutionTemporaire.copier();
      }
    }
    if (resultatTrouve)
      return;

    // si nous sommes au niveau 0 c'est que la solution n'a pas ete trouvee
    if (niveau == 0) {
      return;
    }

    /*
     * boucle for pour traiter toutes les combinaisons par niveau pour une
     * rangee de plaque si des plaques sont identiques on traite plusieurs fois
     * les meme combinaiasons cette boucle a pour but de ne traiter par la suite
     * que les combinaisons interessantes a traiter, elle ralentit le traitement
     * de la boucle lorsque toutes les plaques sont differentes mais l'accelere
     * enormement lorsqu'elles sont identiques. elle a aussi l'avantage de
     * filtrer une premiere fois les solutions en eliminant les solutions
     * identiques
     */

    int combinaison[][] = new int[2][16];
    for (int a = 0; a <= (niveau - 1); a++) {
      for (int b = a + 1; b <= niveau; b++) {
        int c = 0;
        boolean existe = false;
        while (combinaison[0][c] != 0) {
          if ((combinaison[0][c] == plaque[niveau][a] & combinaison[1][c] == plaque[niveau][b]) || (combinaison[1][c] == plaque[niveau][a] & combinaison[0][c] == plaque[niveau][b]))
            existe = true;
          c++;
        }
        if (!existe) {
          if (plaque[niveau][a] > plaque[niveau][b]) {
            combinaison[0][c] = plaque[niveau][a];
            combinaison[1][c] = plaque[niveau][b];
          }
          else {
            combinaison[0][c] = plaque[niveau][b];
            combinaison[1][c] = plaque[niveau][a];
          }
        }
      }
    }

    // Debut du traitement de toutes les combinaisons //
    int compt = 0;
    while (combinaison[0][compt] != 0) {
      /*
       * on choisit deux plaques qui vous nous servir pour les 4
       * CalculDuCompteEstBonDuCompteEstBons possibles
       */
      int chiffre1 = combinaison[0][compt];
      int chiffre2 = combinaison[1][compt];
      /*
       * je rempli le tableau du niveau inferieur avec les plaques restantes le
       * resultat de l'operation y sera insere en premiere position par la suite
       */
      int pointeur = 1;
      boolean p_a = true;
      boolean p_b = true;
      for (int z = 0; z <= niveau; z++) {
        if (plaque[niveau][z] == chiffre1 && p_a) {
          p_a = false;
          continue;
        }
        if (plaque[niveau][z] == chiffre2 && p_b) {
          p_b = false;
          continue;
        }
        plaque[(niveau - 1)][pointeur] = plaque[niveau][z];
        pointeur++;
      }
      /*
       * maintenant on traite toutes les operations possible sur les 2 plaques
       * selectionnees precedement
       */
      // ADDITION
      testerOperation(niveau, chiffre1, Operation.ADDITION, chiffre2);

      // SOUSTRACTION
      if (chiffre1 > chiffre2) {
        testerOperation(niveau, chiffre1, Operation.SOUSTRACTION, chiffre2);
      }
      if (chiffre2 > chiffre1) {
        testerOperation(niveau, chiffre2, Operation.SOUSTRACTION, chiffre1);
      }
      // MULTIPLICATION l'operation est inutile si p1 ou p2 est egale a 1
      if (!(chiffre1 == 1) & !(chiffre2 == 1)) {
        testerOperation(niveau, chiffre1, Operation.MULTPLICATION, chiffre2);
        // DIVISION
        if (chiffre1 % chiffre2 == 0) {
          testerOperation(niveau, chiffre1, Operation.DIVISION, chiffre2);
        }
        if (chiffre2 % chiffre1 == 0) {
          testerOperation(niveau, chiffre2, Operation.DIVISION, chiffre1);
        }
      }
      compt++;
    }
  }

  private void testerOperation(int niveau, int operande1, Operation operation, int operande2) {
    LigneDeCalcul ligneTemporaire = solutionTemporaire.ajouterLigne(operande1, operation, operande2);
    plaque[niveau - 1][0] = ligneTemporaire.getResultat();
    trouverSolutions(niveau - 1);
    solutionTemporaire.supprimerLigne(ligneTemporaire);
  }

  private void ajouterSolution(SolutionChiffres solution) {
    solutions.addElement(solution);
  }
}
