package com.script972.Model;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by script972 on 19.02.2017.
 */
public class Model  {
    DBManipulate dbManipulate = new DBManipulate();

    private static User user;

    public User getUser() {
        return user;
    }

    public String isAccess(String login, String password) throws SQLException {
        if(login.isEmpty())
            return "Логін не заповнений";
         else
            if(password.isEmpty())
            return "Пароль не заповнений";

        User users=dbManipulate.userById(login);
        if(users.getPass().equals(password)) {
            user=users;

            WindowsControl windowsControl=new WindowsControl();
            try {
                windowsControl.start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Ласкаво просимо";
        }
        else
            return "Пароль не вірний";
    }

    public boolean isUser(String id){
        try {
            if(dbManipulate.userById(id).getId()!=0)
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User userById(String id){
        try {
            return dbManipulate.userById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  boolean sendMessage(int to, int from, String subject, String text)
    {
        try {
           return dbManipulate.sendMessage(to,from, subject,text);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<User> getAllUser(){
        ArrayList <User>al=null;
        try {
           al=dbManipulate.allUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return al;
    }


    public ArrayList<MessageLet> fiendMessageByAuther(String auther){
        try {
            return  dbManipulate.getMessageAuther(auther);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<MessageLet> fiendMessageBySubject(String subject){
        try {
            return  dbManipulate.getMessageSubject(subject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User minimumSizeLetter() {
        try {
            return dbManipulate.getMinimumLetter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
