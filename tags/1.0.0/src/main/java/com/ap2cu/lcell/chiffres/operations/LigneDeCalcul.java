package com.ap2cu.lcell.chiffres.operations;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;


public class LigneDeCalcul implements Comparable<LigneDeCalcul> {

  private int operande1;

  private Operation operation;

  private int operande2;

  private int resultat;

  public LigneDeCalcul(int operande1, Operation operation, int operande2, int resultat) {
    this.operande1 = operande1;
    this.operation = operation;
    this.operande2 = operande2;
    this.resultat = resultat;
  }

  public LigneDeCalcul() {
    this(0, null, 0, 0);
  }

  public int getOperande1() {
    return operande1;
  }

  public int getOperande2() {
    return operande2;
  }

  public Operation getOperation() {
    return operation;
  }

  public int getResultat() {
    return resultat;
  }

  public void setOperande1(int operande1) {
    this.operande1 = operande1;
  }

  public void setOperande2(int operande2) {
    this.operande2 = operande2;
  }

  public void setOperation(Operation operation) {
    this.operation = operation;
  }

  public void setResultat(int resultat) {
    this.resultat = resultat;
  }

  public boolean estCorrecte() {
    if(operation == Operation.ADDITION)
      return operande1 + operande2 == resultat;
    else if(operation == Operation.SOUSTRACTION)
      return operande1 - operande2 == resultat;
    else if(operation == Operation.MULTPLICATION)
      return operande1 * operande2 == resultat;
    else if(operation == Operation.DIVISION)
      return operande2 == 0 ? false : operande1 / operande2 == resultat;
    return false;
  }

  public void ajouterOperande(String op) {
    if(operande1 == 0) {
      operande1 = Integer.parseInt(op);
      return;
    }
    if(operation == null) {
      operation = Operation.parse(op.charAt(0));
      return;
    }
    if(operande2 == 0) {
      operande2 = Integer.parseInt(op);
      return;
    }
    if(resultat == 0) {
      resultat = Integer.parseInt(op);
      return;
    }
  }

  public boolean isTerminee() {
    return resultat != 0;
  }
  
  public boolean isVide() {
    return operande1 == 0 && operande2 == 0 && resultat ==0 && operation == null;
  }

  public String toString() {
    return " "+operande1 + operation + operande2 + "=" + resultat;
  }

  public static LigneDeCalcul parse(String ligne)
      throws NoSuchElementException, NumberFormatException {
    StringTokenizer st = new StringTokenizer(ligne, "+-*/=");

    int op1 = Integer.parseInt(st.nextToken());
    char op = ligne.charAt(ligne.indexOf(op1 + "") + (op1 + "").length());
    int op2 = Integer.parseInt(st.nextToken());
    int res = Integer.parseInt(st.nextToken());

    return new LigneDeCalcul(op1, Operation.parse(op), op2, res);
  }

  @Override
  public int compareTo(LigneDeCalcul ligne) {
    if(ligne.getOperande1() == operande1 && ligne.getOperande2() == operande2 && ligne.getOperation() == operation && ligne.getResultat() == resultat)
      return 0;
    return 1;
  }
}
