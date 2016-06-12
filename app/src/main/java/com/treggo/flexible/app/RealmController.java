package com.treggo.flexible.app;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;


import com.treggo.flexible.model.Board;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void waitForChange() {

        realm.setAutoRefresh(true);
    }

    public void stopWaitForChange() {

        realm.stopWaitForChange();
    }

    //clear all objects from Realm
    public void clearAll() {

        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    //find all objects in the Board.class
    public RealmResults<Board> getBoards() {

        return realm.where(Board.class).findAll();
    }

    //query a single item with the given id
    public Board getBoard(String id) {

        return realm.where(Board.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasBoard() {

        return !realm.where(Board.class).isValid();
    }

    //query example
    public RealmResults<Board> queryedBooks() {

        return realm.where(Board.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}