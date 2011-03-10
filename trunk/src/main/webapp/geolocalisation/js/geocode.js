var map, geocoder, iter, directionsService;

var i = 0;
var bounds = new google.maps.LatLngBounds();

function initialize() {
  var position = new google.maps.LatLng(48.85667, 2.350987);

  // ROADMAP, SATELLITE, HYBRID
  var options = {zoom:5,center:position,mapTypeId:google.maps.MapTypeId.HYBRID};
  map = new google.maps.Map(document.getElementById('map_canvas'), options);
  geocoder = new google.maps.Geocoder();
  directionsService = new google.maps.DirectionsService();
}

function latlngToPosition(latlng) {
  var regex = new RegExp('^\\(([\\d.]+), ([\\d.]+)\\)$');
  var lat = parseFloat(latlng.replace(regex,'$1'));
  var lng = parseFloat(latlng.replace(regex,'$2'));
  return new google.maps.LatLng(lat, lng);
}

function addMarker(map,position,icon,letter,background,foreground,tooltip,info) {
  var icon = 'http://chart.apis.google.com/chart?chst=d_map_pin_'+(icon?'icon':'letter')+'&chld='+letter+'|'+background+'|'+foreground;
  var marker = new google.maps.Marker({map:map,position:position,title:tooltip,icon:icon});
  var infoWindow = new google.maps.InfoWindow();
  infoWindow.setContent(info);              
  google.maps.event.addListener(marker, 'click', function(){infoWindow.open(map,marker)});
  bounds.extend(position);
  map.fitBounds(bounds);
//  map.setCenter(position);
//  map.setZoom(10);
}
  
function addressToLatLng(address) {
  geocoder.geocode({'address':address},function(results,status){
    if(status==google.maps.GeocoderStatus.OK) {
      if(results[0]) {
        var position = results[0].geometry.location;
        getElevation(position, function(elevation){
          $('#position').val(position);
          addMarker(map,position,false,i++,'FFFF88','000000',address,'Address: '+address+'<br/>Position: '+position+'<br/>Elevation: '+elevation+" meters");
        });
      }
      else {
        alert("No results found");
      }
    }
    else {
      alert("Unable to geocode "+address+": "+status);
    }         
  });
  return false;
}
  
function positionToAddress(position, ip) {
  getElevation(position, function(elevation){
    geocoder.geocode({'latLng':position},function(results,status){
      if(status==google.maps.GeocoderStatus.OK) {
        if(results[0]) {
          var address = results[0].formatted_address;
          $('#address').val(address);
          addMarker(map,position,false,i++,'88FF88','000000',address,'Address: '+address+'<br/>Position: '+position+'<br/>Elevation: '+elevation+" meters");
        }
        else {
          alert("No results found");
        }
      }
      else {
        alert("Unable to geocode "+address+": "+status);
      }
    });
  });
}        

function getElevation(position, callback) {
  var locations = [];
  var elevation = "";
  locations.push(position);
  var elevator = new google.maps.ElevationService();
  elevator.getElevationForLocations({'locations':locations},function(results,status){
    if(status==google.maps.ElevationStatus.OK) {
      if(results[0]) {
        elevation = results[0].elevation;
        $('#loading').html('');
        callback(elevation);
      }
      else {
        alert("No results found");
      }
    }
    else {
      alert("Unable to geocode "+address+": "+status);
    }
  });
}

function localize() {
  var initialLocation;
  // Try W3C Geolocation (Preferred)
  if(navigator.geolocation) {
    browserSupportFlag = true;
    navigator.geolocation.getCurrentPosition(function(position) {
      initialLocation = new google.maps.LatLng(position.coords.latitude,position.coords.longitude);
      getElevation(initialLocation, function(elevation){
        geocoder.geocode({'latLng':initialLocation},function(results,status){
          if(status==google.maps.GeocoderStatus.OK) {
            if(results[0]) {
              var address = results[0].formatted_address;
              $('#address').val(address);
              addMarker(map,initialLocation,true,'home','FF0000','000000',address,'Address: '+address+'<br/>Position: '+initialLocation+'<br/>Elevation: '+elevation+" meters");
            }
            else {
              alert("No results found");
            }
          }
          else {
            alert("Unable to geocode "+address+": "+status);
          }
        });
      });
    }, function() {
      handleNoGeolocation(browserSupportFlag);
    });
    
  // Try Google Gears Geolocation
  } else if (google.gears) {
    browserSupportFlag = true;
    var geo = google.gears.factory.create('beta.geolocation');
    geo.getCurrentPosition(function(position) {alert(position);
      initialLocation = new google.maps.LatLng(position.latitude,position.longitude);
      map.setCenter(initialLocation);
    }, function() {
      handleNoGeoLocation(browserSupportFlag);
    });
  // Browser doesn't support Geolocation
  } else {
    browserSupportFlag = false;
    handleNoGeolocation(browserSupportFlag);
  }
}
  
function handleNoGeolocation(errorFlag) {
  if (errorFlag == true) {
    alert("Geolocation service failed.");
  } else {
    alert("Your browser doesn't support geolocation.");
  }
}   

function calculateRoute(from,to,by) {
  directionsService.route({origin:from,destination:to,travelMode:by},function(response,status){
    if(status==google.maps.DirectionsStatus.OK) {
      iter.setDirections(response);
      alert("Distance: "+response.routes[0].legs[0].distance.value/1000+" km");
      alert("Temps de route: "+response.routes[0].legs[0].duration.value/3600+" heures");
    }
    else {
      alert("Unable to geocode "+address+": "+status);
    }  
  });
}

function go() {
  var from = $('input[name=from]:checked').val();
  if(from=='address') addressToLatLng($('#address').val());
  else if(from=='position') positionToAddress(latlngToPosition($('#position').val()));
  else if(from=='browser') localize();
  else if(from=='route') calculateRoute($('#from').val(), $('#to').val(), google.maps.DirectionsTravelMode.DRIVING);
}
