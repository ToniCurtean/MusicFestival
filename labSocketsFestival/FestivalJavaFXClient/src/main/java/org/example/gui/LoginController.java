package org.example.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.example.Cashier;
import org.example.FestivalException;
import org.example.IFestivalService;


import java.util.Objects;

public class LoginController {

    public IFestivalService server;

    Parent mainParent;

    private MainViewController mainController;

    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button loginButton;

    public void setServer(IFestivalService server) {
        this.server = server;
    }

    public void setParent(Parent parent){
        this.mainParent=parent;
    }

    public void setMainController(MainViewController mainCon) {
        this.mainController = mainCon;
    }

    public void onLoginButtonClick(ActionEvent actionEvent){
        if (Objects.equals(usernameText.getText(), "") || Objects.equals(passwordText.getText(), ""))
            return;
        try{
            server.login(usernameText.getText(),passwordText.getText(),mainController);
            Stage stage=new Stage();
            stage.setTitle("Window for" + usernameText.getText());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainController.logout();
                    System.exit(0);
                }
            });
            stage.show();
            mainController.setCashier(new Cashier(usernameText.getText(),passwordText.getText()));
            mainController.init();
            ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        }catch(FestivalException e){
            UIAlert.showMessage(null, Alert.AlertType.INFORMATION,"","Couldn't log in!");
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}

