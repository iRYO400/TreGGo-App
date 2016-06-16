package com.treggo.flexible.model;


import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by iRYO400 on 08.06.2016.
 */
public class Card extends RealmObject {

    private String name;

    private String description;

    private RealmList<Label> labelList;

    private RealmList<CheckList> checkLists;

    public RealmList<CheckList> getCheckLists() {
        return checkLists;
    }

    public void setCheckLists(RealmList<CheckList> checkLists) {
        this.checkLists = checkLists;
    }

    public RealmList<Label> getLabelList() {
        return labelList;
    }

    public void setLabelList(RealmList<Label> labelList) {
        this.labelList = labelList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
