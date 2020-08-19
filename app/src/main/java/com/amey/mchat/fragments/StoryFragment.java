 package com.amey.mchat.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.amey.mchat.Models.StoryModel;
import com.amey.mchat.R;
import com.amey.mchat.StoryActivity;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

 public class StoryFragment extends Fragment {

    public StoryFragment() {
        // Required empty public constructor
    }

    private StoryModel storyModel;

     public StoryFragment(StoryModel storyModel) {
         this.storyModel = storyModel;
     }

     private int progressCount=0;
     private int progressBarIndex=0;
     private Timer timer;
     private TimerTask timerTask;
     private ImageView imageView;
     private LinearLayout progressContainer;
     private ProgressBar defaultProgressBar;
     private boolean timerOn=true;
     private float upperlimit,lowerlimit;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }

     @Override
     public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         init(view);
         setProgressBar();
         Glide.with(this).load(storyModel.getImages().get(progressBarIndex)).placeholder(R.drawable.profile).into(imageView);
         setControls();
     }

     @Override
     public void onResume() {
         super.onResume();
         setTimer();
     }

     @Override
     public void onPause() {
         super.onPause();
         timer.cancel();
         timerTask.cancel();
     }

     private void init(View view){
         imageView=view.findViewById(R.id.imageView);
         progressContainer=view.findViewById(R.id.progress_container);
         defaultProgressBar=((ProgressBar)progressContainer.getChildAt(0));
     }
     private void setProgressBar(){
         //dynamically set progress bar to each stories
         if (storyModel.getImages()!=null) {
             for (int i=1;i<storyModel.getImages().size();i++) {
                 ProgressBar progressBar = new ProgressBar(getContext(),null,android.R.attr.progressBarStyleHorizontal);
                 progressBar.setLayoutParams(defaultProgressBar.getLayoutParams());
                 progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.story_progress));
                 progressContainer.addView(progressBar);
             }
         }
     }
     private void setTimer(){
         timer=new Timer();
         timerTask=new TimerTask() {
             @Override
             public void run() {
                 if (timerOn) {
                     getActivity().runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             if (progressCount == 100) {
                                 progressBarIndex++;
                                 if (progressBarIndex < progressContainer.getChildCount()) {
                                     progressCount = 0;     //change image here
                                     Glide.with(getContext()).load(storyModel.getImages().get(progressBarIndex)).into(imageView);
                                 } else {
                                     getActivity().finish();
                                 }
                             } else {
                                 progressCount++;
                                 ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(progressCount);
                             }
                         }
                     });
                 }
             }
         };
         timer.scheduleAtFixedRate(timerTask,0,50);
     }
     @SuppressLint("ClickableViewAccessibility")
     private void setControls(){
         float widthproportion=(getResources().getDisplayMetrics().widthPixels/100.0f)*25;
         upperlimit=getResources().getDisplayMetrics().widthPixels- widthproportion;
         lowerlimit=widthproportion;
         imageView.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {
                 switch (event.getAction()){
                     //user touch on view/story
                     case MotionEvent.ACTION_DOWN:
                         if(event.getX()<=lowerlimit && progressBarIndex>0){//previus image
                             ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(0);
                             progressBarIndex--;
                             ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(0);
                             progressCount=0;
                             Glide.with(getContext()).load(storyModel.getImages().get(progressBarIndex)).into(imageView);
                         }else if (event.getX()>=upperlimit && progressBarIndex<progressContainer.getChildCount()-1){//next image
                             ((ProgressBar) progressContainer.getChildAt(progressBarIndex)).setProgress(100);
                             progressBarIndex++;
                             progressCount=0;
                             Glide.with(getContext()).load(storyModel.getImages().get(progressBarIndex)).into(imageView);
                         }else {
                             timerOn = false;
                             return true;
                         }
                     case MotionEvent.ACTION_UP:
                         timerOn=true;
                         return true;
                 }
                 return false;
             }
         });
     }
 }