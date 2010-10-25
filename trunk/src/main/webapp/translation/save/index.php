<?php
$text = urldecode($_GET["text"]);

function getTranslationFromGoogle($lang, $textToTranslate, $encoding, $example) {
  $displayExample = false;
  $url = "http://translate.google.com/translate_t?ie=UTF-8&tl=$lang&sl=auto&text=".url_encode($textToTranslate);
  $translatedText = file_get_contents($url);    
  if($encoding!='')
    $translatedText = mb_convert_encoding($translatedText, 'UTF-8', mb_detect_encoding($translatedText, $encoding));
  eregi(".*<div id=result_box([^>]*)>([^<]*)</div>.*",$translatedText,$group);
  $result = $group[2];
  if(preg_match('/&#x?[0-9]+;/',$result)) { if($displayExample) $result='['.$example.']'.$result; return $result; }
  $url = "http://ap2cu.com/tools/encoding/encodeFromURL.php?to=UTF-16&text=".url_encode($result);
  $result = file_get_contents($url);
  if($displayExample) $result='['.$example.']'.$result;
  return $result;
}
function url_encode($string) {
  $result = '';
  for($c=0;$c<strlen($string);$c++) $result .= '%'.strtoupper(dechex(ord($string[$c])));
  return $result;
}
function getLanguages() {
  $langs = array();
  $languages = array(
    "sq"=>"Albanian,al.gif,ISO-8859-2,&#x66;&#x61;&#x6c;&#x65;&#x6d;&#x69;&#x6e;&#x64;&#x65;&#x72;&#x69;&#x74;",
    "ca"=>"Catalan,es.gif,ISO-8859-15,&#x67;&#x72;&#xe0;&#x63;&#x69;&#x65;&#x73;",
    "zh-CN"=>"Chinese (Simplified),cn.gif,EUC-CN,&#x8c22;&#x8c22;&#x60a8;",
    "zh-TW"=>"Chinese (Traditional),cn.gif,Big5,&#x8b1d;&#x8b1d;&#x60a8;",
    "hr"=>"Croatian,hr.gif,ISO-8859-15,&#x68;&#x76;&#x61;&#x6c;&#x61;&#x20;&#x74;&#x69;",
    "cs"=>"Czech,cz.gif,ISO-8859-2,&#x64;&#x11b;&#x6b;&#x75;&#x6a;&#x69;&#x20;&#x74;&#x69;",
    "da"=>"Danish,dk.gif,ISO-8859-15,&#x74;&#x61;&#x6b;",
    "nl"=>"Dutch,nl.gif,ISO-8859-15,&#x62;&#x65;&#x64;&#x61;&#x6e;&#x6b;&#x74;",
    "en"=>"English,uk.gif,ISO-8859-15,&#x74;&#x68;&#x61;&#x6e;&#x6b;&#x20;&#x79;&#x6f;&#x75;",
    "et"=>"Estonian,ee.gif,ISO-8859-4,&#x74;&#xe4;&#x6e;&#x61;&#x6e;",
    "tl"=>"Filipino,ph.gif,UTF-8,&#x73;&#x61;&#x6c;&#x61;&#x6d;&#x61;&#x74;",
    "fi"=>"Finnish,fi.gif,ISO-8859-15,&#x6b;&#x69;&#x69;&#x74;&#x6f;&#x73;",
    "fr"=>"French,fr.gif,ISO-8859-15,&#x6d;&#x65;&#x72;&#x63;&#x69;",
    "gl"=>"Galician,es.gif,ISO-8859-15,&#x67;&#x72;&#x61;&#x7a;&#x61;&#x73;",
    "de"=>"German,de.gif,ISO-8859-2,&#x44;&#x61;&#x6e;&#x6b;&#x65;",
    "el"=>"Greek,gr.gif,ISO-8859-7,&#x3c3;&#x3b5;&#x20;&#x3b5;&#x3c5;&#x3c7;&#x3b1;&#x3c1;&#x3b9;&#x3c3;&#x3c4;&#x3ce;",
    "iw"=>"Hebrew,il.gif,ISO-8859-8,&#x5ea;&#x5d5;&#x5d3;&#x5d4;",
    "hi"=>"Hindi,in.gif,UTF-8,&#x927;&#x928;&#x94d;&#x92f;&#x935;&#x93e;&#x926;",
    "hu"=>"Hungarian,hu.gif,ISO-8859-2,&#x6b;&#xf6;&#x73;&#x7a;&#xf6;&#x6e;&#xf6;&#x6d;",
    "id"=>"Indonesian,id.gif,UTF-8,&#x74;&#x65;&#x72;&#x69;&#x6d;&#x61;&#x20;&#x6b;&#x61;&#x73;&#x69;&#x68;",
    "it"=>"Italian,it.gif,ISO-8859-2,&#x67;&#x72;&#x61;&#x7a;&#x69;&#x65;",
    "ja"=>"Japanese,jp.gif,Shift-JIS,&#x3042;&#x308a;&#x304c;&#x3068;&#x3046;&#x3054;&#x3056;&#x3044;&#x307e;&#x3057;&#x305f;",
    "ko"=>"Korean,kr.gif,euc-kr,&#xac10;&#xc0ac;&#xd569;&#xb2c8;&#xb2e4;",
    "lv"=>"Latvian,lv.gif,ISO-8859-15,&#x70;&#x61;&#x6c;&#x64;&#x69;&#x65;&#x73;",
    "lt"=>"Lithuanian,lt.gif,ISO-8859-13,&#x61;&#x10d;&#x69;&#x16b;",
    "mt"=>"Maltese,mt.gif,ISO-8859-3,&#x67;&#x72;&#x61;&#x7a;&#x7a;&#x69;",
    "no"=>"Norwegian,no.gif,ISO-8859-15,&#x74;&#x61;&#x6b;&#x6b;",
    "pl"=>"Polish,pl.gif,ISO-8859-2,&#x64;&#x7a;&#x69;&#x119;&#x6b;&#x75;&#x6a;&#x119;",
    "pt"=>"Portuguese,pt.gif,ISO-8859-15,&#x6f;&#x62;&#x72;&#x69;&#x67;&#x61;&#x64;&#x6f;",
    "ro"=>"Romanian,ro.gif,ISO-8859-2,&#x6d;&#x75;&#x6c;&#x163;&#x75;&#x6d;&#x65;&#x73;&#x63;",
    "sr"=>"Serbian,rs.gif,UTF-8,&#x445;&#x432;&#x430;&#x43b;&#x430;&#x20;&#x442;&#x438;",
    "sk"=>"Slovak,sk.gif,ISO-8859-2,&#x10f;&#x61;&#x6b;&#x75;&#x6a;&#x65;&#x6d;&#x20;&#x74;&#x69;",
    "sl"=>"Slovenian,si.gif,ISO-8859-2,&#x68;&#x76;&#x61;&#x6c;&#x61;",
    "es"=>"Spanish,es.gif,ISO-8859-15,&#x67;&#x72;&#x61;&#x63;&#x69;&#x61;&#x73;",
    "sv"=>"Swedish,se.gif,ISO-8859-15,&#x74;&#x61;&#x63;&#x6b;",
    "tr"=>"Turkish,tr.gif,ISO-8859-9,&#x74;&#x65;&#x15f;&#x65;&#x6b;&#x6b;&#xfc;&#x72;&#x20;&#x65;&#x64;&#x65;&#x72;&#x69;&#x6d;",
    "vi"=>"Vietnamese,vn.gif,UTF-8,&#x63;&#x1ea3;&#x6d;&#x20;&#x1a1;&#x6e;&#x20;&#x62;&#x1ea1;&#x6e;",
    /*"ar"=>"Arabic,sa.gif,ISO-8859-6,&#x634;&#x643;&#x631;&#x627;&#x20;&#x644;&#x643;",
    "bg"=>"Bulgarian,bg.gif,ISO-8859-5,&#x431;&#x43b;&#x430;&#x433;&#x43e;&#x434;&#x430;&#x440;&#x44f;",
    "uk"=>"Ukrainian,ua.gif,KOI8-U,&#x441;&#x43f;&#x430;&#x441;&#x438;&#x431;&#x43e;",
    "ru"=>"Russian,ru.gif,KOI8-R,&#x441;&#x43f;&#x430;&#x441;&#x438;&#x431;&#x43e;",
    "th"=>"Thai,th.gif,iso-8859-10,&#xe02;&#xe2d;&#xe1a;&#xe04;&#xe38;&#xe13;"*/
  );
  if(isset($_GET["langs"]))
    foreach(split(",", $_GET["langs"]) as $lang) $langs[$lang] = $languages[$lang];
  else
    $langs = $languages;
  return $langs;
}
echo "<?xml version='1.0' encoding='UTF-8'?>\n<?xml-stylesheet href='translation.xsl' type='text/xsl'?>\n";?>
<translation>
  <text><![CDATA[<?=$text?>]]></text>
<?
$langs = getLanguages();
foreach($langs as $lang=>$value) {
  list($name,$img,$encoding,$example) = split(",",$value);
  echo "  <to lang=\"$lang\" label=\"$name\" img=\"/res/img/countries/$img\">".getTranslationFromGoogle($lang, $text, $encoding,$example)."</to>\n";
}
?>
</translation>