<?php 

header("Content-Type:application/json");



function logError($message) {
  echo json_encode(array("error"=>$message));
  die;
}

function getIPs() {
  // GET IP
  $ipaddress = $_GET["ip"];
  if(isset($_SERVER['HTTP_X_FORWARDED_FOR']))
    $ipaddress = $_SERVER['HTTP_X_FORWARDED_FOR'];
  else if(isset($_SERVER['HTTP_CLIENT_IP']))
    $ipaddress = $_SERVER['HTTP_CLIENT_IP'];
  else if($ipaddress=='')
    $ipaddress = $_SERVER['REMOTE_ADDR'];
  return split(',',$ipaddress);
}

function readURL($url) {
  $myurl = parse_url($url);
  $host = $myurl["host"];
  $path = $myurl["path"] . "?" . $myurl["query"];
  $timeout = 1;
  $fp = fsockopen ($host, 80, $errno, $errstr, $timeout) or logError('Can not open connection to server.');
  if ($fp) {
    fputs ($fp, "GET $path HTTP/1.0\nHost: " . $host . "\n\n");
    while (!feof($fp)) {
      $buf .= fgets($fp, 128);
    }
    $lines = split("\n", $buf);
    $out = $lines[count($lines)-1];
    fclose($fp);
  }
  return $out;
}

function getLocationWithMaxmind($ipaddress) {
  $maxmind = readURL("http://geoip1.maxmind.com/f?l=C6gdjbKW6Qi9&i=" . $ipaddress);
  list($mm_countrycode, $mm_regioncode, $mm_city, $mm_zipcode, $mm_latitude, $mm_longitude, $mm_metrocode, $mm_areacode, $mm_isp, $mm_organization) = split(",", $maxmind, 10);
  $fips = file_get_contents("http://www.maxmind.com/app/fips_include");
  eregi(".*".$mm_countrycode.",".$mm_regioncode.",\"([^\"]*)\"",$fips,$group);
  $mm_regionname=$group[1];

  //FR,A8,Colombes,,48.916698,2.250000,0,0,"Free SAS","Free SAS"
  return array($mm_countrycode, $mm_regioncode, $mm_regionname, $mm_city, $mm_zipcode, $mm_latitude, $mm_longitude, $mm_metrocode, $mm_areacode, $mm_isp, $mm_organization);
}

function getLocationWithWeLiveFr($ipaddress) {
  // WELIVE.FR method
    
  $inetaddr=ip2long($ipaddress);
  $ipvis=long2ip($inetaddr);

  $sock = fsockopen("www.welive.fr", 80, $errno, $errstr, 30);
  if (!$sock) logError("$errstr ($errno)\n");
  $data = "idweb=" . urlencode("2141291304"). "&codecrypt=" . urlencode("61b7bb7db3622a765ed7dcb82cb2dee0"). "&ipvis=" . urlencode($ipvis). "&monsite=" .urlencode(""); // Inscrivez entre les guillemets l'adresse Web de votre site si vous d�sirez qu'on fasse un lien vers votre site
  fputs($sock, "POST /membres/webmaster/geoip.php HTTP/1.1\r\n");
  fputs($sock, "Host: www.welive.fr\r\n");
  fputs($sock, "Content-type: application/x-www-form-urlencoded\r\n");
  fputs($sock, "Content-length: " . strlen($data) . "\r\n");
  fputs($sock, "Accept: */*\r\n");fputs($sock, "\r\n");
  fputs($sock, "$data\r\n");fputs($sock, "\r\n");
  $headers = "";
  while ($str = trim(fgets($sock, 4096))) {
    $headers .= "$str\n";
  }
  $body = "";
  while (!feof($sock)) {
    $body .= fgets($sock, 4096);
  }
  fclose($sock);
  //// TRANSFORMATION DES INFORMATIONS EN VARIABLES
  try {
    eval($body);
  }
  catch(Exception $e) {

  }
  //// FIN DU SCRIPT      
  return array($provider,$ip2ville,$ip2depcp,$ip2dep,$ip2pays,$ip2payscode,$ip2paysimg);
}

function getGoogleMap($countrycode,$latitude,$longitude) {
  $ll=$latitude.','.$longitude;
  return 'http://maps.google.com/maps?hl='.$countrycode.'&sll='.$ll.'&ll='.$ll.'&q='.$ll.'&z=14&output=embed';
}

?>
<?
$ipaddresses = getIPs();
$minLong=999999999;
$maxLong=-999999999;
$minLat=999999999;
$maxLat=-999999999;

$jsonString = "";

foreach($ipaddresses as $ipaddress) {
  list($mm_countrycode, $mm_regioncode, $mm_regionname, $mm_city, $mm_zipcode, $mm_latitude, $mm_longitude, $mm_metrocode, $mm_areacode, $mm_isp, $mm_organization) =  getLocationWithMaxMind($ipaddress); 
  //list($provider,$ip2ville,$ip2depcp,$ip2dep,$ip2pays,$ip2payscode,$ip2paysimg)=getLocationWithWeLiveFr($ipaddress);

  $countrycode=$mm_countrycode; if($countrycode=='')$countrycode=$ip2payscode;
  eregi(".*<img src=\"([^\"]*)\".*",$ip2paysimg,$group);
  $countryimg=$group[1];
  eregi(".*<span[^>]*>(.*)</span>.*",$ip2pays,$group);
  $countryname=$mm_countryname; if($countryname=='')$countryname=$group[1];
  $zipcode=$mm_zipcode; if($zipcode=='')$zipcode=$ip2depcp;
  eregi(".*<span[^>]*>(.*)</span>.*",$ip2ville,$group);
  $city=$group[1];
  eregi("\"(.*)\"",$mm_isp,$group);
  $mm_isp=$group[1];
  eregi("\"(.*)\"",$mm_organization,$group);
  $mm_organization=$group[1];
  if(isset($minLong)){if($minLong>$mm_longitude){$minLong=$mm_longitude;}}else{$minLong=$mm_longitude;}
  if(isset($maxLong)){if($maxLong<$mm_longitude){$maxLong=$mm_longitude;}}else{$maxLong=$mm_longitude;}
  if(isset($minLat)){if($minLat>$mm_latitude){$minLat=$mm_latitude;}}else{$minLat=$mm_latitude;}
  if(isset($maxLat)){if($maxLat<$mm_latitude){$maxLat=$mm_latitude;}}else{$maxLat=$mm_latitude;}

  $jsonString = array(
      "address" => array(
          "ip"       => $ipaddress,
          "provider" => array (
              "isp"          => $mm_isp ,
              "organization" => $mm_organization ,
              "name"         => $provider 
          ) ,
          "location" => array (
              "coordinates"  => array (
                  "latitude"  => $mm_latitude ,
                  "longitude" => $mm_longitude
              ),
              "details"      => array (
                  "country"    => array (
                      "code" => $countrycode ,
                      "name" => $countryname
                  )  ,
                  "region"     => array (
                      "code" => $mm_regioncode ,
                      "name" => $mm_regionname
                  )  ,
                  "department" => array (
                      "code" => $ip2dep
                  )  ,
                  "city"       => array (
                      "code" => $zipcode ,
                      "name" => $city
                  )
              )           
          )
      )
  );
}
  echo json_encode($jsonString);
?>

