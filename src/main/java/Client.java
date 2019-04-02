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

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String exit = in.readLine();
                            if ("Exit".equals(exit))
                                System.exit(0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            printWriter.println("Login: aa ba");
            printWriter.flush();

            String list = "";

            while (true) {
                list = in.readLine();
                if (!"".equals(list)) {
                    System.out.println(list);
                    break;
                }
                Thread.sleep(10);
            }

            ArrayList<Task> arrayList = new ArrayList<>();
            arrayList.addAll(Arrays.asList(gson.fromJson(list, Task[].class)));
            arrayList.get(0).setActive(true);

            for (Task task : arrayList) {
                System.out.println(task.getTitle() + " " + task.getTime() + " " + task.getStartTime() + " " + task.getEndTime() + " " + task.getRepeatInterval() + " " + task.isActive());
            }

            printWriter.println("Change: aa 0 " + gson.toJson(arrayList.get(0)));

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
