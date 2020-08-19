package com.amey.mchat.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amey.mchat.Models.PostModel;
import com.amey.mchat.R;

import java.util.List;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.Viewholder> {

    private List<PostModel> feeds;
    //add constructor for feeds
    public FeedsAdapter(List<PostModel> feeds) {
        this.feeds = feeds;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
