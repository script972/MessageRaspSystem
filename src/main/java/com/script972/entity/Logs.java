package com.script972.entity;

import java.sql.Timestamp;

public class Logs {
    private long id;
    private long actionPersonDo;
    private String typeAction;
    private Timestamp date;
    private long actionPerson;

    public Logs(long id, long actionPersonDo, String typeAction, Timestamp date, long actionPerson) {
        this.id = id;
        this.actionPersonDo = actionPersonDo;
        this.typeAction = typeAction;
        this.date = date;
        this.actionPerson = actionPerson;
    }

    public Logs() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActionPersonDo() {
        return actionPersonDo;
    }

    public void setActionPersonDo(long actionPersonDo) {
        this.actionPersonDo = actionPersonDo;
    }

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public long getActionPerson() {
        return actionPerson;
    }

    public void setActionPerson(long actionPerson) {
        this.actionPerson = actionPerson;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "id=" + id +
                ", actionPersonDo=" + actionPersonDo +
                ", typeAction='" + typeAction + '\'' +
                ", date=" + date +
                ", actionPerson=" + actionPerson +
                '}';
    }
}
