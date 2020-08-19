package com.amey.mchat.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amey.mchat.Models.StoryModel;
import com.amey.mchat.R;
import com.amey.mchat.StoryActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.storyViewholder> { //press alt+enter to import methods (3 methods are imported)

    private List<StoryModel> list; //insert constructor for list alt+insert

    public StoriesAdapter(List<StoryModel> list) {
        this.list = list;
    }

    //here 3 methods are imported.....
    @NonNull
    @Override
    public storyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item,parent,false);
        return new storyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final storyViewholder holder, final int position) {
        Glide.with(holder.itemView.getContext()).load(list.get(position).getImages().get(0))
                .placeholder(R.drawable.profile).into(holder.thumbnail);
        holder.name.setText(list.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent storyIntent=new Intent(holder.itemView.getContext(), StoryActivity.class);
                holder.itemView.getContext().startActivity(storyIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class storyViewholder extends RecyclerView.ViewHolder{ //press alt+enter to create constructor

        private CircleImageView thumbnail;
        private TextView name;

        public storyViewholder(@NonNull View itemView) {//This is a constructor....
            super(itemView);

            thumbnail=itemView.findViewById(R.id.story);
            name=itemView.findViewById(R.id.name);

        }
    }

}
