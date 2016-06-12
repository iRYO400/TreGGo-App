package com.treggo.flexible.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class Board extends RealmObject {

    @PrimaryKey
    private long id;

    private String name;

    private int type;

    private RealmList<MyList> myLists;

    public RealmList<MyList> getMyLists() {
        return myLists;
    }

    public void setMyLists(RealmList<MyList> myLists) {
        this.myLists = myLists;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}


