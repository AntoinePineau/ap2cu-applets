<?php
  $size=strtolower($_GET["size"]);
  $size = $size == "small" ? 16 : 24;
  $shortcut=strtolower($_GET["shortcut"]);
  header("Content-type: image/png");

  writeEmote($type,$shortcut,$size);
     
  function writeEmote($type,$shortcut,$size) {
    $image = imageCreateFromPNG("emotes/$size/$shortcut.png");
    imageAlphaBlending($image, false);
    imageSaveAlpha($image, true);
    imagePNG($image);
    imageDestroy($image);
  }  
?>