import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import FileSystem.FileObject;
import FileSystem.Synchronisation;

public class ServerB {

//    static String DirectoryAPath = "../src/directory_a"; // format for terminal
//    static String DirectoryBPath = "../src/directory_b"; // format for terminal
//    static String DirectoryAPath = "src/directory_a"; //IDE -- git
//    static String DirectoryBPath = "src/directory_b"; //IDE -- git

    //other folder -- Desktop/Assignment/Distributed_Systems/ ----
    static String DirectoryAPath = "/Users/yogesh/Desktop/Assignment/Distributed_Systems/directory_a"; //IDE -- non-git
    static String DirectoryBPath = "/Users/yogesh/Desktop/Assignment/Distributed_Systems/directory_b"; //IDE -- non-git

    public static void main(String[] args) {
        try(
        //Wait for Connection with Server-A
        ServerSocket serverB = new ServerSocket(5002)
        ){
            while (true){
                Socket serverAtoserverB = serverB.accept();
                System.out.println("Server-B to Server-A connection Established");
                new Thread(()->{//Thread to send files from Server-B to Server-A
                    try{
                        //PrintWriter to print the output but we sent it as a stream to Server-A,
                        //so we pass it as argument..
//            PrintWriter outB = new PrintWriter(serverAtoserverB.getOutputStream());
                        File serverBfile = new File(DirectoryBPath);
                        File[] serverBFileList = serverBfile.listFiles();
                        ArrayList<String> serverBfileSet = new ArrayList<>();
                        SimpleDateFormat date = new SimpleDateFormat(" HH:mm MM:d:y");
                        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));

                        // Read data from directory to server-B
//            PrintStream out = new PrintStream(serverAtoserverB.getOutputStream());
                        ObjectOutputStream out = new ObjectOutputStream(serverAtoserverB.getOutputStream()); //write stream to socket
                        FileObject nfs;
//                        HashMap<String,String> sop = new HashMap<>();
                        //send data to server-1
                        for (File a:serverBFileList){
                            nfs = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
//                            out.println(nfs.reference()+nfs.nameLen()+nfs.fn+" "+nfs.fs+" Bytes"+" "+nfs.fstat);
                            out.writeObject(nfs);
//                            System.out.println(nfs.fn+" "+nfs.fs+" "+nfs.fstat);
                            System.out.println(nfs.fs.length());
                            serverBfileSet.add(nfs.fn);

                        }
                        out.write(1);
                        HashSet<String> fileSetOldB = new HashSet<String>(serverBfileSet);
                        out.flush();
//                        serverB.close();//should not close while using thread.
//                ------ server is updated frquently -------
                        int iw=1;
                        while (true) {
                            //Update the files
                            serverBfile = new File(DirectoryBPath);
                            serverBFileList = serverBfile.listFiles();
                            for (File a:serverBFileList){
                                nfs = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
                                System.out.println(nfs.fn+" "+nfs.fs+" "+nfs.fstat);
                                nfs.fs.length();
                            }
                            serverBfile = new File(DirectoryBPath);
                            String[] newServerBlist = serverBfile.list();
                            HashSet<String> fileSetNewB = new HashSet<String>(List.of(newServerBlist));
                            try {
                                Thread.sleep(1000);
                                Synchronisation.AddFile(fileSetOldB,DirectoryBPath,DirectoryAPath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Thread.sleep(2000);
                            Synchronisation.DeleteFile(fileSetNewB,DirectoryBPath,DirectoryAPath);
                        }


//                        ------- server update end --------
//Store Old Files and New Files...
                    }//end common try
                    catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();//end thread


                new Thread(()->{//Thread to send data from server-A to server-B
                    Socket serverBtoserverA;
                    try{
                        serverBtoserverA = new Socket("localhost",5008);
                        System.out.println("Server-A to Server-B data Connection");
                        ObjectInput serverAoutput = new ObjectInputStream(serverBtoserverA.getInputStream());
                        FileObject nfsBout;
                        HashMap<String, String> serverABlist = new HashMap<>();
                        ArrayList<String> sortedABlist= new ArrayList<>();
                        ArrayList<String> serverABseparatefile = new ArrayList<>();
                        while (serverAoutput.read()!=1){
                            nfsBout = (FileObject) serverAoutput.readObject();
                            System.out.println(nfsBout.fn);
                            serverABlist.put(nfsBout.fn, nfsBout.fs+" Bytes"+ nfsBout.fstat);
                            serverABlist.put(nfsBout.fn, nfsBout.fs+" Bytes"+ nfsBout.fstat);
                        }
                        System.out.println(serverABseparatefile+" Files before update");
                        //Check adds file with content
                        for (String fi : serverABseparatefile) {
//                            String temp = fi.replaceAll(" ","");
//                        System.out.println(temp.length());
                            File sourcefile = new File(DirectoryAPath+"/"+fi);
                            File targetfile = new File(DirectoryBPath+"/"+fi);
                            Synchronisation.copyfilewithContents(sourcefile,targetfile);
                        }


                        while (true){
                            System.out.println("Server A and Client B");
//                            System.out.println(serverABlist);
//                            Thread.sleep(1000);
                            for (String fi : serverABseparatefile) {
//                            String temp = fi.replaceAll(" ","");
//                        System.out.println(temp.length());
                                File sourcefile = new File(DirectoryAPath+"/"+fi);
                                File targetfile = new File(DirectoryBPath+"/"+fi);
                                Synchronisation.copyfilewithContents(sourcefile,targetfile);
                            }

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                ).start();//end thread data server-A to server-B




            }//end while
        }//end server connection try
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
