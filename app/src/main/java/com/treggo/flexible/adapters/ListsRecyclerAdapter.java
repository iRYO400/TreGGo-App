package com.treggo.flexible.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.treggo.flexible.R;
import com.treggo.flexible.activities.DescriptionActivity;
import com.treggo.flexible.adapters.helper.ItemTouchHelperAdapter;
import com.treggo.flexible.adapters.helper.ItemTouchHelperViewHolder;
import com.treggo.flexible.adapters.helper.OnStartDragListener;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.model.Board;

import java.util.Collections;

import io.realm.Realm;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class ListsRecyclerAdapter extends RealmRecyclerViewAdapter<Board>
        implements ItemTouchHelperAdapter
{

    private static final String TAG = "mLogs";
    private final OnStartDragListener mDragStartListener;


    private Context context;
    private Realm realm;
    private long boardID;
    private int boardPosition;
    private int listPosition;


    public ListsRecyclerAdapter(Context context, long boardID, int boardPosition, int listPosition,
                                OnStartDragListener dragListener) {

        this.context = context;
        this.boardID = boardID;
        this.boardPosition = boardPosition;
        this.listPosition = listPosition;
        this.mDragStartListener = dragListener;
        setHasStableIds(true);
    }

    @Override
    public CardListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        }

        cardListVH.card_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DescriptionActivity.class);
                intent.putExtra("c_bID", boardID);
                intent.putExtra("c_bPosition", boardPosition);
                intent.putExtra("c_lPosition", listPosition);
                intent.putExtra("c_cPosition", position);
                context.startActivity(intent);
            }
        });

//        cardListVH.card_list.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
//                    ClipData.Item item = new ClipData.Item(getItem(boardPosition).getMyLists().get(listPosition).getCards().get(position).getName());
//                    ClipData data = ClipData.newPlainText((CharSequence) v.getTag(), listPosition + "," + position );
//                    v.startDrag(data, shadowBuilder, v, 0);
//                    v.setVisibility(View.INVISIBLE);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        cardListVH.card_list.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              mDragStartListener.onStartDrag(cardListVH);
                Log.d(TAG, "Dragged");
                return false;
            }
        });
        cardListVH.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) ==
                        MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(cardListVH);
                }
                return false;
            }
        });
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        realm.beginTransaction();
        Collections.swap(getItem(boardPosition).getMyLists().get(listPosition).getCards(), fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        realm.commitTransaction();
        Log.d(TAG, "Dropped_____");
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
            try {
                return getRealmAdapter().getItem(boardPosition).getMyLists().get(listPosition).getCards().size();
            } catch (ArrayIndexOutOfBoundsException e) {
                Log.d(TAG, "Cyka o5 ETA OSHIBKA. B_Position " + boardPosition + " L_Position " + listPosition);
            }

        }
        return 0;
    }


    public class CardListViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder
    {

        private CardView card_list;
        private TextView card_list_name;
        private ImageView handleView;

        public CardListViewHolder(View itemView) {
            super(itemView);
            card_list = (CardView) itemView.findViewById(R.id.card_list);
            card_list_name = (TextView) itemView.findViewById(R.id.card_list_name);
            handleView = (ImageView) itemView.findViewById(R.id.handleView);
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
