package com.treggo.flexible.card.attachment;

import io.realm.RealmObject;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public class AttachedString extends RealmObject {
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
