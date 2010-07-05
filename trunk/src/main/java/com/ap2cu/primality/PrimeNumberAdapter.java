package com.ap2cu.primality;

import java.util.LinkedList;
import java.util.List;

public abstract class PrimeNumberAdapter implements PrimeNumberListener {

  protected List<PrimeNumberListener> listeners;

  public PrimeNumberAdapter() {
    this.listeners = new LinkedList<PrimeNumberListener>();
  }

  protected void notifyPrimeNumberFound(long n) {
    PrimeNumberEvent event = new PrimeNumberEvent(new Number(n));
    for (int i = 0; i < listeners.size(); i++) {
      // System.out.println(n+" is prime !");
      listeners.get(i).onPrimeNumberFound(event);
    }
  }

  public void addPrimeNumberListener(PrimeNumberListener listener) {
    if (listeners.contains(listener))
      return;
    listeners.add(listener);
  }

  public abstract void onPrimeNumberFound(PrimeNumberEvent event);

}
