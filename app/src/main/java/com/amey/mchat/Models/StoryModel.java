package com.amey.mchat.Models;

import java.util.List;

public class StoryModel {

    //declaration
    private List<String> images;
    private String name;

    //alt+insert(fn+delete) to create constructor for all declaration
    public StoryModel(List<String> images, String name) {
        this.images = images;
        this.name = name;
    }
    //alt+insert(fn+delete) to create getter & setter for both(after creating a constructor)

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
