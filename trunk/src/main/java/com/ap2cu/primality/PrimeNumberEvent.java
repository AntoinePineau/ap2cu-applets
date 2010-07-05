package com.ap2cu.primality;

import java.util.EventObject;

public class PrimeNumberEvent extends EventObject {

  private static final long serialVersionUID = 1L;
  protected Number number;

  public PrimeNumberEvent(Number number) {
    super(number);
    this.number = number;
  }
  
  
  public Number getNumber() {
    return number;
  }
}
