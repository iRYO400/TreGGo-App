package com.treggo.flexible.card.attachment;

import android.support.v7.widget.RecyclerView;


import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public abstract class RealmRecyclerViewAttachedAdapter<T extends RealmObject> extends RecyclerView.Adapter<AttachedRecyclerViewHolder> {

    private RealmBaseAdapter<T> realmBaseAdapter;

    public RealmBaseAdapter<T> getRealmAdapter() {
        return realmBaseAdapter;
    }

    public void setRealmAdapter(RealmBaseAdapter<T> realmAdapter) {
        realmBaseAdapter = realmAdapter;
    }

    public T getItem(int position) {
        return realmBaseAdapter.getItem(position);
    }

    public long getItemID(int position) {
        return realmBaseAdapter.getItemId(position);
    }
}

