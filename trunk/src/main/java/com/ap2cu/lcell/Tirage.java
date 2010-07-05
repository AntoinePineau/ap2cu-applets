package com.ap2cu.lcell;

import java.util.HashMap;
import java.util.Random;

import com.ap2cu.lcell.exception.SolutionIncorrecteException;

public abstract class Tirage {

  public static Random random = new Random();

  protected static HashMap<Regle, Tirage> tirages;

  protected Regle regle;

  public Tirage() {
  // TODO: TO BE IMPLEMENTED
  }

  protected Tirage(Regle regle) {
    this.regle = regle;
  }


  protected Tirage nouveauTirage() {
    // TODO: TO BE IMPLEMENTED
    return null;
  }

  public Solution donnerSolution() {
    // TODO: TO BE IMPLEMENTED
    return null;
  }

  public void verifierSolutionProposee(Solution solution) throws SolutionIncorrecteException {}
}
