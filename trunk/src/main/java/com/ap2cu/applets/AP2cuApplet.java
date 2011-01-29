package com.ap2cu.applets;

import java.applet.Applet;
import java.util.List;

import com.ap2cu.gui.CommandLine;
import com.ap2cu.lcell.chiffres.operations.LigneDeCalcul;
import com.ap2cu.lcell.chiffres.regles.RegleChiffres;
import com.ap2cu.lcell.chiffres.solution.SolutionChiffres;
import com.ap2cu.lcell.chiffres.tirage.TirageChiffres;
import com.ap2cu.lcell.exception.ConfigurationException;
import com.ap2cu.lcell.lettres.dico.Dictionnaire;
import com.ap2cu.lcell.lettres.regles.ListeDeMots;
import com.ap2cu.lcell.lettres.regles.RegleLettres;
import com.ap2cu.lcell.lettres.solution.SolutionLettres;
import com.ap2cu.lcell.lettres.tirage.TirageLettres;
import com.ap2cu.postget.Form;
import com.ap2cu.xml.XMLUtils;

public class AP2cuApplet extends Applet {

  private static final long serialVersionUID = 1L;
  private static RegleChiffres regleChiffres;
  private static RegleLettres regleLettres;
  
  public Form getForm(String action) {
    return new Form(action);
  }

  public com.ap2cu.primality.Number getNumber(long l) {
    return new com.ap2cu.primality.Number(l);
  }
  
  public XMLUtils getXMLUtils() {
    return XMLUtils.getXMLUtils();
  }

  public RegleChiffres obtenirLesReglesDuCompteEstBon() throws ConfigurationException {
    if (regleChiffres == null)
      regleChiffres = new RegleChiffres();
    return regleChiffres;
  }

  public RegleLettres obtenirLesReglesDuMotLePlusLong() throws ConfigurationException {
    if (regleLettres == null)
      regleLettres = new RegleLettres();
    return regleLettres;
  }
  
  public SolutionChiffres creerSolutionPourLeCompteEstBon() {
    return new SolutionChiffres();
  }
  
  public SolutionLettres creerSolutionPourLeMotLePlusLong(String mot) {
    return new SolutionLettres(mot);
  }

  public TirageChiffres creerTirageChiffres(List<Integer> chiffres, int resultatAAtteindre) {
    return new TirageChiffres(chiffres, resultatAAtteindre);
  }
  
  public TirageLettres creerTirageLettres(String mot) {
    return new TirageLettres(mot);
  }
  
  public LigneDeCalcul creerLigne(String ligne) {
    return LigneDeCalcul.parse(ligne);
  }

  public void log(String message) {
    System.out.println(message);
  }

  public static void main(String[] args) throws Exception {
    AP2cuApplet ap2cu = new AP2cuApplet();
    System.out.println("  LATINE  existe ? "+Dictionnaire.getDictionnaire().existe("LATINE"));
    System.out.println("  LATINES existe ? "+Dictionnaire.getDictionnaire().existe("LATINES"));
    ListeDeMots l = ap2cu.creerTirageLettres("LATINESA").trouverTousLesMots();
    System.out.println("Mots de 6 lettres: "+l.recupererMotsDe_X_Lettres(6));
    System.out.println("Mots de 7 lettres: "+l.recupererMotsDe_X_Lettres(7));
//    testerLeCompteEstBon(ap2cu);
//    testerLeMotLePlusLong(ap2cu);
  }
  
  protected static void testerLeCompteEstBon(AP2cuApplet ap2cu) throws ConfigurationException {
    RegleChiffres regleChiffres = ap2cu.obtenirLesReglesDuCompteEstBon();
    try {
      TirageChiffres tirageChiffres = regleChiffres.genererTirage();
      System.out.println(tirageChiffres.getResultatAAtteindre());
      System.out.println(tirageChiffres.getChiffres());
      SolutionChiffres solutionChiffres = ap2cu.creerSolutionPourLeCompteEstBon();
      String in = ">";
      while(!in.equals("")) {
        System.out.print("\n>");
        in = CommandLine.readUserTyping();
        if(in.equals(""))
          break;
        LigneDeCalcul ligne = LigneDeCalcul.parse(in);
        solutionChiffres.ajouterLigne(ligne);
      }
      System.out.print(solutionChiffres);
      System.out.println(regleChiffres.attribuerNombreDePoints(solutionChiffres, tirageChiffres));
      System.out.print("Une solution de l'ordinateur: ");
      System.out.println(tirageChiffres.donnerSolution());
    }
    catch(Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }
  
  protected static void testerLeMotLePlusLong(AP2cuApplet ap2cu) throws ConfigurationException {
    RegleLettres regleLettres = ap2cu.obtenirLesReglesDuMotLePlusLong();
    try {
      TirageLettres tirageLettres = regleLettres.genererTirage();
      System.out.println(tirageLettres.getLettres());
      System.out.print("\n>");
      SolutionLettres solutionLettres = ap2cu.creerSolutionPourLeMotLePlusLong(CommandLine.readUserTyping());
      System.out.println(regleLettres.attribuerNombreDePoints(solutionLettres, tirageLettres));
      System.out.print("Une solution de l'ordinateur: ");
      System.out.println(tirageLettres.donnerSolution());
    }
    catch(Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }
}
