<?php
$width = $_GET['width'];
$height = $_GET['height'];
?>
<html>
  <head>
    <script>
      function execute() {
        window.top.top.resize(<?php echo $width;?>,<?php echo $height;?>);
      }
    </script>
  </head>
	<body onload="execute()"></body>
</html>