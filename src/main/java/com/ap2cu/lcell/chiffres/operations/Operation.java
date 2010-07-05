package com.ap2cu.lcell.chiffres.operations;

import java.util.Vector;

public abstract class Operation {

  protected final static char ADDITION_CHAR = '+';
  protected final static char SOUSTRACTION_CHAR = '-';
  protected final static char MULTIPLICATION_CHAR = '*';
  protected final static char DIVISION_CHAR = '/';

  protected char operationChar;

  static class Addition extends Operation {
    protected Addition() {
      super(ADDITION_CHAR);
    }

    @Override
    public int calculer(int operande1, int operande2) {
      return operande1+operande2;
    } 
  }

  static class Soustraction extends Operation {
    protected Soustraction() {
      super(SOUSTRACTION_CHAR);
    } 

    @Override
    public int calculer(int operande1, int operande2) {
      return operande1-operande2;
    } 
  }

  static class Multiplication extends Operation {
    protected Multiplication() {
      super(MULTIPLICATION_CHAR);
    } 

    @Override
    public int calculer(int operande1, int operande2) {
      return operande1*operande2;
    } 
  }

  static class Division extends Operation {
    protected Division() {
      super(DIVISION_CHAR);
    } 

    @Override
    public int calculer(int operande1, int operande2) {
      return operande1/operande2;
    } 
  }
  
  public final static Addition ADDITION = new Addition();
  public final static Soustraction SOUSTRACTION = new Soustraction();
  public final static Multiplication MULTPLICATION = new Multiplication();
  public final static Division DIVISION = new Division();


  public Operation(char operation_char) {
    this.operationChar = operation_char;
  }

  public char getOperationChar() {
    return operationChar;
  }

  public static Operation parse(char c) {
    if (c == ADDITION_CHAR)
      return ADDITION;
    if (c == SOUSTRACTION_CHAR)
      return SOUSTRACTION;
    if (c == MULTIPLICATION_CHAR)
      return MULTPLICATION;
    if (c == DIVISION_CHAR)
      return DIVISION;
    return null;
  }

  public static Vector<Operation> getAllOperations() {
    Vector<Operation> v = new Vector<Operation>();
    v.add(ADDITION);
    v.add(SOUSTRACTION);
    v.add(MULTPLICATION);
    v.add(DIVISION);
    return v;
  }

  public String toString() {
    return operationChar + "";
  }
  
  public abstract int calculer(int operande1, int operande2);

}
