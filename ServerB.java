import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;
import FileSystem.FileObject;

public class ServerB {

//    static String DirectoryAPath = "../src/directory_a"; // format for terminal
//    static String DirectoryBPath = "../src/directory_b"; // format for terminal
    static String DirectoryAPath = "src/directory_a"; //IDE
    static String DirectoryBPath = "src/directory_b"; //IDE

    public static void main(String[] args) {


        try {
            //Wait for Connection with Server-A
            ServerSocket serverB = new ServerSocket(5002);

            Socket serverAtoserverB = serverB.accept();
            System.out.println("Server-B to Server-A connection Established");

            //PrintWriter to print the output but we sent it as a stream to Server-A,
            //so we pass it as argument..
//            PrintWriter outB = new PrintWriter(serverAtoserverB.getOutputStream());

            File serverBfile = new File(DirectoryBPath);
            File[] file = serverBfile.listFiles();
            SimpleDateFormat date = new SimpleDateFormat(" H:m MM:d:y");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));

            // Read data from directory to server-B
//            PrintStream out = new PrintStream(serverAtoserverB.getOutputStream());
            ObjectOutputStream out = new ObjectOutputStream(serverAtoserverB.getOutputStream()); //write stream to socket
            FileObject nfs = null;
            HashMap<String,String> sop = new HashMap<>();
            //send data to server-1
            for (File a:file){
                nfs = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
//                out.println(nfs.reference()+nfs.nameLen()+nfs.fn+" "+nfs.fs+" Bytes"+" "+nfs.fstat);
                out.writeObject(nfs);
//            System.out.println(nfs.fn+" "+nfs.fs+" "+nfs.fstat);
                nfs.fs.length();

            }
//            out.write(file.length);
            out.write(1);
            out.flush();
            serverB.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
