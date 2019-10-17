package com.circularbyte;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.circularbyte.callrecorder.R;

import java.io.File;
import java.io.IOException;

import io.apptik.widget.MultiSlider;


public class TrimSound extends AppCompatActivity {

    public TrimmingCompletedListener trimmingCompletedListener;
    private MediaPlayer mp;
    Button trimnow;
    MultiSlider multiSlider;
    MultiSlider.Thumb nThumb1; //= multiSlider.new Thumb();
    MultiSlider.Thumb nThumb2;// = multiSlider.new Thumb();
    ImageView playControl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trim_sound);

        multiSlider = (MultiSlider) findViewById(R.id.slider);
        LinearLayout control = (LinearLayout) findViewById(R.id.control);
        playControl = (ImageView) findViewById(R.id.player_control);


        trimnow = (Button)findViewById(R.id.trimnow);
        trimnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cutAndSaveFile(Utility.editsound.getPath(),nThumb1.getValue(),nThumb2.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

       nThumb1 = multiSlider.new Thumb();
       nThumb2 = multiSlider.new Thumb();

        multiSlider.setNumberOfThumbs(0);

        nThumb1.setValue(0);
        nThumb2.setValue(Integer.parseInt(Utility.editsound.getDuration()));
        multiSlider.setMax(Integer.parseInt(Utility.editsound.getDuration()));


        multiSlider.addThumb(nThumb1);
        multiSlider.addThumb(nThumb2);

       // Toast.makeText(this, "D: "+Utility.editsound.getDuration(), Toast.LENGTH_SHORT).show();

        control.findViewById(R.id.player_control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(TrimSound.this, "Clicked: "+Utility.editsound.getName(), Toast.LENGTH_SHORT).show();

                if(!Utility.editsound.isPlaying()){
                    int duration = Integer.parseInt(Utility.editsound.getDuration());
                    int start1 = nThumb1.getValue();
                    Toast.makeText(TrimSound.this, "Star: "+start1, Toast.LENGTH_SHORT).show();

                    int end1 = nThumb2.getValue();

                    Toast.makeText(TrimSound.this, "End: "+end1, Toast.LENGTH_SHORT).show();

                    float startSec1 = ((start1 / 100f) * duration);
                    float endSec1 = ((end1 / 100f) * duration);

                    int[] startArray = new int[] {(int)startSec1};
                    int[] endArray = new int[] {(int)endSec1};
                    playAudio(Utility.editsound.getPath(), start1, end1);
                    Utility.editsound.setPlaying(true);

                }else{
                    Utility.editsound.setPlaying(false);
                    mp.stop();
                }

                if(!Utility.editsound.isPlaying()){
                    playControl.setImageResource(R.drawable.ic_play);
                }else{
                    playControl.setImageResource(R.drawable.ic_pause);
                }
            }
        });


    }

    private void playAudio(String path, final int start, final int end) {
        try {
            mp = new MediaPlayer();
            mp.setDataSource(path);
            mp.prepare();
            mp.seekTo(start * 1000);
            mp.start();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(mp.isPlaying()) {
                        mp.stop();
                        playControl.setImageResource(R.drawable.ic_pause);
                    }
                    playControl.setImageResource(R.drawable.ic_play);
                    Toast.makeText(TrimSound.this, "Stoped: "+end+" and "+start, Toast.LENGTH_SHORT).show();
                    Utility.logcat("Stop Once");
                    if(start > 1){

                        try {
                            mp.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mp.start();

                    }else{
                        mp.release();
                        Utility.editsound.setPlaying(false);
                    }
                }
            }, (end-start) * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static final String DIR_NAME = Environment.getExternalStorageDirectory()+"/calls/";


    private void cutAndSaveFile(final String path, final Integer start, final Integer end) throws Exception {

        final ProgressDialog progressDialog = new ProgressDialog(TrimSound.this);
        progressDialog.show();
        progressDialog.setTitle("Wait");
        progressDialog.setCancelable(false);
        final FFmpeg ffmpeg = FFmpeg.getInstance(TrimSound.this);

            Toast.makeText(TrimSound.this, "Start " + start+" End "+end, Toast.LENGTH_SHORT).show();

            Utility.logcat("Length: " + start+" ");
            final int netSecond1 = end - start;
           // String[] commands = new String[]{"-ss", start + "", "-t", netSecond1 + "", "-i", path, "calls" + "/" + Utility.editsound.getName()+"trim" + ".ogg"};
        String[] commands = new String[] {"-ss", start+"", "-t", netSecond1+"", "-i", path, DIR_NAME+"/"+Utility.editsound.getName().replaceAll(".mp3","")+"trim"+".mp3"};

      //  String[] commands = new String[] {"-i", "concat:"+concat, "-c", "copy", OUTPUT_FOLDER + filename+".mp3"};

       // String[] commands = new String[] {"-i",path, "-ss",start+"", "-t", netSecond1+"", "-acodec","cop", path, DIR_NAME+"/"+Utility.editsound.getName().replaceAll(".mp3","")+"trim"+".mp3"};


        ffmpeg.loadBinary(new LoadBinaryResponseHandler());
            ffmpeg.execute(commands, new ExecuteBinaryResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    if (trimmingCompletedListener != null) {
                        trimmingCompletedListener.trimCompleted(Utility.editsound.getName()+"");
                    }
                    Log.e("ffmpeg",message+" ");

                   // flag = 1;
                    progressDialog.dismiss();

                    try {
                        copympfile();
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(TrimSound.this, "Exception Occured", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    if (trimmingCompletedListener != null) {
                        trimmingCompletedListener.onProgress();
                    }
                    Log.e("ffmpeg",message+" ");
                    progressDialog.setMessage("Please Wait!");
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    if (trimmingCompletedListener != null) {
                        trimmingCompletedListener.onFailed();
                    }
                    Log.e("ffmpeg",message+" ");

                    progressDialog.dismiss();
                    new AlertDialog.Builder(TrimSound.this).setMessage(message+"").setTitle("Alert!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });


       // String[] commands = new String[] {"-ss", start+"", "-t", netSecond1+"", "-i", path, DIR_NAME+"/"+Utility.editsound.getName().replaceAll(".mp3","")+"trim"+".mp3"};

        }

        private void copympfile() throws Exception
        {
            final ProgressDialog progressDialog = new ProgressDialog(TrimSound.this);
            progressDialog.show();
            progressDialog.setTitle("Copying");
            progressDialog.setCancelable(false);

            final FFmpeg ffmpegone = FFmpeg.getInstance(TrimSound.this);

            String[] command2 = new String[]{"-i", "concat:" + DIR_NAME + "/" + Utility.editsound.getName().replaceAll(".mp3", "") + "trim" + ".mp3", "-c", "copy", DIR_NAME + Utility.editsound.getName().replaceAll(".mp3", "") + "trimmed" + ".mp3"};
            String concat = "";
            ffmpegone.loadBinary(new LoadBinaryResponseHandler());
            ffmpegone.execute(command2, new ExecuteBinaryResponseHandler() {

                @Override
                public void onSuccess(String message) {
                    super.onSuccess(message);
                    progressDialog.dismiss();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        final Uri contentUri = Uri.parse("file://" + Environment.getExternalStorageDirectory());
                        scanIntent.setData(contentUri);
                        sendBroadcast(scanIntent);
                    } else {
                        final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                        sendBroadcast(intent);
                    }

                    Utility.Toast(TrimSound.this, "Trimmed Success!");
                    new AlertDialog.Builder(TrimSound.this).setMessage("Successfully Trimmed").setTitle("Success!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(TrimSound.this,AudioListActivity.class));
                            finish();
                            dialog.dismiss();
                        }
                    }).show();

                    deletesound();

                }

                @Override
                public void onProgress(String message) {
                    super.onProgress(message);
                    progressDialog.setTitle("Copying!");
                    progressDialog.setMessage("Please Wait!");
                }

                @Override
                public void onFailure(String message) {
                    super.onFailure(message);
                    progressDialog.dismiss();
                    Utility.logcat("Failed Message: " + message);
                    Utility.Toast(TrimSound.this, "Trimmed Failed!");
                    //finish();
                }
            });
        }


        public void deletesound()
        {
            Utility.logcat(DIR_NAME + "/" + Utility.editsound.getName().replaceAll(".mp3", "") + "trim" + ".mp3");
            File file = new File(DIR_NAME + "/" + Utility.editsound.getName().replaceAll(".mp3", "") + "trim" + ".mp3");
            if(file.isFile()){
                if(file.delete()){
                    Utility.Toast(TrimSound.this, "File Deleted!");
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                }
            }

        }
    public void setTrimmingCompletedListener(TrimmingCompletedListener listener){
        this.trimmingCompletedListener = listener;
    }

    public interface TrimmingCompletedListener{
        void trimCompleted(String name);
        void onProgress();
        void onFailed();
    }
}
