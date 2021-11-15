import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class lab3 {
        public static void main(String[] args) throws IOException {
            Socket client = new Socket("localhost",5001);

            // Data receive from server-1
            InputStream server = client.getInputStream();
            BufferedReader s=new BufferedReader(new InputStreamReader(server));
            String str;
            //Print the data.
            while ((str= s.readLine())!=null){
                System.out.println(str);
            }
            client.close();
        }


}
