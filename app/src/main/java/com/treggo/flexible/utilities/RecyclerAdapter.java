package com.treggo.flexible.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.treggo.flexible.R;
import com.treggo.flexible.adapters.RealmRecyclerViewAdapter;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.Board;

import io.realm.Realm;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class RecyclerAdapter extends RealmRecyclerViewAdapter<Board> {


    private static final String TAG = "mLogs";

    private Context context;
    private Realm realm;
    private long boardID;
    private int boardPosition;
    private int listPosition;

    private TinyDB tinyDB;

    public RecyclerAdapter(Context context, long boardID, int listPosition){
        this.context = context;
        this.boardID = boardID;
        this.listPosition = listPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        tinyDB = new TinyDB(context);
        boardPosition = tinyDB.getInt("boardPosition");
        switch (viewType){
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_in_list, parent, false);
                break;
        }
        return new CardListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        this.realm = RealmController.getInstance().getRealm();

        CardListViewHolder cardListVH = (CardListViewHolder) holder;

        //Имя карточки в списке
        if(getItem(boardPosition).getMyLists().get(listPosition).getCards().isValid()) {
            cardListVH.card_list_name.setText(getItem(boardPosition).getMyLists().get(listPosition).getCards().get(position).getName());
        }
    }


    @Override
    public int getItemCount() {
        if(getRealmAdapter() != null){
//            return 3;
            try {
                return getRealmAdapter().getItem(boardPosition).getMyLists().get(listPosition).getCards().size();
            }
            catch (ArrayIndexOutOfBoundsException e){
                Log.d(TAG, "Cyka o5 ETA OSHIBKA. B_Position " + boardPosition + " L_Position " + listPosition);
            }

        }
        return 0;
    }

    public static class CardListViewHolder extends RecyclerView.ViewHolder {

        private CardView card_list;
        private TextView card_list_name;

        public CardListViewHolder(View itemView) {
            super(itemView);

            card_list = (CardView) itemView.findViewById(R.id.card_list);
            card_list_name = (TextView)itemView.findViewById(R.id.card_list_name);
        }
    }
}
