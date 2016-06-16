package com.treggo.flexible.adapters;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.treggo.flexible.R;
import com.treggo.flexible.activities.DescriptionActivity;
import com.treggo.flexible.adapters.helper.ItemTouchHelperAdapter;
import com.treggo.flexible.adapters.helper.ItemTouchHelperViewHolder;
import com.treggo.flexible.adapters.helper.OnStartDragListener;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.Board;
import com.treggo.flexible.model.MyList;
import com.treggo.flexible.utilities.TinyDB;

import java.util.Collections;

import io.realm.Realm;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class ListsRecyclerAdapter extends RealmRecyclerViewAdapter<Board> implements ItemTouchHelperAdapter, View.OnDragListener {

    private static final String TAG = "mLogs";

    private final OnStartDragListener mDragStartListener;

    private Context context;
    private Realm realm;
    private long boardID;
    private int boardPosition;
    private int listPosition;


    public ListsRecyclerAdapter(Context context, long boardID, int boardPosition, int listPosition, OnStartDragListener dragListener) {
        setHasStableIds(true);
        this.context = context;
        this.boardID = boardID;
        this.boardPosition = boardPosition;
        this.listPosition = listPosition;
        this.mDragStartListener = dragListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        switch (viewType) {
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_in_list, parent, false);
                break;
        }
        return new CardListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        this.realm = RealmController.getInstance().getRealm();

        final CardListViewHolder cardListVH = (CardListViewHolder) holder;

        //Имя карточки в списке
        if (getItem(boardPosition).getMyLists().get(listPosition).getCards().isValid()) {
            cardListVH.card_list_name.setText(getItem(boardPosition).getMyLists().get(listPosition).getCards().get(position).getName());

            MyList m  = getItem(boardPosition).getMyLists().get(0);

        }

//        cardListVH.card_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, DescriptionActivity.class);
//                intent.putExtra("c_bID", boardID);
//                intent.putExtra("c_bPosition", boardPosition);
//                intent.putExtra("c_lPosition", listPosition);
//                intent.putExtra("c_cPosition", position);
//                context.startActivity(intent);
//            }
//        });

        cardListVH.card_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(ClipData.newPlainText("", ""), shadowBuilder, v, 0);
                    v.setVisibility(View.INVISIBLE);
                    return true;
                } else {
                    return false;
                }
            }
        });
//        cardListVH.card_list.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                mDragStartListener.onStartDrag(cardListVH);
//                return false;
//            }
//        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        realm.beginTransaction();
        Collections.swap(getItem(boardPosition).getMyLists().get(listPosition).getCards(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        realm.commitTransaction();
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public long getItemID(int position) {
        return super.getItemID(position);
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
//            return 3;
            try {
                return getRealmAdapter().getItem(boardPosition).getMyLists().get(listPosition).getCards().size();
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d(TAG, "Cyka o5 ETA OSHIBKA. B_Position " + boardPosition + " L_Position " + listPosition);
            }

        }
        return 0;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {

        return false;
    }


    public static class CardListViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        private CardView card_list;
        private TextView card_list_name;

        public CardListViewHolder(View itemView) {
            super(itemView);

            card_list = (CardView) itemView.findViewById(R.id.card_list);
            card_list_name = (TextView) itemView.findViewById(R.id.card_list_name);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.RED);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }
}
