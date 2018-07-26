package com.example.dell.letchat.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.letchat.R;
import com.example.dell.letchat.adapter.ThemeAdapter;
import com.example.dell.letchat.model.AppConstant;
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
    private String colorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        initView();
        checkActivityColor();
        setStaticThemeList();

        txtSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTheme();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setStaticThemeList() {
        themeModelList.add(new ThemeModel("Dark Vader", "#000000", "#FFFFFF", 1));
        themeModelList.add(new ThemeModel("JUST WHITE AND PURPLE", "#000000", "#443266", 2));
        themeModelList.add(new ThemeModel("Minion", "#F5E050", "#1E6A9F", 3));
        adapter = new ThemeAdapter(this, themeModelList, this, mBackgroundColor, mTextColor);
        mThemeListView.setAdapter(adapter);
    }

    private void checkActivityColor() {
        SharedPreferences preferences = getSharedPreferences(AppConstant.THEME_PREF, MODE_PRIVATE);
        int background = preferences.getInt(AppConstant.BACKGROUND_KEY, 0);
        int textColor = preferences.getInt(AppConstant.TEXT_COLOR_KEY, 0);

        if (background != 0 && textColor != 0) {
            mBackgroundColor = background;
            mTextColor = textColor;
            setActivityColor(mBackgroundColor, mTextColor);
        }
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
                mBackgroundColor = R.color.dark_vader;
                mTextColor = R.color.colorWhite;
                colorType = "dark";
                adapter.setListColor(mBackgroundColor, mTextColor);
                setActivityColor(mBackgroundColor, mTextColor);
                break;
            case 1:
                mBackgroundColor = R.color.colorWhite;
                mTextColor = R.color.colorPurple;
                colorType = "white";
                adapter.setListColor(mBackgroundColor, mTextColor);
                setActivityColor(mBackgroundColor, mTextColor);
                break;
            default:
                mBackgroundColor = R.color.colorYellow;
                mTextColor = R.color.colorBlue;
                colorType = "yellow";
                adapter.setListColor(mBackgroundColor, mTextColor);
                setActivityColor(mBackgroundColor, mTextColor);
                break;
        }
    }

    private void saveTheme() {
        SharedPreferences.Editor editor = getSharedPreferences(AppConstant.THEME_PREF, MODE_PRIVATE).edit();
        editor.putInt(AppConstant.BACKGROUND_KEY, mBackgroundColor);
        editor.putInt(AppConstant.TEXT_COLOR_KEY, mTextColor);
        if (colorType.compareTo("dark") ==0){
            editor.putInt(AppConstant.ROUND_VIEW_KEY, R.drawable.round_white_button);
        } else if (colorType.compareTo("white") == 0){
            editor.putInt(AppConstant.ROUND_VIEW_KEY, R.drawable.round_purple_button);
        } else {
            editor.putInt(AppConstant.ROUND_VIEW_KEY, R.drawable.round_blue_button);
        }
        editor.apply();
        Toast.makeText(this, "Theme has been saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setActivityColor(int background, int textColor){
        mThemeListView.setDivider(getDrawable(background));
        mThemeListView.setDividerHeight(130);
        container.setBackground(getDrawable(background));
        txtToolBarTitle.setTextColor(getResources().getColor(textColor));
        txtSave.setTextColor(getResources().getColor(textColor));
        view.setBackground(getDrawable(textColor));
        ivBack.setColorFilter(getResources().getColor(textColor));
    }
}
