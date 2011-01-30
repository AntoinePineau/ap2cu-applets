package com.ap2cu.decompile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;

import net.sf.jdec.config.Configuration;
import net.sf.jdec.main.ConsoleLauncher;

import com.ap2cu.utils.FileUtils;

public class Decompile {

  public static File decompile(String jarFile) {
    long id = System.currentTimeMillis();
    File sourceZipFile = new File(jarFile.replaceAll("^(.*)\\.jar$", "$1_src.zip"));
    String bin = "bin"+id;
    String src = "src"+id;
    
    File binFolder = new File(bin);
    File srcFolder = new File(src);
    
    if(!binFolder.exists())
      binFolder.mkdirs();
    if(!srcFolder.exists())
      srcFolder.mkdirs();
    
    Configuration.setFileExtension("java");
    Configuration.setTempDir(bin);
    Configuration.setSkipClassVersionCheck("true");
    Configuration.setBkpoppath(src);
    ConsoleLauncher.decompileJarAndZip(jarFile, sourceZipFile.getAbsolutePath());
    FileUtils.deleteFile(binFolder);
    FileUtils.deleteFile(srcFolder);
    
    return sourceZipFile;     
  }
  
  public static void main(String[] args) throws Exception {
    String jar = "C:\\Users\\Antoine PINEAU\\workspace\\AP2cu - Applets\\lib\\schneider-oreo-core-1.0.0.jar";
    System.out.println(decompile(jar));
  }
}