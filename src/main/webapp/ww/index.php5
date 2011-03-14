<?php
  function rnd($i) { $a = floor($i); return ( $i - $a > 0.5 ) ? $a + 1 : $a; }
  $p = $_GET["p"];
  $l = $_GET["l"];
  $f = $_GET["f"];
  $g = $_GET["g"];
  $q = $_GET["q"];
  if($q == 0) $q = 100;
  $int = $_GET["int"];
  $points = ($p/10.9375+$l/3.888883+$f/35.00049+$g/9.2105535)*$q/100;
  $points = $int == "on" ? rnd($points) : rnd(100*points)/100;
?>
<html>
  <head>
    <title>Weight Watchers ProPoints calculator</title>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
    <link type='text/css' href='/res/css/common.css' rel='stylesheet'/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/stat.js"></script>    
    <style type="text/css">
      input { text-align:right; width:50px }
      #int { width:10px; margin-left:0 }
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
    <h1>Weight Watchers ProPoints calculator</h1>    
    <form method="">
    <table>
      <tr>
        <td><label for="p">Proteins</label></td>
        <td><input id="p" name="p" type="text" value="<?=$p?>"/> g</td>
      </tr>
      <tr>
        <td><label for="l">Lipids</label></td>
        <td><input id="l" name="l" type="text" value="<?=$l?>"/> g</td>
      </tr>
      <tr>
        <td><label for="f">Fibers</label></td>
        <td><input id="f" name="f" type="text" value="<?=$f?>"/> g</td>
      </tr>
      <tr>
        <td><label for="g">Glucids</label></td>
        <td><input id="g" name="g" type="text" value="<?=$g?>"/> g</td>
      </tr>
      <tr>
        <td><label for="q"><i>Quantity</i></label></td>
        <td><input id="q" name="q" type="text" value="<?=$q?>"/> g</td>
      </tr>
      <tr>
        <td><h3 id="points"><?=$points?></h3></td>
        <td><label><input type="checkbox" checked="checked" id="int" name="int">Integer</label></td>
      </tr>
      <tr>
        <td>&#160;</td>
        <td><input type="submit" value="Calculer"/></td>
      </tr>
    </table>
    </form>
  </body>
</html>