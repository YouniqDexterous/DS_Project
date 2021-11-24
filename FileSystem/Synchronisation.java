package FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;

public class Synchronisation {
    public static void copyfilewithContents(File sourcefile, File targetfile) {
//The below code copies files with checking contents...
        Path source = Path.of(String.valueOf(sourcefile));
        Path target = Path.of(String.valueOf(targetfile));

        try {
            Files.copy(source,target, StandardCopyOption.REPLACE_EXISTING);
            targetfile.setLastModified(sourcefile.lastModified());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void AddFile(HashSet<String> oldfile, String pathA, String pathofB) throws IOException {
        //Adds files if created in Server-B
        File file1 = new File(pathofB);
        String[] filelist = file1.list();
        HashSet<String> fileSettemp = new HashSet<>(Arrays.asList(filelist));
        HashSet<String> deletefile = new HashSet<String>(oldfile);
        for (String eachfile:fileSettemp){
            file1 = new File(pathA+"/"+eachfile);
            if (!file1.isFile())
                file1.createNewFile();
        }

    }


    //Checking proper working of set functions.
    public static void DeleteFile(HashSet<String> fileA,String pathA, String pathofB ){

        File file1 = new File(pathofB); //direc_b
        File file2 = new File(pathA); //direc_b
        String[] filelist = file1.list();
        HashSet<String> fileSettemp = new HashSet<>(Arrays.asList(filelist));
        HashSet<String> deletefile = new HashSet<String>(fileA);
        deletefile.removeAll(fileSettemp);
        for (String eachfile:deletefile){
            file1 = new File(pathofB+"/"+eachfile);
            file2 = new File(pathA+"/"+eachfile);
            if (file1.isFile() && !file2.isFile())
                file1.delete();
        }
//        files are deleted but previous deleted files are also store check if deletefile.clear(); works...
    }

//FileSystem End

}
