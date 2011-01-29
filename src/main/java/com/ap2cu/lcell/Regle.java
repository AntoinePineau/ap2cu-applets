package com.ap2cu.lcell;

import java.net.URL;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ap2cu.lcell.exception.ConfigurationException;
import com.ap2cu.lcell.exception.SolutionIncorrecteException;

public abstract class Regle {

  protected Element configuration;
  
  protected Regle(URL url) throws ConfigurationException {
    try {
      configuration = new SAXReader().read(url).getRootElement();
      chargerDonneesInitiales(); 
    }
    catch(Exception e) {
      System.out.println(e.getMessage());
      throw new ConfigurationException("Erreur pendant le chargement des regles "+url, e);
    }
  }
  
  protected abstract void chargerDonneesInitiales() throws ConfigurationException;
  
  public abstract Points attribuerNombreDePoints(Solution solution, Tirage tirage) throws SolutionIncorrecteException;
  
}
