package com.ap2cu.lcell.exception;

public class DictionnaireException extends Exception {

  private static final long serialVersionUID = 1L;

  public DictionnaireException() {
    super();
  }
  
  public DictionnaireException(String message) {
    super(message);
  }
  
  public DictionnaireException(Exception exception) {
    super(exception);
  }
  
  public DictionnaireException(String message, Exception exception) {
    super(message, exception);
  }
}
