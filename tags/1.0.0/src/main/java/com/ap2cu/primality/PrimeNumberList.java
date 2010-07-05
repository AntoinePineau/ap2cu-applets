package com.ap2cu.primality;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class PrimeNumberList extends HashMap<Long, Long>{

  private static final long serialVersionUID = 1L;

  public void addPrimeNumber(Long number) {
    put(number, getMultiplicity(number) + 1);
  }

  public void addAll(PrimeNumberList list) {
    for(Long number : list.keySet()) {
      long multiplicity = list.get(number);
      for(int i=0;i<multiplicity;i++)
        addPrimeNumber(number);
    }
  }
  
  public Long getMultiplicity(Long number) {
    if(containsKey(number))
      return get(number);
    return 0L;
  }
  
  public String toString() {
    List<Long> keys = new ArrayList<Long>(keySet());
    Collections.sort(keys);
    StringBuffer s = new StringBuffer();
    for(Long number : keys)
      s.append(number).append("(").append(getMultiplicity(number)).append(") x ");
    if(s.length()>2) s.setLength(s.length()-3);
    return s.toString();
  }
}
