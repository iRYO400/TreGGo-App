package com.treggo.flexible.card.checkList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.treggo.flexible.R;
import com.treggo.flexible.app.RealmController;
import com.treggo.flexible.board.Board;
import com.treggo.flexible.card.attachment.AttachedRecyclerViewHolder;

import io.realm.Realm;

/**
 * Created by iRYO400 on 24.06.2016.
 */
public class CheckListRecyclerViewAdapter extends RealmRecyclerViewCheckLAdapter<Board> {

    public static final int PARENT = 0;
    public static final int CHILD = 1;
    public static final String TAG = "mLogs";

    private Realm realm;
    private Context context;
    private Board board;
    private int lPosition;
    private int cPosition;

    public CheckListRecyclerViewAdapter(Context context, Board board, int lPosition, int cPosition) {

        this.context = context;
        this.board = board;
        this.lPosition = lPosition;
        this.cPosition = cPosition;
    }

    @Override
    public int getItemViewType(int position) {
        if (!board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().isEmpty()) {
            switch (board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().get(position).getViewType()) {
                case 0:
                    return PARENT;
                default:
                    return CHILD;
            }
        }
        return 0;
    }


    @Override
    public CheckLRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case PARENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_checkl_parent, parent, false);
                return new CheckLRecyclerViewHolder.VHParent(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_checkl_child, parent, false);
                return new CheckLRecyclerViewHolder.VHChild(view);
        }
    }


    @Override
    public void onBindViewHolder(CheckLRecyclerViewHolder holder, int position) {
        this.realm = RealmController.getInstance().getRealm();
        if(board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().isValid()
                && !board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().isEmpty()){
            switch (holder.getItemViewType()){
                case PARENT:
                    final CheckLRecyclerViewHolder.VHParent parent = (CheckLRecyclerViewHolder.VHParent) holder;
//                    parent.cardBtnAdd.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if(!parent.cardEditCL.getText().toString().equals("")){
//                                CheckList checkList = new CheckList();
//                                checkList.setName(parent.cardEditCL.getText().toString());
//                                checkList.setViewType(CHILD);
//                                realm.beginTransaction();
//                                board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().add(checkList);
//                                realm.commitTransaction();
//                                notifyDataSetChanged();
//                            }
//                        }
//                    });

                    parent.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(context, "LONG CLICK CHECKLIST EDIT TEXT", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });

                    break;
                default:

                    final CheckLRecyclerViewHolder.VHChild child = (CheckLRecyclerViewHolder.VHChild) holder;
                    child.card_title.setText(board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().get(position).getName());
                    child.card_cb.setChecked(board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().get(position).isChecked());
                    child.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            Toast.makeText(context, "LONG CLICK CHECKLIST ITEM ", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                    break;
            }
        }
    }


    @Override
    public int getItemCount() {
        return board.getMyLists().get(lPosition).getCards().get(cPosition).getCheckLists().size();
    }
}
