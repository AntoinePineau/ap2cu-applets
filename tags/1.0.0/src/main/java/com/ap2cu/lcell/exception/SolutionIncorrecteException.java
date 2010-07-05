package com.ap2cu.lcell.exception;

public class SolutionIncorrecteException extends Exception {

  private static final long serialVersionUID = 1L;

  public SolutionIncorrecteException() {
    super();
  }
  
  public SolutionIncorrecteException(String message) {
    super(message);
  }
  
  public SolutionIncorrecteException(Exception exception) {
    super(exception);
  }
  
  public SolutionIncorrecteException(String message, Exception exception) {
    super(message, exception);
  }
}
