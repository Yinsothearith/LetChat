package com.example.dell.letchat.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.dell.letchat.R;
import com.example.dell.letchat.model.AppConstant;
import com.example.dell.letchat.model.ChannelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private List<ChannelModel> channelModelList = new ArrayList<>();
    private RelativeLayout mContainer;
    private EditText etUserName;
    private Button btnJoin;
    private TextView txtAppName, txtName, txtChannel;
    private ImageView ivUser, ivOmnichannel;
    private View channelUnderline, userNameUnderline;
    private Spinner spListChannel;
    private String mChannelId, mChannelName;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        checkActivityColor();
        jsonObjectRequest();

        if (!isNetworkAvailable()){
            Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT).show();
        }

        spListChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChannelModel model = channelModelList.get(position);
                mChannelId = model.getId();
                mChannelName = model.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spListChannel.setPrompt("Choose Channel");
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()){
                    postUser();
                } else
                Toast.makeText(LoginActivity.this, "No internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

        etUserName.setText("Lollipop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkActivityColor();
    }

    private void jsonObjectRequest() {
        String mUrlGetChannel = "https://fierce-wildwood-40527.herokuapp.com/api/v1/channels";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlGetChannel, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject channel = (JSONObject) data.get(i);
                        String name = channel.getString("name");
                        int count = channel.getInt("count");
                        String id = channel.getString("_id");

                        channelModelList.add(new ChannelModel(name, count, id));
                        Log.e("Channel : ", name + "   " + count + "   " + id);
                    }

                    List<String> spinnerList = new ArrayList<>();
                    for (ChannelModel model : channelModelList) {
                        spinnerList.add(model.getName() + " (" + model.getCount() + ")");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    spListChannel.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    Log.d("Volley Error: ", error.getMessage());
                } catch (Exception e){
                    Toast.makeText(LoginActivity.this, "Server Down", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void postUser() {
        String mUrlPostUser = "https://fierce-wildwood-40527.herokuapp.com/api/v1/users";
        StringRequest postRequest = new StringRequest(Request.Method.POST, mUrlPostUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        String id = null;
                        String createdAt = null;
                        try {
                            JSONObject object = new JSONObject(response);
                            id = object.getString("_id");
                            createdAt = object.getString("createdAt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LoginActivity.this, ChatActivity.class);
                        intent.putExtra("channelId", mChannelId);
                        intent.putExtra("ChannelName", mChannelName);
                        intent.putExtra("_id", id);
                        intent.putExtra("createdAt", createdAt);
                        intent.putExtra("userName", etUserName.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", error.toString());
                        String username = etUserName.getText().toString();
                        if (TextUtils.isEmpty(username)) {
                            Toast.makeText(LoginActivity.this, "Please Input UserName", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(LoginActivity.this, "UserName has already used", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", etUserName.getText().toString());
                params.put("channelId", mChannelId);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkActivityColor() {
        SharedPreferences preferences = getSharedPreferences(AppConstant.THEME_PREF, MODE_PRIVATE);
        int mBackgroundColor = preferences.getInt(AppConstant.BACKGROUND_KEY, 0);
        int mTextColor = preferences.getInt(AppConstant.TEXT_COLOR_KEY, 0);
        int roundView = preferences.getInt(AppConstant.ROUND_VIEW_KEY, 0);

        if (mBackgroundColor != 0 && mTextColor != 0) {
            setActivityColor(mBackgroundColor, mTextColor, roundView);
        }
    }

    private void setActivityColor(int background, int textColor, int roundView) {
        mContainer.setBackground(getDrawable(background));
        etUserName.setBackground(getDrawable(background));
        etUserName.setTextColor(getResources().getColor(textColor));
        btnJoin.setTextColor(getResources().getColor(background));
        btnJoin.setBackground(getDrawable(roundView));
        txtAppName.setTextColor(getResources().getColor(textColor));
        txtName.setTextColor(getResources().getColor(textColor));
        txtChannel.setTextColor(getResources().getColor(textColor));
        ivUser.setColorFilter(getResources().getColor(textColor));
        ivOmnichannel.setColorFilter(getResources().getColor(textColor));
        channelUnderline.setBackground(getDrawable(textColor));
        userNameUnderline.setBackground(getDrawable(textColor));
    }

    private void initView() {
        mContainer = findViewById(R.id.container);
        channelUnderline = findViewById(R.id.channel_underline);
        userNameUnderline = findViewById(R.id.username_underline);
        ivOmnichannel = findViewById(R.id.iv_omnichannel);
        ivUser = findViewById(R.id.iv_user);
        txtChannel = findViewById(R.id.txt_channel);
        txtName = findViewById(R.id.txt_name);
        txtAppName = findViewById(R.id.app_name);
        etUserName = findViewById(R.id.et_name);
        btnJoin = findViewById(R.id.btn_join);
        spListChannel = findViewById(R.id.sp_list_channel);
    }
}
