package com.treggo.flexible.card.label;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.treggo.flexible.R;
import com.treggo.flexible.board.Board;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public class LabelRecyclerViewAdapter extends RealmRecyclerViewLabelAdapter<Board> {

    private static final String TAG = "mLogs";
    EditLabelsFromAdapter editLabelsFromAdapter;

    private Context context;
    private Board board;
    private int lPosition;
    private int cPosition;

    public LabelRecyclerViewAdapter(Context context, Board board, int lPosition, int cPosition){
        this.context = context;
        this.board = board;
        this.lPosition = lPosition;
        this.cPosition = cPosition;
    }

    @Override
    public LabelRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_label, parent, false);
        return new LabelRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LabelRecyclerViewHolder holder, int position) {
        editLabelsFromAdapter = (EditLabelsFromAdapter) context;
        if(board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().isValid()) {
            if (board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().get(position).isChecked()) {
                holder.labelName.setMinWidth(context.getResources().getDimensionPixelOffset(R.dimen.minimum_width_free_label));
                holder.labelName.setText(board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().get(position).getLabelName());

                switch (board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().get(position).getColor()){
                        case 0:
                            holder.card_label.setBackgroundResource(R.drawable.label_background_blue);
                            break;
                        case 1:
                            holder.card_label.setBackgroundResource(R.drawable.label_background_green);
                            break;
                        case 2:
                            holder.card_label.setBackgroundResource(R.drawable.label_background_grey);
                            break;
                        case 3:
                            holder.card_label.setBackgroundResource(R.drawable.label_background_red);
                            break;
                        case 4:
                            holder.card_label.setBackgroundResource(R.drawable.label_background_yellow);
                            break;
                        case 5:
                            holder.card_label.setBackgroundResource(R.drawable.label_background_pink);
                            break;
                    }
            }
        }
        holder.card_label.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLabelsFromAdapter.editLabels();
            }
        });
    }

    @Override
    public int getItemCount() {
        return board.getMyLists().get(lPosition).getCards().get(cPosition).getLabelList().size();
    }
}
