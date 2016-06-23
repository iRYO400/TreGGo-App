package com.treggo.flexible.card.models;


import io.realm.RealmObject;

/**
 * Created by iRYO400 on 15.06.2016.
 */
public class CheckList extends RealmObject {

    private String name;

    private boolean isDone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
