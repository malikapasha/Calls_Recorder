package com.circularbyte;

import android.view.View;

/**
 * Created by Tahir-Laptop on 1/17/2018.
 */

public class AudioModel
{
    private String name, path, duration, size;

    public boolean isCutFile() {
        return cutFile;
    }

    public void setCutFile(boolean cutFile) {
        this.cutFile = cutFile;
    }

    private boolean cutFile;
    private boolean isPlaying = false;
    private int numberOfPointers;
    private View view;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getNumberOfPointers() {
        return numberOfPointers;
    }

    public void setNumberOfPointers(int numberOfPointers) {
        this.numberOfPointers = numberOfPointers;
    }

    public AudioModel(String name, String path, String duration, String size) {
        this.name = name;
        this.path = path;
        this.duration = duration;
        this.size = size;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getDuration() {
        return duration;
    }

    public String getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
