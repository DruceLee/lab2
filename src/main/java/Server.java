import controllers.ServerSceneController;
import controllers.ServerThread;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.TaskIO;
import model.Task;
import model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Application {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    private final static ArrayList<User> usersList = new ArrayList<>();
    private final static TreeMap<String, ArrayList<Task>> tasksList = new TreeMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXML/ServerScene.fxml"));
        Parent root = fxmlLoader.load();

        ((ServerSceneController) fxmlLoader.getController()).setObservableList(usersList);

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 444, 318));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                TaskIO readerWriter = new TaskIO();
                readerWriter.writeData(tasksList, usersList);

                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");

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


        launch(args);

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
