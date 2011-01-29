package com.ap2cu.parser.html.allocine;

import java.io.DataInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.ap2cu.parser.html.HtmlParser;
import com.ap2cu.utils.Encoding;

public class AllocineParser extends HtmlParser {

  private String allocine = "http://www.allocine.fr";

  private String strRecherche = allocine + "/recherche/?motcle=";

  private String lien;

  private String nomFilm;

  private String pageVoulue;

  private static HashMap<String, AllocineParser> parsers = new HashMap<String, AllocineParser>();

  public static AllocineParser getAllocineParser(String nomFilm) {
    if (parsers.containsKey(nomFilm))
      return parsers.get(nomFilm);
    AllocineParser parser = new AllocineParser(nomFilm);
    parsers.put(nomFilm, parser);
    return parser;
  }

  protected AllocineParser(String nomFilm) {
    this.nomFilm = nomFilm;
    strRecherche += nomFilm.replace(' ', '+');
    String pageRecherche = recupererContenuHTML(strRecherche);
    try {
      pageVoulue = recupererPageVoulue(pageRecherche, nomFilm);
    }
    catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (pageVoulue == null)
      System.out.println(strRecherche);
  }

  public void recupererAffiche(String fileName) {
    File f = new File(fileName);
    f.mkdir();
    int index = pageVoulue.indexOf("width=\"100%\"><img src=\"") + 23;
    String lienAffiche = pageVoulue.substring(index, pageVoulue.indexOf(".jpg", index) + 4);
    try {
      ImageIO.write(ImageIO.read(new URL(lienAffiche)), "jpg", new File(fileName + "\\" + nomFilm + ".jpg"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static String recupererContenuHTML(String url) {
    System.out.println("parsing: " + url);
    byte[] b = null;
    try {
      URLConnection conn = new URL(url).openConnection();
      conn.setDoInput(true);
      conn.setUseCaches(false);
      DataInputStream in = new DataInputStream(conn.getInputStream());
      b = new byte[50000];
      in.readFully(b);
      in.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return new String(b);
  }

  protected String recuperer(String regex) {
    Pattern pattern = Pattern.compile("^.*" + regex + ".*$", Pattern.DOTALL | Pattern.MULTILINE);
    Matcher matcher = pattern.matcher(pageVoulue);
    return matcher.find() ? matcher.replaceAll("$1") : null;
  }

  public static String supprimerBalises(String s) {
    return s.replaceAll("<([^>]+)>([^<]*)</\\1>", "$2");
  }
  
  private String recupererPageVoulue(String pageRecherche, String aRechercher) throws UnsupportedEncodingException {
    Matcher matcher = Pattern.compile("<a href='(/film/fichefilm_gen_cfilm=.*?.html)'>(.*?)</a>", Pattern.DOTALL | Pattern.MULTILINE).matcher(pageRecherche);
    int i = 0;
    while (matcher.find(i++)) {
      String movieName = nomFilm.toLowerCase();
      String insideLink = supprimerBalises(matcher.group(2)).toLowerCase().trim();
      if (insideLink.equals(movieName)) {
        lien = matcher.group(1);
        System.out.println(lien);
        return new String(recupererContenuHTML(allocine + lien).getBytes("ISO-8859-1"), Encoding.DEFAULT);
      }
    }
    return "";
  }

  public String[][] recupererActeurs() {
    if (lien == null)
      return null;
    String castingLien = lien.replace("fichefilm", "casting");
    String casting = recupererContenuHTML(allocine + castingLien);
    int index = casting.indexOf("Acteur(s)");
    if (index == -1)
      return null;
    Vector<String[]> v = new Vector<String[]>();
    boolean b = true;
    while (b) {
      index = casting.indexOf("<h5>", index) + 4;
      String pseudo = casting.substring(index, casting.indexOf("<", index));
      b = !pseudo.equals("Sc�nariste");
      index = casting.indexOf("class=\"link1\"><h4>", index) + 18;
      String nom = casting.substring(index, casting.indexOf("<", index));
      if (b)
        v.add(new String[] { pseudo, nom });
    }
    return (String[][]) v.toArray(new String[][] {});
  }

  public String recupererResume() {
    return recuperer("<span[^>]*>Synopsis : </span>([^<]+)</p>");
  }

  public String recupererDateDeSortie() {
    return recuperer("<a href=.*?/film/agenda.html\\?week=.*?>(.*?)<");
  }

  public String recupererRealisateur() {
    int index = pageVoulue.indexOf(">", pageVoulue.indexOf("Realise par")) + 1;
    return pageVoulue.substring(index, pageVoulue.indexOf("<", index));
  }

  public String recupererDuree() {
    int index = pageVoulue.indexOf(": ", pageVoulue.indexOf("Duree")) + 2;
    return pageVoulue.substring(index, pageVoulue.indexOf(".", index));
  }

  public String recupererGenre() {
    int index = pageVoulue.indexOf(": ", pageVoulue.indexOf("Genre")) + 2;
    return pageVoulue.substring(index, pageVoulue.indexOf("<", index));
  }

  public String recupererTitre() {
    return nomFilm;
  }

}
