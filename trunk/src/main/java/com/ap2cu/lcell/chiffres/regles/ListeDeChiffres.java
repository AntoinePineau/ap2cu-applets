package com.ap2cu.lcell.chiffres.regles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ListeDeChiffres extends HashMap<Integer, Integer>{

  private List<Integer> keyList;
  private static final long serialVersionUID = 1L;
  
  private void initKeyList() {
    this.keyList = new LinkedList<Integer>(keySet());    
  }
  
  public void ajouterChiffre(Integer c) {
    put(c, nombreDOccurrences(c) + 1);
    initKeyList();
  }

  public void supprimerChiffre(Integer c) {
    int nb = nombreDOccurrences(c);
    if(nb > 1) 
      put(c, nb - 1);      
    else 
      remove(c);    
    initKeyList();
  }
  
  public Integer getChiffre(int i) {
    Integer c = keyList.get(i);
    supprimerChiffre(c);
    return c;
  }

  public void addAll(ListeDeChiffres list) {
    for(Integer c : list.keySet()) {
      long multiplicity = list.get(c);
      for(int i=0;i<multiplicity;i++)
        ajouterChiffre(c);
    }
    initKeyList();
  }
  
  public int nombreDOccurrences(Integer Integer) {
    if(containsKey(Integer))
      return get(Integer);
    return 0;
  }
  
  public String toString() {
    List<Integer> keys = new ArrayList<Integer>(keySet());
    StringBuffer s = new StringBuffer();
    for(Integer c : keys)
      s.append(c).append("(").append(nombreDOccurrences(c)).append(") , ");
    if(s.length()>2) s.setLength(s.length()-3);
    return s.toString();
  }
}
