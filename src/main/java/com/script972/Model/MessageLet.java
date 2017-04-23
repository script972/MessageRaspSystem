package com.script972.Model;

import java.sql.Date;

/**
 * Created by script972 on 19.02.2017.
 */
public class MessageLet {
    private int id;
    private String subject;
    private String text;
    private java.sql.Date dateSend;
    private String FirstNameSend;
    private String LastNameSend;
    private String SecondNameSend;
    private String FirstNameRecip;
    private String LastNameRecip;
    private String SecondNameRecip;


    public MessageLet(int id, String subject, String text, Date dateSend, String firstNameSend, String lastNameSend, String secondNameSend, String firstNameRecip, String lastNameRecip, String secondNameRecip) {
        this.id = id;
        this.subject = subject;
        this.text = text;
        this.dateSend = dateSend;
        FirstNameSend = firstNameSend;
        LastNameSend = lastNameSend;
        SecondNameSend = secondNameSend;
        FirstNameRecip = firstNameRecip;
        LastNameRecip = lastNameRecip;
        SecondNameRecip = secondNameRecip;
    }


    @Override
    public String toString() {
        return "MessageLet{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", dateSend=" + dateSend +
                ", FirstNameSend='" + FirstNameSend + '\'' +
                ", LastNameSend='" + LastNameSend + '\'' +
                ", SecondNameSend='" + SecondNameSend + '\'' +
                ", FirstNameRecip='" + FirstNameRecip + '\'' +
                ", LastNameRecip='" + LastNameRecip + '\'' +
                ", SecondNameRecip='" + SecondNameRecip + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public String getFirstNameSend() {
        return FirstNameSend;
    }

    public void setFirstNameSend(String firstNameSend) {
        FirstNameSend = firstNameSend;
    }

    public String getLastNameSend() {
        return LastNameSend;
    }

    public void setLastNameSend(String lastNameSend) {
        LastNameSend = lastNameSend;
    }

    public String getSecondNameSend() {
        return SecondNameSend;
    }

    public void setSecondNameSend(String secondNameSend) {
        SecondNameSend = secondNameSend;
    }

    public String getFirstNameRecip() {
        return FirstNameRecip;
    }

    public void setFirstNameRecip(String firstNameRecip) {
        FirstNameRecip = firstNameRecip;
    }

    public String getLastNameRecip() {
        return LastNameRecip;
    }

    public void setLastNameRecip(String lastNameRecip) {
        LastNameRecip = lastNameRecip;
    }

    public String getSecondNameRecip() {
        return SecondNameRecip;
    }

    public void setSecondNameRecip(String secondNameRecip) {
        SecondNameRecip = secondNameRecip;
    }
}
