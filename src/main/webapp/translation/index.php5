<?php

  function translate($word, $targetLanguage) {
    $key = 'AIzaSyDgRuw8vFN_oaiKRMR1XY8IrYW2ywMwyvc';
    $json = json_decode(file_get_contents("https://www.googleapis.com/language/translate/v2?key=$key&q=$word&target=$targetLanguage"));
    return $json->data->translations[0]->translatedText;
  }

  function getLanguagesForGoogle() {
    $langs = array(
      "af"=>"Afrikaans,ZA",
      "sq"=>"Albanian,AL",
      "ar"=>"Arabic,SA",
      "be"=>"Belarusian,BY",
      "bg"=>"Bulgarian,BG",
      "ca"=>"Catalan,ES",
      "zh-CN"=>"Chinese Simplified,CN",
      "zh-TW"=>"Chinese Traditional,TW",
      "hr"=>"Croatian,HR",
      "cs"=>"Czech,CZ",
      "da"=>"Danish,DK",
      "nl"=>"Dutch,NL",
      "en"=>"English,GB",
      "et"=>"Estonian,EE",
      "tl"=>"Filipino,PH",
      "fi"=>"Finnish,FI",
      "fr"=>"French,FR",
      "gl"=>"Galician,ES",
      "de"=>"German,DE",
      "el"=>"Greek,GR",
      "ht"=>"Haitian Creole,HT",
      "iw"=>"Hebrew,IL",
      "hi"=>"Hindi,IN",
      "hu"=>"Hungarian,HU",
      "is"=>"Icelandic,IS",
      "id"=>"Indonesian,ID",
      "ga"=>"Irish,IE",
      "it"=>"Italian,IT",
      "ja"=>"Japanese,JP",
      "lv"=>"Latvian,LV",
      "lt"=>"Lithuanian,LT",
      "mk"=>"Macedonian,MK",
      "ms"=>"Malay,MY",
      "mt"=>"Maltese,MT",
      "no"=>"Norwegian,NO",
      "fa"=>"Persian,IR",
      "pl"=>"Polish,PL",
      "pt"=>"Portuguese,PT",
      "ro"=>"Romanian,RO",
      "ru"=>"Russian,RU",
      "sr"=>"Serbian,RS",
      "sk"=>"Slovak,SK",
      "sl"=>"Slovenian,SL",
      "es"=>"Spanish,ES",
      "sw"=>"Swahili,CD",
      "sv"=>"Swedish,SE",
      "th"=>"Thai,TH",
      "tr"=>"Turkish,TR",
      "uk"=>"Ukrainian,UA",
      "vi"=>"Vietnamese,VN",
      "cy"=>"Welsh,GB",
      "yi"=>"Yiddish,IL"
    );
    return $langs;
  }

  $languages = getLanguagesForGoogle();
  $text = urldecode($_GET["text"]);
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
<?
  foreach(split(",", $_GET["langs"]) as $lang) {
    $values = split(",",$languages[$lang]);
    $translation = translate($text, $lang);
    echo "<img src='http://ap2cu.com/tools/icomatics/flags/?size=small&country=".$values[1]."' alt='$lang'/> ".$values[0]." => ".$translation."<br/>";
  }
?>
  </body>
</html>