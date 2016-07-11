package com.treggo.flexible.card.checkList;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.treggo.flexible.R;

/**
 * Created by iRYO400 on 24.06.2016.
 */
public class CheckLRecyclerViewHolder extends RecyclerView.ViewHolder {

    public CheckLRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public static class VHParent extends CheckLRecyclerViewHolder {

        public CardView cardView;
        public EditText cardEditCL;
        public Button cardBtnAdd;

        public VHParent(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_checkListP);
            cardEditCL = (EditText) itemView.findViewById(R.id.card_checkL_et);
//            cardBtnAdd = (Button) itemView.findViewById(R.id.card_checkL_btn_add);
        }
    }

    public static class VHChild extends CheckLRecyclerViewHolder{

        public CardView cardView;
        public TextView card_title;
        public CheckBox card_cb;

        public VHChild(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_checkListC);
            card_title = (TextView) itemView.findViewById(R.id.card_checkL_nameC);
            card_cb = (CheckBox) itemView.findViewById(R.id.card_checkL_cbC);
        }
    }
}


