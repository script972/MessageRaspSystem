package com.script972.Model;

import com.mysql.jdbc.log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by script972 on 19.02.2017.
 */
public class DBManipulate {

    private static final String SELECTuserByID="SELECT * FROM letter.user WHERE id=?";
    private static final String SELECTuser="SELECT * FROM letter.user";
    private static final String SENDMESSAGE="INSERT INTO letter.message(sender, recipient, subject, text, dateSend) VALUES (?,?,?,?,NOW())";
    private static final String MESSAGEBYAUR="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
            "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
            "    WHERE sender=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";

    private static final String MESSAGEBYLASTNAME="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
            "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
            "    WHERE senduser.LastName=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";

    private static final String MESSAGEBYLASTNAMEFIRSTNAME="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
            "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
            "    WHERE senduser.LastName=? AND senduser.FirstName=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";

    private static final String MESSAGEBYFULLNAME="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
            "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
            "    WHERE senduser.LastName=? AND senduser.FirstName=? AND senduser.SecondName= ?   AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";


    private static final String MESSAGEBYSUBJECT="    SELECT message.id, sender, recipient, subject, text, dateSend, " +
            "senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
            "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
            "    WHERE subject=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";
    private static final String MESSAGEMINIMUM="SELECT user.id, send.LastName, send.FirstName, send.SecondName, recip.LastName, " +
            "recip.FirstName, recip.SecondName, subject, text, dateSend\n" +
            "FROM message, user, user AS recip, user AS send WHERE text=(SELECT min(text) FROM message) AND send.id = message.sender  AND recip.id = message.recipient LIMIT 1";

    private static final String NewUser="INSERT INTO letter.user(password, SecondName, LastName, FirstName) VALUES (?,?,?,?)";
    private static final String idByName="SELECT id FROM letter.user WHERE LastName=? AND FirstName=? AND SecondName=?";



    private static final String USERNAME="script972";
    private static final String PASSWORD="root";
    private static final String URL="jdbc:mysql://127.0.0.1:3306/letter?useSSL=false";
    private Connection conn;
    private User minimumLetter;

    private void connect() {
        DBProcessor db= null;
        try {
            db = new DBProcessor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn = db.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static java.sql.Date getCurrentJavaSqlDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }


    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public User userById(String login) throws SQLException {
        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(SELECTuserByID);
        preparedStatement.setInt(1, Integer.parseInt(login));
        ResultSet rs = preparedStatement.executeQuery();

        int userid = 0;
        String firstname = null;
        String lastname = null;
        String secondname = null;
        Date birthday = null;
        String pass = null;


        while (rs.next()) {
            userid = rs.getInt("id");
            firstname = rs.getString("FirstName");
            lastname=rs.getString("LastName");
            secondname=rs.getString("SecondName");
            birthday=rs.getDate("birthday");
            pass=rs.getString("password");
        }
        close();
        return new User(userid, firstname, lastname, secondname, birthday, pass);
    }

    public ArrayList<User> allUser() throws SQLException {
        ArrayList<User> al=new ArrayList<User>();
        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(SELECTuser);
        ResultSet rs = preparedStatement.executeQuery();
        int userid = 0;
        String firstname = null;
        String lastname = null;
        String secondname = null;
        Date birthday = null;
        String pass = null;
        while (rs.next()) {
            userid = rs.getInt("id");
            firstname = rs.getString("FirstName");
            lastname=rs.getString("LastName");
            secondname=rs.getString("SecondName");
            birthday=rs.getDate("birthday");
            pass=rs.getString("password");
            al.add(new User(userid, firstname, lastname, secondname, birthday, pass));
        }
        close();
        return al;
    }

    public boolean sendMessage(int to, int from, String subject, String text) throws SQLException {
        connect();
      //  Date date = getCurrentJavaSqlDate();
        PreparedStatement preparedStatement=conn.prepareStatement(SENDMESSAGE);
        preparedStatement.setInt(1, from);
        preparedStatement.setInt(2, to);
        preparedStatement.setString(3, subject);
        preparedStatement.setString(4, text);
        //preparedStatement.setDate(5, (java.sql.Date) date);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        close();

        return true;
    }



    public ArrayList<MessageLet> getMessageAutherByID(String auther) throws SQLException {
        ArrayList<MessageLet> al=new ArrayList<MessageLet>();
        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEBYAUR);
        preparedStatement.setInt(1, Integer.parseInt(auther));
        ResultSet rs = preparedStatement.executeQuery();
         int id;
         String subject;
         String text;
         java.sql.Date dateSend;
         String FirstNameSend;
         String LastNameSend;
         String SecondNameSend;
         String FirstNameRecip;
         String LastNameRecip;
         String SecondNameRecip;


        while (rs.next()) {
            id=rs.getInt("id");
            subject=rs.getString("subject");
            text=rs.getString("text");
            dateSend=rs.getDate("dateSend");
            FirstNameSend = rs.getString("FirstName");
            LastNameSend=rs.getString("LastName");
            SecondNameSend=rs.getString("SecondName");

            FirstNameRecip = rs.getString(10);
            LastNameRecip=rs.getString(11);
            SecondNameRecip=rs.getString(12);
            MessageLet ms=new MessageLet(id, subject, text, dateSend, FirstNameSend, LastNameSend, SecondNameSend, FirstNameRecip, LastNameRecip, SecondNameRecip);
            al.add(ms);
            }
            preparedStatement.close();
        close();
        return al;
    }

    public ArrayList<MessageLet> getMessageSubject(String subjectF) throws SQLException {
        ArrayList<MessageLet> al=new ArrayList<MessageLet>();
        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEBYSUBJECT);
        preparedStatement.setString(1, subjectF);
        ResultSet rs = preparedStatement.executeQuery();
        int id;
        String subject;
        String text;
        java.sql.Date dateSend;
        String FirstNameSend;
        String LastNameSend;
        String SecondNameSend;
        String FirstNameRecip;
        String LastNameRecip;
        String SecondNameRecip;


        while (rs.next()) {
            id=rs.getInt("id");
            subject=rs.getString("subject");
            text=rs.getString("text");
            dateSend=rs.getDate("dateSend");
            FirstNameSend = rs.getString("FirstName");
            LastNameSend=rs.getString("LastName");
            SecondNameSend=rs.getString("SecondName");

            FirstNameRecip = rs.getString(10);
            LastNameRecip=rs.getString(11);
            SecondNameRecip=rs.getString(12);
            MessageLet ms=new MessageLet(id, subject, text, dateSend, FirstNameSend, LastNameSend, SecondNameSend, FirstNameRecip, LastNameRecip, SecondNameRecip);
            al.add(ms);
        }
        preparedStatement.close();
        close();
        return al;
    }

    public User getMinimumLetter() throws SQLException {
        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEMINIMUM);
        ResultSet rs = preparedStatement.executeQuery();
        int id;
        String FirstNameSend;
        String LastNameSend;
        String SecondNameSend;

        while (rs.next()) {
            id=rs.getInt("id");
            FirstNameSend = rs.getString("FirstName");
            LastNameSend=rs.getString("LastName");
            SecondNameSend=rs.getString("SecondName");

           User user=new User(id, FirstNameSend, LastNameSend, SecondNameSend);
           return user;
        }
        preparedStatement.close();
        close();
        return null;
    }


    public ArrayList<MessageLet> getMessageAutherByLastName(String LName) throws SQLException {
        ArrayList<MessageLet> list = new ArrayList<MessageLet>();

        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEBYLASTNAME);
        preparedStatement.setString(1, LName);
        ResultSet rs = preparedStatement.executeQuery();
        int id;
        String subject;
        String text;
        java.sql.Date dateSend;
        String FirstNameSend;
        String LastNameSend;
        String SecondNameSend;
        String FirstNameRecip;
        String LastNameRecip;
        String SecondNameRecip;


        while (rs.next()) {
            id=rs.getInt("id");
            subject=rs.getString("subject");
            text=rs.getString("text");
            dateSend=rs.getDate("dateSend");
            FirstNameSend = rs.getString("FirstName");
            LastNameSend=rs.getString("LastName");
            SecondNameSend=rs.getString("SecondName");

            FirstNameRecip = rs.getString(10);
            LastNameRecip=rs.getString(11);
            SecondNameRecip=rs.getString(12);
            MessageLet ms=new MessageLet(id, subject, text, dateSend, FirstNameSend, LastNameSend, SecondNameSend, FirstNameRecip, LastNameRecip, SecondNameRecip);
            list.add(ms);
        }
        preparedStatement.close();
        close();

        return list;
    }

    public ArrayList<MessageLet> getMessageAutherByLastNameFirstName(String LName, String FName) throws SQLException {
        ArrayList<MessageLet> list = new ArrayList<MessageLet>();

        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEBYLASTNAMEFIRSTNAME);
        preparedStatement.setString(1, LName);
        preparedStatement.setString(2, FName);
        ResultSet rs = preparedStatement.executeQuery();
        int id;
        String subject;
        String text;
        java.sql.Date dateSend;
        String FirstNameSend;
        String LastNameSend;
        String SecondNameSend;
        String FirstNameRecip;
        String LastNameRecip;
        String SecondNameRecip;


        while (rs.next()) {
            id=rs.getInt("id");
            subject=rs.getString("subject");
            text=rs.getString("text");
            dateSend=rs.getDate("dateSend");
            FirstNameSend = rs.getString("FirstName");
            LastNameSend=rs.getString("LastName");
            SecondNameSend=rs.getString("SecondName");

            FirstNameRecip = rs.getString(10);
            LastNameRecip=rs.getString(11);
            SecondNameRecip=rs.getString(12);
            MessageLet ms=new MessageLet(id, subject, text, dateSend, FirstNameSend, LastNameSend, SecondNameSend, FirstNameRecip, LastNameRecip, SecondNameRecip);
            list.add(ms);
        }
        preparedStatement.close();
        close();

        return list;
    }


    public ArrayList<MessageLet> getMessageAutherByFullName(String LName, String FName, String SName) throws SQLException {
        ArrayList<MessageLet> list =new ArrayList<MessageLet>();

        connect();
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEBYFULLNAME);
        preparedStatement.setString(1, LName);
        preparedStatement.setString(2, FName);
        preparedStatement.setString(3, SName);
        ResultSet rs = preparedStatement.executeQuery();
        int id;
        String subject;
        String text;
        java.sql.Date dateSend;
        String FirstNameSend;
        String LastNameSend;
        String SecondNameSend;
        String FirstNameRecip;
        String LastNameRecip;
        String SecondNameRecip;


        while (rs.next()) {
            id=rs.getInt("id");
            subject=rs.getString("subject");
            text=rs.getString("text");
            dateSend=rs.getDate("dateSend");
            FirstNameSend = rs.getString("FirstName");
            LastNameSend=rs.getString("LastName");
            SecondNameSend=rs.getString("SecondName");

            FirstNameRecip = rs.getString(10);
            LastNameRecip=rs.getString(11);
            SecondNameRecip=rs.getString(12);
            MessageLet ms=new MessageLet(id, subject, text, dateSend, FirstNameSend, LastNameSend, SecondNameSend, FirstNameRecip, LastNameRecip, SecondNameRecip);
            list.add(ms);
        }
        preparedStatement.close();
        close();

        return list;
    }

    public int createNewUser(String lastName, String firstName, String secondName, String password) throws SQLException {
        connect();
        //  Date date = getCurrentJavaSqlDate();
        PreparedStatement preparedStatement=conn.prepareStatement(NewUser);
        preparedStatement.setString(1, password);
        preparedStatement.setString(2, secondName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, firstName);
        //preparedStatement.setDate(5, (java.sql.Date) date);
        preparedStatement.executeUpdate();

        preparedStatement = conn.prepareStatement(idByName);
        preparedStatement.setString(1, lastName);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, secondName);
        ResultSet rs = preparedStatement.executeQuery();
        int id=0;
        while (rs.next()){
            id=rs.getInt("id");
        }
        preparedStatement.close();
        close();
        return id;
    }
}
