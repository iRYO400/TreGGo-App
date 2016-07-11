package com.treggo.flexible.lists;

import android.support.v7.widget.RecyclerView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public abstract class RealmRecyclerViewListAdapter<T extends RealmObject>
        extends RecyclerView.Adapter<ListsRecyclerAdapter.MyViewHolder> {

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

    @Override
    public void onViewAttachedToWindow(ListsRecyclerAdapter.MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }
}
