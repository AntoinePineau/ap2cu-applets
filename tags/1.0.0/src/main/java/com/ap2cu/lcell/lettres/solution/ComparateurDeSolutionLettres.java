package com.ap2cu.lcell.lettres.solution;

import java.util.Comparator;


public class ComparateurDeSolutionLettres implements Comparator<SolutionLettres> {

	public final static int SUPERIEUR = 1;
	public final static int EGAL = 0;
	public final static int INFERIEUR = -1;
	
	public int compare(SolutionLettres solution1, SolutionLettres solution2) {
		if(solution1.getMot().length()<solution2.getMot().length()) return INFERIEUR;
		if(solution1.getMot().length()>solution2.getMot().length()) return SUPERIEUR;
		return solution1.getMot().compareTo(solution2.getMot());
	}

}
