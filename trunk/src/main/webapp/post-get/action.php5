<?php
header("Content-Type:text/xml");
function display_post_get_header() {
  echo "<request>\n";
  if($_GET || $_POST) {
    echo "  <params>\n";
    if ($_POST) {
      echo "    <post>\n";
      while (list($key, $val) = each($_POST))
        echo "      <param name='$key'><![CDATA[$val]]></param>\n";
      echo "    </post>\n";
    }
    if ($_GET) {
      echo "    <get>\n";
      while (list($key, $val) = each($_GET))
        echo "      <param name='$key'><![CDATA[$val]]></param>\n";
      echo "    </get>\n";
    }
    echo "  </params>\n";
  }
  if ($_SERVER) {
    echo "  <headers>\n";
    $http = "";
    $geoip = "";
    $remote = "";
    $request = "";
    $server = "";
    $content = "";
    $script = "";
    $misc = "";
    $other = "";
    while (list($key, $val) = each($_SERVER)) {
      if(strpos($key,'CONTENT_')===0) {
        $content .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
      else if(strpos($key,'SCRIPT_')===0) {
        $script .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
      else if(strpos($key,'GEOIP_')===0) {
        $geoip .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
      else if(strpos($key,'REMOTE_')===0) {
        $remote .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
      else if(strpos($key,'REQUEST_')===0 || strpos($key,'HTTP_')===0) {
        $keyName = strpos($key,'HTTP_')===0 ?substr($key,5) : $key;
        $request .= "      <header name='$keyName'><![CDATA[$val]]></header>\n";
      }
      else if(strpos($key,'SERVER_')===0) {
        $server .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
      else if(strpos($key,'DOCUMENT_ROOT')===0 || strpos($key,'UNIQUE_ID')===0 || strpos($key,'PATH')===0 || strpos($key,'UID')===0) {
        $misc .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
      else {
        $other .= "      <header name='$key'><![CDATA[$val]]></header>\n";
      }
    }
    if($request!="") echo "    <request>\n$request    </request>\n";
    if($content!="") echo "    <content>\n$content    </content>\n";
    //if($script!="") echo "    <script>\n$script    </script>\n";
    //if($geoip!="") echo "    <geoip>\n$geoip    </geoip>\n";
    if($remote!="") echo "    <remote>\n$remote    </remote>\n";
    if($server!="") echo "    <server>\n$server    </server>\n";
    //if($misc!="") echo "    <misc>\n$misc    </misc>\n";
    if($other!="") echo "    <others>\n$other    </others>\n";
    
    echo "  </headers>\n";
  }
  echo "</request>\n";
}
// End of display_post_get
echo "<?xml version='1.0' encoding='UTF-8'?>\n";
display_post_get_header();
?>
