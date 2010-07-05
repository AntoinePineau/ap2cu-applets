package com.ap2cu.lcell.exception;

public class ConfigurationException extends Exception {

  private static final long serialVersionUID = 1L;

  public ConfigurationException() {
    super();
  }
  
  public ConfigurationException(String message) {
    super(message);
  }
  
  public ConfigurationException(Exception exception) {
    super(exception);
  }
  
  public ConfigurationException(String message, Exception exception) {
    super(message, exception);
  }
}
