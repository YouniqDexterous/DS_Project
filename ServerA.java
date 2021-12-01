import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import FileSystem.FileObject;
import FileSystem.Synchronisation;


public class ServerA {
//    static String DirectoryAPath = "../src/directory_a"; // format for terminal
//    static String DirectoryBPath = "../src/directory_b"; // format for terminal
//    static String DirectoryAPath = "src/directory_a"; //IDE -- git
//    static String DirectoryBPath = "src/directory_b"; //IDE -- git

    //other folder -- Desktop/Assignment/Distributed_Systems/ ----
    static String DirectoryAPath = "/Users/yogesh/Desktop/Assignment/Distributed_Systems/directory_a"; //IDE -- non-git
    static String DirectoryBPath = "/Users/yogesh/Desktop/Assignment/Distributed_Systems/directory_b"; //IDE -- non-git


    public static void main(String[] args) {
        try (
             //Establish the connection Server-A ---> cilent
             ServerSocket serverA = new ServerSocket(5001);
        )
        {
            while (true){//RECEIVE DATA FROM SERVER-B PROCESS THE DATA SEND DATA FROM SERVER-A TO CLIENT
                //accept connection
                Socket clienttoServerA = serverA.accept();
                System.out.println("Client to ServerA connection Established");

//                create thread to keep connection alive
                new Thread(()->{
                            try {
                                //Write the output and in order to do so we need StreamWriter I'm using PrintWriter
                                //since we are writing to client we pass client connection as argument
                                PrintWriter out = new PrintWriter(clienttoServerA.getOutputStream());
                                //Read File from Directory....
                                File serverADirectory = new File(DirectoryAPath);
                                File[] file = serverADirectory.listFiles();
                                //Date Formatter
                                SimpleDateFormat date = new SimpleDateFormat(" H:m MM:d:y");
                                date.setTimeZone(TimeZone.getTimeZone("GMT-5"));

                                //Access the files as Object
                                FileObject nfsA = null;
                                HashMap<String,String > serverAfinallist=new HashMap<>(); //Final serverA and serverB combined List
                                HashMap<String,String > serverAlist=new HashMap<>();
                                ArrayList<String> sortList = new ArrayList<>(); // List to sort all files with name;
                                ArrayList<String> serverAsep = new ArrayList<>();
                                ArrayList<String> serverBseparatefile = new ArrayList<>();
                                for (File a:file){
                                    nfsA = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
                                    serverAfinallist.put(nfsA.fn, nfsA.fs+" Bytes"+ nfsA.fstat);
                                    serverAlist.put(nfsA.fn, nfsA.fs+" Bytes"+ nfsA.fstat);
                                    sortList.add(nfsA.fn);
                                }
                                HashSet<String> fileSetOldA = new HashSet<String>(serverAsep);


                                //Establish Connection to ServerB
                                Socket clientAServerB = new Socket("localhost",5002);
//------------------ Server B files as Objects -------------------------------
                                ObjectInput serverBoutput = new ObjectInputStream(clientAServerB.getInputStream());
                                FileObject nfsBout;
                                while (serverBoutput.read()!=1){
                                    nfsBout = (FileObject) serverBoutput.readObject();
                                    System.out.println(nfsBout.fn);
                                    serverAfinallist.put(nfsBout.fn, nfsBout.fs+" Bytes"+ nfsBout.fstat);
                                    serverAlist.put(nfsBout.fn, nfsBout.fs+" Bytes"+ nfsBout.fstat);
                                    sortList.add(nfsBout.fn);
                                }
//------------------ Server B files as Objects End -------------------------------

                                //Sort the files
                                Collections.sort(sortList);
//                                System.out.println("Sorted List: "+sortList.toString());
                                ArrayList<String> NonDuplicate = new ArrayList<>(serverAfinallist.keySet());
                                Collections.sort(NonDuplicate);

                                //Send Files to Client
                                for (String directories:NonDuplicate){
                                    out.println(directories+" "+serverAfinallist.get(directories));
                                }
                                out.flush();
                                clienttoServerA.close();
//                                clientAServerB.close();


//                   ---------- File Synchronisation --------

                                System.out.println(serverBseparatefile+"Server before update");
                                //Copy Files of ServerB that are not in ServerA
                                for (String fi : serverBseparatefile) {
//                                    String temp = fi.replaceAll(" ","");
//                        System.out.println(temp.length());
                                    File sourcefile = new File(DirectoryAPath+"/"+fi);
                                    File targetfile = new File(DirectoryBPath+"/"+fi);
                                    Synchronisation.copyfilewithContents(sourcefile,targetfile);
                                }
                                while (true) {
                                    //Update the files
                                    //Read file after few seconds
                                    serverADirectory = new File(DirectoryAPath);
                                    String[] newServerlist = serverADirectory.list();
                                    HashSet<String> fileSetNewA = new HashSet<String>(List.of(newServerlist));
                                    try {
//                                        Thread.sleep(1000);
                                        Synchronisation.AddFile(fileSetOldA,DirectoryAPath,DirectoryBPath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Synchronisation.DeleteFile(fileSetNewA,DirectoryAPath,DirectoryBPath);
                                    for (String fi : serverBseparatefile) {
//                                    String temp = fi.replaceAll(" ","");
//                        System.out.println(temp.length());
                                        File sourcefile = new File(DirectoryAPath+"/"+fi);
                                        File targetfile = new File(DirectoryBPath+"/"+fi);
                                        Synchronisation.copyfilewithContents(sourcefile,targetfile);
                                    }
                                }
//                --------------    End file Synchronisation -------------
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                }).start();//end while


                new Thread(()-> {//SEND DATA FROM SERVER-A TO SERVER-B
                    try (ServerSocket serverAtoserverB = new ServerSocket(5008)) {
                        Socket serverAclientB = serverAtoserverB.accept();
                        System.out.println("ServerA TO B connected");
                        //PrintWriter to print the output but we sent it as a stream to Server-A,
                        //so we pass it as argument..
//            PrintWriter outB = new PrintWriter(serverAtoserverB.getOutputStream());

                        File serverAfile = new File(DirectoryAPath);
                        File[] serverAFileList = serverAfile.listFiles();
                        SimpleDateFormat date = new SimpleDateFormat(" H:m MM:d:y");
                        date.setTimeZone(TimeZone.getTimeZone("GMT-5"));

                        // Read data from directory to server-B
//            PrintStream out = new PrintStream(serverAtoserverB.getOutputStream());
                        ObjectOutputStream out = new ObjectOutputStream(serverAclientB.getOutputStream()); //write stream to socket
                        FileObject nfs = null;
                        HashMap<String,String> sop = new HashMap<>();
                        //send data to server-1
                        for (File a:serverAFileList){
                            nfs = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
//                            out.println(nfs.reference()+nfs.nameLen()+nfs.fn+" "+nfs.fs+" Bytes"+" "+nfs.fstat);
                            out.writeObject(nfs);
//                            System.out.println(nfs.fn+" "+nfs.fs+" "+nfs.fstat);
                            nfs.fs.length();

                        }
                        out.write(1);
                        out.flush();
//                        serverB.close();//should not close while using thread.
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }//END Sending DATA FROM SERVER-A TO SERVER-B
                ).start();//end thread


            }//end thread receiving DATA FROM SERVER-B PROCESS THE DATA SEND DATA FROM SERVER-A TO CLIENT
        }//end try block of connection
         catch (IOException e) {
            e.printStackTrace();
        }
    }//end main

}

