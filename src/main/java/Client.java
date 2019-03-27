import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Gson gson = new Gson();

            Socket client = new Socket("127.0.0.1", 1111);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            BufferedWriter out = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream()));

            out.write("Login: aa ba");
            out.flush();
/*            while (true) {
                String list = in.readLine();
                if (!"".equals(list)) {
                    System.out.println(list);
                    break;
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
