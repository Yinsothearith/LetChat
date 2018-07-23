package com.example.dell.letchat.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.letchat.R;
import com.example.dell.letchat.model.ThemeClickListener;
import com.example.dell.letchat.model.ThemeModel;

import java.util.List;

public class ThemeListAdapter extends BaseAdapter {
    public static ImageView imageOne;
    public static ImageView imageTwo;
    public static ImageView imageThree;

    private List<ThemeModel>    themeModelList;
    private ThemeClickListener themeClickListener;

    public ThemeListAdapter(List<ThemeModel> themeModelList, ThemeClickListener clickListener) {
        this.themeModelList = themeModelList;
        this.themeClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return themeModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return themeModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return themeModelList.get(position).getItemType();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        switch (getItemViewType(position)){
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dark_vader_theme, parent, false);
                imageOne = view.findViewById(R.id.iv_check);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.white_purple_theme, parent, false);
                imageTwo = view.findViewById(R.id.iv_check);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.minion_theme, parent, false);
                imageThree = view.findViewById(R.id.iv_check);
                break;
        }

        TextView txtThemeName = view.findViewById(R.id.theme_name);
        TextView txtPrimary = view.findViewById(R.id.txt_primary);
        TextView txtSecondary = view.findViewById(R.id.txt_secondary);
        final ImageView ivCheck = view.findViewById(R.id.iv_check);

        ThemeModel themeModel = themeModelList.get(position);
        txtThemeName.setText(themeModel.getThemeName());
        txtPrimary.setText(themeModel.getPrimaryColor());
        txtSecondary.setText(themeModel.getSecondaryColor());
        ivCheck.setVisibility(View.INVISIBLE);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeClickListener.onThemeItemClicked(position);
            }
        });
        return view;
    }
}
