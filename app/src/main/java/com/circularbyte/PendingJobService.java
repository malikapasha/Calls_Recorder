package com.circularbyte;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
;

import java.io.File;


public class PendingJobService extends Service {

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private Handler myhandler;
    Runnable therunnable;
    private Boolean stop;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // Toast.makeText(getBaseContext(), "Service on create", Toast.LENGTH_SHORT).show();

        myhandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        therunnable = new Runnable() {
            @Override
            public void run() {

                if(validateMicAvailability())
                {
                   // Toast.makeText(PendingJobService.this, "In use now", Toast.LENGTH_SHORT).show();
                    Log.e("Service","In use");
                }
                else
                {
                //    Toast.makeText(PendingJobService.this, "Not In use", Toast.LENGTH_SHORT).show();
                    Log.e("Service","Not in use");
                    bringApplicationToFront();
                }

                myhandler.postDelayed(therunnable,3000);
            }
        };

        myhandler.postDelayed(therunnable,3000);
        return START_STICKY;
    }

    private void bringApplicationToFront()
    {
        Intent notificationIntent = new Intent(PendingJobService.this, AudioTrimmerActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        try
        {
            pendingIntent.send();
        }
        catch (PendingIntent.CanceledException e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
//        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    public static boolean getMicrophoneAvailable(Context context) {
        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        recorder.setOutputFile(new File(context.getCacheDir(), "MediaUtil#micAvailTestFile").getAbsolutePath());
        boolean available = true;
        try {
            recorder.prepare();
            recorder.start();

        }
        catch (Exception exception) {
            available = false;
        }
        recorder.release();
        return available;
    }

    private boolean validateMicAvailability(){
        Boolean available = true;
//        AudioRecord recorder =
//                new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
//                        AudioFormat.CHANNEL_IN_MONO,
//                        AudioFormat.ENCODING_DEFAULT, 44100);
//        try{
//            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_STOPPED ){
//                available = false;
//
//            }
//
//            recorder.startRecording();
//            if(recorder.getRecordingState() != AudioRecord.RECORDSTATE_RECORDING){
//                recorder.stop();
//                available = false;
//
//            }
//            recorder.stop();
//        } finally{
//            recorder.release();
//            recorder = null;
//        }

        AudioManager am = (AudioManager) PendingJobService.this.getSystemService(Context.AUDIO_SERVICE);
        final int mode = am.getMode();
        if(AudioManager.MODE_IN_CALL == mode){
          available = true;
        } else if(AudioManager.MODE_IN_COMMUNICATION == mode){
            // device is in communiation mode, i.e. in a VoIP or video call
            available = false;
        } else if(AudioManager.MODE_RINGTONE == mode){
            // device is in ringing mode, some incoming is being signalled
            available = true;
        } else {
            // device is in normal mode, no incoming and no audio being played
            available = true;
        }


        return available;
    }


    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

}
