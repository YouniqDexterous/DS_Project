package FileSystem;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class FileLock {
    //    static String DirectoryAPath = "../src/directory_a"; // format for terminal
//    static String DirectoryBPath = "../src/directory_b"; // format for terminal
//    static String DirectoryAPath = "src/directory_a"; //IDE -- git
//    static String DirectoryBPath = "src/directory_b"; //IDE -- git

    //other folder -- Desktop/Assignment/Distributed_Systems/ ----
    static String DirectoryAPath = "/Users/yogesh/Desktop/Assignment/Distributed_Systems/directory_a"; //IDE -- non-git
    static String DirectoryBPath = "/Users/yogesh/Desktop/Assignment/Distributed_Systems/directory_b"; //IDE -- non-git

//    public static void main(String[] args) {
//        if (args.length != 2) {
//            System.err.println("Please use cmd: java fileName lock/unlock index");
//        }
//        System.out.println(args.length);
//
//        FileLock(args[0],Integer.parseInt(args[1]),DirectoryAPath);
//
//    }

    public static void Locking(String LockStatus, int Index, String FilePath){


        File file1 = new File(FilePath);
        File[] listofFile = file1.listFiles();
        Arrays.sort(listofFile);

        System.out.println(listofFile[Index]);
        try {
            java.nio.channels.FileLock lockFile;
            if (LockStatus.equals("lock")) {
                String indexofFile = String.valueOf(listofFile[Index]);
//        FileOutputStream fout = new FileOutputStream(listofFile[Integer.parseInt(args[1])]);//get filename from cmd line
                RandomAccessFile fra = new RandomAccessFile(indexofFile, "rw");
                FileChannel fch = fra.getChannel();
                lockFile = fch.lock();
                listofFile[Index].setWritable(false);
                System.out.println("locked Successfully");
            } else {
                if (LockStatus.equals("unlock")) {
                    listofFile[Index].setWritable(true);
//                    DS_Thread.updatefile()
//          ---- Update Files ----
//                    File serverA = new File(DirectoryAPath);
//                    String[] serverAfilesList = serverA.list();
//                    for (String fi : serverAfilesList) {
////                                    String temp = fi.replaceAll(" ","");
////                        System.out.println(temp.length());
//                        File sourcefile = new File(DirectoryBPath +"/"+fi);
//                        File targetfile = new File(DirectoryAPath +"/"+fi);
//                        if (targetfile.canWrite()){
//                            Synchronisation.copyfilewithContents(sourcefile, targetfile);
//                        }
//
//                    }
                    Synchronisation.copyfilewithContentsWritable(DirectoryAPath,DirectoryBPath);
//          ---- Update Files Ends ----

                    System.out.println("File Released");
                } else System.out.println("No valid lock operation");
            }
//            Thread.sleep(35000);
//                lockFile.release();


        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }
}
