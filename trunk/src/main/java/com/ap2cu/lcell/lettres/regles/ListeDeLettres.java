package com.ap2cu.lcell.lettres.regles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ListeDeLettres extends HashMap<Character, Integer>{

  private List<Character> keyList;
  private static final long serialVersionUID = 1L;
  
  private void initKeyList() {
    this.keyList = new LinkedList<Character>(keySet());    
  }
  
  public void ajouterLettre(Character c) {
    put(c, nombreDOccurrences(c) + 1);
    initKeyList();
  }

  public void supprimerLettre(Character c) {
    int nb = nombreDOccurrences(c);
    if(nb > 1) 
      put(c, nb - 1);      
    else 
      remove(c);    
    initKeyList();
  }
  
  public Character getLettre(int i) {
    Character c = keyList.get(i);
    supprimerLettre(c);
    return c;
  }

  public void addAll(ListeDeLettres list) {
    for(Character c : list.keySet()) {
      long multiplicity = list.get(c);
      for(int i=0;i<multiplicity;i++)
        ajouterLettre(c);
    }
    initKeyList();
  }
  
  public int nombreDOccurrences(Character Character) {
    if(containsKey(Character))
      return get(Character);
    return 0;
  }
  
  public String toString() {
    List<Character> keys = new ArrayList<Character>(keySet());
    StringBuffer s = new StringBuffer();
    for(Character c : keys)
      s.append(c).append("(").append(nombreDOccurrences(c)).append(") , ");
    if(s.length()>2) s.setLength(s.length()-3);
    return s.toString();
  }
}
