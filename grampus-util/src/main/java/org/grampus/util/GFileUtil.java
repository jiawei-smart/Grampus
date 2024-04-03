package org.grampus.util;

import org.grampus.log.GLogger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.CodeSource;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GFileUtil {
    public static void extractFileFromJar(String jarFilePath,String folderPath, String targetPath){
        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(Paths.get(jarFilePath)))) {
            ZipEntry entry;
            Path outputParentPath = Paths.get(targetPath);
            if(isExisted(outputParentPath)){
                Files.createDirectories(outputParentPath);
            }
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().startsWith(folderPath)) {
                    String fileName = entry.getName().substring(folderPath.length());
                    Path outputPath = Paths.get(targetPath+(fileName.startsWith(File.separator) ? fileName: File.separator+fileName));
                    Files.copy(zipInputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            GLogger.error("GFileUtil: failure to extract files [{}] from jar [{}] ,with [{}]",folderPath,jarFilePath,e);
        }
    }

    public static boolean isExistedInClasspath(String filename){
        try {
            return isExisted(Paths.get(Objects.requireNonNull(GFileUtil.class.getClassLoader().getResource(filename)).toURI()));
        } catch (URISyntaxException e) {
            GLogger.error("GFileUtil: failure to check if the file [{}] exist in class path, with [{}]",filename,e);
            return false;
        }
    }

    public static boolean isExisted(Path filePath){
        return Files.exists(filePath);
    }

    public static Path getResourcesPath()  {
        ClassLoader classLoader = GFileUtil.class.getClassLoader();
        try {
            URL url = classLoader.getResource("");
            if(url != null){
                return Paths.get(url.toURI());
            }
        } catch (URISyntaxException e) {
            GLogger.error("GFileUtil: failure to get resources folder, {}",e);
        }
        return Path.of("");
    }

    public static Path getResourcesPath(String file) {
        ClassLoader classLoader = GFileUtil.class.getClassLoader();
        try {
            URL url = classLoader.getResource(file);
            if(url != null){
                return Paths.get(url.toURI());
            }
        } catch (URISyntaxException e) {
            GLogger.error("GFileUtil: failure to get resources folder, {}",e);
        }
        return Path.of("");
    }

    public static String getResourcesAbsolutePath()  {
        return getResourcesPath().toAbsolutePath().toString();
    }

    public static String getResourcesAbsolutePath(String file) {
        return getResourcesPath().toAbsolutePath().toString()+'/'+file;
    }

    public static void createFolderInClasspath(String folder) {
        new File(getResourcesAbsolutePath(),folder).mkdir();
    }

    public String getClassLocation(Class clazz){
        CodeSource src = clazz.getProtectionDomain().getCodeSource();
        if (src != null) {
            return src.getLocation().toString();
        } else {
            return GStringUtil.emptyString();
        }
    }

}
