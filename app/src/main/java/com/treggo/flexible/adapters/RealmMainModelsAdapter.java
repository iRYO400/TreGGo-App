package com.treggo.flexible.adapters;

import android.content.Context;

import com.treggo.flexible.model.MainModel;

import io.realm.RealmResults;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class RealmMainModelsAdapter extends RealmModelAdapter<MainModel> {

    public RealmMainModelsAdapter(Context context, RealmResults<MainModel> realmResults, boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }
}
