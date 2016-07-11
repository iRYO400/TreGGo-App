package com.treggo.flexible.board;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.treggo.flexible.R;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.lists.MiddleActivity;
import com.treggo.flexible.utilities.TinyDB;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by iRYO400 on 09.06.2016.
 */
public class BoardsRecyclerAdapter extends RealmRecyclerViewBoardAdapter<Board> {

    final Context context;
    private Realm realm;
    private TinyDB tinyDB;
    private RealmResults<Board> results;

    public BoardsRecyclerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public BoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_board, parent, false);
        return new BoardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardViewHolder holder, final int position) {
        tinyDB = new TinyDB(context);
        realm = RealmController.getInstance().getRealm();
        results = realm.where(Board.class).findAll();
        final Board board = results.get(position);

        final BoardViewHolder boardViewHolder = (BoardViewHolder) holder;
        //Check for Validation,
        //if yes,
        //set Name and Checkbox
        if (getItem(position).isValid()) {
            boardViewHolder.nameBoard.setText(board.getName());
            boardViewHolder.cbFavorite.setChecked(board.isFavorite());
        }

        //is Board Favorite?
        boardViewHolder.cbFavorite.setTag(getItem(position));
        boardViewHolder.cbFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CheckBox checkBox = (CheckBox) buttonView;
                Board b = (Board) checkBox.getTag();
                realm.beginTransaction();
                b.setFavorite(checkBox.isChecked());
                realm.copyToRealmOrUpdate(b);
                realm.commitTransaction();
            }
        });

        //Override LONG click
        boardViewHolder.card_board.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                final View subView = inflater.inflate(R.layout.dialog_board_settings, null);
                Button btnFavorite = (Button) subView.findViewById(R.id.btn_board_favorite);
                if(!board.isFavorite()){
                    btnFavorite.setText(R.string.board_favorite);
                }else {
                    btnFavorite.setText(R.string.board_unfavorite);
                }
                Button btnDelete = (Button) subView.findViewById(R.id.btn_board_delete);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setView(subView);
                final AlertDialog show = builder.show();
                btnFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        realm.beginTransaction();
                        if(!board.isFavorite()){
                            board.setFavorite(true);
                        }else {
                            board.setFavorite(false);
                        }
                        realm.copyToRealmOrUpdate(board);
                        realm.commitTransaction();
                        notifyDataSetChanged();
                        show.dismiss();
                    }
                });

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        realm.beginTransaction();
                        results.deleteFromRealm(boardViewHolder.getAdapterPosition());
                        realm.commitTransaction();
                        notifyDataSetChanged();
                        show.dismiss();
                    }
                });

                return false;
            }
        });

        //Override SHORT CLICK
        boardViewHolder.card_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MiddleActivity.class);
                //Передаем индекс доски и ID

                tinyDB.putInt("boardPosition", boardViewHolder.getAdapterPosition());
                Board b = results.get(boardViewHolder.getAdapterPosition());
                long id = b.getId();
                intent.putExtra("BoardID", id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            return getRealmAdapter().getCount();
        }
        return 0;
    }

    public static class BoardViewHolder extends RecyclerView.ViewHolder {

        public CardView card_board;
        public TextView nameBoard;
        public CheckBox cbFavorite;

        public BoardViewHolder(View itemView) {
            super(itemView);

            card_board = (CardView) itemView.findViewById(R.id.card_board);
            nameBoard = (TextView) itemView.findViewById(R.id.nameBoardTV);
            cbFavorite = (CheckBox) itemView.findViewById(R.id.cbFavorite);

        }
    }
}
