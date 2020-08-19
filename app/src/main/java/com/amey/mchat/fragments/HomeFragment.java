 package com.amey.mchat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amey.mchat.Adapters.FeedsAdapter;
import com.amey.mchat.Adapters.StoriesAdapter;
import com.amey.mchat.Models.PostModel;
import com.amey.mchat.Models.StoryModel;
import com.amey.mchat.R;

import java.util.ArrayList;
import java.util.List;

 public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private StoriesAdapter storiesAdapter;
    private FeedsAdapter feedsAdapter;
    private RecyclerView stories,feeds;
    public static List<StoryModel> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        LinearLayoutManager storiesLayoutManager =new LinearLayoutManager(getContext());
        storiesLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        stories.setLayoutManager(storiesLayoutManager);

        LinearLayoutManager feedsLayoutManager =new LinearLayoutManager(getContext());
        feedsLayoutManager.setOrientation(RecyclerView.VERTICAL);
        feeds.setLayoutManager(feedsLayoutManager);

        List <String> images=new ArrayList<>();
        images.add("https://www.asakura-japan.com/data/asakura-japan/product/20191212_355038.jpg");
        images.add("https://www.asakura-japan.com/data/asakura-japan/product/20191212_355038.jpg");
        //images.add("");

        list=new ArrayList<>();
        list.add(new StoryModel(images,"Amey palshetkar"));
        list.add(new StoryModel(images,"Sanket Pardule"));
        list.add(new StoryModel(images,"Avi Saku"));
        list.add(new StoryModel(images,"Saurabh Naru"));
        list.add(new StoryModel(images,"Ramesh Boltan"));
        list.add(new StoryModel(images,"Maddy Sawant"));
        list.add(new StoryModel(images,"Balu Shirke"));

        storiesAdapter=new StoriesAdapter(list);
        stories.setAdapter(storiesAdapter);

        List<PostModel> feedlist=new ArrayList<>();
        feedlist.add(new PostModel("","","","","","","",""));
        feedlist.add(new PostModel("","","","","","","",""));
        feedlist.add(new PostModel("","","","","","","",""));
        feedlist.add(new PostModel("","","","","","","",""));
        feedlist.add(new PostModel("","","","","","","",""));
        feedlist.add(new PostModel("","","","","","","",""));
        feedlist.add(new PostModel("","","","","","","",""));

        feedsAdapter=new FeedsAdapter(feedlist);
        feeds.setAdapter(feedsAdapter);

    }

    private void init(View view){

        stories=view.findViewById(R.id.stories);
        feeds=view.findViewById(R.id.feeds);
    }
}