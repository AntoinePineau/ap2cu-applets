<?php

  if(isset($_SERVER['HTTP_X_FORWARDED_FOR']))
    $ipaddress = $_SERVER['HTTP_X_FORWARDED_FOR'];
  else if(isset($_SERVER['HTTP_CLIENT_IP']))
    $ipaddress = $_SERVER['HTTP_CLIENT_IP'];
  else if($ipaddress=='')
    $ipaddress = $_SERVER['REMOTE_ADDR'];

?>
<html>
  <head>
    <title>Geolocalisation by browser, IP or address</title>
    <meta http-equiv='Content-Type' content='text/html; charset=utf-8' />
    <link type='text/css' href='/res/css/common.css' rel='stylesheet'/>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js"></script>
    <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>
    <script type="text/javascript" src="http://ap2cu.com/res/js/stat.js"></script>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
    <script type="text/javascript" src="js/geocode.js"></script>
    <script type="text/javascript">
    function locate() {
      var mode = $('input[name=by]:checked').val();
      $('#loading').html('&#160;&#160;<img src="/res/img/ajax-loader.gif" title="loading..." alt="loading..."/>&#160;&#160;Wait...');
      if(mode == 'ip') {
        var ip = $('#ip').val();
        $.get('service/json/?ip='+ip, function(data) {
          var x = parseFloat(data.address.location.coordinates.latitude);
          var y = parseFloat(data.address.location.coordinates.longitude);
          positionToAddress(new google.maps.LatLng(x, y), ip);
        });
      }
      else if(mode == 'address') {
        var ip = $('#ip').val();
        addressToLatLng($('#address').val());
      }
      else if(mode == 'browser') {
        localize();
      }
    }
    function changeMode() {
      var mode = $('input[name=by]:checked').val();
      $('td.address, td.ip').hide();
      $('td.'+mode).show();
    }
    </script>
  </head>
  <body onload="initialize();changeMode();">
    <h1>Geolocalisation by :
      <label><input type="radio" name="by" value="browser" checked="checked" onClick="changeMode()"/>Browser</label>&#160;&#160;
      <label><input type="radio" name="by" value="ip" onClick="changeMode()"/>IP</label>&#160;&#160;
      <label><input type="radio" name="by" value="address" onClick="changeMode()"/>Address</label>
    </h1>
    <form onSubmit="locate()" action="javascript:void(0)">
      <table>
        <tr>
          <td class="ip"><label for="ip">IP:</label></td>
          <td class="ip">
            <input type="text" id="ip" name="ip" value="<?=$ipaddress?>"/>
          </td>
          <td class="address"><label for="address">Address:</label></td>
          <td class="address">
            <textarea id="address" name="address"></textarea>
          </td>
          <td>
            <input type="submit" value="Locate"/>
          </td>
          <td id="loading">&#160;</td>
        </tr>
      </table>
    </form>
    <div id="map_canvas" style="width:100%;height:80%;"></div>
  </body>
</html>