package com.yanxit.reminders;

/**
 * Created by yanxit on 8/8/2017.
 */

public class Reminder {
    private int id;
    private String content;
    private int important;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }

    public Reminder(int id, String content, int important) {
        this.id = id;
        this.content = content;
        this.important = important;
    }

    public Reminder() {
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", important=" + important +
                '}';
    }
}
