<?php
 header('Content-Type: text/html; charset=iso-8859-1'); 
$text = $_GET["text"];

echo "
<html>
  <head>
    <title>Encoding characters in UTF-8 / Online Tool</title>
    <script type='text/javascript' src='http://www.google-analytics.com/ga.js'></script>
    <script type='text/javascript' src='http://ap2cu.com/res/js/stat.js'></script>
    <link type='text/css' href='/res/css/common.css' rel='stylesheet'/>
    <style type='text/css'>
      textarea { margin-bottom: 20px }
    </style>
    <script>
function dec2hex ( textString ) {
 return (textString+0).toString(16).toUpperCase();
}

function  dec2hex2 ( textString ) {
  var hexequiv = new Array ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
  return hexequiv[(textString >> 4) & 0xF] + hexequiv[textString & 0xF];
}

function  dec2hex4 ( textString ) {
  var hexequiv = new Array ('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');
  return hexequiv[(textString >> 12) & 0xF] + hexequiv[(textString >> 8) & 0xF] + hexequiv[(textString >> 4) & 0xF] + hexequiv[textString & 0xF];
}

function convertCP2Char ( textString, special ) {
  var outputString = '';
  textString = textString.replace(/^\s+/, '');
  if (textString.length == 0) { return ''; }
  textString = textString.replace(/\s+/g, ' ');
  var listArray = textString.split(' ');
  for ( var i = 0; i < listArray.length; i++ ) {
    var n = parseInt(listArray[i], 16);
    if (n <= 0xFFFF) {
      outputString += String.fromCharCode(n);
    } else if (n <= 0x10FFFF) {
      n -= 0x10000
      outputString += String.fromCharCode(0xD800 | (n >> 10)) + String.fromCharCode(0xDC00 | (n & 0x3FF));
    } else {
      outputString += '!erreur ' + dec2hex(n) +'!';
    }
  }
  return( outputString );
}


function convertCP2URL ( textString, special ) {
  return convertCP2UTF16(textString, special).replace(/&#x([^;]+);/gi, '%$1').replace(/ /,'%20');
}


function convertCP2UTF16 ( textString, special ) {
  var outputString = '';
  textString = textString.replace(/^\s+/, '');
  if (textString.length == 0) { return ''; }
  textString = textString.replace(/\s+/g, ' ');
  var listArray = textString.split(' ');

  var excludedChars = convertChar2CP(document.unicode.excludedChars.value).split(' ');
  var regexString = '^(3[0-9]|4[1-9A-F]|5[0-9AF]|6[1-9A-F]|7[0-9A]';
  for ( var i = 0; i < excludedChars.length; i++ )
    regexString += '|'+excludedChars[i];
  regexString += ')$';  
  var regex = new RegExp(regexString, 'i');
  
  for ( var i = 0; i < listArray.length; i++ ) {
    var n = parseInt(listArray[i], 16);
    if(special && regex.test(listArray[i])) { 
      outputString += convertCP2Char(listArray[i], true);
    }
    else {
      outputString += '&#x' + dec2hex(n) + ';';
    }
  }
  return( outputString );
}


function convertCP2UTF8 ( textString, special ) {
  var outputString = '';
  textString = textString.replace(/^\s+/, '');
  if (textString.length == 0) { return ''; }
  textString = textString.replace(/\s+/g, ' ');
  var listArray = textString.split(' ');
  
  var excludedChars = convertChar2CP(document.unicode.excludedChars.value).split(' ');
  var regexString = '^(3[0-9]|4[1-9A-F]|5[0-9AF]|6[1-9A-F]|7[0-9A]';
  for ( var i = 0; i < excludedChars.length; i++ )
    regexString += '|'+excludedChars[i];
  regexString += ')$';  
  var regex = new RegExp(regexString, 'i');
  
  for ( var i = 0; i < listArray.length; i++ ) {
    var n = parseInt(listArray[i], 16);
    //alert(regex+' :::: '+listArray[i]+' :::: '+regex.test(listArray[i]));
    if(special && regex.test(listArray[i])) { 
      outputString += convertCP2Char(listArray[i], true);
    }
    else {
      outputString += ('&#' + n + ';');
    }
  }
  return( outputString );
}


function convertChar2CP ( textString, special ) {
  var outputString = '';
  var haut = 0;
  var n = 0;
  for (var i = 0; i < textString.length; i++) {
    var b = textString.charCodeAt(i);  // alert('b:'+dec2hex(b));
      if (b < 0 || b > 0xFFFF) {
        outputString += '!erreur ' + dec2hex(b) + '!';
      }
      if (haut != 0) {
        if (0xDC00 <= b && b <= 0xDFFF) {
          outputString += dec2hex(0x10000 + ((haut - 0xD800) << 10) + (b - 0xDC00)) + ' ';
          haut = 0;
          continue;
        } else {
          outputString += '!erreur ' + dec2hex(haut) + '!';
          haut = 0;
        }
      }
      if (0xD800 <= b && b <= 0xDBFF) {
        haut = b;
      } else {
        outputString += dec2hex(b) + ' ';
      }
  }
  return( outputString.replace(/ $/, '') );
}


function convertURL2CP ( textString, special ) {
  return convertUTF162CP(textString.replace(/%([^%]{2})/gi, '&#x$1;'), special);
}


function convertUTF162CP ( textString, special ) {
  if(!special) 
    return textString.replace(/&#x/gi,'').replace(/;/g,' ').replace(/ $/,'');
  
  var outputString = '';
  for(var i=0;i<textString.length;i++) {
    if (i > 0) { outputString += ' ';}
    var c = textString.charAt(i);
    if(c==' ') {
      outputString += '20';
    }
    else if(c!='&') {
      outputString += convertChar2CP(c, true);
    }
    else {
      var b = c;
      while(c!=';' && i<textString.length) {
        c = textString.charAt(++i);
        b += c;
      }
      outputString += b.replace(/&#x([^;]+);/gi,'$1');
    }
  }
  return outputString;
}


function convertUTF82CP ( textString, special ) {
  var outputString = '';
  textString = textString.replace(/^\s(.*)\s$/g, '$1');
  
  if(special) {
    for (var i = 0; i < textString.length; i++) {
      if (i > 0) { outputString += ' ';}
      var c = textString.charAt(i);
      if(c==' ') {
        outputString += '20';
      }
      else if(c!='&') {
        outputString += convertChar2CP(c, true);
      }
      else {
        var b = c;
        while(c!=';' && i<textString.length) {
          c = textString.charAt(++i);
          b += c;
        }
        outputString += dec2hex(parseInt(b.replace(/&#([^;]+);/gi,'$1'), 10));
      }
    }
  }
  else {
    var listArray = textString.split(';');
    for (var i = 0; i < listArray.length-1; i++) {
      if (i > 0) { outputString += ' ';}
      var c = listArray[i].substring(2, listArray[i].length);
      var n = parseInt(c, 10);
      outputString += dec2hex(n);
    }
  }
  return( outputString );
}

function URLencode(textString, special) {
  return encodeURIComponent(textString);
}
function URLdecode(textString, special) {
  return decodeURIComponent(textString.replace(/\+/g,  ' '));
}


function decodedChanged() {
  var special = document.unicode.specialonly.checked;
  document.unicode.codePoints.value = convertChar2CP(document.unicode.decoded.value, special);
  document.unicode.utf8Encoded.value = convertCP2UTF8(document.unicode.codePoints.value, special);
  document.unicode.utf16Encoded.value = convertCP2UTF16(document.unicode.codePoints.value, special);
  document.unicode.urlEncoded.value = URLencode(document.unicode.decoded.value, special);
}
function urlEncodedChanged() {
  var special = document.unicode.specialonly.checked;
  
  document.unicode.decoded.value = URLdecode(document.unicode.urlEncoded.value, special);
  document.unicode.codePoints.value = convertChar2CP(document.unicode.decoded.value, special);
  
  //document.unicode.codePoints.value = convertURL2CP(document.unicode.urlEncoded.value, special);
  document.unicode.utf8Encoded.value = convertCP2UTF8(document.unicode.codePoints.value, special);
  document.unicode.utf16Encoded.value = convertCP2UTF16(document.unicode.codePoints.value, special);
  //document.unicode.decoded.value = convertCP2Char(document.unicode.codePoints.value, special);
}
function utf8EncodedChanged() {
  var special = document.unicode.specialonly.checked;
  document.unicode.codePoints.value = convertUTF82CP(document.unicode.utf8Encoded.value, special);
  document.unicode.decoded.value = convertCP2Char(document.unicode.codePoints.value, special);
  document.unicode.utf16Encoded.value = convertCP2UTF16(document.unicode.codePoints.value, special);
  document.unicode.urlEncoded.value = URLencode(document.unicode.decoded.value, special);
}
function utf16EncodedChanged() {
  var special = document.unicode.specialonly.checked;
  document.unicode.codePoints.value = convertUTF162CP(document.unicode.utf16Encoded.value, special);
  document.unicode.utf8Encoded.value = convertCP2UTF8(document.unicode.codePoints.value, special);
  document.unicode.decoded.value = convertCP2Char(document.unicode.codePoints.value, special);
  document.unicode.urlEncoded.value = URLencode(document.unicode.decoded.value, special);
}
function specialChanged() {
  decodedChanged();
}
</script></head>";
?>
<body onload="decodedChanged()">
<h1>Encoding characters - Online Tool</h1>
<form name="unicode" action="javascript:void(0)">
  <input type="hidden" size="50" name="codePoints"/>
  <table border="0">
    <tr>
      <td colspan="3">Only encode special characters that don't match [<a target="_blank" href="/tools/regex/lesson.html">\w</a><input name="excludedChars" type="text" value="., ();:" onchange="specialChanged();"/>] <input type="checkbox" name="specialonly" onchange="specialChanged();"/></td>
    </tr>
    <tr><td colspan="3">&#160;</td></tr>
    <tr>
      <td><h3>Decoded text:</h3></td>
      <td>&#160;</td>
      <td><h3>URL Encoded text:</h3></td>
    </tr>
    <tr>
      <td><textarea name="decoded" rows="7" cols="50" onblur="decodedChanged();"><?php echo $text; ?></textarea></td>
      <td>&#160;</td>
      <td><textarea name="urlEncoded" rows="7" cols="50" onblur="urlEncodedChanged();"></textarea></td>
    </tr>
    <tr>
      <td><h3>UTF-16 Encoded text:</h3></td>
      <td>&#160;</td>
      <td><h3>UTF-8 Encoded text:</h3></td>
    </tr>
    <tr>
      <td><textarea name="utf16Encoded" rows="7" cols="50" onblur="utf16EncodedChanged();"></textarea></td>
      <td>&#160;</td>
      <td><textarea name="utf8Encoded" rows="7" cols="50" onblur="utf8EncodedChanged();"></textarea></td>
    </tr>
  </table>
</form>
<br/>
</body>
</html>