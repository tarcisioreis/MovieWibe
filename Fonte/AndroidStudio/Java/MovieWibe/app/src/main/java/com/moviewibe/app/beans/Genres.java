package com.moviewibe.app.beans;

public class Genres extends AbstractBean {

    private int id;
    private String name;

    public Genres() {}

    public Genres(int id, String name) {
        setId(id);
        setName(name);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return this.getName();
    }

}
