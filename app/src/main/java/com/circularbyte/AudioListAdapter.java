package com.circularbyte;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.circularbyte.callrecorder.R;
import com.circularbyte.models.AudioModel;

import java.io.File;
import java.util.ArrayList;


public class AudioListAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<AudioModel> list;
    private MediaPlayer mp;
    private boolean shouldDeleteVisible;
    private boolean showSec;

    public AudioListAdapter(Context context, ArrayList<AudioModel> list, boolean shouldDeleteVisible, boolean showSec) {
        this.context = context;
        this.list = list;
        this.shouldDeleteVisible = shouldDeleteVisible;
        this.showSec = showSec;
    }

    @Override
    public int getCount() {
        return list.size();
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
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.audio_list_single_item, null);
        }
        final AudioModel model = (AudioModel) getItem(i);
        TextView title = view.findViewById(R.id.title);
        TextView duration = view.findViewById(R.id.duration);
        TextView size = view.findViewById(R.id.size);
        ImageView playerControl = view.findViewById(R.id.player_control);
        ImageView delete = view.findViewById(R.id.delete);

        ImageView share = view.findViewById(R.id.action_share);

        title.setText(model.getName());
        size.setText(model.getSize());
        if(showSec)
            duration.setText(model.getDuration() + " Sec");
        else {
            duration.setText(model.getDuration());
            size.setText("");
        }

        if(model.isPlaying()){
           playerControl.setImageResource(R.drawable.ic_pause);
        }else {
            playerControl.setImageResource(R.drawable.ic_play);
        }

        if(shouldDeleteVisible)
            delete.setVisibility(View.VISIBLE);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setCancelable(false).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utility.logcat(list.get(i).getPath());
                        File file = new File(list.get(i).getPath());
                        if(file.isFile()){
                            if(file.delete()){
                                list.remove(i);
                                notifyDataSetChanged();
                                Utility.Toast(context, "File Deleted!");
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                            }
                        }
                        dialog.dismiss();
                    }
                }).show();

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent shareIntent = new Intent();
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(list.get(i).getPath())));
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_title));
                shareIntent.putExtra(Intent.EXTRA_HTML_TEXT, Html.fromHtml(context.getString(R.string.email_body_html)));
                shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body));

                shareIntent.setType("audio/*");
                context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.action_share))); //my codes
            }
        });
       // SongsToEditAdapter adapter = new SongsToEditAdapter(MainActivity.this, com.jlcsoftware.utils.Utility.songToEdit);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.logcat(list.get(i).getPath());
                Utility.editsound = list.get(i);
                Intent intent = new Intent(context, TrimSound.class);
                context.startActivity(intent);
            }
        });

        playerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.isPlaying()){
                    mp.stop();
                    mp.release();
                    model.setPlaying(false);
                }else{
                    playAudio(model.getPath());
                    model.setPlaying(true);
                }
                notifyDataSetChanged();
            }
        });
        list.get(i).setView(view);
        return view;
    }

    private void playAudio(String path){
        try {
            mp = new MediaPlayer();
            mp.setDataSource(path);
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
