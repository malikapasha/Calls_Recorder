package com.circularbyte;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.circularbyte.models.AudioModel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class Utility
{
    public static void logcat(String message){
        Log.i("SongMedley", message);
    }

    public static void Toast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static final ArrayList<AudioModel> songToEdit = new ArrayList<>();

    public static AudioModel editsound = new AudioModel();

    public static final Map<String, Integer> hashtable = new Hashtable<String, Integer>();
}
