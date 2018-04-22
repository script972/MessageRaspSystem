package com.script972.dao;

import com.script972.Model.DBProcessor;
import com.script972.entity.MessageLet;
import com.script972.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by script972 on 19.02.2017.
 */
public class DBManipulate {

    private static final String USERNAME="root";
    private static final String PASSWORD="root";
    private static final String URL="jdbc:mysql://127.0.0.1:3306/letter?useSSL=false";
    private static final String URL2="jdbc:mysql://127.0.0.1:3306/letter2?useSSL=false";
    private Connection conn;
    private Connection conn2;

    private void connect() {
        DBProcessor db= null;
        try {
            db = new DBProcessor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            long s=System.currentTimeMillis();
            conn = db.getConnection(URL, USERNAME, PASSWORD);
            System.out.println(System.currentTimeMillis()-s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect2() {
        DBProcessor db= null;
        try {
            db = new DBProcessor();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            long s=System.currentTimeMillis();
            conn2 = db.getConnection(URL2, USERNAME, PASSWORD);
            System.out.println(System.currentTimeMillis()-s);
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
        String SELECTuserByID="SELECT * FROM letter.user WHERE id=?";
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
        String SELECTuser="SELECT * FROM letter.user";
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
        connect2();
        conn.setAutoCommit(false);
        conn2.setAutoCommit(false);
        PreparedStatement preparedStatement = null;
      //  Date date = getCurrentJavaSqlDate();
        try {

            String SENDMESSAGE = "INSERT INTO letter.message(sender, recipient, subject, text, dateSend) VALUES (?,?,?,?,NOW())";
            preparedStatement = conn.prepareStatement(SENDMESSAGE);
            preparedStatement.setInt(1, from);
            preparedStatement.setInt(2, to);
            preparedStatement.setString(3, subject);
            preparedStatement.setString(4, text);
            preparedStatement.executeUpdate();
            conn.commit();

            String addLog="INSERT INTO letter2.logs" +
                    "(action_person_do, type_action, date, action_person) VALUES\n" +
                    "  (?,?,?,?);";
            preparedStatement = conn2.prepareStatement(addLog);
            preparedStatement.setInt(1, from);
            preparedStatement.setString(2,"Send Message");
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setInt(4, to);
            preparedStatement.executeUpdate();
            conn2.commit();


        }catch (Exception ex){
            conn2.rollback();
            conn.rollback();

        }finally {
            preparedStatement.close();
            conn2.close();
            close();
        }



        return true;
    }

    public ArrayList<MessageLet> getMessageAutherByID(String auther) throws SQLException {
        ArrayList<MessageLet> al=new ArrayList<MessageLet>();
        connect();
        String MESSAGEBYAUR="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
                "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
                "    WHERE sender=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";
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
        String MESSAGEBYSUBJECT="    SELECT message.id, sender, recipient, subject, text, dateSend, " +
                "senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
                "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
                "    WHERE subject=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";
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
        String MESSAGEMINIMUM="SELECT user.id, send.LastName, send.FirstName, send.SecondName, recip.LastName,\n" +
                "  recip.FirstName, recip.SecondName, subject, text, dateSend FROM\n" +
                "  message, user, user AS recip, user AS send WHERE LENGTH(message.text)=(SELECT MIN(LENGTH(text)) FROM message) LIMIT 1;\n" +
                "\n";

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
        String MESSAGEBYLASTNAME="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
                "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
                "    WHERE senduser.LastName=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";

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
        String MESSAGEBYLASTNAMEFIRSTNAME="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
                "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
                "    WHERE senduser.LastName=? AND senduser.FirstName=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";

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
        String MESSAGEBYFULLNAME="    SELECT message.id, sender, recipient, subject, text, dateSend, senduser.FirstName, senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
                "    FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
                "    WHERE senduser.LastName=? AND senduser.FirstName=? AND senduser.SecondName= ?   AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";


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

    public ArrayList<MessageLet> getMessageByRecipientID(String auther) throws SQLException {
        ArrayList<MessageLet> al=new ArrayList<MessageLet>();
        connect();
        String MESSAGEBYRECIPIENTID="SELECT senduser.id ,sender, recipient, subject, text, dateSend, senduser.FirstName,\n" +
                "  senduser.LastName, senduser.SecondName, recuser.FirstName, recuser.LastName, recuser.SecondName\n" +
                "FROM letter.message, letter.user as senduser, letter.user as recuser\n" +
                "WHERE recipient=?  AND sender=senduser.id AND recipient=recuser.id ORDER BY dateSend DESC";
        PreparedStatement preparedStatement = conn.prepareStatement(MESSAGEBYRECIPIENTID);
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

    public int createNewUser(String lastName, String firstName, String secondName, String password) throws SQLException {
        connect();
        String idByName="SELECT id FROM letter.user WHERE LastName=? AND FirstName=? AND SecondName=?";

        //  Date date = getCurrentJavaSqlDate();
        String NewUser="INSERT INTO letter.user(password, SecondName, LastName, FirstName) VALUES (?,?,?,?)";

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

    public ArrayList<User> getUsetWithCounting() throws SQLException {
        ArrayList<User> al=new ArrayList<User>();
        connect();
        String SELECTuser="SELECT * FROM letter.user";
        PreparedStatement preparedStatement = conn.prepareStatement(SELECTuser);
        ResultSet rs = preparedStatement.executeQuery();
        int userid = 0;
        String firstname = null;
        String lastname = null;
        String secondname = null;
        Date birthday = null;
        String pass = null;
        int send=0;
        int recip=0;
        while (rs.next()) {
            userid = rs.getInt("id");
            firstname = rs.getString("FirstName");
            lastname=rs.getString("LastName");
            secondname=rs.getString("SecondName");
            birthday=rs.getDate("birthday");
            pass=rs.getString("password");
            send=countSend(userid);
            recip=countRecip(userid);
            al.add(new User(userid, firstname, lastname, secondname, birthday, pass, send, recip));
           // System.out.println(new User(userid, firstname, lastname, secondname, birthday, pass, send, recip));
        }
        close();
        return al;
    }

    private int countSend(int id) throws SQLException {
        connect();
        String countSender="SELECT COUNT(DISTINCT message.id) FROM message, user WHERE message.sender=?";

        PreparedStatement preparedStatement = conn.prepareStatement(countSender);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
           return rs.getInt(1);
        }
        close();
        return 0;

    }

    private int countRecip(int id) throws SQLException {
        connect();
        String countRecipient="SELECT COUNT(DISTINCT message.id) FROM message, user WHERE message.recipient=?";

        PreparedStatement preparedStatement = conn.prepareStatement(countRecipient);
        preparedStatement.setInt(1, id);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            return rs.getInt(1);
        }
        close();
        return 0;
    }
}
