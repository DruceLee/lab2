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

    private ArrayList<User> usersList = new ArrayList<>();

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn login;
    @FXML
    private TableColumn password;
    @FXML
    private TableColumn isBanned;
    @FXML
    private TableColumn admin;

    public void setParams(ArrayList<User> usersList) {
        this.usersList = usersList;
        observableList = FXCollections.observableArrayList(usersList);

        login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        isBanned.setCellValueFactory(new PropertyValueFactory<User, Boolean>("banned"));
        admin.setCellValueFactory(new PropertyValueFactory<User, String>("admin"));

        table.setItems(observableList);
    }

    @FXML
    public void initialize() {
    }

    public void rebut(ActionEvent actionEvent) {
    }

    public void banUnban(ActionEvent actionEvent) {
        User user = table.getSelectionModel().getSelectedItem();
        user.setBanned(!user.isBanned());
        table.refresh();
    }

    public void exit(ActionEvent actionEvent) {
    }
}
