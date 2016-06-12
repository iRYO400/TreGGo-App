package com.treggo.flexible.model;


import io.realm.RealmObject;

/**
 * Created by iRYO400 on 08.06.2016.
 */
public class Card extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
