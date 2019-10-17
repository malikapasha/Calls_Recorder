package com.circularbyte;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.circularbyte.callrecorder.R;

import java.io.IOException;
import java.util.ArrayList;

import io.apptik.widget.MultiSlider;
import com.circularbyte.models.AudioModel;

public class SongsToEditAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<AudioModel> list;
    private MediaPlayer mp;
    public static final String DIR_NAME = Environment.getExternalStorageDirectory()+"/calls/";
    public TrimmingCompletedListener trimmingCompletedListener;
    public interface TrimmingCompletedListener{
        void trimCompleted(int position);
        void onProgress();
        void onFailed();
    }

    public void setTrimmingCompletedListener(TrimmingCompletedListener listener){
        this.trimmingCompletedListener = listener;
    }

    public SongsToEditAdapter(Context context, ArrayList<AudioModel> list) {
        this.context = context;
        this.list = list;
        mp = new MediaPlayer();
    }

    @Override
    public int getCount() {
        return list.size() > 3 ? 3 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.thumbs, null);

        MultiSlider multiSlider = view.findViewById(R.id.slider);
        LinearLayout control = view.findViewById(R.id.control);
        ImageView playControl = view.findViewById(R.id.player_control);

        ((TextView)view.findViewById(R.id.text)).setText(list.get(i).getName());
        multiSlider.setNumberOfThumbs(0);

        if(list.get(i).getNumberOfPointers() == 2){
            final MultiSlider.Thumb nThumb1 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb2 = multiSlider.new Thumb();

            nThumb1.setValue(0);
            nThumb2.setValue(100);

            multiSlider.addThumb(nThumb1);
            multiSlider.addThumb(nThumb2);

            control.findViewById(R.id.player_control).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!list.get(i).isPlaying()){
                        int duration = Integer.parseInt(list.get(i).getDuration());
                        int start1 = nThumb1.getValue();
                        int end1 = nThumb2.getValue();

                        float startSec1 = ((start1 / 100f) * duration);
                        float endSec1 = ((end1 / 100f) * duration);

                        int[] startArray = new int[] {(int)startSec1};
                        int[] endArray = new int[] {(int)endSec1};
                        playAudio(i, list.get(i).getPath(), startArray, endArray);
                        list.get(i).setPlaying(true);
                        notifyDataSetChanged(i);
                    }else{
                        list.get(i).setPlaying(false);
                        notifyDataSetChanged(i);
                        mp.stop();
                    }
                }
            });

        }else if(list.get(i).getNumberOfPointers() == 4){
            final MultiSlider.Thumb nThumb1 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb2 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb3 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb4 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb5 = multiSlider.new Thumb();

            nThumb1.setValue(0);
            nThumb2.setValue(45);

            nThumb3.setValue(50).setEnabled(false);

            nThumb4.setValue(55);
            nThumb5.setValue(100);

            multiSlider.addThumb(nThumb1);
            multiSlider.addThumb(nThumb2);
            multiSlider.addThumb(nThumb3);
            multiSlider.addThumb(nThumb4);
            multiSlider.addThumb(nThumb5);


            control.findViewById(R.id.player_control).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!list.get(i).isPlaying()) {
                        int duration = Integer.parseInt(list.get(i).getDuration());

                        int start1 = nThumb1.getValue();
                        int end1 = nThumb2.getValue();

                        int start2 = nThumb4.getValue();
                        int end2 = nThumb5.getValue();

                        float startSec1 = ((start1 / 100f) * duration);
                        float endSec1 = ((end1 / 100f) * duration);

                        float startSec2 = ((start2 / 100f) * duration);
                        float endSec2 = ((end2 / 100f) * duration);

                        int[] startArray = new int[]{(int) startSec1, (int) startSec2};
                        int[] endArray = new int[]{(int) endSec1, (int) endSec2};
                        playAudio(i, list.get(i).getPath(), startArray, endArray);
                        list.get(i).setPlaying(true);
                        notifyDataSetChanged(i);
                    }else{
                        list.get(i).setPlaying(false);
                        notifyDataSetChanged(i);
                        mp.stop();
                    }
                }
            });

        }else if(list.get(i).getNumberOfPointers() == 6){
            final MultiSlider.Thumb nThumb1 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb2 = multiSlider.new Thumb();
            MultiSlider.Thumb nThumb3 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb4 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb5 = multiSlider.new Thumb();
            MultiSlider.Thumb nThumb6 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb7 = multiSlider.new Thumb();
            final MultiSlider.Thumb nThumb8 = multiSlider.new Thumb();

            nThumb1.setValue(0);
            nThumb2.setValue(25);

            nThumb3.setValue(30).setEnabled(false);

            nThumb4.setValue(35);
            nThumb5.setValue(65);

            nThumb6.setValue(70).setEnabled(false);

            nThumb7.setValue(75);
            nThumb8.setValue(100);

            nThumb1.setThumbOffset(5);
            nThumb2.setThumbOffset(5);
            nThumb4.setThumbOffset(5);
            nThumb5.setThumbOffset(5);
            nThumb7.setThumbOffset(5);
            nThumb8.setThumbOffset(5);

            multiSlider.addThumb(nThumb1);
            multiSlider.addThumb(nThumb2);
            multiSlider.addThumb(nThumb3);
            multiSlider.addThumb(nThumb4);
            multiSlider.addThumb(nThumb5);
            multiSlider.addThumb(nThumb6);
            multiSlider.addThumb(nThumb7);
            multiSlider.addThumb(nThumb8);

            control.findViewById(R.id.player_control).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!list.get(i).isPlaying()) {
                        int duration = Integer.parseInt(list.get(i).getDuration());

                        int start1 = nThumb1.getValue();
                        int end1 = nThumb2.getValue();

                        int start2 = nThumb4.getValue();
                        int end2 = nThumb5.getValue();

                        int start3 = nThumb7.getValue();
                        int end3 = nThumb8.getValue();

                        float startSec1 = ((start1 / 100f) * duration);
                        float endSec1 = ((end1 / 100f) * duration);

                        float startSec2 = ((start2 / 100f) * duration);
                        float endSec2 = ((end2 / 100f) * duration);

                        float startSec3 = ((start3 / 100f) * duration);
                        float endSec3 = ((end3 / 100f) * duration);

                        int[] startArray = new int[]{(int) startSec1, (int) startSec2, (int) startSec3};
                        int[] endArray = new int[]{(int) endSec1, (int) endSec2, (int) endSec3};
                        playAudio(i, list.get(i).getPath(), startArray, endArray);
                        list.get(i).setPlaying(true);
                        notifyDataSetChanged(i);
                    }else{
                        list.get(i).setPlaying(false);
                        notifyDataSetChanged(i);
                        mp.stop();
                    }
                }
            });
        }

        if(!list.get(i).isPlaying()){
            playControl.setImageResource(R.drawable.ic_play);
        }else{
            playControl.setImageResource(R.drawable.ic_pause);
        }

        list.get(i).setView(view);
        return view;
    }

    @SuppressLint("CutPasteId")
    public void playAll(int pos){
        int start[], end[];
        if(pos == 0 && list.size() > pos){
            start = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(0).getValue()};
            end = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(1).getValue()};
            playAudio(pos, list.get(pos).getPath(), start, end);
        }else if(pos == 1 && list.size() > pos){
            start = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(0).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(3).getValue()};
            end = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(1).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(4).getValue()};
            playAudio(pos, list.get(pos).getPath(), start, end);
        }else if(pos == 2 && list.size() > pos){
            start = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(0).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(3).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(6).getValue()};
            end = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(1).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(4).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(7).getValue()};
            playAudio(pos, list.get(pos).getPath(), start, end);
        }
    }

    private void notifyDataSetChanged(int position) {
        if(!list.get(position).isPlaying()){
            ((ImageView)list.get(position).getView().findViewById(R.id.player_control)).setImageResource(R.drawable.ic_play);
        }else{
            ((ImageView)list.get(position).getView().findViewById(R.id.player_control)).setImageResource(R.drawable.ic_pause);
        }
    }

    private void playAudio(final int position, String path, final int start[], final int end[]) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(path);
            mp.prepare();
            mp.start();

            mp.seekTo(start[0] * 1000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mp.stop();

                    Utility.logcat("Stop Once");
                    if(start.length > 1){

                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.start();
                        mp.seekTo(start[1] * 1000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mp.stop();

                                Utility.logcat("Stop Again");
                                if(start.length > 2){
                                    try {
                                        mp.prepare();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mp.start();
                                    mp.seekTo(start[2] * 1000);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mp.stop();
                                            mp.release();
                                            Utility.logcat("Stop & Released");
                                            list.get(position).setPlaying(false);
                                            notifyDataSetChanged(position);
                                        }
                                    }, (end[2]-start[2]) * 1000);
                                }else{
                                    mp.release();
                                    list.get(position).setPlaying(false);
                                    notifyDataSetChanged(position);

                                    if(list.size() == 3){
                                        playAll(2);
                                    }
                                }
                            }
                        }, (end[1]-start[1]) * 1000);
                    }else{
                        mp.release();
                        list.get(position).setPlaying(false);
                        notifyDataSetChanged(position);

                        if(list.size() > 1){
                            playAll(1);
                        }
                    }
                }
            }, (end[0]-start[0]) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("CutPasteId")
    public void cutFiles(int pos,int count) throws Exception{
       // Integer start[], end[];
        ArrayList<Integer> startarray = new ArrayList<Integer>();
        ArrayList<Integer> endarray = new ArrayList<Integer>();

//for start
        for(int i =0; i <=count;i+=3)
        {

            Toast.makeText(context, "see"+i, Toast.LENGTH_SHORT).show();
            startarray.add(((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(i).getValue());
        }

//for end
        for(int i =1; i <=count+1;i+=3)
        { Toast.makeText(context, "see end "+i, Toast.LENGTH_SHORT).show();
            endarray.add(((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(i).getValue());
        }

        Integer[] start = new Integer[startarray.size()];
        start = startarray.toArray(start);

        Integer[] end = new Integer[endarray.size()];
        end = endarray.toArray(end);

        cutAndSaveFile(pos, list.get(pos).getPath(), start, end,count);

        //dynamic arraylist and later on converstion required here
//        if(pos == 0){
//            start = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(0).getValue()};
//            end = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(1).getValue()};
//            cutAndSaveFile(pos, list.get(pos).getPath(), start, end);
//        }else if(pos == 1){
//            start = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(0).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(3).getValue()};
//            end = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(1).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(4).getValue()};
//            cutAndSaveFile(pos, list.get(pos).getPath(), start, end);
//        }else if(pos == 2){
//            start = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(0).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(3).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(6).getValue()};
//            end = new int[] {((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(1).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(4).getValue(), ((MultiSlider)list.get(pos).getView().findViewById(R.id.slider)).getThumb(7).getValue()};
//            cutAndSaveFile(pos, list.get(pos).getPath(), start, end);
//        }
    }

    private void cutAndSaveFile(final int pos, final String path, final Integer start[], final Integer end[],Integer count) throws Exception {
        final FFmpeg ffmpeg = FFmpeg.getInstance(context);


        int pcount=count/2;

        for(int i =0; i <pcount;i++)
        {
            Toast.makeText(context, "cut and save "+i, Toast.LENGTH_SHORT).show();

            Utility.logcat("Length: "+start.length);
            final int netSecond1 = end[i] - start[i];
            String[] commands = new String[] {"-ss", start[i]+"", "-t", netSecond1+"", "-i", path, DIR_NAME+"/"+pos+"Out"+(i+1)+".mp3"};
            ffmpeg.loadBinary(new LoadBinaryResponseHandler());
            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    if(trimmingCompletedListener != null){
                        trimmingCompletedListener.trimCompleted(pos);
                    }
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    if(trimmingCompletedListener != null){
                        trimmingCompletedListener.onProgress();
                    }
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    if(trimmingCompletedListener != null){
                        trimmingCompletedListener.onFailed();
                    }
                }
            });
        }

//        if(pos == 0) {
//            Utility.logcat("Length: "+start.length);
//            final int netSecond1 = end[0] - start[0];
//            String[] commands = new String[] {"-ss", start[0]+"", "-t", netSecond1+"", "-i", path, DIR_NAME+"/Out1.mp3"};
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler());
//            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onSuccess(String message) {
//                    super.onSuccess(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.trimCompleted(pos);
//                    }
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    super.onProgress(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.onProgress();
//                    }
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    super.onFailure(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.onFailed();
//                    }
//                }
//            });
//        }else if(pos == 1){
//            Utility.logcat("Length: "+start.length);
//            final int netSecond1 = end[0] - start[0];
//            String[] commands = new String[] {"-ss", start[0]+"", "-t", netSecond1+"", "-i", path, DIR_NAME+"Out2.mp3"};
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler());
//            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onSuccess(String message) {
//                    super.onSuccess(message);
//
//                    final int netSecond2 = end[1] - start[1];
//                    String[] commands = new String[] {"-ss", start[1]+"", "-t", netSecond2+"", "-i", path, DIR_NAME+"Out3.mp3"};
//                    try {
//                        ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
//                            @Override
//                            public void onSuccess(String message) {
//                                super.onSuccess(message);
//
//                                if(trimmingCompletedListener != null){
//                                    trimmingCompletedListener.trimCompleted(pos);
//                                }
//                            }
//
//                            @Override
//                            public void onProgress(String message) {
//                                super.onProgress(message);
//                                if(trimmingCompletedListener != null){
//                                    trimmingCompletedListener.onProgress();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(String message) {
//                                super.onFailure(message);
//                                if(trimmingCompletedListener != null){
//                                    trimmingCompletedListener.onFailed();
//                                }
//                            }
//                        });
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    super.onProgress(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.onProgress();
//                    }
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    super.onFailure(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.onFailed();
//                    }
//                }
//            });
//        }else if(pos == 2){
//            Utility.logcat("Length: "+start.length);
//            final int netSecond1 = end[0] - start[0];
//            String[] commands = new String[] {"-ss", start[0]+"", "-t", netSecond1+"", "-i", path, DIR_NAME+"Out4.mp3"};
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler());
//            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
//                @Override
//                public void onSuccess(String message) {
//                    super.onSuccess(message);
//
//                    final int netSecond2 = end[1] - start[1];
//                    String[] commands = new String[] {"-ss", start[1]+"", "-t", netSecond2+"", "-i", path, DIR_NAME+"Out5.mp3"};
//                    try {
//                        ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
//                            @Override
//                            public void onSuccess(String message) {
//                                super.onSuccess(message);
//
//                                final int netSecond3 = end[2] - start[2];
//                                String[] commands = new String[] {"-ss", start[2]+"", "-t", netSecond3+"", "-i", path, DIR_NAME+"Out6.mp3"};
//                                try {
//                                    ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
//                                        @Override
//                                        public void onSuccess(String message) {
//                                            super.onSuccess(message);
//
//                                            if(trimmingCompletedListener != null){
//                                                trimmingCompletedListener.trimCompleted(pos);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onProgress(String message) {
//                                            super.onProgress(message);
//                                            if(trimmingCompletedListener != null){
//                                                trimmingCompletedListener.onProgress();
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onFailure(String message) {
//                                            super.onFailure(message);
//                                            if(trimmingCompletedListener != null){
//                                                trimmingCompletedListener.onFailed();
//                                            }
//                                        }
//                                    });
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onProgress(String message) {
//                                super.onProgress(message);
//                                if(trimmingCompletedListener != null){
//                                    trimmingCompletedListener.onProgress();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(String message) {
//                                super.onFailure(message);
//                                if(trimmingCompletedListener != null){
//                                    trimmingCompletedListener.onFailed();
//                                }
//                            }
//                        });
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onProgress(String message) {
//                    super.onProgress(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.onProgress();
//                    }
//                }
//
//                @Override
//                public void onFailure(String message) {
//                    super.onFailure(message);
//                    if(trimmingCompletedListener != null){
//                        trimmingCompletedListener.onFailed();
//                    }
//                }
//            });
//        }
    }
}
