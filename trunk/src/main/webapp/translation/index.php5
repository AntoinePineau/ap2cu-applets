<?php

function toString($language) {
  return $language->flag.' <b>'.$language->name.'</b> : '.$language->text.'<br/>';
}

$url = 'http://ap2cu.com/tools/translation/service/json/?text='.$_GET['text'].'&langs='.$_GET['langs'];
$json = json_decode(file_get_contents($url));

foreach($json->translation->from as $code => $f) {
  $from = toString($json->translation->from->$code);
}
?>
<html>
  <head>
    <title>Translation</title>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
    <link type='text/css' href='/res/css/common.css' rel='stylesheet'/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/stat.js"></script>
  </head>
  <body>
   <b>Translated from</b> <?=$from?><br/>
<?
  foreach($json->translation->to as $code => $to) {
    echo toString($json->translation->to->$code);
  }
?>
  </body>
</html>