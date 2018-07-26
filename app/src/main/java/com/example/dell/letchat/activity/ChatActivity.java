package com.example.dell.letchat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dell.letchat.R;
import com.example.dell.letchat.adapter.ChatAdapter;
import com.example.dell.letchat.model.AppConstant;
import com.example.dell.letchat.model.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatActivity extends AppCompatActivity {

    private TextView toolbarTitle, txtChannelName, txtOnlineStatus;
    private RelativeLayout mContainer;
    private LinearLayout mContainEmoji;
    private ImageView ivSetting, ivKeyboard, ivSend, ivLol, ivCry, ivLike, ivSurprised;
    private View toolbarUnderline, recyclerUnderline, chatUnderline;
    private EditText etMessage;
    private RecyclerView recyclerView;
    private String addNewMessageUrl = "https://fierce-wildwood-40527.herokuapp.com/chatroom";
    private String mUserId, mUserName, mChannelId;
    public static Socket socket;
    private int mOnlineCount, mBackground = R.color.dark_vader, mTextColor = R.color.colorWhite, mRoundView = R.drawable.round_white_button;
    private ChatAdapter mAdapter;
    private List<UserModel> userModelList = new ArrayList<>();
    private boolean keyboardEmoji = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        checkActivityColor();
        getChatInformation();
        setUpChatRecyclerView();
        getOldMessage();

        try {
            socket = IO.socket(addNewMessageUrl);
            socket.on(Socket.EVENT_CONNECT, onConnect());
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect());
            socket.on(Socket.EVENT_ERROR, onError());
            socket.on("addMessage", onAddMessageEvent());
            socket.on("count", onCountEvent());
            socket.connect();
            socket.emit("join", mUserId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSettingActivity();
            }
        });

        ivLol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable image = getResources().getDrawable(R.drawable.lol_26);
                sendEmojiMessage(image, "lol");
            }
        });

        ivCry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable image = getResources().getDrawable(R.drawable.crying_26_1);
                sendEmojiMessage(image, "cry");
            }
        });

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable image = getResources().getDrawable(R.drawable.facebook_like_32);
                sendEmojiMessage(image, "like");
            }
        });

        ivSurprised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable image = getResources().getDrawable(R.drawable.surprised_32);
                sendEmojiMessage(image, "surprise");
            }
        });

        ivKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keyboardEmoji){
                    ivKeyboard.setImageDrawable(getDrawable(R.drawable.lol_26));
                    mContainEmoji.setVisibility(View.VISIBLE);
                    keyboardEmoji = false;
                }
                else {
                    ivKeyboard.setImageDrawable(getDrawable(R.drawable.keyboard_24_2));
                    mContainEmoji.setVisibility(View.GONE);
                    keyboardEmoji = true;
                }

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem;
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                if (!isLoadingData && lastVisibleItem > 5 && !isDone) {
//                    setOffset(getItemCount());
//                    onRequestData(onDataResponse);
//                }
            }
        });

    }

    private Emitter.Listener onConnect() {
        Emitter.Listener onConnectListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("Connection: ", "Connected");
            }
        };
        return onConnectListener;
    }

    private Emitter.Listener onDisconnect() {
        Emitter.Listener onDisconnectListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("Connection: ", "Disconnected");
            }
        };
        return onDisconnectListener;
    }

    private Emitter.Listener onError() {
        Emitter.Listener onErrorListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.e("Error : ", args.toString());
            }
        };
        return onErrorListener;
    }

    private Emitter.Listener onAddMessageEvent() {
        Emitter.Listener onAddMessageEventListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String username;
                        String content;
                        String createdAt;
                        String types;
                        try {
                            username = data.getString("username");
                            content = data.getString("content");
                            createdAt = data.getString("createdAt");
                            types = data.getString("types");
//                            @SuppressLint("SimpleDateFormat")
//                            SimpleDateFormat formattedDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
////                            formattedDate.setTimeZone(TimeZone.getTimeZone("UTC+7"));
//                            String time = String.valueOf(formattedDate.parse(createdAt));
//                            Log.e("TimeStamp", time);
////                            Log.e("DateTime: ", dateFormat.toString());
//
//                            SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
//                            Log.e("TimeStamp", String.valueOf(formatTime.parse(time)));

                            if (username.compareTo(mUserName) == 0) {
                                Toast.makeText(ChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                            } else {
                                checkContentMessage(content, username, types);
                            }
                        } catch (Exception e) {
                            Log.e("Error", e.toString());
                            return;
                        }
                    }
                });
            }
        };
        return onAddMessageEventListener;
    }

    private void getEmojiMessage(String username, String types, Drawable emoji) {
        userModelList.add(new UserModel(username, types, emoji));
        mAdapter.addMessage(userModelList);
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    private Emitter.Listener onCountEvent() {
        Emitter.Listener onCountEventListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        mOnlineCount = (int) args[0];
                        txtOnlineStatus.setText("Online Status: " + String.valueOf(mOnlineCount));
                    }
                });
            }
        };
        return onCountEventListener;
    }

    private void getChatInformation() {
        String mChannelName = getIntent().getStringExtra("ChannelName");
        mChannelId = getIntent().getStringExtra("channelId");
        mUserId = getIntent().getStringExtra("_id");
        mUserName = getIntent().getStringExtra("userName");
        Log.e("UserName", mUserName);
        final String createdAt = getIntent().getStringExtra("createdAt");
        txtChannelName.setText(mChannelName);
    }

    private void getOldMessage() {
        String getMessageUrl = "https://fierce-wildwood-40527.herokuapp.com/api/v1/channels/" + mChannelId + "/messages?limit=5";
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getMessageUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject message = (JSONObject) data.get(i);
                        String content = message.getString("content");
                        String username = message.getString("username");
                        String types = message.getString("types");

                        checkContentMessage(content, username, types);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error ", error.toString());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }

    private void setUpChatRecyclerView() {
        mAdapter = new ChatAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.addMessage(userModelList);
        mAdapter.setColor(mBackground, mTextColor);

        Log.e("rvc"," "+recyclerView.computeVerticalScrollOffset());
    }

    private void sendMessage() {
        String content = etMessage.getText().toString();
        userModelList.add(new UserModel(content, mUserName, "userMessage"));
        mAdapter.addMessage(userModelList);
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        etMessage.setText(null);

        Log.e("UserID", mUserId);

        JSONObject json = new JSONObject();
        try {
            json.put("userId", mUserId);
            json.put("content", content);
            json.put("types", "message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("ddMessage", json.toString());

        socket.emit("newMessage", json);
    }

    private void sendEmojiMessage(Drawable imageId, String content) {
        userModelList.add(new UserModel(mUserName, "userEmoji", imageId));
        mAdapter.addMessage(userModelList);
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());

        JSONObject json = new JSONObject();
        try {
            json.put("userId", mUserId);
            json.put("content", content);
            json.put("types", "sticker");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("newMessage", json);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("left", mUserId);
        if (socket.connected()) {
            socket.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkActivityColor();
        mAdapter.setColor(mBackground, mTextColor);
        recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    private void checkContentMessage(String content, String username,  String types){
        switch (content) {
            case "lol":
                getEmojiMessage(username, types, getResources().getDrawable(R.drawable.lol_26));
                break;
            case "like":
                getEmojiMessage(username, types, getResources().getDrawable(R.drawable.facebook_like_32));
                break;
            case "cry":
                getEmojiMessage(username, types, getResources().getDrawable(R.drawable.crying_26_1));
                break;
            case "surprise":
                getEmojiMessage(username, types, getResources().getDrawable(R.drawable.surprised_32));
                break;
            default:
                userModelList.add(new UserModel(content, username, types));
                mAdapter.addMessage(userModelList);
                recyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                break;
        }
    }

    private void nextSettingActivity() {
        Intent intent = new Intent(ChatActivity.this, SettingActivity.class);
        intent.putExtra(AppConstant.USER_ID, mUserId);
        startActivity(intent);
    }

    private void checkActivityColor() {
        SharedPreferences preferences = getSharedPreferences(AppConstant.THEME_PREF, MODE_PRIVATE);
        int backgroundColor = preferences.getInt(AppConstant.BACKGROUND_KEY, 0);
        int textColor = preferences.getInt(AppConstant.TEXT_COLOR_KEY, 0);
        int roundView = preferences.getInt(AppConstant.ROUND_VIEW_KEY, 0);

        Log.e("Color", String.valueOf(backgroundColor));
        Log.e("Color", String.valueOf(textColor));
        Log.e("Color", String.valueOf(roundView));

        if (backgroundColor != 0 && textColor != 0) {
            mBackground = backgroundColor;
            mTextColor = textColor;
            mRoundView = roundView;
            setActivityColor(mBackground, mTextColor, mRoundView);
        }
    }

    private void setActivityColor(int background, int textColor, int roundView) {
        toolbarTitle.setTextColor(getResources().getColor(textColor));
        txtChannelName.setTextColor(getResources().getColor(textColor));
        txtOnlineStatus.setTextColor(getResources().getColor(textColor));
        mContainer.setBackground(getDrawable(background));
        ivSetting.setColorFilter(getResources().getColor(textColor));
        ivKeyboard.setColorFilter(getResources().getColor(textColor));
        ivSend.setColorFilter(getResources().getColor(textColor));
        ivLol.setColorFilter(getResources().getColor(textColor));
        ivCry.setColorFilter(getResources().getColor(textColor));
        ivLike.setColorFilter(getResources().getColor(textColor));
        ivSurprised.setColorFilter(getResources().getColor(textColor));
        toolbarUnderline.setBackgroundColor(getResources().getColor(textColor));
        recyclerUnderline.setBackgroundColor(getResources().getColor(textColor));
        chatUnderline.setBackgroundColor(getResources().getColor(textColor));
        recyclerView.setBackgroundColor(getResources().getColor(background));
        etMessage.setTextColor(getResources().getColor(background));
        etMessage.setBackground(getDrawable(roundView));
    }

    private void initView() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        txtChannelName = findViewById(R.id.txt_channel_name);
        txtOnlineStatus = findViewById(R.id.txt_online_status);
        mContainer = findViewById(R.id.container);
        mContainEmoji = findViewById(R.id.content_emoji);
        ivSetting = findViewById(R.id.iv_setting);
        ivKeyboard = findViewById(R.id.iv_keyboard);
        ivSend = findViewById(R.id.iv_send);
        ivLol = findViewById(R.id.iv_lol);
        ivCry = findViewById(R.id.iv_cry);
        ivLike = findViewById(R.id.iv_like);
        ivSurprised = findViewById(R.id.iv_surprised);
        toolbarUnderline = findViewById(R.id.toolbar_underline);
        recyclerUnderline = findViewById(R.id.recycler_underline);
        chatUnderline = findViewById(R.id.chat_underline);
        etMessage = findViewById(R.id.et_message);
        recyclerView = findViewById(R.id.rv_chat);
    }
}
