package com.treggo.flexible.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.treggo.flexible.R;
import com.treggo.flexible.card.models.CheckList;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by iRYO400 on 15.06.2016.
 */
public class CheckListAdapter extends RealmBaseAdapter<CheckList> implements ListAdapter {

    private static class ViewHolder{
        TextView tvName;
        CheckBox chDone;
    }

    public CheckListAdapter(Context context, OrderedRealmCollection<CheckList> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.lv_textV);
            viewHolder.chDone = (CheckBox) convertView.findViewById(R.id.lv_checkB);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CheckList item = adapterData.get(position);
        viewHolder.tvName.setText(item.getName());
        viewHolder.chDone.setChecked(item.isDone());

        return convertView;
    }
}
