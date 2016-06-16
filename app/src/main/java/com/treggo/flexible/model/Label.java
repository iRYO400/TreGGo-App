package com.treggo.flexible.model;

import io.realm.RealmObject;

/**
 * Created by iRYO400 on 13.06.2016.
 */
public class Label extends RealmObject {

    private String labelName;

    private boolean isChecked;

    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
