import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class lab3 {
    //command lab3.java lock/unlock index
        public static void main(String[] args) throws IOException {
            String lockStatus = (args.length>1)?args[0].substring(1):"";
            String indexOfFile = (args.length>1)?args[1].substring(1):"";

            new Thread(()->{
                try{
                    Socket client = new Socket("localhost",5001);
                    System.out.println("Client Started");
                    // Data receive from server-1
                    InputStream server = client.getInputStream();
                    BufferedReader s=new BufferedReader(new InputStreamReader(server));
                    String str;
                    //Print the data.
                    while ((str= s.readLine())!=null){
                        System.out.println(str);
                    }
//                            client.close();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }).start();


            new Thread(()->{

                try (ServerSocket clenttoServerA = new ServerSocket(5010)) //lock
                {
                    System.out.println(lockStatus+" "+indexOfFile+" stat");
                    Socket clientServerAlock = clenttoServerA.accept();
                    System.out.println("Client to ServerA for lock status");
                    PrintWriter out = new PrintWriter(clientServerAlock.getOutputStream());
                    out.println(lockStatus+" "+indexOfFile);
                    out.flush();
                    clientServerAlock.close();
                } catch (IOException  e) {
                    e.printStackTrace();
                }
            }).start();
        }


}
