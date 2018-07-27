package com.example.dell.letchat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dell.letchat.R;
import com.example.dell.letchat.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<UserModel> mUserModelList = new ArrayList<>();
    private int mBackground, mTextColor;

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

        switch (mUserModelList.get(position).getType()){
            case "message":
                return 2;
            case "sticker":
                return 4;
            case "userMessage":
                return 1;
            default:
                return 3;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserModel userModel = mUserModelList.get(position);

        switch (holder.getItemViewType()){
            case 1:
                UserMessageViewHolder userMessageViewHolder = (UserMessageViewHolder) holder;
                userMessageViewHolder.initColorView();
                userMessageViewHolder.initMassageView(userModel);
                break;
            case 2:
                MemberMessageViewHolder memberMessageViewHolder = (MemberMessageViewHolder) holder;
                memberMessageViewHolder.initColorView();
                memberMessageViewHolder.initMassageView(userModel);
                break;
            case 3:
                UserEmojiMessageViewHolder userEmojiMessageViewHolder = (UserEmojiMessageViewHolder) holder;
                userEmojiMessageViewHolder.initColorView();
                userEmojiMessageViewHolder.initEmojiView(userModel);
                break;
            default:
                MemberEmojiMessageViewHolder memberEmojiMessageViewHolder = (MemberEmojiMessageViewHolder) holder;
                memberEmojiMessageViewHolder.initColorView();
                memberEmojiMessageViewHolder.initEmojiView(userModel);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mUserModelList.size();
    }

    public void addNewMessage(List<UserModel> userModelList){
        this.mUserModelList = userModelList;
        notifyItemRangeChanged(getItemCount() +1, userModelList.size());
    }

    public void addOldMessage(List<UserModel> userModelList){
        this.mUserModelList = userModelList;
        notifyDataSetChanged();
    }

    public void setColor(int background, int textColor){
        this.mBackground = background;
        this.mTextColor = textColor;
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

        void initMassageView(UserModel userModel){
            txtUserName.setText(userModel.getUsername());
            txtMessage.setText(userModel.getContent());
        }

        void initColorView(){
            txtUserName.setTextColor(mContext.getResources().getColor(mTextColor));
            txtMessage.setTextColor(mContext.getResources().getColor(mTextColor));
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

        void initEmojiView(UserModel userModel){
            txtUserName.setText(userModel.getUsername());
            ivEmoji.setImageDrawable(userModel.getEmoji());
        }

        void initColorView(){
            txtUserName.setTextColor(mContext.getResources().getColor(mTextColor));
            ivEmoji.setColorFilter(mContext.getResources().getColor(mTextColor));
        }
    }

    class MemberEmojiMessageViewHolder extends UserEmojiMessageViewHolder{

        MemberEmojiMessageViewHolder(View itemView) {
            super(itemView);
        }
    }

}
