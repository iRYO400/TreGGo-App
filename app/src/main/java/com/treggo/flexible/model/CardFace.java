package com.treggo.flexible.model;


import java.io.Serializable;

/**
 * Created by iRYO400 on 08.06.2016.
 */
public class CardFace implements Serializable {

    private String name;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
