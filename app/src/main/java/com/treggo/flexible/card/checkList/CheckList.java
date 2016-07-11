package com.treggo.flexible.card.checkList;


import io.realm.RealmObject;

/**
 * Created by iRYO400 on 15.06.2016.
 */
public class CheckList extends RealmObject {

    private String name;

    private boolean isChecked;

    private int viewType;


    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
