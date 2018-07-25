package com.example.dell.letchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.letchat.R;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout mContainer;
    private TextView txtToolbarTitle, txtClose, txtTheme, txtBeep, txtThemeName;
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
                setActivityColor(R.color.colorWhite, R.color.colorPurple);
            }
        });
    }

    private void setActivityColor(int background, int textColor) {
        mContainer.setBackground(getDrawable(background));
        txtToolbarTitle.setTextColor(getResources().getColor(textColor));
        txtClose.setTextColor(getResources().getColor(textColor));
        txtTheme.setTextColor(getResources().getColor(textColor));
        txtBeep.setTextColor(getResources().getColor(textColor));
        txtThemeName.setTextColor(getResources().getColor(textColor));
        ivForward.setColorFilter(getResources().getColor(textColor));
        ivBeepCheck.setColorFilter(getResources().getColor(textColor));
        toolbarUnderline.setBackground(getDrawable(textColor));
    }

    private void initView() {
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
