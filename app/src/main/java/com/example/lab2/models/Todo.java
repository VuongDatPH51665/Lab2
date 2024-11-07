package com.example.lab2.models;

import java.util.HashMap;

public class Todo {
    private String id;
    private String title;
    private String content;
    private String date;
    private String type;
    private int status;

    public Todo() {
    }

    public Todo(String title, String content, String date, String type, int status) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static interface UpdateInterFace {
        void addOnClickButton(Todo todo);
        void updateTodoList(Todo todo);
        void deleteTodoList(Todo todo);
    }
    public HashMap<String, String> converHashMap() {
        HashMap work = new HashMap();
        work.put("id", id);
        work.put("title", title);
        work.put("content", content);
        work.put("date", date);
        work.put("type", type);
        return work;
    }
}
