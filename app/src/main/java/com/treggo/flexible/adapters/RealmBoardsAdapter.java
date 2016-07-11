package com.treggo.flexible.adapters;

import android.content.Context;

import com.treggo.flexible.board.Board;

import io.realm.RealmResults;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class RealmBoardsAdapter extends RealmModelAdapter<Board> {

    public RealmBoardsAdapter(Context context, RealmResults<Board> realmResults) {
        super(context, realmResults);
    }
}
