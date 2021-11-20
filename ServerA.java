import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.TimeZone;
import FileSystem.FileObject;


public class ServerA {
//    static String DirectoryAPath = "src/directory_a"; //terminal
    static String DirectoryAPath = "directory_a"; //terminal

    public static void main(String[] args) {
        try {
            //Establish the connection Server-A ---> cilent
            ServerSocket serverA = new ServerSocket(5001);
            Socket clienttoServerA = serverA.accept();
            System.out.println("Client to ServerA connection Established");

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
            FileObject nfs1 = null;
            HashMap<String,String > server1finallist=new HashMap<>(); //Final server1 and server2 combined List
            HashMap<String,String > server1list=new HashMap<>();
            ArrayList<String> sortList = new ArrayList<>(); // List to sort all files with name;
            for (File a:file){
                nfs1 = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
                server1finallist.put(nfs1.fn, nfs1.fs+" Bytes"+ nfs1.fstat);
                server1list.put(nfs1.fn, nfs1.fs+" Bytes"+ nfs1.fstat);
                sortList.add(nfs1.fn);
            }

            //Establish Connection to ServerB
            Socket clientAServerB = new Socket("localhost",5002);
//------------------ Server B files as Objects -------------------------------
            ObjectInput serverBoutput = new ObjectInputStream(clientAServerB.getInputStream());
            FileObject nfsBout;
            while (serverBoutput.read()!=1){
                nfsBout = (FileObject) serverBoutput.readObject();
                System.out.println(nfsBout.fn);
                server1finallist.put(nfsBout.fn, nfsBout.fs+" Bytes"+ nfsBout.fstat);
                server1list.put(nfsBout.fn, nfsBout.fs+" Bytes"+ nfsBout.fstat);
                sortList.add(nfsBout.fn);
            }

//------------------ Server B files as Objects End -------------------------------




//------------------ Server B files as Strings -------------------------------
//            //Make a StreamReader to accept the data sent from Server-B
//            InputStream serverBoutput = clientAServerB.getInputStream();
//            //Convert the stream obtained from ServerB to required Format I used BufferReader..
//            BufferedReader s = new BufferedReader(new InputStreamReader(serverBoutput));
//            String str;
//            while ( (str = s.readLine()) != null ){
////            System.out.println(str);
//                int tempsize =(int) Character.getNumericValue(str.charAt(0));
//                int temp = (int) Integer.parseInt(str.substring(1,tempsize+1));
//                server1finallist.put(str.substring(tempsize+1,temp+3),str.substring(temp+3));
//                sortList.add(str.substring(tempsize+1,temp+3));
//            }
//------------------ Server B files as Strings End -------------------------------


            //Sort the files
            Collections.sort(sortList);

            //Send Files to Client
            for (String directories:sortList){
                //directories are sorted files and .get(directories) give the remaining information...
                out.println(directories+" "+server1finallist.get(directories));
            }
            out.flush();
            clienttoServerA.close();
            clientAServerB.close();


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}
