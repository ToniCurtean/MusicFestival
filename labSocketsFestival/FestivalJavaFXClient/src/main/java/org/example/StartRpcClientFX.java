package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gui.LoginController;
import org.example.gui.MainViewController;
import org.example.rpcprotocol.ServicesRpcProxy;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    public static void main(String[] args) {
        launch();
    }

    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";
    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/festivalclient.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find festivalclient.properties " + e);
            return;
        }

        String serverIP = clientProps.getProperty("festival.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("festival.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IFestivalService server = new ServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("login-view.fxml"));
        Parent root=loader.load();

        LoginController ctrl =
                loader.<LoginController>getController();
        ctrl.setServer(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("main-view.fxml"));
        Parent croot=cloader.load();

        MainViewController mainCtrl =
                cloader.<MainViewController>getController();
        mainCtrl.setServer(server);

        ctrl.setMainController(mainCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("Festival muzica");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}