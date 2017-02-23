package com.script972.controller;

import com.script972.Model.MessageLet;
import com.script972.Model.Model;
import com.script972.Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by script972 on 19.02.2017.
 */
public class MainController implements Initializable {
    Model model=new Model();
    @FXML
    private Label errorOut;
    @FXML
    private TextField login;
    @FXML
    private TextField pass;




    @FXML
    TextField byAuther;
    @FXML
    TextField bySubject;
    @FXML
    TextField subject;
    @FXML
    TextField to;
    @FXML
    TextArea message;
    @FXML
    Label uData;
    @FXML
    Label statusError;
    @FXML
    Label minimumUser;
    @FXML
    RadioButton RadioAuther;
    @FXML
    RadioButton RadioSubject;



    private ObservableList<MessageLet> messageData = FXCollections.observableArrayList();




    /*Таблица на 1 ТАБ*/
    @FXML
    private TableView <MessageLet> TableMessage;
    @FXML
    private TableColumn <MessageLet, Integer >id;
    @FXML
    private TableColumn<MessageLet, String> senderLastName;
    @FXML
    private TableColumn<MessageLet, String> senderFirstName;
    @FXML
    private TableColumn<MessageLet, String> senderSecondName;
    @FXML
    private TableColumn<MessageLet, String> recipientLastName;
    @FXML
    private TableColumn<MessageLet, String> recipientFirstName;
    @FXML
    private TableColumn<MessageLet, String> recipientSecondName;
    @FXML
    private TableColumn<MessageLet, String> subjectC;
    @FXML
    private TableColumn<MessageLet, String> text;
    @FXML
    private TableColumn<MessageLet, java.sql.Date> time;
/*///Таблица на 1 ТАб*/


/*ТАблица на 3 ТАБ*/

/*//// Таблица на 3 Таб */

    public void enter(ActionEvent actionEvent) {
        String login=this.login.getText();
        String pass=this.pass.getText();
        try {
            errorOut.setText(model.isAccess(login,pass));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        /*uData.setText("Ти есть "+String.valueOf(model.getUser().getFirstName())+" твой ID "+model.getUser().getId());*/
        if(to.getText().isEmpty() || message.getText().isEmpty()) {
            statusError.setText("Пропуски заполни");
            return;
        }else
        {
            try{
                int toS=Integer.valueOf(to.getText());
                if(model.isUser(to.getText()))
                model.sendMessage(toS, model.getUser().getId(), subject.getText(), message.getText());
                else {
                    statusError.setText("Данный получатель отсутствует");
                    return;
                }
            }
            catch (Exception e){
                statusError.setText("Типы данних получателя не верний");
            }
        }
        User us=model.userById(to.getText());
        statusError.setText("Сообщение для "+us.getLastName()+" "+us.getFirstName()+" "+us.getSecondName()+" отправленно ");
        cleanMessageForm();
    }

    private boolean cleanMessageForm() {
        subject.clear();
        to.clear();
        message.clear();
        return true;
    }


    public void initialize(URL location, ResourceBundle resources) {
        try {
            uData.setText("Ти есть "+String.valueOf(model.getUser().getFirstName())+" твой ID "+model.getUser().getId());
        }
        catch (Exception e){
        }
    }

    public void fiendMessage(ActionEvent actionEvent) {
        if(RadioAuther.isSelected()) {
            TableMessage.getItems().clear();
            ArrayList<MessageLet> list = model.fiendMessageByAuther(byAuther.getText());
            messageData.addAll(list);
            id.setCellValueFactory(new PropertyValueFactory<MessageLet, Integer>("id"));
            senderLastName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("LastNameSend"));
            senderFirstName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("FirstNameSend"));
            senderSecondName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("SecondNameSend"));

            recipientLastName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("LastNameRecip"));
            recipientFirstName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("FirstNameRecip"));
            recipientSecondName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("SecondNameRecip"));

            subjectC.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("subject"));
            text.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("text"));
            time.setCellValueFactory(new PropertyValueFactory<MessageLet, java.sql.Date>("dateSend"));
            TableMessage.setItems(messageData);
        } else
            if(RadioSubject.isSelected())
            {
                TableMessage.getItems().clear();
                ArrayList<MessageLet> list=model.fiendMessageBySubject(bySubject.getText());
                messageData.addAll(list);
                id.setCellValueFactory(new PropertyValueFactory<MessageLet, Integer>("id"));
                senderLastName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("LastNameSend"));
                senderFirstName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("FirstNameSend"));
                senderSecondName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("SecondNameSend"));

                recipientLastName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("LastNameRecip"));
                recipientFirstName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("FirstNameRecip"));
                recipientSecondName.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("SecondNameRecip"));

                subjectC.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("subject"));
                text.setCellValueFactory(new PropertyValueFactory<MessageLet, String>("text"));
                time.setCellValueFactory(new PropertyValueFactory<MessageLet, java.sql.Date>("dateSend"));

                TableMessage.setItems(messageData);
            }
    }


    public void minUser(MouseEvent mouseEvent) {
        User minLetter=model.minimumSizeLetter();
        minimumUser.setText(" = "+minLetter.getLastName()+" "+minLetter.getFirstName()+" "+minLetter.getSecondName()+" ID "+ minLetter.getId());

    }

    public void chooseRadioAuther(ActionEvent actionEvent) {
        RadioSubject.setSelected(false);
    }

    public void chooseRadioSubject(ActionEvent actionEvent) {
        RadioAuther.setSelected(false);
    }
}
