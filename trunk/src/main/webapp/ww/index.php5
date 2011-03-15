<?php
  function rnd($i) { $a = floor($i); return ( $i - $a > 0.5 ) ? $a + 1 : $a; }
  $p = $_GET["p"];
  $l = $_GET["l"];
  $f = $_GET["f"];
  $g = $_GET["g"];
  $q = $_GET["q"];
  $lang = $_GET["lang"];
  if($q == 0) $q = 100;
  $int = $_GET["int"];
  $points = ($p/10.9375+$l/3.888883+$f/35.00049+$g/9.2105535)*$q/100;
  $points = $int == "on" ? rnd($points) : rnd(100*points)/100;

  $title = $lang == 'fr' ? 'Calculatrice WeightWatchers ProPoints' : ($lang == 'es' ? 'Calculadora WeightWatchers ProPoints' : 'Weight Watchers ProPoints Calculator');
  $pLabel = $lang == 'fr' ? 'Protéïnes' : ($lang == 'es' ? 'Proteínas' : 'Proteins');
  $lLabel = $lang == 'fr' ? 'Lipides' : ($lang == 'es' ? 'Lípidos' : 'Lipids');
  $fLabel = $lang == 'fr' ? 'Fibres' : ($lang == 'es' ? 'Fibras' : 'Fiber');
  $gLabel = $lang == 'fr' ? 'Glucides' : ($lang == 'es' ? 'Glúcidos' : 'Carbohydrates');
  $qLabel = $lang == 'fr' ? 'Quantité' : ($lang == 'es' ? 'Cantidad' : 'Quantity');
  $iLabel = $lang == 'fr' ? 'Entier' : ($lang == 'es' ? 'Entero' : 'Integer');
  $cLabel = $lang == 'fr' ? 'Calculer' : ($lang == 'es' ? 'Calcular' : 'Calculate');
  $iTip = $lang == 'fr' ? 'Si coché, le résultat sera approximé à la valeur entière la plus proche' : ($lang == 'es' ? 'Si está marcado, el resultado se aproxima al valor entero más cercano' : 'If checked, the result will be approximated to the closest integer value');
  $country1 = $lang == 'fr' || $lang == 'es' ? 'gb' : 'fr';
  $country2 =  $lang == 'es' ? 'fr' : 'es';
  $lang1 = $lang == 'fr' || $lang == 'es' ? 'English' : 'Français';
  $lang2 =  $lang == 'es' ? 'Français' : 'Español';
?>
<html>
  <head>
    <title>WeightWatchers ProPoints Calculator - Calculatrice - Calculadora</title>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
    <link type='text/css' href='/res/css/common.css' rel='stylesheet'/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/stat.js"></script>    
    <style type="text/css">
      input { text-align:right; width:50px }
      #int { width:10px; margin-left:0 }
      tr.bordered td, tr.bordered td * { color:#CC2222; font-style:italic }
      /*tr.bordered td { border-top:1px solid #CC2222; border-bottom:1px solid #CC2222 }*/
      a img { border:0 }
      h3 { color:#CC2222 }
    </style>
    <script type="text/javascript">
      function get(id) { var v = parseFloat($('#'+id).val()); return isNaN(v) ? 0 : v; }      
      function rnd(i) { var a = Math.floor(i); return ( i - a > 0.5 ) ? a + 1 : a; }
      
      function calculate() {
        var p =get('p');
        var l = get('l');
        var f = get('f');
        var g = get('g');
        var q = get('q');
        if(q==0) q=100;
        var points = (p/10.9375+l/3.888883+f/35.00049+g/9.2105535)*q/100;
        $('#points').text( $('#int').is(':checked') ? rnd(points) : rnd(100*points)/100);
      }
      
      $(document).ready(function() {
        $(":submit").hide();
        $(":text").change(calculate);
        $(":checkbox").change(calculate);
      });
    </script>
  </head>
  <body>
    <form method=""> 
    <div style="text-align:right;width:100%">
      <a href="?lang=<?=$country1?>"><img src="/tools/icomatics/flags/?country=<?=$country1?>" alt="<?=$lang1?>" title="<?=$lang1?>" /></a>&#160;&#160;
      <a href="?lang=<?=$country2?>"><img src="/tools/icomatics/flags/?country=<?=$country2?>" alt="<?=$lang2?>" title="<?=$lang2?>" /></a>
    </div>
    <h1><?=$title?></h1>
    <table cellspacing="0">
      <tr>
        <td><label for="p"><?=$pLabel?>&#160;&#160;</label></td>
        <td><input id="p" name="p" type="text" value="<?=$p?>"/> g</td>
      </tr>
      <tr>
        <td><label for="l"><?=$lLabel?>&#160;&#160;</label></td>
        <td><input id="l" name="l" type="text" value="<?=$l?>"/> g</td>
      </tr>
      <tr>
        <td><label for="f"><?=$fLabel?>&#160;&#160;</label></td>
        <td><input id="f" name="f" type="text" value="<?=$f?>"/> g</td>
      </tr>
      <tr>
        <td><label for="g"><?=$gLabel?>&#160;&#160;</label></td>
        <td><input id="g" name="g" type="text" value="<?=$g?>"/> g</td>
      </tr>
      <tr class="bordered">
        <td><label for="q"><?=$qLabel?>&#160;&#160;</label></td>
        <td><input id="q" name="q" type="text" value="<?=$q?>"/> g</td>
      </tr>
      <tr>
        <td><h3 id="points"><?=$points?></h3></td>
        <td><label><input type="checkbox" checked="checked" id="int" name="int" title="<?=$iTip?>"><?=$iLabel?></label></td>
      </tr>
      <tr>
        <td>&#160;</td>
        <td><input type="submit" value="<?=$cLabel?>"/></td>
      </tr>
    </table>
    </form>
  </body>
</html>