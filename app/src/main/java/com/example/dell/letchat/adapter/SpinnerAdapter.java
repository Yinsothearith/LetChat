package com.example.dell.letchat.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.dell.letchat.model.ChannelModel;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    List<ChannelModel> channelModelList;

    public SpinnerAdapter(List<ChannelModel> channelModelList) {
        this.channelModelList = channelModelList;
    }

    @Override
    public int getCount() {
        return channelModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return channelModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
