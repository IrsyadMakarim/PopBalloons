package com.internal.popfruit;

public class Items {

    int id;
    String score;

    public Items() {
    }

    public Items(String score) {

        this.score = score;

    }

    public Items(int id, String score){

        this.id = id;
        this.score = score;
    }

    public void setId(int id){ this.id = id; }

    public int getId() { return id; }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}