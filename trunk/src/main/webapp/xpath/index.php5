<?php
$xmlAsString = $_POST["xml"];
$xslAsString = $_POST["xsl"];
$xpath = $_POST["xpath"];
$mode = $_POST["mode"];
?>
<html>
  <head>
    <title>XML / XSL / XPath online tool</title>
    <style type="text/css">
      html, html * { font: 0.95em Courier New; }
      h1 { text-decoration:underline; display:inline; }
      label { font-weight:bold;font-size:1.1em; }
      .pointer {cursor:pointer; }
      #result { float:right; width:50%; }
      #html { border:solid 1px #000; height:80%; }
      #form { display:inline; }
    </style>
    <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/stat.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/jquery.js"></script>
    <link type="text/css" href="/res/css/common.css" rel="stylesheet"/>
    <script type="text/javascript">
      function modeChanged(radio) {
        $('div[class="input"]').hide();
        $('#'+radio.value).show();
      }
    </script>
  </head>
  <body>
<?
function asXML($node) {
  $tmp_doc = new DOMDocument();
  $tmp_doc->appendChild($tmp_doc->importNode($node,true));
  return escapeXML($tmp_doc->saveHTML());
}
function escapeQuote($text) {
  return str_replace('\"','"', str_replace("\'","'", $text));
}
function escapeXML($text) {
  return str_replace("\n",'<br/>', str_replace('>','&gt;', str_replace('<','&lt;', str_replace(' ','&nbsp;', $text))));
}

if(isset($xmlAsString)) {
  $xmlAsString = escapeQuote($xmlAsString);
  $xslAsString = escapeQuote($xslAsString);
  $xpath = escapeQuote($xpath);
  try {
    $xml = new DOMDocument(); $xml->loadXML($xmlAsString);
  }
  catch(Exception $e) { echo "Error while loading XML: ".$e->getMessage(); }
  if($mode=='xsl') {
    try {
      $xsl = new DOMDocument(); $xsl->loadXML($xslAsString);
      $proc = new XSLTProcessor();  $proc->importStyleSheet($xsl);
      $result = $proc->transformToXML($xml);
    }
    catch(Exception $e) { echo "Error while transforming XML with XSL: ".$e->getMessage(); }
  }
  else if($mode=='xpath') {
    try {      
      $xp = new domxpath($xml);
      $nodes = $xp->query($xpath);
      
      $root = '';
      $nodesRoot = $xp->query('/');
      foreach ($nodesRoot as $node) { $root = simplexml_import_dom($node)->asXML(); }
      $result = escapeXML($root);
      
      $pattern = '_vrPfd5|kdFr_R_T0oep__';
      $attributesArray = array();
      $nodesArray = array();
      foreach ($nodes as $node) {
        $value = "";
        if(!$node instanceof DOMAttr) array_push($nodesArray, $node);
        else array_push($attributesArray, $node);        
      }
      
      $valuesArray = array();
      if(!empty($attributesArray)) {
        $attributes = $xp->query('//@*');
        foreach ($attributes as $attribute) {
          foreach($attributesArray as $node) {
            if($attribute === $node) {
              $node->value .= $pattern;
              $value = $node->name.'="'.$node->value.'"';
              array_push($valuesArray,$value);
              break;
            }
          }
        }
        foreach ($nodesRoot as $n) { $root = simplexml_import_dom($n)->asXML(); }
        $result = escapeXML($root);
        
        for ($i = 0; $i < count($attributesArray); $i++) {
          $node = $attributesArray[$i];
          $node->value = str_replace($pattern, '', $node->value);          
          $result = str_replace($valuesArray[$i], '<font>'.$node->name.'="'.$node->value.'"'.'</font>', $result); 
        }
      }
      else if(!empty($nodesArray)) {      
        $allNodes = $xp->query('//*');
        foreach ($allNodes as $oneNode) {
          foreach($nodesArray as $node) {
            $result = str_replace(asXML($node), '<font>'.asXML($node).'</font>', $result);
          }
        }
      }
    }
    catch(Exception $e) { echo "Error while applying XPath query to XML: ".$e->getMessage(); }
  }  
}
?>
    <form name="xmlForm" action="" method="POST">
      <h1>
        <label>XML</label> and 
        <input  class="pointer" id="xslRadio" type="radio" value="xsl" name="mode" <?if(!isset($mode) || $mode=='xsl'){?>checked="checked"<?}?> onclick="modeChanged(this)">
        <label for="xslRadio" class="pointer">XSL</label>
         / 
        <input  class="pointer"id="xpathRadio" type="radio" value="xpath" name="mode" <?if($mode=='xpath'){?>checked="checked"<?}?> onclick="modeChanged(this)">
        <label for="xpathRadio" class="pointer">XPath</label>
         online tool</h1>
      &nbsp;(<a href="http://www.xsltfunctions.com/xsl/">XSL Help</a> and
      <a href="http://www.zvon.org/xxl/XPathTutorial/General/examples.html">XPath Help</a>)
      <br/><br/>   
    <?if(isset($result)) {?>
    <div id="result">
      Result:<br/>
      <div id="html"><?=$result?></div>
    </div>
    <?}?>  
      <div id="form">
        <div id="xml">
          <h3>XML Document as a String:</h3>
          <textarea cols="80" rows="15" name="xml"><?if(isset($xmlAsString)) {echo $xmlAsString;} else {?>
<root>
  <level id="1">
    <label lang="en">Level One</label>
    <label lang="fr">Niveau Un</label>
  </level>
  <level id="2">
    <label lang="en">Level Two</label>
    <label lang="fr">Niveau Deux</label>
  </level>
  <level id="3">
    <label lang="en">Level Three</label>
    <label lang="fr">Niveau Trois</label>
  </level>
</root><?}?></textarea><br/><br/>
        </div>
        <div class="input" id="xsl" style="display:<?if(!isset($mode) || $mode=='xsl') echo 'block'; else echo 'none';?>">
          <h3>XSL Transformation as a String:</h3>
          <textarea cols="80" rows="10" name="xsl"><?if(isset($xslAsString)) {echo $xslAsString;} else {?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <xsl:value-of select="/root/level[@id='2']/label[@lang='fr']"/>
  </xsl:template >
</xsl:stylesheet><?}?></textarea><br/><br/>
        </div>
        <div class="input" id="xpath" style="display:<?if($mode=='xpath') echo 'block'; else echo 'none';?>">
          <h3>XPath Query:</h3>
          <textarea cols="80" rows="10" name="xpath"><?if(isset($xpath)) {echo $xpath;} else {?>
/root/level[@id='2']/label[@lang='fr']<?}?>
          </textarea><br/><br/>
        </div>
        <input type="submit" value="Run"></input>
      </div>
    </form>
  </body>
</html>