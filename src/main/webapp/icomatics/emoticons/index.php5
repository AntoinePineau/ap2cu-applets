<?php
  $size=strtolower($_GET["size"]);
  $size = $size == "small" ? 16 : 24;
  $shortcut=strtolower($_GET["shortcut"]);
  $type=strtolower($_GET["type"]);
  if($type!="jpeg" && $type!="gif") $type="png";
  header("Content-type: image/".$type);

  writeEmote($type,$shortcut,$size);
     
  function writeEmote($type,$shortcut,$size) {
    $image = imageCreateFromPNG("emotes/$size/$shortcut.png");
    $bounds = createTransparentImage($size, $size);

    if(imageCopy($bounds, $image, 0, 0, 0, 0, $size, $size)) {
      if($type=="gif") imageGIF($bounds);
      else if($type=="jpeg") imageJPEG($bounds);
      else imagePNG($bounds);
    }
    imageDestroy($bounds);
    imageDestroy($image);
  }  


  function createTransparentImage($w, $h) {
    $im = imageCreateTrueColor($w, $h);
    imageAlphaBlending($im, false);
    imageSaveAlpha($im, true);
    return $im;
  }
?>