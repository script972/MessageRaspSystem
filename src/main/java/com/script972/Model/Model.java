package com.script972.Model;

import com.script972.Windows.RegisterWindows;
import com.script972.Windows.WindowsControl;
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
        try{
            return  dbManipulate.getMessageAutherByID(auther);
        }catch (NumberFormatException e)
        {
            String [] FIO=auther.split(" ");


            switch (FIO.length){
                case 0: return null;
                case 1:
                    try {
                        return dbManipulate.getMessageAutherByLastName(FIO[0]);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                case 2:
                    try {
                        return dbManipulate.getMessageAutherByLastNameFirstName(FIO[0], FIO[1]);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        return dbManipulate.getMessageAutherByFullName(FIO[0], FIO[1], FIO[2]);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        }
        catch (SQLException e){
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

    public void openRegistrationWindow() {
        RegisterWindows gw=new RegisterWindows();
        try {
            gw.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int registerNewPerson(String lastName, String firstName, String secondName, String password) {
        try {
           return dbManipulate.createNewUser(lastName, firstName, secondName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }


}
