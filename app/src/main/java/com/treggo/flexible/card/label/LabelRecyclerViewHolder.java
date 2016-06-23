package com.treggo.flexible.card.label;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.treggo.flexible.R;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public class LabelRecyclerViewHolder extends RecyclerView.ViewHolder {

    public CardView card_label;
    public TextView labelName;
    public LabelRecyclerViewHolder(View itemView) {
        super(itemView);

        card_label = (CardView) itemView.findViewById(R.id.card_label);
        labelName = (TextView) itemView.findViewById(R.id.labelName);

    }
}
