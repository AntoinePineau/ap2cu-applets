package com.ap2cu.lcell.chiffres.regles;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

import com.ap2cu.lcell.Points;
import com.ap2cu.lcell.Regle;
import com.ap2cu.lcell.Solution;
import com.ap2cu.lcell.Tirage;
import com.ap2cu.lcell.chiffres.solution.SolutionChiffres;
import com.ap2cu.lcell.chiffres.tirage.TirageChiffres;
import com.ap2cu.lcell.exception.ConfigurationException;

public class RegleChiffres extends Regle {

  protected static TirageChiffres tirageChiffres;
  protected ListeDeChiffres listeDeChiffres;
  protected int nombreDeChiffres;
  protected int minimumPourLeCompteATrouver;
  protected int maximumPourLeCompteATrouver;
  protected int nombreDePointsPourLeBonCompte;
  protected int nombreDePointsPourUnCompteApproche;

  public RegleChiffres() throws ConfigurationException {
    super(RegleChiffres.class.getResource("config/regles.xml"));
  }

  public TirageChiffres genererTirage() {
    if (tirageChiffres != null) {
      for (int c : tirageChiffres.getChiffres()) {
        listeDeChiffres.ajouterChiffre(c);
      }
    }
    List<Integer> c = new LinkedList<Integer>();
    int p = 0;
    for (int i = 0; i < nombreDeChiffres; i++) {
      p = Tirage.random.nextInt(listeDeChiffres.size());
      c.add(listeDeChiffres.getChiffre(p));
    }
    while (p < minimumPourLeCompteATrouver) {
      p = Tirage.random.nextInt(maximumPourLeCompteATrouver);
    }
    tirageChiffres = new TirageChiffres(c, p);
    return tirageChiffres;
  }

  @Override
  public Points attribuerNombreDePoints(Solution solution, Tirage tirage) {
    try {
      if(solution == null || solution.estVide())
        return new Points(0, "Vous n'avez rien proposé");
      tirage.verifierSolutionProposee(solution);
      int resultat = ((SolutionChiffres) solution).getResultat();
      int resultatAAtteindre = ((TirageChiffres) tirage).getResultatAAtteindre();
      return resultat == resultatAAtteindre ? new Points(nombreDePointsPourLeBonCompte, "Le compte est bon") : new Points(nombreDePointsPourUnCompteApproche, "Compte approché");
    }
    catch (Exception e) {
     return new Points(0, e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void chargerDonneesInitiales() throws ConfigurationException {
    this.maximumPourLeCompteATrouver = Integer.parseInt(configuration.element("compte-a-trouver").attributeValue("max"));
    this.minimumPourLeCompteATrouver = Integer.parseInt(configuration.element("compte-a-trouver").attributeValue("min"));
    this.nombreDeChiffres = Integer.parseInt(configuration.element("chiffres").attributeValue("nb"));
    this.nombreDePointsPourLeBonCompte = Integer.parseInt(configuration.element("points").elementText("bon-compte"));
    this.nombreDePointsPourUnCompteApproche = Integer.parseInt(configuration.element("points").elementText("compte-approchant"));
    this.listeDeChiffres = new ListeDeChiffres();
    List<Element> chiffresElements = (List<Element>) configuration.element("chiffres").elements("chiffre");
    for (Element chiffreElement : chiffresElements) {
      int max = Integer.parseInt(chiffreElement.attributeValue("max"));
      int chiffre = Integer.parseInt(chiffreElement.getText());
      for (int i = 0; i < max; i++)
        listeDeChiffres.ajouterChiffre(chiffre);
    }
  }
}
