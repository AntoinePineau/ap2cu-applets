<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
 
  <xsl:template match="/translation/to">
    <tr>
      <td><img src="{@img}" alt="{@label}" title="{@label}" style="border: 1px solid #000;"/></td>
      <td>&#160;</td>
      <td><b><xsl:value-of select="."/></b></td>
    </tr>
  </xsl:template>
   
  <xsl:template match="/">
    <html>
      <head>
        <meta content="text/html; charset='UTF-8'" http-equiv="content-type"/>
        <script type='text/javascript' src='http://www.google-analytics.com/ga.js'></script>
        <script type='text/javascript' src='http://ap2cu.com/res/js/stat.js'></script>
      </head>
      <body>
        <table style="border:none">
          <tr>
            <td colspan="3">Original Text: <b><xsl:value-of disable-output-escaping="yes" select="/translation/text"/></b><br/></td>
          </tr>
          <tr><td colspan="3">&#160;</td></tr>
          <xsl:apply-templates select="/translation/to"/>          
        </table>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>