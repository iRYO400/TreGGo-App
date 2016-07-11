package com.treggo.flexible.card.attachment;

import io.realm.RealmObject;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public class AttachedString extends RealmObject {
    private String picPath;
    private int viewType;

    private String docName;
    private String docInfo;


    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocInfo() {
        return docInfo;
    }

    public void setDocInfo(String docInfo) {
        this.docInfo = docInfo;
    }
}
