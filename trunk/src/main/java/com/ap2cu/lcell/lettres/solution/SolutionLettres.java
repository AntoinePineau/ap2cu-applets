package com.ap2cu.lcell.lettres.solution;

import com.ap2cu.lcell.Solution;

public class SolutionLettres implements Solution {

  protected String mot;
  
  public SolutionLettres(String motPropose) {
    this.mot = motPropose;
  }
  
  public String getMot() {
    return mot;
  }

  @Override
  public int compareTo(Solution solution) {
    if(solution instanceof SolutionLettres) {
      SolutionLettres solutionLettres = (SolutionLettres)solution;
      if(mot!=null && mot.equals(solutionLettres.getMot()))
        return 0;
      return 1;        
    }
    return 1;
  }
  
  @Override
  public String toString() {
    return mot;
  }

  @Override
  public boolean estVide() {
    return mot == null || "".equals(mot);
  }
}
