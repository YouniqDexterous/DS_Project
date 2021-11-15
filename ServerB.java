import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;
import FileSystem.FileObject;

public class ServerB {

//   static String DirectoryBPath = "src/directory_b";//terminal
    static String DirectoryBPath = "directory_b";
    public static void main(String[] args) {


        try {
            //Wait for Connection with Server-A
            ServerSocket serverB = new ServerSocket(5002);

            Socket serverAtoserverB = serverB.accept();
            System.out.println("Server-B to Server-A connection Established");

            //PrintWriter to print the output but we sent it as a stream to Server-A,
            //so we pass it as argument..
            PrintWriter outB = new PrintWriter(serverAtoserverB.getOutputStream());

            File serverBfile = new File(DirectoryBPath);
            File[] file = serverBfile.listFiles();
            SimpleDateFormat date = new SimpleDateFormat(" H:m MM:d:y");
            date.setTimeZone(TimeZone.getTimeZone("GMT-5"));

            // Read data from directory to server-2
//    {Reference}- https://docs.oracle.com/javase/7/docs/api/java/io/PrintStream.html
            PrintStream out = new PrintStream(serverAtoserverB.getOutputStream());
            FileObject nfs = null;
            HashMap<String,String> sop = new HashMap<>();

            //send data to server-1
            for (File a:file){
                nfs = new FileObject(a.getName(),Long.toString(a.length()),date.format(a.lastModified()));
                out.println(nfs.reference()+nfs.nameLen()+nfs.fn+" "+nfs.fs+" Bytes"+" "+nfs.fstat);
//            System.out.println(nfs.fn+" "+nfs.fs+" "+nfs.fstat);
                nfs.fs.length();
            }
            out.flush();
            serverB.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
