package com.ap2cu.lcell;

public class Points {

  private int nombreDePoints;
  private String justification;
  
  public Points(int nombreDePoints, String justification) {
    this.nombreDePoints = nombreDePoints;
    this.justification = justification;
  }
  
  public int getNombreDePoints() {
    return nombreDePoints;
  }
  
  public String getJustification() {
    return justification;
  }
  
  @Override
  public String toString() {
    return new StringBuffer(" => ").append(nombreDePoints).append(" points (").append(justification).append(")").toString();
  }
}
