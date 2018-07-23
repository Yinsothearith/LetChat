package com.example.dell.letchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dell.letchat.adapter.ThemeListAdapter;
import com.example.dell.letchat.model.ThemeClickListener;
import com.example.dell.letchat.model.ThemeModel;

import java.util.ArrayList;
import java.util.List;

public class ThemeActivity extends AppCompatActivity implements ThemeClickListener{

    ListView mThemeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        initView();

        List<ThemeModel> themeModelList = new ArrayList<>();
        themeModelList.add(new ThemeModel("Dark Vader", "#000000", "#FFFFFF", 1));
        themeModelList.add(new ThemeModel("JUST WHITE AND PURPLE", "#000000", "#443266", 2));
        themeModelList.add(new ThemeModel("Minion", "#F5E050", "#1E6A9F", 3));

        ThemeListAdapter adapter = new ThemeListAdapter(themeModelList, this);
        mThemeListView.setAdapter(adapter);

    }

    @Override
    public void onThemeItemClicked(int position) {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
                ThemeListAdapter.imageOne.setVisibility(View.VISIBLE);
                ThemeListAdapter.imageTwo.setVisibility(View.INVISIBLE);
                ThemeListAdapter.imageThree.setVisibility(View.INVISIBLE);
                break;
            case 1:
                ThemeListAdapter.imageOne.setVisibility(View.INVISIBLE);
                ThemeListAdapter.imageTwo.setVisibility(View.VISIBLE);
                ThemeListAdapter.imageThree.setVisibility(View.INVISIBLE);
                break;
            case 2:
                ThemeListAdapter.imageOne.setVisibility(View.INVISIBLE);
                ThemeListAdapter.imageTwo.setVisibility(View.INVISIBLE);
                ThemeListAdapter.imageThree.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initView() {
        mThemeListView = findViewById(R.id.list_theme_item);
    }
}
