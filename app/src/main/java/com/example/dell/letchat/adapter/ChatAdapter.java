package com.example.dell.letchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.letchat.R;
import com.example.dell.letchat.model.ChatModel;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<ChatModel> mChatModelList;

    public ChatAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case 1: return new UserMessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_right_layout, parent, false));
            case 2: return new MemberMessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_left_layout, parent, false));
            case 3: return new UserEmojiMessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_emoji_right, parent, false));
            default: return new MemberEmojiMessageViewHolder(LayoutInflater.from(mContext).inflate(R.layout.chat_emoji_left, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {

        switch (mChatModelList.get(position).getType()){
            case "message":
                return 1;
            default:
                return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel chatModel = mChatModelList.get(position);

        switch (holder.getItemViewType()){
            case 1:
                UserMessageViewHolder userMessageViewHolder = (UserMessageViewHolder) holder;
                userMessageViewHolder.initMassageView(chatModel);
                break;
            case 2:
                MemberMessageViewHolder memberMessageViewHolder = (MemberMessageViewHolder) holder;
                memberMessageViewHolder.initMassageView(chatModel);
                break;
            case 3:
                UserEmojiMessageViewHolder userEmojiMessageViewHolder = (UserEmojiMessageViewHolder) holder;
                userEmojiMessageViewHolder.initEmojiView(chatModel);
                break;
            default:
                MemberEmojiMessageViewHolder memberEmojiMessageViewHolder = (MemberEmojiMessageViewHolder) holder;
                memberEmojiMessageViewHolder.initEmojiView(chatModel);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mChatModelList.size();
    }

    public void addMessage(List<ChatModel> chatModelList){
        mChatModelList = chatModelList;
        notifyDataSetChanged();
    }

    class UserMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView txtUserName;
        private TextView txtMessage;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            txtMessage = itemView.findViewById(R.id.txt_message);
        }

        void initMassageView(ChatModel chatModel){
            txtUserName.setText(chatModel.getUsername());
            txtMessage.setText(chatModel.getContent());
        }
    }
    class MemberMessageViewHolder extends UserMessageViewHolder{

        public MemberMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

    class UserEmojiMessageViewHolder extends RecyclerView.ViewHolder{

        private TextView txtUserName;
        private ImageView ivEmoji;

        UserEmojiMessageViewHolder(View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txt_user_name);
            ivEmoji = itemView.findViewById(R.id.iv_emoji_message);
        }

        void initEmojiView(ChatModel chatModel){
            txtUserName.setText(chatModel.getUsername());
            ivEmoji.setImageResource(R.drawable.lol_26);
        }
    }

    class MemberEmojiMessageViewHolder extends UserEmojiMessageViewHolder{

        MemberEmojiMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

}
