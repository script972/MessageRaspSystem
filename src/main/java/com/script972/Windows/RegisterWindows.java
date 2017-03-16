package com.script972.Windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by script972 on 16.03.2017.
 */
public class RegisterWindows extends Application {
    public void start(Stage primaryStage) throws Exception {
        String fxmlFile = "/fxml/register.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        primaryStage.setTitle("Регистрация");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
