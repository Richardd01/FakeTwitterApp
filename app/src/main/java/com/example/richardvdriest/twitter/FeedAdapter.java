package com.example.richardvdriest.twitter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.MyViewHolder>{

    private List<Tweet> itemList;
    private int profilePicId;
    private String content;
    private Context context;


    public FeedAdapter(Context context, List<Tweet> itemList){
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Tweet tweet = itemList.get(position);

        holder.profileImageView.setImageResource(tweet.getProfilePicId());
        holder.usernameTextView.setText(tweet.getUsername());
        holder.contentTextView.setText(tweet.getContent());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileImageView;
        public TextView usernameTextView;
        public TextView contentTextView;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profile_image_view);
            usernameTextView = itemView.findViewById(R.id.username_text_view);
            contentTextView = itemView.findViewById(R.id.content_text_view);

        }
    }
}
