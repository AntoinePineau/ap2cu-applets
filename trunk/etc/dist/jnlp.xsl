<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />

  <xsl:template match="/">
    <jnlp spec="1.0+" codebase="{/applets/codebase}" href="{/applets/jnlp}">
      <information>
        <title><xsl:value-of select="/applets/name"/></title>
        <vendor><xsl:value-of select="/applets/vendor"/></vendor>
      </information>
      <resources>
        <j2se version="1.6+" href="http://java.sun.com/products/autodl/j2se" />
        <xsl:apply-templates select="/applets/classpath/jar" />
      </resources>
      <xsl:apply-templates select="/applets/applet" />
      <update check="background" />
    </jnlp>
  </xsl:template>

  <xsl:template match="//jar">
    <jar href="{.}">
      <xsl:if test="normalize-space(@version)!=''"><xsl:attribute name="version"><xsl:value-of select="@version"/></xsl:attribute></xsl:if>
      <xsl:attribute name="main">
        <xsl:choose>
          <xsl:when test="normalize-space(@main)='true'">true</xsl:when>
          <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
      </xsl:attribute>
    </jar>
  </xsl:template>
  
  <xsl:template match="//applet">
    <applet-desc name="{@name}" main-class="{@class}" width="{@width}" height="{@height}"></applet-desc>
  </xsl:template>
  
</xsl:stylesheet>
