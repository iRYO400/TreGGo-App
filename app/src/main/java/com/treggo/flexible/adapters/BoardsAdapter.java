package com.treggo.flexible.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import com.treggo.flexible.model.Board;
import com.treggo.flexible.utilities.TinyDB;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class BoardsAdapter extends RealmRecyclerViewAdapter<Board> {

    final Context context;
    private Realm realm;
    private TinyDB tinyDB;

    public BoardsAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_board, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        tinyDB = new TinyDB(context);
        realm = RealmController.getInstance().getRealm();

        final Board board = getItem(position);

        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        if(getItem(position).isValid()) {
            cardViewHolder.nameBoard.setText(board.getName());
//            cardViewHolder.cbFavorite.setChecked(board.isFavorite());
        }


        //Override LONG click
        cardViewHolder.card_board.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(context);
                View subView = layoutInflater.inflate(R.layout.dialog_laoyut2, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.
                RealmResults<Board> results = realm.where(Board.class).findAll();

                Board mModel = results.get(position);
                String name = mModel.getName();

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
        cardViewHolder.card_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MiddleActivity.class);
                //Передаем индекс доски
                RealmResults<Board> results = realm.where(Board.class).findAll();
                tinyDB.putInt("boardPosition", position);
                Board b = results.get(position);
                long id = b.getId();
                intent.putExtra("BoardID", id);
                context.startActivity(intent);
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

        public CardView card_board;
        public TextView nameBoard;
        public CheckBox cbFavorite;


        public CardViewHolder(View itemView) {
            super(itemView);

            card_board = (CardView) itemView.findViewById(R.id.card_board);
            nameBoard = (TextView) itemView.findViewById(R.id.nameBoardTV);
            cbFavorite = (CheckBox) itemView.findViewById(R.id.cbFavorite);

        }
    }
}
