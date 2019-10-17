package com.circularbyte;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.circularbyte.callrecorder.R;
import com.circularbyte.models.AudioModel;

import java.util.ArrayList;

public class AudioListActivity extends AppCompatActivity {

    ListView audioList;
    ArrayList<AudioModel> list;

    LayoutInflater li;
    View promptsView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        audioList = (ListView) findViewById(R.id.audioList);

        list = getAudioList();
        //audioList.setAdapter(new AudioListAdapter(this, list, false, true));

        audioList.setAdapter(new AudioListAdapter(this, list, false, true));

//        audioList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//
//            }
//        });
    }

    private ArrayList<AudioModel> getAudioList(){
        ArrayList<AudioModel> list = new ArrayList<>();
       // String[] projection = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA};
        String[] projection = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.DATA};

        Cursor audioCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%calls%"}, null);
        if(audioCursor != null){
            if(audioCursor.moveToFirst()){
                do{
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                    int size = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE);
                    int duration = (audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    Toast.makeText(this, "Duration "+duration+" ", Toast.LENGTH_SHORT).show();
                    int data = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

                    list.add(new AudioModel(audioCursor.getString(audioIndex), audioCursor.getString(data), Integer.parseInt(audioCursor.getString(duration))/1000+"", humanReadableByteCount(Long.parseLong(audioCursor.getString(size)), true)));
                }while(audioCursor.moveToNext());
            }
            audioCursor.close();
        }
        return list;
    }

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
