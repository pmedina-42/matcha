package org.example.model.entity;

import java.sql.Timestamp;
import java.util.Objects;

public class Log {

    public enum Type {
        LIKE,
        VIEW
    }

    private Integer id;
    private Type type;
    private String sender;
    private String receiver;
    private Timestamp date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", type=" + type +
                ", issuing='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        return Objects.equals(id, log.id) && type == log.type && Objects.equals(sender, log.sender) && Objects.equals(receiver, log.receiver) && Objects.equals(date, log.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, sender, receiver, date);
    }
}
