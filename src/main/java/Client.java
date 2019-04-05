import com.google.gson.Gson;
import model.Task;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        try {
            Gson gson = new Gson();

            Socket client = new Socket("127.0.0.1", 1488);
            PrintWriter printWriter = new PrintWriter(client.getOutputStream());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(client.getInputStream()));

            printWriter.println("Login: aa ba");
            printWriter.flush();

            String list;

            while (true) {
                list = in.readLine();
                if (!list.isEmpty()) {
                    System.out.println(list);
                    break;
                }
                Thread.sleep(10);
            }

            printWriter.println("Exit work: ");
            printWriter.flush();
            /*ObjectInputStream in = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());

            out.writeObject("Login: aa ba");
            out.flush();
            while (true) {
                String list = (String) in.readObject();
                if (!"".equals(list)) {
                    System.out.println(list);
                    break;
                }
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
