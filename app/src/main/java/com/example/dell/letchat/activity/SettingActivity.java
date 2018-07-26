package com.example.dell.letchat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.letchat.R;
import com.example.dell.letchat.model.AppConstant;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout mContainer;
    private TextView txtToolbarTitle, txtClose, txtTheme, txtBeep, txtThemeName, txtLeave;
    private ImageView ivForward, ivBeepCheck;
    private View toolbarUnderline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextThemeActivity();
            }
        });

        txtLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ChatActivity.socket.emit("left", "abc");
                    startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkActivityColor();
    }

    private void nextThemeActivity() {
        startActivity(new Intent(SettingActivity.this, ThemeActivity.class));
    }

    private void checkActivityColor(){
        SharedPreferences preferences = getSharedPreferences(AppConstant.THEME_PREF, MODE_PRIVATE);
        int mBackgroundColor = preferences.getInt(AppConstant.BACKGROUND_KEY, 0);
        int mTextColor = preferences.getInt(AppConstant.TEXT_COLOR_KEY, 0);

        if (mBackgroundColor != 0 && mTextColor != 0) {
            setActivityColor(mBackgroundColor, mTextColor);
        }
    }

    private void setActivityColor(int background, int textColor) {
        mContainer.setBackground(getDrawable(background));
        txtToolbarTitle.setTextColor(getResources().getColor(textColor));
        txtClose.setTextColor(getResources().getColor(textColor));
        txtTheme.setTextColor(getResources().getColor(textColor));
        txtBeep.setTextColor(getResources().getColor(textColor));
        txtLeave.setTextColor(getResources().getColor(textColor));
        txtThemeName.setTextColor(getResources().getColor(textColor));
        ivForward.setColorFilter(getResources().getColor(textColor));
        ivBeepCheck.setColorFilter(getResources().getColor(textColor));
        toolbarUnderline.setBackground(getDrawable(textColor));
    }

    private void initView() {
        txtLeave = findViewById(R.id.txt_leave);
        mContainer = findViewById(R.id.container);
        txtToolbarTitle = findViewById(R.id.toolbar_title);
        txtClose = findViewById(R.id.txt_close);
        txtTheme = findViewById(R.id.txt_theme);
        txtThemeName = findViewById(R.id.txt_theme_name);
        txtBeep = findViewById(R.id.txt_beep);
        ivForward = findViewById(R.id.iv_theme_forward);
        ivBeepCheck = findViewById(R.id.iv_beep_check);
        toolbarUnderline = findViewById(R.id.toolbar_underline);
    }
}
