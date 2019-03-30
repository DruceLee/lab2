package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;

import java.util.List;


public class ServerSceneController {
    private ObservableList<User> observableList;

    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn login;
    @FXML
    private TableColumn password;
    @FXML
    private TableColumn isBanned;

    public void setObservableList(List<User> list) {
        observableList = FXCollections.observableArrayList(list);

        table.setItems(observableList);
    }

    @FXML
    public void initialize() {
        login.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        isBanned.setCellValueFactory(new PropertyValueFactory<User, Boolean>("banned"));
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
