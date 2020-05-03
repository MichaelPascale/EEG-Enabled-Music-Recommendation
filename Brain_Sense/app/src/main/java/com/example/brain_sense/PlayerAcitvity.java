package com.example.brain_sense;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class PlayerAcitvity extends MainActivity {

    public static final String TAG = "PlayerAcitvity";
    public static int MEDIA_RES_ID = R.raw.jazz_in_paris;

    private TextView mTextDebug;
    private SeekBar mSeekbarAudio;
    private ScrollView mScrollContainer;
    private PlayerAdapter mPlayerAdapter;
    private boolean mUserIsSeeking = false;
    public static final String delimiter = ",";

    private String classified_song_list_pos[]={ "Jam Rock", "Jazz in Paris", "Postive Metal", "Rock and Rolla", "Rock Transition", "Sick beat"};
    private String classified_names[]={ "jam_rock_pos.wav", "jazz_in_paris.wav", "metal_loop_pos.wav", "rock_and_roll_loop_pos.wav", "rock_trans_pos.wav"};
    private String classified_song_list_neg[]={"Sick beat"};
    private String classified_song_list_neutral[] = {"can we sing oolala"};
    ListView myList;

    Button SongPlay,SongPlay2;
    public int songID;
    TextView song_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_acitvity);
        initializeUI();
        initializeSeekbar();
        initializePlaybackController();
        Log.d(TAG, "onCreate: finished");
       // myList=findViewById(R.id.song_scroll);
        //myList.setAdapter(new ArrayAdapter<String>(this,R.layout.activity_player_acitvity, listview_array));
        //Helper.getListViewSize(myList);

        int mood = 0;
        String path = "android.resource://" + getPackageName() + "/" + R.raw.predicted_emotion;
        String emotion = read(path);

        if(emotion.equals("Positive"))
        {
            mood = 1;
        }
        else if(emotion.equals("Negative"))
        {
            mood = 2;
        }
        else
        {
            mood = 3;
        }
        for(int it = 0; it <= 8 ;it++ )
        {
            if(mood == 1)
            {
                song_text = findViewById(R.id.song1);
                song_text.setText(classified_song_list_pos[0]);

                song_text = findViewById(R.id.song2);
                song_text.setText(classified_song_list_neg[0]);

               // song_text = findViewById(R.id.song1);
                //song_text.setText(classified_song_list_neg[0]);

            }
            else if(mood == 2)
            {

                LinearLayout mainLayout=this.findViewById(R.id.);
                mainLayout.setVisibility(2);

                song_text = findViewById(R.id.song1);
                song_text.setText(classified_song_list_pos[0]);

                song_text = findViewById(R.id.song2);
                song_text.setText(classified_song_list_pos[0]);

                //song_text = findViewById(R.id.song1);
                //song_text.setText(classified_song_list_pos[0]);

            }
            else
            {
                song_text = findViewById(R.id.song1);
                song_text.setText(classified_song_list_pos[0]);

                song_text = findViewById(R.id.song2);
                song_text.setText(classified_song_list_pos[0]);

            }
        }

        SongPlay = findViewById(R.id.play1);
        SongPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                //
                songID = 1;
                SelectSong(songID);

            }
        });


        SongPlay2 = findViewById(R.id.play2);
        SongPlay2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                //
                songID = 2;
                SelectSong(songID);

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: create MediaPlayer");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
            Log.d(TAG, "onStop: don't release MediaPlayer as screen is rotating & playing");
        } else {
            mPlayerAdapter.release();
            Log.d(TAG, "onStop: release MediaPlayer");
        }
    }

    public void SelectSong(int id)
    {
        if(id == 1)
        {
            MEDIA_RES_ID = R.raw.jazz_in_paris;
        }
        else if(id == 2)
        {
            MEDIA_RES_ID = R.raw.sick_beat;
        }
        else if(id == 3)
        {
            MEDIA_RES_ID = R.raw.jam_rock_pos;
        }
        else if(id == 4)
        {
            MEDIA_RES_ID = R.raw.rock_and_roll_loop_pos;
        }
        else if(id == 5)
        {
            MEDIA_RES_ID = R.raw.jazz_in_paris;
        }
        else
        {
            MEDIA_RES_ID = R.raw.jazz_in_paris;
        }

    }

    private void PosNeg()
    {
        int flagGenre = 1;

        if(flagGenre == 1)
        {

        }
        else if(flagGenre == 2)
        {

        }
        else
        {

        }

    }


    public String read(String csvFile) {
        InputStream is= getResources().openRawResource(R.raw.predicted_emotion);
        String[] tempArr = {" "};
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );
            String line = "";
            while((line = br.readLine()) != null) {
                tempArr = line.split(delimiter);
            }
            br.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return tempArr[0];
    }



    private void initializeUI() {
        mTextDebug = findViewById(R.id.text_debug);
        Button mPlayButton = findViewById(R.id.button_play);
        Button mPauseButton =  findViewById(R.id.button_pause);
        Button mResetButton = findViewById(R.id.button_reset);
        mSeekbarAudio =  findViewById(R.id.seekbar_audio);
        mScrollContainer = findViewById(R.id.scroll_container);

        mPauseButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.pause();
                    }
                });
        mPlayButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.loadMedia(MEDIA_RES_ID);
                        mPlayerAdapter.play();
                    }
                });
        mResetButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPlayerAdapter.reset();
                    }
                });
    }

    private void initializePlaybackController() {
        MediaPlayerHolder mMediaPlayerHolder = new MediaPlayerHolder(this);
        Log.d(TAG, "initializePlaybackController: created MediaPlayerHolder");
        mMediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mMediaPlayerHolder;
        Log.d(TAG, "initializePlaybackController: MediaPlayerHolder progress callback set");
    }

    private void initializeSeekbar() {
        mSeekbarAudio.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int userSelectedPosition = 0;

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = true;
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            userSelectedPosition = progress;
                        }
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mUserIsSeeking = false;
                        mPlayerAdapter.seekTo(userSelectedPosition);
                    }
                });
    }

    public class PlaybackListener extends PlaybackInfoListener {

        @Override
        public void onDurationChanged(int duration) {
            mSeekbarAudio.setMax(duration);
            Log.d(TAG, String.format("setPlaybackDuration: setMax(%d)", duration));
        }

        @TargetApi(Build.VERSION_CODES.N_MR1)
        @Override
        public void onPositionChanged(int position) {
            if (!mUserIsSeeking) {
                mSeekbarAudio.setProgress(position, true);
                Log.d(TAG, String.format("setPlaybackPosition: setProgress(%d)", position));
            }
        }

        @Override
        public void onStateChanged(@State int state) {
            String stateToString = PlaybackInfoListener.convertStateToString(state);
            onLogUpdated(String.format("onStateChanged(%s)", stateToString));
        }

        @Override
        public void onPlaybackCompleted() {
        }

        @Override
        public void onLogUpdated(String message) {
            if (mTextDebug != null) {
                mTextDebug.append(message);
                mTextDebug.append("\n");
                // Moves the scrollContainer focus to the end.
                mScrollContainer.post(
                        new Runnable() {
                            @Override
                            public void run() {
                                mScrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
            }
        }
    }
}
