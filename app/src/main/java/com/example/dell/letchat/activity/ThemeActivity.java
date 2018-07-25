package com.example.dell.letchat.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.letchat.R;
import com.example.dell.letchat.adapter.ThemeAdapter;
import com.example.dell.letchat.model.AppConstant;
import com.example.dell.letchat.model.ThemeChangeListener;
import com.example.dell.letchat.model.ThemeClickListener;
import com.example.dell.letchat.model.ThemeModel;

import java.util.ArrayList;
import java.util.List;

public class ThemeActivity extends AppCompatActivity implements ThemeClickListener {

    private ListView mThemeListView;
    private ThemeAdapter adapter;
    private List<ThemeModel> themeModelList = new ArrayList<>();
    private RelativeLayout container;
    private ImageView ivBack;
    private TextView txtToolBarTitle, txtSave;
    private View view;
    private int mBackgroundColor = R.color.dark_vader;
    private int mTextColor = R.color.colorWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        initView();

        themeModelList.add(new ThemeModel("Dark Vader", "#000000", "#FFFFFF", 1));
        themeModelList.add(new ThemeModel("JUST WHITE AND PURPLE", "#000000", "#443266", 2));
        themeModelList.add(new ThemeModel("Minion", "#F5E050", "#1E6A9F", 3));

        adapter = new ThemeAdapter(this, themeModelList, this);
        mThemeListView.setAdapter(adapter);

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences(AppConstant.USER_PREF, MODE_PRIVATE).edit();
                editor.putInt(AppConstant.BACKGROUND_KEY, mBackgroundColor);
                editor.putInt(AppConstant.TEXT_COLOR_KEY, mTextColor);
                editor.apply();
            }
        });

    }

    private void initView() {
        mThemeListView = findViewById(R.id.list_theme_item);
        container = findViewById(R.id.container);
        txtToolBarTitle = findViewById(R.id.toolbar_title);
        txtSave = findViewById(R.id.txtSave);
        view = findViewById(R.id.toolbar_underline);
        ivBack = findViewById(R.id.iv_back);
    }

    @Override
    public void onThemeItemClicked(int position) {
        switch (position) {
            case 0:
                setColor(mBackgroundColor, mTextColor);
                break;
            case 1:
                mBackgroundColor = R.color.colorWhite;
                mTextColor = R.color.colorPurple;
                setColor(mBackgroundColor, mTextColor);
                break;
            default:
                mBackgroundColor = R.color.colorYellow;
                mTextColor = R.color.colorBlue;
                setColor(mBackgroundColor, mTextColor);
                break;
        }
    }

    private void setColor(int background, int textColor){
        mThemeListView.setDivider(getDrawable(background));
        mThemeListView.setDividerHeight(130);
        container.setBackground(getDrawable(background));
        txtToolBarTitle.setTextColor(getResources().getColor(textColor));
        txtSave.setTextColor(getResources().getColor(textColor));
        view.setBackground(getDrawable(textColor));
        ivBack.setColorFilter(getResources().getColor(textColor));
    }
}
