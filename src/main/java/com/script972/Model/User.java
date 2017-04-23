package com.script972.Model;

import java.util.Date;

/**
 * Created by script972 on 19.02.2017.
 */
public class User {
    private int id;
    private String FirstName;
    private String LastName;
    private String SecondName;
    private Date birthday;
    private String pass;

    private int countSend;
    private int countRecip;

    public int getCountSend() {
        return countSend;
    }

    public void setCountSend(int countSend) {
        this.countSend = countSend;
    }

    public int getCountRecip() {
        return countRecip;
    }

    public void setCountRecip(int countRecip) {
        this.countRecip = countRecip;
    }

    public User(int id, String firstName, String lastName, String secondName, Date birthday, String pass, int countSend, int countRecip) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        SecondName = secondName;
        this.birthday = birthday;
        this.pass = pass;
        this.countSend = countSend;
        this.countRecip = countRecip;
    }

    public User(int id, String firstName, String lastName, String secondName, Date birthday, String pass) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        SecondName = secondName;
        this.birthday = birthday;
        this.pass = pass;
    }

    public User(int id, String firstName, String lastName, String secondName) {
        this.id = id;
        FirstName = firstName;
        LastName = lastName;
        SecondName = secondName;
    }

    public User(String pass) {
        this.pass = pass;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getSecondName() {
        return SecondName;
    }

    public void setSecondName(String secondName) {
        SecondName = secondName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", SecondName='" + SecondName + '\'' +
                ", birthday=" + birthday +
                ", pass='" + pass + '\'' +
                ", countSend=" + countSend +
                ", countRecip=" + countRecip +
                '}';
    }

}
