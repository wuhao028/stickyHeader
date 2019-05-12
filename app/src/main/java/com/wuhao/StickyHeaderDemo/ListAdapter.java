package com.wuhao.StickyHeaderDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wuhao.StickyHeaderDemo.model.ItemModel;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ItemModel> {

    private List<ItemModel> rewardList;
    private Context context;

    public ListAdapter(Context context, List<ItemModel> rewards) {
        super(context, R.layout.countdown_item, rewards);
        rewardList = rewards;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.countdown_item, null);
            viewHolder = new ViewHolder();
            viewHolder.points = convertView.findViewById(R.id.countdown_item_reward_points);
            viewHolder.timestamp = convertView.findViewById(R.id.countdown_item_reward_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.timestamp.setText(rewardList.get(position).timestamp);
        viewHolder.points.setText(rewardList.get(position).points);
        return convertView;
    }

    public static class ViewHolder {
        TextView timestamp;
        TextView points;
    }
}