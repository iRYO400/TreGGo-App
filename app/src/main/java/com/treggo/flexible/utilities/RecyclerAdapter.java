package com.treggo.flexible.utilities;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.treggo.flexible.R;
import com.treggo.flexible.model.CardFace;

import java.util.ArrayList;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    protected static final int TYPE_HEADER = 0;
    protected static final int TYPE_CELL = 1;

    private Context context;
    private ArrayList<CardFace> faceCards;


    public RecyclerAdapter(Context context, ArrayList<CardFace> arrayList){

        this.context = context;
        this.faceCards = arrayList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private View label;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.cardNameTV);
            label =  itemView.findViewById(R.id.label1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position){
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
//            case TYPE_HEADER:
//                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hvp_header_placeholder, parent,false);
//                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_card, parent, false);
                break;
        }

        return new ViewHolder(view) {};
    }
    

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(faceCards.get(position).getName());
        if(faceCards.get(position).getColor() != -1){
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setBackgroundColor(Color.BLACK);

        }
    }


    @Override
    public int getItemCount() {
        return faceCards.size();
    }
}
