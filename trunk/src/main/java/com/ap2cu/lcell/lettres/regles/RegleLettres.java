package com.ap2cu.lcell.lettres.regles;

import java.util.List;

import org.dom4j.Element;

import com.ap2cu.lcell.Points;
import com.ap2cu.lcell.Regle;
import com.ap2cu.lcell.Solution;
import com.ap2cu.lcell.Tirage;
import com.ap2cu.lcell.exception.ConfigurationException;
import com.ap2cu.lcell.lettres.solution.SolutionLettres;
import com.ap2cu.lcell.lettres.tirage.TirageLettres;

public class RegleLettres extends Regle {

  protected final static String VOYELLE = "voyelle";
  protected final static String CONSONNE = "consonne";
  protected ListeDeLettres consonnesDisponibles;
  protected ListeDeLettres voyellesDisponibles;
  protected int nombreDeLettres;
  protected int nombreDePointsParLettres;
  protected static TirageLettres tirageLettres;

  public RegleLettres() throws ConfigurationException {
    super(RegleLettres.class.getResource("config/regles.xml"));
  }

  public TirageLettres genererTirage() {
    if (tirageLettres != null) {
      for (char c : tirageLettres.getLettres().toUpperCase().toCharArray())
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U' || c == 'Y') {
          voyellesDisponibles.ajouterLettre(c);
        }
        else {
          consonnesDisponibles.ajouterLettre(c);
        }
    }
    String s = "";
    for (int i = 0; i < nombreDeLettres;) {
      s += consonnesDisponibles.getLettre(Tirage.random.nextInt(consonnesDisponibles.size()));
      i++;
      if (i < nombreDeLettres) {
        s += voyellesDisponibles.getLettre(Tirage.random.nextInt(voyellesDisponibles.size()));
        i++;
      }
    }
    tirageLettres = new TirageLettres(s);
    return tirageLettres;
  }

  @Override
  public Points attribuerNombreDePoints(Solution solution, Tirage tirage) {
    try {
      if(solution == null || solution.estVide())
        return new Points(0, "Vous n'avez rien proposé");
      tirage.verifierSolutionProposee(solution);
      int nombreDeLettres = ((SolutionLettres) solution).getMot().length();
      return new Points(nombreDePointsParLettres*nombreDeLettres, "Mot de "+nombreDeLettres+" lettres");
    }
    catch(Exception e) {
      return new Points(0, e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void chargerDonneesInitiales() throws ConfigurationException {
    this.nombreDeLettres = Integer.parseInt(configuration.element("lettres").attributeValue("nb"));
    this.nombreDePointsParLettres = Integer.parseInt(configuration.element("points").elementText("points-par-lettre"));
    this.consonnesDisponibles = new ListeDeLettres();
    this.voyellesDisponibles = new ListeDeLettres();
    List<Element> lettresElements = configuration.element("lettres").elements("lettre");
    for (Element lettreElement : lettresElements) {
      int nombreMaximumDOccurences = Integer.parseInt(lettreElement.attributeValue("max"));
      String type = lettreElement.attributeValue("type");
      char caractere = lettreElement.getStringValue().charAt(0);
      if (VOYELLE.equalsIgnoreCase(type)) {
        for (int i = 0; i < nombreMaximumDOccurences; i++)
          voyellesDisponibles.ajouterLettre(caractere);
      }
      else if (CONSONNE.equalsIgnoreCase(type)) {
        for (int i = 0; i < nombreMaximumDOccurences; i++)
          consonnesDisponibles.ajouterLettre(caractere);
      }
      else {
        throw new ConfigurationException("La lettre " + lettreElement + " est de type inconnu");
      }
    }
  }
}
