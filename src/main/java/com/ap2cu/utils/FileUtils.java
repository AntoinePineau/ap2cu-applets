package com.ap2cu.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileUtils {

  public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
    byte[] buffer = new byte[1024];
    int len;

    while ((len = in.read(buffer)) >= 0)
      out.write(buffer, 0, len);

    in.close();
    out.close();
  }

  @SuppressWarnings("unchecked")
  public static boolean unzipFileTo(File zipFile, File targetFolder) {
    if(!targetFolder.exists())
      targetFolder.mkdirs();
    
    Enumeration<ZipEntry> entries;
    ZipFile zip = null;
    try {
      zip = new ZipFile(zipFile);

      entries = (Enumeration<ZipEntry>) zip.entries();

      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();

        File entryFile = new File(targetFolder, entry.getName());
        if(entryFile.isDirectory()) {
          entryFile.mkdirs();
          continue;
        }
        if(!entryFile.getParentFile().exists())
          entryFile.getParentFile().mkdirs();

        System.err.println("Extracting file: " + entryFile.getAbsolutePath());
        copyInputStream(zip.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(entryFile)));
      }

    }
    catch (IOException ioe) {
      System.err.println("Unhandled exception:");
      ioe.printStackTrace();
      return false;
    }
    finally {
      if (zip != null)
        try {
          zip.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
    }
    return true;
  }

  public static boolean zipFilesTo(File sourceFilepath, List sourceFiles, File targetZipFilepath) {
    boolean zipped = false;
    // Create a buffer for reading the files
    byte[] buf = new byte[1024];
    try {
      System.out.println("==========================================================================================");
      System.out.println("Creating zip file: " + targetZipFilepath);
      System.out.println("------------------------------------------------------------------------------------------");
      // Create the ZIP file
      ZipOutputStream out = new ZipOutputStream(new FileOutputStream(targetZipFilepath));

      for (int i = 0; i < sourceFiles.size(); i++) {
        String sourceFile = ((String) sourceFiles.get(i)).substring(sourceFilepath.getAbsolutePath().length() + 1);
        System.out.println("  adding " + sourceFile);
        FileInputStream in = new FileInputStream((String) sourceFiles.get(i));

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(sourceFile));

        // Transfer bytes from the file to the ZIP file
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }

        // Complete the entry
        out.closeEntry();
        in.close();
      }
      // Complete the ZIP file
      out.close();
      zipped = true;
      System.out.println("------------------------------------------------------------------------------------------");
      System.out.println("Created successfully zip file: " + targetZipFilepath);
      System.out.println("==========================================================================================");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return zipped;
  }

  public static boolean switchFiles(File file1, File file2) {
    System.out.println("Switch files " + file1 + " and " + file2);
    boolean switched = false;
    try {
      File fileTmp = new File(file1.getAbsolutePath() + ".tmp");
      if (moveFile(file2, fileTmp)) {
        switched = moveFile(file1, file2) && moveFile(fileTmp, file1);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return switched;
  }

  public static boolean moveFile(File fileSource, File fileDest) {
    System.out.println("Rename " + fileSource + " into " + fileDest);
    return fileSource.renameTo(fileDest);
  }

  public static boolean zipFolder(File sourceFilepath, String matchingRegex, File targetZipFilepath) {
    return zipFilesTo(sourceFilepath, getAllFiles(sourceFilepath, matchingRegex), targetZipFilepath);
  }

  public static boolean deleteFile(File file) {
    if (file.exists()) {
      if (file.isFile()) {
        System.out.println("deleting " + file);
        boolean b = file.delete();
        System.out.println(" => "+b);
        while(!b)
          file.delete();
        return b;
      }
      File[] files = file.listFiles();
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
          deleteFile(files[i]);
        }
        else {
          System.out.print("deleting " + files[i]);
          boolean b = file.delete();
          System.out.println(" => "+b);
          while(!b)
            file.delete();
        }
      }
    }
    System.out.print("deleting " + file);
    boolean b = file.delete();
    System.out.println(" => "+b);
    while(!b)
      file.delete();
    return b;
  }

  public static void deleteAllSVNfolders(String path) {
    File[] files = new File(path).listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isDirectory())
        if (".svn".equals(file.getName()))
          deleteFile(file);
        else
          deleteAllSVNfolders(file.getAbsolutePath());
    }
  }

  public static void cleanSVNfolder(String path) {
    deleteAllSVNfolders(path);
  }

  /**
   * This function will copy files or directories from one location to another.
   * note that the source and the destination must be mutually exclusive. This
   * function can not be used to copy a directory to a sub directory of itself.
   * The function will also have problems if the destination files already
   * exist.
   * 
   * @param src
   *          -- A File object that represents the source for the copy
   * @param dest
   *          -- A File object that represents the destination for the copy.
   * @throws IOException
   *           if unable to copy.
   */
  public static void copyFiles(File src, File dest) throws IOException {
    // Check to ensure that the source is valid...
    if (!src.exists()) {
      throw new IOException("copyFiles: Cannot find source: " + src.getAbsolutePath() + ".");
    }
    else if (!src.canRead()) { // check to ensure we have rights to the
      // source...
      throw new IOException("copyFiles: No right to source: " + src.getAbsolutePath() + ".");
    }
    // is this a directory copy?
    if (src.isDirectory()) {
      if (!dest.exists()) { // does the destination already exist?
        // if not we need to make it exist if possible (note this is mkdirs not
        // mkdir)
        if (!dest.mkdirs()) {
          throw new IOException("copyFiles: Could not create directory: " + dest.getAbsolutePath() + ".");
        }
      }
      // get a listing of files...
      String list[] = src.list();
      // copy all the files in the list.
      for (int i = 0; i < list.length; i++) {
        if (!".svn".equals(list[i])) {
          File dest1 = new File(dest, list[i]);
          File src1 = new File(src, list[i]);
          copyFiles(src1, dest1);
        }
      }
    }
    else {
      // This was not a directory, so lets just copy the file
      FileInputStream fin = null;
      FileOutputStream fout = null;
      byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can change
      // this).
      int bytesRead;
      try {
        if (!dest.exists()) {
          new File(dest.getParent()).mkdirs();
        }
        // open the files for input and output
        fin = new FileInputStream(src);
        fout = new FileOutputStream(dest);
        // while bytesRead indicates a successful read, lets write...
        while ((bytesRead = fin.read(buffer)) >= 0) {
          fout.write(buffer, 0, bytesRead);
        }
        System.out.println(src.getAbsolutePath() + " has been copied to " + dest.getAbsolutePath());
      }
      catch (IOException e) { // Error copying file...
        IOException wrapper = new IOException("copyFiles: Unable to copy file: " + src.getAbsolutePath() + " to " + dest.getAbsolutePath());
        wrapper.initCause(e);
        wrapper.setStackTrace(e.getStackTrace());
        throw wrapper;
      }
      finally { // Ensure that the files are closed (if they were open).
        if (fin != null) {
          fin.close();
        }
        if (fout != null) {
          fout.close();
        }
      }
    }
  }

  public static List<String> getAllFiles(File folder) {
    return getAllFiles(folder, null);
  }

  public static List<String> getAllFiles(File folder, String matchingRegex) {
    return getAllFiles(folder, new ArrayList<String>(), matchingRegex);
  }

  private static List<String> getAllFiles(File folder, List<String> fileList, String matchingRegex) {
    File[] files = folder.listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isDirectory()) {
        if (!".svn".equals(file.getName()))
          getAllFiles(file, fileList, matchingRegex);
      }
      else {
        if (matchingRegex == null || Pattern.matches(matchingRegex, file.getAbsolutePath()))
          fileList.add(file.getAbsolutePath().replaceAll("\\\\", "/"));
      }
    }
    return fileList;
  }

  protected static void printFiles(File folder) {
    File[] files = folder.listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      if (file.isDirectory()) {
        if (!".svn".equals(file.getName()))
          printFiles(file);
      }
      else
        System.out.println(file.getAbsolutePath().replaceAll("\\\\", "/"));
    }
  }
}
