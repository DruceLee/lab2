package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Task;
import model.TaskIO;
import model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class ServerSceneController {
    private ObservableList<User> observableList;
    private ServerSocket serverSocket;

    private ArrayList<User> usersList = new ArrayList<>();
    private TreeMap<String, ArrayList<Task>> tasksList = new TreeMap<>();

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn login;
    @FXML
    private TableColumn password;
    @FXML
    private TableColumn isBanned;

    @FXML
    public void initialize() {
        TaskIO readerWriter = new TaskIO();
        readerWriter.readData(tasksList, usersList);
        observableList = FXCollections.observableArrayList(usersList);

        login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        isBanned.setCellValueFactory(new PropertyValueFactory<User, Boolean>("banned"));

        table.setItems(observableList);
    }

    public void rebut(ActionEvent actionEvent) {
    }

    public void banUnban(ActionEvent actionEvent) {
        User user = table.getSelectionModel().getSelectedItem();
        user.setBanned(!user.isBanned());
        table.refresh();

        synchronized (usersList) {
            for (int i = 0; i < usersList.size(); i++) {
                if (user.equals(usersList.get(i))) {
                    User user1 = usersList.get(i);
                    user1.setBanned(!user1.isBanned());
                }
            }
        }
    }

    public void exit(ActionEvent actionEvent) {
    }
}
