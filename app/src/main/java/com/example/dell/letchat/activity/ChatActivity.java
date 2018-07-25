package com.example.dell.letchat.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.letchat.R;
import com.example.dell.letchat.adapter.ChatAdapter;
import com.example.dell.letchat.model.AppConstant;
import com.example.dell.letchat.model.ChatModel;

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
    private ImageView ivSetting, ivEmoji, ivSend, ivLol, ivCry, ivLike, ivSurprised;
    private View toolbarUnderline, recyclerUnderline;
    private EditText etMessage;
    private RecyclerView recyclerView;
    private String url = "https://fierce-wildwood-40527.herokuapp.com/chatroom";
    private String userId, userName;
    private Socket socket;
    private List<ChatModel> chatModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();

        int onlineCount = getIntent().getIntExtra("Online", 0);
        String channelName = getIntent().getStringExtra("ChannelName");
        userId = getIntent().getStringExtra("_id");
        userName = getIntent().getStringExtra("userName");
        final String createdAt = getIntent().getStringExtra("createdAt");
        txtChannelName.setText(channelName);
        txtOnlineStatus.setText("Online Now: " + onlineCount);


        final ChatAdapter adapter = new ChatAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.addMessage(chatModelList);

        try {
            socket = IO.socket(url);
            socket.on(Socket.EVENT_CONNECT, onConnect());
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect());
            socket.on(Socket.EVENT_ERROR, onError());
            socket.on("addMessage", onAddMessageEvent());
            socket.on("count", onCountEvent());
            socket.connect();
            socket.emit("join", userId);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etMessage.getText().toString();
                chatModelList.add(new ChatModel(content, userName, userId, "message", "123"));
                adapter.addMessage(chatModelList);
                etMessage.setText(null);

                JSONObject json = new JSONObject();
                try {
                    json.put("userId", userId);
                    json.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("ddMessage", json.toString());

                socket.emit("newMessage", json);
            }
        });

    }

    private Emitter.Listener onConnect() {
        Emitter.Listener onConnectListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        };
        return onConnectListener;
    }

    private Emitter.Listener onDisconnect() {
        Emitter.Listener onDisconnectListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        };
        return onDisconnectListener;
    }

    private Emitter.Listener onError(){
        Emitter.Listener onErrorListener = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.e("Error : " ,args.toString());
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
                        String message;
                        try {
                            username = data.getString("username");
                            message = data.getString("content");
                        } catch (JSONException e) {
                            return;
                        }

                        // add the message to view
                        Log.e("addmessage", username);
                        Log.e("message", message);
                    }
                });
            }
        };
        return onAddMessageEventListener;
    }

    private Emitter.Listener onCountEvent() {
        Emitter.Listener onCountEventListener = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        };
        return onCountEventListener;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("left", userId);
        if (socket.connected()) {
            socket.disconnect();
        }
    }

    private void initView() {
        toolbarTitle = findViewById(R.id.toolbar_title);
        txtChannelName = findViewById(R.id.txt_channel_name);
        txtOnlineStatus = findViewById(R.id.txt_online_status);
        mContainer = findViewById(R.id.container);
        ivSetting = findViewById(R.id.iv_setting);
        ivEmoji = findViewById(R.id.iv_emoji);
        ivSend = findViewById(R.id.iv_send);
        ivLol = findViewById(R.id.iv_lol);
        ivCry = findViewById(R.id.iv_cry);
        ivLike = findViewById(R.id.iv_like);
        ivSurprised = findViewById(R.id.iv_surprised);
        toolbarUnderline = findViewById(R.id.toolbar_underline);
        recyclerUnderline = findViewById(R.id.recycler_underline);
        etMessage = findViewById(R.id.et_message);
        recyclerView = findViewById(R.id.rv_chat);
    }
}
