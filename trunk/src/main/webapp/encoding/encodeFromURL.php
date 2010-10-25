<?php
$text = urldecode($_GET["text"]);
$to = urldecode($_GET["to"]);

function url_encode($string) {
  $result = '';
  for($c=0;$c<strlen($string);$c++) $result .= '%'.strtoupper(dechex(ord($string[$c])));
  return $result;
}
function utf8encode($string) {
  $chars = split(';',preg_replace('/&#x/','',utf16encode($string)));
  $result = '';
  foreach($chars as $char) if($char!='') $result.='&#'.hexdec($char).';';
  return $result;
}

function utf16encode($string) {
  return preg_replace('/%([^%]+)/','&#x$1;', url_encode($string));
}
if($to == "UTF-8") echo utf8encode($text);
else if($to == "UTF-16") echo utf16encode($text);
?>