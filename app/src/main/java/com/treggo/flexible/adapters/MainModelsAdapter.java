package com.treggo.flexible.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.treggo.flexible.R;
import com.treggo.flexible.activities.MiddleActivity;
import com.treggo.flexible.app.Preferences;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.MainModel;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class MainModelsAdapter extends RealmRecyclerViewAdapter<MainModel> {

    final Context context;
    private Realm realm;
    private LayoutInflater inflater;

    public MainModelsAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_board, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        realm = RealmController.getInstance().getRealm();

        MainModel mainModel = getItem(position);

        CardViewHolder cardViewHolder = (CardViewHolder) holder;

        cardViewHolder.nameBoard.setText(mainModel.getNameBoard());
        cardViewHolder.cbFavorite.setChecked(mainModel.isFavorite());

        //Override LONG click
        cardViewHolder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                RealmResults<MainModel> results = realm.where(MainModel.class).findAll();
                MainModel mModel = results.get(position);
                String name = mModel.getNameBoard();

                realm.beginTransaction();
                results.deleteFromRealm(position);
                realm.commitTransaction();

                if(results.size()==0){
                    Preferences.with(context).setPreLoad(false);
                }
                notifyDataSetChanged();

                Toast.makeText(context, name + " is removed", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //Override SHORT CLICK
        cardViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, MiddleActivity.class);
//
//                context.startActivity(intent);

                RealmResults<MainModel> results = realm.where(MainModel.class).findAll();
                MainModel mModel = results.get(position);
                long id = mModel.getId();
                String name = mModel.getNameBoard();
                int type = mModel.getBoardType();
                notifyDataSetChanged();
                Toast.makeText(context, "ID " + id +  " Position is " + position + ", Name " + name +
                        ", Type " + type, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {

        if(getRealmAdapter() != null){
            return getRealmAdapter().getCount();
        }
        return 0;
    }


    public static class CardViewHolder extends RecyclerView.ViewHolder{

        public CardView card;
        public TextView nameBoard;
        public CheckBox cbFavorite;


        public CardViewHolder(View itemView) {
            super(itemView);

            card = (CardView) itemView.findViewById(R.id.card_board);
            nameBoard = (TextView) itemView.findViewById(R.id.nameBoardTV);
            cbFavorite = (CheckBox) itemView.findViewById(R.id.cbFavorite);

        }
    }
}
