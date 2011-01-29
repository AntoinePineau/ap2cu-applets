package com.ap2cu.parser.html.allocine.beans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ap2cu.parser.html.allocine.AllocineParser;
import com.ap2cu.utils.FileUtils;

public class Movie {

  protected String name;
  protected String anneeRealisation;
  protected String dateSortie;

  protected String resume;
  protected String avisPresse;
  protected String avisPublic;

  public static final String EOL = "\n";
  public static final String SEP = ": ";

  public static Movie getMovieFromAllocine(String movieName) {
    Movie movie = new Movie();
    AllocineParser parser = AllocineParser.getAllocineParser(movieName);

    movie.setName(parser.recupererTitre());
    movie.setAnneeSortie(parser.recupererDateDeSortie());
    movie.setResume(parser.recupererResume());

    return movie;
  }

  @Override
  public String toString() {
    StringBuffer s = new StringBuffer(name).append(EOL);
    append(s, "Annee Realisation", anneeRealisation);
    append(s, "Date Sortie", dateSortie);
    append(s, "Resume", resume);
    append(s, "Avis Presse", avisPresse);
    append(s, "Avis Public", avisPublic);
    return s.toString();
  }

  private void append(StringBuffer s, String text, String value) {
    if (value != null)
      s.append(text).append(SEP).append(value).append(EOL);
  }

  public static void main(String[] args) {
    List<String> files = FileUtils.getAllFiles(new File("D:/downloads/movies"), "(.*)\\.(avi|mkv|mov)$");
    for (int i = 0; i < files.size(); i++) {
      String file = files.get(i);
      String movieName = file.replaceAll("^.*/(.*?)\\.(avi|mkv|mov)$", "$1");
      Movie movie = getMovieFromAllocine(movieName);
      File resumeFile = new File(new File(file).getParentFile(), "resume.txt");
      FileOutputStream out = null;
      try {
        out = new FileOutputStream(resumeFile);
        out.write(movie.getResume().getBytes());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      finally {
        if(out!=null)
          try {
            out.close();
          }
          catch (IOException e) {
            e.printStackTrace();
          }
      }
//      return;
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAnneeRealisation() {
    return anneeRealisation;
  }

  public void setAnneeRealisation(String anneeRealisation) {
    this.anneeRealisation = anneeRealisation;
  }

  public String getAnneeSortie() {
    return dateSortie;
  }

  public void setAnneeSortie(String anneeSortie) {
    this.dateSortie = anneeSortie;
  }

  public String getResume() {
    return resume;
  }

  public void setResume(String resume) {
    this.resume = resume;
  }

  public String getAvisPresse() {
    return avisPresse;
  }

  public void setAvisPresse(String avisPresse) {
    this.avisPresse = avisPresse;
  }

  public String getAvisPublic() {
    return avisPublic;
  }

  public void setAvisPublic(String avisPublic) {
    this.avisPublic = avisPublic;
  }

}
