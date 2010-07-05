package com.ap2cu.lcell.lettres.dico;

import java.util.Comparator;

public class ComparateurDeMots implements Comparator<String> {

  private final static int SUPERIEUR = 1;
  private final static int INFERIEUR = -1;
	
	private Ordre ordre;
	
	public ComparateurDeMots(Ordre ordre) {
    this.ordre = ordre;
  }
	
	public int compare(String mot1, String mot2) {
		if(mot1.length()<mot2.length()) return ordre == Ordre.ASCENDANT ? INFERIEUR : SUPERIEUR;
		if(mot1.length()>mot2.length()) return ordre == Ordre.ASCENDANT ? SUPERIEUR : INFERIEUR;
		return ordre == Ordre.ASCENDANT ? mot1.compareTo(mot2) : mot2.compareTo(mot1);
	}

}
