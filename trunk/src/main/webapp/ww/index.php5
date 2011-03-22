<?php
  //function rnd($i) { $a = floor($i); return ( $i - $a > 0.5 ) ? $a + 1 : $a; }

  function getCurrentLanguage() {
    $lang = $_GET["lang"];
    if($lang == null || $lang == '') {
      $acceptLanguage = $_SERVER['HTTP_ACCEPT_LANGUAGE'];
      eregi("^(,?([a-z]{2})(-[A-Z]{2})?)+;.*$",$acceptLanguage,$group);
      $lang=$group[2];
    }
    return $lang;
  }

  function rnd($points, $integerValue) {
    return ($integerValue ? round($points) : round(100 * $points) / 100);
  }
  
  function calculateLostPoints($weight, $minutes, $intensity, $integerValue) {
    $coeff = $intensity == 1 ? 0.0007286 : ($intensity == 2 ? 0.00101527 : 0.00254749);
    $points = $weight * $minutes * $coeff;
    return rnd($points, $integerValue);
  }
  
  function calculateTakenPoints($p, $l, $f, $g, $b, $q, $integerValue) {
    $points = ($p / 10.9375 + $l / 3.888883 + $f / 35.00049 + $g / 9.2105535) * $q / $b;
    return rnd($points, $integerValue);
  }

  // COMMON
  $lang = getCurrentLanguage();
  $int = $_GET["int"];

  $eTip = $lang == 'fr' ? 'Si coché, le résultat sera approximé à la valeur entière la plus proche' : ($lang == 'es' ? 'Si está marcado, el resultado se aproxima al valor entero más cercano' : 'If checked, the result will be approximated to the closest integer value');
  $country1 = $lang == 'fr' || $lang == 'es' ? 'gb' : 'fr';
  $country2 =  $lang == 'es' ? 'fr' : 'es';
  $lang1 = $lang == 'fr' || $lang == 'es' ? 'English' : 'Français';
  $lang2 =  $lang == 'es' ? 'Français' : 'Español';

  $title = 'WeightWatchers<sup>&copy;</sup>&#160;&#160;&#160;';
  $title .= '<a href="?lang='.$country1.'"><img src="/tools/icomatics/flags/?country='.$country1.'" alt="'.$lang1.'" title="'.$lang1.'" /></a>&#160;&#160;';
  $title .= '<a href="?lang='.$country2.'"><img src="/tools/icomatics/flags/?country='.$country2.'" alt="'.$lang2.'" title="'.$lang2.'" /></a><br/>';

  $title .= ($lang == 'fr' ? 'Calculatrice ProPoints<sup>&reg;</sup>' : ($lang == 'es' ? 'Calculadora ProPoints<sup>&reg;</sup>' : 'ProPoints<sup>&reg;</sup> Calculator'));

  $pLabel = $lang == 'fr' ? 'Protéïnes' : ($lang == 'es' ? 'Proteínas' : 'Proteins');
  $lLabel = $lang == 'fr' ? 'Lipides' : ($lang == 'es' ? 'Lípidos' : 'Lipids');
  $fLabel = $lang == 'fr' ? 'Fibres' : ($lang == 'es' ? 'Fibras' : 'Fiber');
  $gLabel = $lang == 'fr' ? 'Glucides' : ($lang == 'es' ? 'Glúcidos' : 'Carbohydrates');
  $bLabel = $lang == 'fr' ? 'Pour' : ($lang == 'es' ? 'Por' : 'For');
  $qLabel = $lang == 'fr' ? 'Quantité' : ($lang == 'es' ? 'Cantidad' : 'Quantity');
  $eLabel = $lang == 'fr' ? 'Entier' : ($lang == 'es' ? 'Entero' : 'Integer');
  $cLabel = $lang == 'fr' ? 'Calculer' : ($lang == 'es' ? 'Calcular' : 'Calculate');

  $alimentsTab = $lang == 'fr' ? 'Aliments' : ($lang == 'es' ? 'Alimentos' : 'Food');
  $activiteTab = $lang == 'fr' ? 'Activité' : ($lang == 'es' ? 'Actividad' : 'Activity');

  $wLabel = $lang == 'fr' ? 'Poids' : ($lang == 'es' ? 'Peso' : 'Weight');
  $mLabel = $lang == 'fr' ? 'Durée' : ($lang == 'es' ? 'Duración' : 'Duration');
  $iLabel = $lang == 'fr' ? 'Intensité' : ($lang == 'es' ? 'Intensidad' : 'Intensity');

  $i1 = $lang == 'fr' ? 'Faible' : ($lang == 'es' ? 'Baja' : 'Low');
  $i2 = $lang == 'fr' ? 'Modérée' : ($lang == 'es' ? 'Moderadad' : 'Moderate');
  $i3 = $lang == 'fr' ? 'Elevée' : ($lang == 'es' ? 'Alta' : 'High');

  $p = $_GET["p"];
  $l = $_GET["l"];
  $f = $_GET["f"];
  $g = $_GET["g"];
  $b = $_GET["b"];
  $q = $_GET["q"];
  $w = $_GET["w"];
  $m = $_GET["m"];
  $i = $_GET["i"];
  if($q == 0) $q = 100;
  if($b == 0) $b = 100;


  $modeActivity = $_GET["mode"] == "activity";
  if($modeActivity) {
    // ACTIVITY
    $points = calculateLostPoints($w, $m, $i, $int == "on");
  }
  else {
    // FOOD
    $points = calculateTakenPoints($p, $l, $f, $g, $b, $q, $int == "on");
  }
?>
<html>
  <head>
    <title>WeightWatchers &copy;  ProPoints &reg;  Calculator - Calculatrice - Calculadora</title>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
    <link type='text/css' href='/res/css/common.css' rel='stylesheet'/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/stat.js"></script>    
    <style type="text/css">
      input { text-align:right; width:50px }
      .tab input { width:10px }
      input#submit { width:auto }
      #int { width:10px; margin-left:0 }
      tr.bordered td, tr.bordered td * { color:#CC2222; font-style:italic }
      /*tr.bordered td { border-top:1px solid #CC2222; border-bottom:1px solid #CC2222 }*/
      a img { border:0 }
      h3 { color:#CC2222; padding-left: 5px; text-align:left}
      table { border-collapse:collapse }
      table tbody, table tfoot tr { border:solid 2px #E0E0E0; }  
      thead tr th { padding-bottom:5px; border:1px outset white; text-align:left; } 
      tbody tr td { padding: 5px } 
      tfoot tr th { padding-top:10px } 
      .food { display:<?=$modeActivity?'none':''?>; }
      .activity { display:<?=$modeActivity?'':'none'?>; }
      .tab {
        background-color:#AEAEAE;
        border-radius:10px 10px 0 0;
        -moz-border-radius:10px 10px 0 0;
        padding:10px 10px 5px 0;
        cursor:pointer;
      }
      .tab.selected { background-color:#E0E0E0; }
      .tab a, .tab a:hover, .tab a:visited { font-weight:bold; text-decoration:none; color:#000000  }
    </style>
    <script type="text/javascript">
      function get(id) { var v = parseFloat($('#'+id).val()); return isNaN(v) ? 0 : v; }
      
      function changeMode() {
        var mode = $('input[name=mode]:checked').val();
        $('.food, .activity').hide();
        $('.tab').removeClass('selected');
        $('.'+mode).show();
        $('#'+mode).addClass('selected');
      }
      
      function calculate() {
        var p = get('p');
        var l = get('l');
        var f = get('f');
        var g = get('g');
        var b = get('b');
        var q = get('q');
        var w = get('w');
        var m = get('m');
        var i = $('#i option:selected').val();
        var mode = $('input[name=mode]:checked').val();
        if(q==0) q=100;
        if(b==0) b=100;
        var c = i == 1 ? 0.0007286 : (i == 2 ? 0.00101527 : 0.00254749);        
        var points = mode=='activity'?w*m*c:(p/10.9375+l/3.888883+f/35.00049+g/9.2105535)*q/b;
        $('#points').text( $('#int').is(':checked') ? Math.round(points) : Math.round(100*points)/100);
      }
      
      $(document).ready(function() {
        $(":submit").hide();
        $(":radio").click(changeMode);
        $(":input").change(calculate);
      });
    </script>
  </head>
  <body>
    <form>
      <input type="hidden" name="lang" value="<?=$lang?>"/>
      <h1><?=$title?></h1>
      <table cellspacing="0">
        <thead>
          <tr>
            <th colspan="2">
              <label id="food" class="tab <?if(!$modeActivity)echo'selected';?>"><input type="radio" name="mode" value="food"     <?if(!$modeActivity)echo'checked="checked"';?>/><noscript><a href="?lang=<?=$lang?>&mode=food"></noscript><?=$alimentsTab?><noscript></a></noscript></label>
              <label id="activity" class="tab <?if($modeActivity)echo'selected';?>"><input type="radio" name="mode" value="activity" <?if( $modeActivity)echo'checked="checked"';?>/><noscript><a href="?lang=<?=$lang?>&mode=activity"></noscript><?=$activiteTab?><noscript></a></noscript></label>
            </th>
          </tr>
        </thead>
        <tbody>
          <tr class="food">
            <td><label for="p"><?=$pLabel?>&#160;&#160;</label></td>
            <td><input id="p" name="p" type="text" value="<?=$p?>"/> g</td>
          </tr>
          <tr class="food">
            <td><label for="l"><?=$lLabel?>&#160;&#160;</label></td>
            <td><input id="l" name="l" type="text" value="<?=$l?>"/> g</td>
          </tr>
          <tr class="food">
            <td><label for="f"><?=$fLabel?>&#160;&#160;</label></td>
            <td><input id="f" name="f" type="text" value="<?=$f?>"/> g</td>
          </tr>
          <tr class="food">
            <td><label for="g"><?=$gLabel?>&#160;&#160;</label></td>
            <td><input id="g" name="g" type="text" value="<?=$g?>"/> g</td>
          </tr>
          <tr class="food">
            <td><label for="b"><?=$bLabel?>&#160;&#160;</label></td>
            <td><input id="b" name="b" type="text" value="<?=$b?>"/> g</td>
          </tr>
          <tr class="bordered food">
            <td><label for="q"><?=$qLabel?>&#160;&#160;</label></td>
            <td><input id="q" name="q" type="text" value="<?=$q?>"/> g</td>
          </tr>
          <tr class="activity">
            <td><label for="w"><?=$wLabel?>&#160;&#160;</label></td>
            <td><input id="w" name="w" type="text" value="<?=$w?>"/> kg</td>
          </tr>
          <tr class="activity">
            <td><label for="m"><?=$mLabel?>&#160;&#160;</label></td>
            <td><input id="m" name="m" type="text" value="<?=$m?>"/> min</td>
          </tr>
          <tr class="activity">
            <td><label for="i"><?=$iLabel?>&#160;&#160;</label></td>
            <td>
              <select id="i" name="i">
              <?for($v=1;$v<4;$v++){$name="i$v";?>
                <option value="<?=$v?>" <?if($i==$v){echo 'selected="selected"';}?>><?=$$name?></option>
              <?}?>
              </select>
            </td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <th><h3 id="points"><?=$points?></h3></th>
            <th><label><input type="checkbox" checked="checked" id="int" name="int" title="<?=$eTip?>"><?=$eLabel?></label></th>
          </tr>
        </tfoot>
      </table>
      <input id="submit" type="submit" value="<?=$cLabel?>"/>
    </form>
  </body>
</html>