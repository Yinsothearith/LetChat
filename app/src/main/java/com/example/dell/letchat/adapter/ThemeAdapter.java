package com.example.dell.letchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.letchat.R;
import com.example.dell.letchat.model.ThemeClickListener;
import com.example.dell.letchat.model.ThemeModel;

import java.util.List;

public class ThemeAdapter extends BaseAdapter {

    public static ImageView imageOne;
    public static ImageView imageTwo;
    public static ImageView imageThree;

    private List<ThemeModel> themeModelList;
    private ThemeClickListener themeClickListener;
    private Context mContext;

    public ThemeAdapter(Context context, List<ThemeModel> themeModelList, ThemeClickListener clickListener) {
        this.themeModelList = themeModelList;
        this.themeClickListener = clickListener;
        this.mContext = context;
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

    @SuppressLint({"ResourceAsColor", "CutPasteId"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dark_vader_theme, parent, false);

        TextView txtThemeName = view.findViewById(R.id.dark_vader);
        TextView txtPrimary = view.findViewById(R.id.txt_primary);
        TextView txtSecondary = view.findViewById(R.id.txt_secondary);
        final ImageView ivCheck = view.findViewById(R.id.iv_check);

        ThemeModel themeModel = themeModelList.get(position);
        txtThemeName.setText(themeModel.getThemeName());
        txtPrimary.setText(themeModel.getPrimaryColor());
        txtSecondary.setText(themeModel.getSecondaryColor());
        ivCheck.setVisibility(View.INVISIBLE);

        if (position == 1){
            txtSecondary.setTextColor(mContext.getResources().getColor(R.color.colorPurple));
        } else if (position == 2){
            txtPrimary.setTextColor(mContext.getResources().getColor(R.color.colorYellow));
            txtSecondary.setTextColor(mContext.getResources().getColor(R.color.colorBlue));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        themeClickListener.onThemeItemClicked(position);
                        break;
                    case 1:
                        themeClickListener.onThemeItemClicked(position);
                        break;
                    default:
                        themeClickListener.onThemeItemClicked(position);
                        break;
                }
            }
        });
        return view;
    }
}