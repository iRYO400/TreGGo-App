package com.treggo.flexible.card.checkList;

import android.support.v7.widget.RecyclerView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;

/**
 * Created by iRYO400 on 24.06.2016.
 */
public abstract class RealmRecyclerViewCheckLAdapter<T extends RealmObject> extends RecyclerView.Adapter<CheckLRecyclerViewHolder> {

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
