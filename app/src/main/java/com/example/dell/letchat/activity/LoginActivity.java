package com.example.dell.letchat.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private String mUrlGet = "https://fierce-wildwood-40527.herokuapp.com/api/v1/channels";
    private String mUrlPost = "https://fierce-wildwood-40527.herokuapp.com/api/v1/users";
    private List<ChannelModel> channelModelList = new ArrayList<>();
    private RelativeLayout mContainer;
    private EditText etUserName;
    private Button btnJoin;
    private TextView txtAppName, txtName, txtChannel, txtChannelName;
    private ImageView ivUser, ivOmnichannel;
    private View channelUnderline, userNameUnderline;
    private Spinner spListChannel;
    private int mBackgroundColor, mTextColor, mOnlineCount;
    private String mChannelId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        checkActivityColor();
        jsonObjectRequest();

        spListChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                txtChannelName.setText(String.valueOf(parent.getSelectedItem()));
                ChannelModel model = channelModelList.get(position);
                mChannelId = model.getId();
                mOnlineCount = model.getCount() + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                txtChannelName.setText("Choose Channel");
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postUser();
            }
        });
    }

    private void checkActivityColor() {
        SharedPreferences preferences = getSharedPreferences(AppConstant.USER_PREF, MODE_PRIVATE);
        mBackgroundColor = preferences.getInt(AppConstant.BACKGROUND_KEY, 0);
        mTextColor = preferences.getInt(AppConstant.TEXT_COLOR_KEY, 0);

        if (mBackgroundColor == 0 && mTextColor == 0) {

        } else
            setActivityColor(mBackgroundColor, mTextColor);
    }

    private void jsonObjectRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mUrlGet, null, new Response.Listener<JSONObject>() {
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
                Log.d("Volley Error: ", error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void postUser() {

        StringRequest postRequest = new StringRequest(Request.Method.POST, mUrlPost,
                new Response.Listener<String>()
                {
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
                        intent.putExtra("Online", mOnlineCount);
                        intent.putExtra("ChannelName", txtChannelName.getText().toString());
                        intent.putExtra("_id", id);
                        intent.putExtra("createdAt", createdAt);
                        intent.putExtra("userName", etUserName.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", error.toString());
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("username", etUserName.getText().toString());
                params.put("channelId", mChannelId);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }

    private void setActivityColor(int background, int textColor) {
        mContainer.setBackground(getDrawable(background));
        etUserName.setBackground(getDrawable(background));
        etUserName.setTextColor(getResources().getColor(textColor));
        txtChannelName.setTextColor(getResources().getColor(textColor));
        btnJoin.setTextColor(getResources().getColor(background));
        btnJoin.setBackground(getDrawable(textColor));
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
        txtChannelName = findViewById(R.id.txt_channel_name);
        btnJoin = findViewById(R.id.btn_join);
        spListChannel = findViewById(R.id.sp_list_channel);
    }
}
