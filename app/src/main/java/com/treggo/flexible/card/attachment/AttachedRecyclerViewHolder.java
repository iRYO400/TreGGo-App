package com.treggo.flexible.card.attachment;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.treggo.flexible.R;

/**
 * Created by iRYO400 on 23.06.2016.
 */
public class AttachedRecyclerViewHolder extends RecyclerView.ViewHolder{

    public AttachedRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public static class AttachedRVPics extends AttachedRecyclerViewHolder {

        public CardView card_pic;
        public ImageView card_pic_img;

        public AttachedRVPics(View itemView) {
            super(itemView);
            card_pic = (CardView) itemView.findViewById(R.id.card_attach_pic);
            card_pic_img = (ImageView) itemView.findViewById(R.id.card_attach_img);
        }
    }

    public static class AttachedRVDocs extends AttachedRecyclerViewHolder {

        public CardView card_doc;
        public TextView card_doc_name;
        public TextView card_doc_info;

        public AttachedRVDocs(View itemView) {
            super(itemView);
            card_doc = (CardView) itemView.findViewById(R.id.card_attach_doc);
            card_doc_name = (TextView) itemView.findViewById(R.id.card_attach_doc_name);
            card_doc_info = (TextView) itemView.findViewById(R.id.card_attach_doc_info);
        }
    }
}
