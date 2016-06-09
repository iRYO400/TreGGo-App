package com.treggo.flexible.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.treggo.flexible.R;
import com.treggo.flexible.model.Board;

import java.util.ArrayList;

/**
 * Created by iRYO400 on 07.06.2016.
 */
public class MyListViewAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Board> arrayList;

    public MyListViewAdapter(Context context, ArrayList<Board> boards){
        this.context = context;
        this.arrayList = boards;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view ==null){
            view = layoutInflater.inflate(R.layout.item, parent, false);
        }

        Board board = (Board) getItem(position);

        ((TextView) view.findViewById(R.id.tvName)).setText(board.getName());

        return view;
    }

    public Board getBoard(int position){
        return (Board) getItem(position);
    }

}
