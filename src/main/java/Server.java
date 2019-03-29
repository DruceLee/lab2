import controllers.ServerThread;
import model.TaskIO;
import model.Task;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server {
    static private ServerSocket serverSocket;
    static private Socket socket;
    static private ObjectOutputStream objectOutputStream;
    static private ObjectInputStream objectInputStream;

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
        ArrayList<User> usersList = new ArrayList<>();
        TreeMap<String, ArrayList<Task>> tasksList = new TreeMap<>();

        /*map.put("aa", "ba");
        map.put("ab", "bb");
        map.put("ac", "bc");
        map.put("ad", "bd");
        map.put("ae", "be");

        ArrayList<model.Task> arrayList = new ArrayList<>();
        arrayList.add(new model.Task("ata", dateFormat.parse("1999-12-12/12:12:12"), false));
        map1.put("aa", arrayList);
        map1.put("ab", arrayList);
        map1.put("ac", arrayList);
        map1.put("ad", arrayList);
        map1.put("ae", arrayList);*/

        TaskIO readerWriter = new TaskIO();

        readerWriter.readData(tasksList, usersList);

        try {
            serverSocket = new ServerSocket(1488);
            while (true) {
                new ServerThread(serverSocket.accept(), usersList, tasksList).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        readerWriter.writeData(tasksList, usersList);
    }
}
