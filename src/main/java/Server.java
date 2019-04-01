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
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Application {
    private static ServerSocket serverSocket;

    private final static ArrayList<User> usersList = new ArrayList<>();
    private final static TreeMap<String, ArrayList<Task>> tasksList = new TreeMap<>();

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("FXML/ServerScene.fxml"));
        Parent root = fxmlLoader.load();

        ((ServerSceneController) fxmlLoader.getController()).setParams(usersList);

        primaryStage.setTitle("Server");
        primaryStage.setScene(new Scene(root, 444, 318));
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                synchronized (tasksList){
                    synchronized (usersList) {
                        TaskIO readerWriter = new TaskIO();
                        readerWriter.writeData(tasksList, usersList);

                        Platform.exit();
                        System.exit(0);
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        TaskIO taskIO = new TaskIO();

        /*usersList.add(new User("aa", "ba", false));
        usersList.add(new User("ab", "bb", false));
        usersList.add(new User("ac", "bc", false));
        usersList.add(new User("ad", "bd", false));
        usersList.add(new User("ae", "be", false));
        usersList.add(new User("af", "bf", false));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd/hh:mm:ss");
        ArrayList<Task> arrayList = new ArrayList<>();
        arrayList.add(new Task("Hello", new Date(), false));
        for (User user : usersList) {
            tasksList.put(user.getLogin(), arrayList);
        }*/
        taskIO.readData(tasksList, usersList);

        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(1488);
                while (true) {
                    new ServerThread(serverSocket.accept(), usersList, tasksList).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        launch(args);


    }
}
