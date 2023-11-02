package org.example.gui;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class UIAlert {
    public void showErrorMesage(Stage owner, String text){
        Alert message=new Alert(Alert.AlertType.ERROR,text);
        message.initOwner(owner);
        message.setTitle("Eroare!");
        message.showAndWait();
    }

    public static void showMessage(Stage owner, Alert.AlertType type, String header, String text){
        Alert message=new Alert(type,text);
        message.initOwner(owner);
        message.setHeaderText(header);
        message.showAndWait();
    }
}
