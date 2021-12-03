package FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.HashSet;

public class Synchronisation {

    public static void copyfilewithContentsWritable(String DirectoryAPath,String DirectoryBPath){
        File serverA = new File(DirectoryAPath);
        String[] serverAfilesList = serverA.list();
        for (String fi : serverAfilesList) {
//                                    String temp = fi.replaceAll(" ","");
//                        System.out.println(temp.length());
            File sourcefile = new File(DirectoryBPath +"/"+fi);
            File targetfile = new File(DirectoryAPath +"/"+fi);
            if (targetfile.canWrite()){
                Synchronisation.copyfilewithContents(sourcefile, targetfile);
            }

        }
    }



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
//        HashSet<String> deletefile = new HashSet<String>(oldfile);
        for (String eachfile:fileSettemp){
            file1 = new File(pathA+"/"+eachfile);
            if (!file1.isFile())
                file1.createNewFile();
        }

    }

    public static void DeleteFile(HashSet<String> fileA,String pathA, String pathofB ){
        //delete(.. , removes from file, checking file)
        File file1 = new File(pathofB);
        String[] filelist = file1.list();
        HashSet<String> fileSettemp = new HashSet<>(Arrays.asList(filelist));
        HashSet<String> deletefile = new HashSet<String>(fileA);
//        System.out.println(deletefile+" original-A");
//        System.out.println(fileSettemp+" temp-B");
        System.out.println(deletefile.removeAll(fileSettemp));
//        System.out.println("Files Deleted: "+deletefile+" deletefile_op");
        for (String eachfile:deletefile){
            file1 = new File(pathA+"/"+eachfile);
            if (file1.isFile())
                file1.delete();
        }

//        files are deleted but previous deleted files are also store check if deletefile.clear(); works...
    }



////  --------- NOT WORKING -------------
//    //Checking proper working of set functions.
//    public static void DeleteFile(HashSet<String> fileA,String pathA, String pathofB ){
//
//        File file1 = new File(pathofB); //direc_b
//        File file2 = new File(pathA); //direc_b
//        String[] filelist = file1.list();
//        HashSet<String> fileSettemp = new HashSet<>(Arrays.asList(filelist));
//        HashSet<String> deletefile = new HashSet<String>(fileA);
//        deletefile.removeAll(fileSettemp);
//        for (String eachfile:deletefile){
//            file1 = new File(pathofB+"/"+eachfile);
//            file2 = new File(pathA+"/"+eachfile);
//            if (file1.isFile() && !file2.isFile())
//                file1.delete();
//        }
////        files are deleted but previous deleted files are also store check if deletefile.clear(); works...
//    }
////    ---------- Not Working ------------

//FileSystem End

}
