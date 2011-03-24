<?php
header("Content-Type:application/json");

  function translationAsJSON($langCode, $langName, $langFlag, $translation) {
    return array ("flag"=>"<img src='http://ap2cu.com/tools/icomatics/flags/?size=small&country=$langFlag' alt='$langCode'/>",
                  "name"=>$langName,
                  "text" => $translation);
  }

  function translate($word, $targetLanguage) {
    $key = 'AIzaSyDgRuw8vFN_oaiKRMR1XY8IrYW2ywMwyvc';
    $url = "https://www.googleapis.com/language/translate/v2?key=$key&q=".urlencode($word)."&target=$targetLanguage";
    $json = json_decode(file_get_contents($url));
    $first = $json->data->translations[0];
    return array($first->translatedText, $first->detectedSourceLanguage);
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

  $detectedLanguage = '';
  foreach(split(",", $_GET["langs"]) as $lang) {
    $values = split(",",$languages[$lang]);
    list($translation, $detect) = translate($text, $lang);
    if($detectedLanguage == '') $detectedLanguage = $detect;
    $to = translationAsJSON($lang,$values[0],$values[1],$translation);
    $tos->$lang = $to;
  }

  if($detectedLanguage != '') {
    $values = split(",",$languages[$detectedLanguage]);
    $from = array($detectedLanguage => translationAsJSON($detectedLanguage,$values[0],$values[1],$text));
  }


  $json = array( "translation" => array( "from" => $from, "to" => $tos ) );

  echo json_encode($json);
?>